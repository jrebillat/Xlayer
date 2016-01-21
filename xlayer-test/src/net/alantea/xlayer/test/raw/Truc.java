package net.alantea.xlayer.test.raw;

import java.util.ArrayList;
import java.util.List;

public class Truc
{
   int machin;
   List<Packed> packedElts = new ArrayList<>();
   @SuppressWarnings("unused")
   private Bidule bidule;
   List<Bidule> bidules = new ArrayList<>();
   Bidule one;
   Bidule2 two;

   
   public Truc()
   {
      System.out.println("creating truc");
   }
   /**
    * @return the machin
    */
   public int getMachin()
   {
      return machin;
   }

   /**
    * @param machin the machin to set
    */
   public void setMachin(int machin)
   {
      System.out.println("set machin = " + machin);
      this.machin = machin;
   }


   /**
    * @param bidule the bidule to set
    */
   public void setBidule(Bidule bidule)
   {
      System.out.println("set bidule");
      this.bidule = bidule;
   }
   
   /**
    * @return the Packeds
    */
   public List<Packed> getPackeds()
   {
      return packedElts;
   }
   
   /**
    * @param houplas the Packeds to set
    */
   public void setPackeds(List<Packed> packeds)
   {
      this.packedElts = packeds;
   }
   
   public void addAll(List<Bidule> bids)
   {
      bidules.addAll(bids);
   }

   /**
    * @param truc the truc to set
    */
   public void biduloide(Bidule one, Bidule2 two)
   {
      this.one = one;
      this.two = two;
   }
   
   /**
    * @param truc the truc to set
    */
   public int biduler()
   {
      return 12;
   }
}
