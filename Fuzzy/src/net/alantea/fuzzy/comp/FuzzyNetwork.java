package net.alantea.fuzzy.comp;

import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.cond.FuzzyGaussianCondition;
import net.alantea.fuzzy.cond.FuzzyLinearCondition;
import net.alantea.fuzzy.op.FuzzyAnd;
import net.alantea.fuzzy.op.FuzzyNot;
import net.alantea.fuzzy.op.FuzzyOr;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyConstant;
import net.alantea.fuzzy.val.FuzzyValue;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FuzzyNetwork
{
   private Hashtable<String, FuzzyValue> labels = new Hashtable<String, FuzzyValue>();

   public FuzzyNetwork()
   {
   }

   public void readFile(String path) throws FuzzyException
   {
      try
      {
         DocumentBuilder db = DocumentBuilderFactory.newInstance()
               .newDocumentBuilder();
         Document doc = db.parse(path);

         Node n = doc.getDocumentElement().getFirstChild();
         while (n != null)
         {
            if (n.getNodeName().equals("set"))
               loadSet(n);
            else if (n.getNodeName().equals("rule"))
               loadRule(n);
            else if (n.getNodeName().equals("condition"))
               loadCondition(n);
            else if (n.getNodeName().equals("value")) loadValue(n);

            n = n.getNextSibling();
         }
      }
      catch (Exception e)
      {
         throw new FuzzyException("Unable to parse file", e);
      }
   }

   public void addValue(String label, FuzzyValue value) throws FuzzyException
   {
      if (label == null) throw new FuzzyException("Null label");
      if (value == null) throw new FuzzyException("Null value");
      labels.put(label, value);
   }

   public FuzzyValue getValue(String label) throws FuzzyException
   {
      if (label == null) throw new FuzzyException("Null label");
      FuzzyValue v = labels.get(label);
      if (v == null) throw new FuzzyException("Null value");
      return v;
   }

   public double getDoubleValue(String label) throws FuzzyException
   {
      return labels.get(label).getValue();
   }

   private void loadSet(Node node) throws FuzzyException
   {
      FuzzySet set = new FuzzySet();

      NamedNodeMap map = node.getAttributes();
      Node n = map.getNamedItem("label");
      if (n != null)
      {
         addValue(n.getNodeValue().trim(), set);
      }

      n = node.getFirstChild();
      while (n != null)
      {
         if (n.getNodeName().equals("rule")) set.addRule(loadRule(n));
         n = n.getNextSibling();
      }
   }

   private FuzzyRule loadRule(Node node) throws FuzzyException
   {
      FuzzyCondition cond = null;
      FuzzyValue output = null;

      Node n = node.getFirstChild();
      while (n != null)
      {
         if (n.getNodeName().equals("output"))
            output = loadValue(n);
         else if (n.getNodeName().equals("condition")) cond = loadCondition(n);
         else if (n.getNodeName().equals("and")) cond = loadAnd(n);
         else if (n.getNodeName().equals("or")) cond = loadOr(n);
         else if (n.getNodeName().equals("not")) cond = loadNot(n);
         n = n.getNextSibling();
      }

      FuzzyRule rule = new FuzzyRule(cond, output);

      NamedNodeMap map = node.getAttributes();
      n = map.getNamedItem("label");
      if (n != null)
      {
         addValue(n.getNodeValue().trim(), rule);
      }

      return rule;
   }

   private FuzzyAnd loadAnd(Node node) throws FuzzyException
   {
      FuzzyAnd and = new FuzzyAnd();

      Node n = node.getFirstChild();
      while (n != null)
      {
         if (n.getNodeName().equals("condition"))
            and.addCondition(loadCondition(n));
         n = n.getNextSibling();
      }

      return and;
   }

   private FuzzyOr loadOr(Node node) throws FuzzyException
   {
      FuzzyOr or = new FuzzyOr();

      Node n = node.getFirstChild();
      while (n != null)
      {
         if (n.getNodeName().equals("condition"))
            or.addCondition(loadCondition(n));
         n = n.getNextSibling();
      }

      return or;
   }

   private FuzzyNot loadNot(Node node) throws FuzzyException
   {
      FuzzyCondition cond = null;

      Node n = node.getFirstChild();
      while ((n != null)&&(cond == null))
      {
         if (n.getNodeName().equals("condition"))
            cond = loadCondition(n);
         n = n.getNextSibling();
      }
      return new FuzzyNot(cond);
   }

   private FuzzyValue loadValue(Node node) throws FuzzyException
   {
      FuzzyValue value = null;
      String label = null;

      NamedNodeMap map = node.getAttributes();

      Node n = map.getNamedItem("link");
      if (n != null)
      {
         value = getValue(n.getNodeValue().trim());
      }
      else
      {
         n = map.getNamedItem("label");
         if (n != null)
         {
            label = n.getNodeValue().trim();
         }

         n = map.getNamedItem("value");
         if (n != null)
         {
            value = new FuzzyConstant(Double.parseDouble(n.getNodeValue()));
            if (label != null)
            {
               addValue(label, value);
            }
         }
      }
      return value;
   }

   private FuzzyCondition loadCondition(Node node) throws FuzzyException
   {
      FuzzyCondition cond = null;
      FuzzyValue input = null;
      FuzzyValue start = null;
      FuzzyValue upstart = null;
      FuzzyValue upend = null;
      FuzzyValue end = null;
      FuzzyValue top = null;
      FuzzyValue width = null;
      String shape = "linear";

      NamedNodeMap map = node.getAttributes();
      Node n = map.getNamedItem("link");
      if (n != null)
      {
         cond = (FuzzyCondition)getValue(n.getNodeValue().trim());
      }
      else
      {
         n = map.getNamedItem("shape");
         if (n != null)
         {
            shape = n.getNodeValue().trim();
         }

         n = node.getFirstChild();
         while (n != null)
         {
            if (n.getNodeName().equals("input"))
               input = loadValue(n);
            else if (n.getNodeName().equals("start"))
               start = loadValue(n);
            else if (n.getNodeName().equals("up"))
               upstart = loadValue(n);
            else if (n.getNodeName().equals("fail"))
               upend = loadValue(n);
            else if (n.getNodeName().equals("end"))
               end = loadValue(n);
            else if (n.getNodeName().equals("top"))
               top = loadValue(n);
            else if (n.getNodeName().equals("width")) width = loadValue(n);
            n = n.getNextSibling();
         }

         if (shape.equals("linear"))
            cond = new FuzzyLinearCondition(input, start, upstart, upend, end);
         else cond = new FuzzyGaussianCondition(input, top, width);

         n = map.getNamedItem("label");
         if (n != null)
         {
            addValue(n.getNodeValue().trim(), cond);
         }
      }
      return cond;
   }
}
