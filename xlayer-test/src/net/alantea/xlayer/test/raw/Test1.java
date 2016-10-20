package net.alantea.xlayer.test.raw;

import java.util.List;

import net.alantea.xlayer.Manager;

public class Test1
{
   static final String DONNEES_XML = "/rawfile.xml";
   
   public static void main(String[] args)
   {
      Manager manager = new Manager();
      manager.addPackage("net.alantea.xlayer.test.raw");
      Root root = new Root();
      List<String> errors = manager.parseResource(root, DONNEES_XML);
      for (String err : errors)
      {
         System.out.println(err);
      }
      Truc t = root.getTruc();
      System.out.println("OK " + t);
   }
}
