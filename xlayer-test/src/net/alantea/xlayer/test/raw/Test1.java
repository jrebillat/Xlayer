package net.alantea.xlayer.test.raw;

import net.alantea.xlayer.Manager;

public class Test1
{
   static final String DONNEES_XML = "rawfile.xml";
   
   public static void main(String[] args)
   {
      Manager.addPackage("net.alantea.hypra.test.raw");
      Root root = new Root();
      Manager.parseFile(root, DONNEES_XML);
      Truc t = root.getTruc();
      System.out.println("OK " + t);
   }
}
