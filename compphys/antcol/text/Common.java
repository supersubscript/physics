/* Gathering class for settings and utilities */
public class Common
{
   public static final int ITERATIONS = 3000;
   // Starting ants per race
   public static final int ANTHILLANTS = 1000000;
   // New ants when migration occurs
   public static final int NEWANTHILLPOP = 0;
   // Root of number of squares
   public static final int MATRIXSIZE = 100;
   // Pixels in frame
   public static final int FRAMESIZE = 1000;
   // Size of a separate square in graphical rep.
   public static final int SQSIZE = FRAMESIZE / MATRIXSIZE;
   // Distance constant for how new anthills to occur
   public static final int ANTHILLDIST = 40;
   // Number of new foodstacks
   public static final int FOODSTACKS = 3;
   // New food size max value
   public static final int FOODSTACKSIZE = 15000;
   // Food limit for when to commence migrationary measures
   public static final int MIGRATE_LIMIT = 8000;
   // Maximum amount of anthills
   public static final int ANTHILL_LIMIT = 25;
   // New ants in anthill per timestep
   public static final int ANTGENERATION = 1;
   // Bias to go towards food
   public static final double FOODBIAS = 3.;
   // Bias to return home when having gathered food
   public static final double HOMEBIAS = 10000.;
   // Combat modifying constant
   public static final double COMBATCONST = 0.001;
   // Chance of new food during timestep
   public static final double FOODCHANCE = 1 / 100.;
   // Constant for chance of standing still
   public static final double NOTHING_DIR = 10.;
}