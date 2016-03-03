package experiments;

import java.util.HashSet;
import java.util.Set;

import organisms.Trivial;
import physics.CoordinateSystem;
import physics.ToroidalCoordinateSystem;
import physics.ToroidalCoordinateSystem.ToroidalCoordinate;
import physics.World;
import settings.LocationFilter;
import settings.SelectionRule;
import settings.Settings;

/**
 * This toy examples implements the rules of Conway's Game of Life into the evolution framework.
 * Note that, due to the nature of the implementation, the actual Game of Life is not fully expressible.
 * More precisely, every cell can create only at most one offspring.
 * Multiple cells can also try to place their offspring in the came cell, in which case one division will be aborted.
 */
public class _1_ConwaysGameOfLife {
	
	public static String printGrid(World<?> w, int gridSize) {
		char[] output = new char[gridSize*gridSize];
		for (int i=0; i<gridSize*gridSize; i++) output[i]='.';
		for (CoordinateSystem.Coordinate co : w.getOccupiedLocations())
			output[((ToroidalCoordinate)co).left + gridSize*((ToroidalCoordinate)co).right] = '#';
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<output.length; i++) {
			if (i>0 && i%gridSize == 0) builder.append('\n');
			builder.append(output[i]);
		}
		return builder.toString();
	}
	
	public static void main(String[] args) {
		
		// Settings
		int gridSize = 20;
		
		ToroidalCoordinateSystem cs = new ToroidalCoordinateSystem(gridSize);
		Set<CoordinateSystem.Coordinate> startingLocations = new HashSet<>();
			// Start with a blinker
			startingLocations.add(cs.get(9, 10));
			startingLocations.add(cs.get(10,10));
			startingLocations.add(cs.get(11,10));
		
		Settings<Trivial> settings = new Settings<>();
		settings.setDeathRule(SelectionRule.maxNeighbors(1, 1));
		settings.setDeathRule(SelectionRule.minNeighbors(4, 1));
		settings.setSplittingRule(SelectionRule.trivialTrue());
		settings.setOrganismFactory(new Trivial.Factory());
		settings.setCoordinateSystem(cs);
		settings.setSpawnRule(LocationFilter.neighbors(3, 1), 1);
		
		// World
		World<Trivial> w = new World<>(settings);
		for (CoordinateSystem.Coordinate co : startingLocations) w.spawnOrganism(settings.getOrganismFactory().random(), co);
		
		System.out.println(printGrid(w, gridSize));
		
		// Time
		w.tick();
		
		System.out.println(printGrid(w, gridSize));
		
		w.tick();
		
		System.out.println(printGrid(w, gridSize));
		
		
	}

}
