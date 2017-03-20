import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class AntPanel extends JPanel
{
   // private Image screenBuffer;
   Random rand = new Random();
   Square[][] squares;
   private ArrayList<Food> food;
   private ArrayList<Anthill> anthills;

   public AntPanel(final int width, final int height)
   {
   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      // Draw all ants
      for (int i = 0; i < squares.length; i++)
         for (int j = 0; j < squares.length; j++)
         {
            int[][] ants = squares[i][j].getAnts();

            // Draw first ant type
            g.setColor(Color.BLUE);
            for (int k = 0; k < ants[0][0] + ants[0][1]; k++)
               g.fillOval(Common.SQSIZE * i + rand.nextInt(Common.SQSIZE),
                     Common.SQSIZE * j + rand.nextInt(Common.SQSIZE), 3, 3);

            // Draw second ant type
            g.setColor(Color.RED);
            for (int k = 0; k < ants[1][0] + ants[1][1]; k++)
               g.fillOval(Common.SQSIZE * i + rand.nextInt(Common.SQSIZE),
                     Common.SQSIZE * j + rand.nextInt(Common.SQSIZE), 3, 3);
         }

      // Draw all food
      g.setColor(Color.GREEN);
      for (int i = 0; i < food.size(); i++)
      {
         Food f = food.get(i); // Stupid errors if for-each ...
         int radius = (int) Math.sqrt(f.getAmt());
         g.fillOval((int) (f.getX() * Common.SQSIZE),
               (int) (f.getY() * Common.SQSIZE), (int) Math.sqrt(f.getAmt()),
               radius);
      }

      // Draw all anthills
      for (int i = 0; i < anthills.size(); i++)
      {
         Anthill hill = anthills.get(i); // Stupid errors if for-each ...

         if (hill.getType() == 0)
            g.setColor(Color.BLACK);
         else
            g.setColor(Color.MAGENTA);

         g.fillRect((int) (hill.getX() * Common.SQSIZE),
               (int) (hill.getY() * Common.SQSIZE), 10, 10);
      }
   }

   public void update(Square[][] squares, ArrayList<Food> food,
         ArrayList<Anthill> anthills)
   {
      this.squares = squares;
      this.food = food;
      this.anthills = anthills;
   }
}