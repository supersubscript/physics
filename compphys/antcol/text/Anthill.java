public class Anthill
{
   private int x;
   private int y;
   private int food = 0;
   private int type;

   public Anthill(int x, int y, int type)
   {
      this.x = x;
      this.y = y;
      this.type = type;
   }

   public void addFood(int amt)
   {
      food += amt;
   }

   public int getFood()
   {
      return food;
   }

   public int getType()
   {
      return type;
   }

   public int getX()
   {
      return x;
   }

   public int getY()
   {
      return y;
   }

   // Erase food in anthill (due to migration)
   public void nullFood()
   {
      food = 0;
   }

}
