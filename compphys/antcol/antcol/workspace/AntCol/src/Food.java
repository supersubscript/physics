public class Food
{
   private int x, y, amount;

   public Food(int x, int y, int amount)
   {
      this.x = x;
      this.y = y;
      this.amount = amount;
   }

   public int getAmt()
   {
      return amount;
   }

   public int getX()
   {
      return x;
   }

   public int getY()
   {
      return y;
   }

   public void setAmt(int amt)
   {
      this.amount = amt;
   }
}
