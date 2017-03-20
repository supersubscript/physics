package experiments;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

import general.Distribution;
import general.Prime;
import general.Sequence;
import organisms.Plant;
import physics.ToroidalCoordinateSystem;
import physics.World;
import settings.CrossOverOperator;
import settings.DaytimeRule;
import settings.LocationFilter;
import settings.MutationOperator;
import settings.RealReader;
import settings.SelectionRule;
import settings.Settings;

/**
 * An experiment where Organisms are evolved in an analogy to plants and their evolutionary trade-off for height.
 * Higher plants receive more light, but cost more energy.
 * In this experiment, plants live in a toroidal grid world.
 * Height is encoded directly in the genome of a plant as a single real number.
 * Each plant receives the maximal amount of light when it is the highest plant in its neighborhood, and slightly less when it is the second highest, etc. decreasing exponentially.
 * Ex aequos are resolved randomly.
 * The Plant genome also contains the probability of attempting reproduction at each tick.
 * 
 * This experiment is designed to not have a heterogeneous Nash equilibrium and therefore encourage speciation or oscillation.
 * A population of high plants leaves a niche for very short plants which receive the residual light.
 * A population of short plants can be exploited by any slightly larger plant.
 */
public class _4_PlantHeightEvolution {
	
	public static void main(String[] args) {
		
		int gridSize  			= 20;
		double deathRate        = 0.05;       
		double gamma 			= 0.7;
		int initialGenomeSize	= 200;
		double substitProb 		= 0.0001;
		double indelProb 		= 0.00001;
		double initialEnergy 	= 2.;
		int spawnDistance       = 3;
		int initialPop 			= 200;
		int printStep           = 100;
		int duration 			= 100000;
		
		ToDoubleFunction<Plant> reproductionCost = (Plant p) -> initialEnergy + p.getValue(0);
		
		// Reading the genome
		Sequence consensus     = new Sequence(new boolean[]{true,true,false,true,true});
		Sequence separator     = new Sequence(new boolean[]{false,false,true,true,true,false,false});
		int valuediam          = 3;
		RealReader internalReader = RealReader.applyFunction(RealReader.gray(), (x) -> Math.exp(20./Prime.get(x.intValue())));
		RealReader reader      = RealReader.consensus(consensus, valuediam, internalReader);
		
		// Settings
		Settings<Plant> settings = new Settings<>();
			// Plants are in a grid
		settings.setCoordinateSystem(new ToroidalCoordinateSystem(gridSize));
			// Plants die when below energy, randomly and when their genome is too large
		settings.setDeathRule(Plant.tresholdEnergy(0.));
		settings.setDeathRule(SelectionRule.constantRate(deathRate));
		settings.setDeathRule(SelectionRule.directApply((Plant p) -> p.getValues().size() > 2));
			// Plants reproduce at a rate defined by the second number in their genome
		settings.setSplittingRule(SelectionRule.directApply((Plant p) -> p.getValue(1, 0.)));
			// Plants receive light according tot the height distribution rule
		settings.setDaytimeRuleRule(heightDistributionRule(gamma, 1));
			// Plants spawn only in empty cells and at most 1 cell away from their parent(s)
		settings.setSpawnRule(LocationFilter.empty(), spawnDistance);
			// Plant biology has the following parameters
		settings.setOrganismFactory(new Plant.Factory(
					// Initial genome size is constant
				Distribution.constant(initialGenomeSize),
					// Mutation happens with a constant substitution and indel rate (only indels of size 1)
				MutationOperator.combine(
						MutationOperator.poisson_sub(substitProb),
						MutationOperator.poisson_indel(indelProb)
						), 
					// Crossover happens according to the singleSynapse mechanism
				CrossOverOperator.singleSynapse(0), 
					// Real numbers are read from the binary genome according to the mechanism described above
				reader, 
					// Subsequences representing real numbers in the genome are separated by the given Sequence
				separator, 
					// Plants are born with initial energy given by
				initialEnergy, 
					// Reproduction costs plants energy given by
				reproductionCost));
		
		// World
		World<Plant> w = new World<>(settings);
		for (int i=0; i<initialPop; i++) w.spawnRandom();
		
		// Time
		try {
			PrintStream ps = new PrintStream("Plants.txt");
			
			for (int i=0; i<duration; i++) {
				w.tick();
				if (i % printStep == 0) {
					for (Plant p : w.getPopulation()) {
						System.out.println(i+", " + p);
						ps.println(i+", " + p);
					}
				}
			}
			
			ps.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		
	}
	
	/**
	 * Distribute light according to Plant size.
	 * The first double in the Plant's phenotype is its height.
	 * The light it receives is 1, times gamma for each Plant in its neighborhood that is higher. 
	 */
	public static DaytimeRule<Plant> heightDistributionRule(double gamma, int neighborDistance) {
		return new DaytimeRule<Plant>() {
			@Override
			public double light(World<Plant> w, Plant o) {
				List<Plant> neighbors = new ArrayList<>(w.getNeighbors(o, neighborDistance));
				neighbors.add(o);
				Collections.shuffle(neighbors);
				neighbors.sort(new Comparator<Plant>(){
					@Override
					public int compare(Plant o1, Plant o2) {
						Double height1 = o1.getValue(0);
						Double height2 = o2.getValue(0);
						return -height1.compareTo(height2);
					}
				});
				return Math.pow(gamma, 1 + neighbors.indexOf(o));
			}
		};
	}

}
