package stateSpace;

import java.util.ArrayList;
import java.util.HashMap;

import goalTest.GoalTester;
import util.Measurement;
import util.Vector;

public class Cartesian extends StateSpace<Vector> {

	private int multiDimensionalMoveLimit;
	private Measurement measurement = Measurement.EUCLIDEAN;
	private HashMap<Vector, Boolean> map = new HashMap<>();
	
	public Cartesian(Vector start, int multiDimensionalMoveLimit, GoalTester<Vector> goalTester, boolean[][][] map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				for(int z = 0; z < map[0][0].length; z++) {
					this.map.put(new Vector(x, y, z), map[x][y][z]);
				}
			}
		}
	}
	
	public Cartesian(Vector start, int multiDimensionalMoveLimit, GoalTester<Vector> goalTester, boolean[][] map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				this.map.put(new Vector(x, y), map[x][y]);
			}
		}
	}
	
	public Cartesian(Vector start, int multiDimensionalMoveLimit, GoalTester<Vector> goalTester, HashMap<Vector, Boolean> map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		this.map = map;
	}
	
	public Cartesian(Vector start, int multiDimensionalMoveLimit, GoalTester<Vector> goalTester) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
	}

	@Override
	public ArrayList<Vector> getNeighbors(Vector node) {
		ArrayList<Vector> neighbors = new ArrayList<>();
		for(int dimensionDifferences = 1; dimensionDifferences <= multiDimensionalMoveLimit; dimensionDifferences++) {
			for(int[] subset : generateSubsets(dimensionDifferences)) {
				for(int distro = 0; distro < Math.pow(2, dimensionDifferences); distro++) {
					int distroCopy = distro;
					Vector neighbor = (Vector) node.clone();
					for(int index : subset) {
						int offset = distroCopy % 2 == 0 ? -1 : 1;
						neighbor.set(index, node.get(index) + offset);
						distroCopy /= 2;
					}
					if(!map.containsKey(neighbor) || map.get(neighbor)) neighbors.add(neighbor);
				}
			}
		}
		return neighbors;
	}

	@Override
	public double getCost(Vector from, Vector to) {
		return from.distance(to, measurement);
	}
	
	public int getDimensions() {
		return getStart().getDimensions();
	}
	
	public int[][] generateSubsets(int length) {
        ArrayList<int[]> result = new ArrayList<>();

        class Helper {
            void generateCombinations(int start, int[] current, int index) {
                if(index == length) {
                    result.add(current.clone());
                    return;
                }

                for(int i = start; i < getDimensions(); i++) {
                    current[index] = i;
                    generateCombinations(i + 1, current, index + 1);
                }
            }
        }

        new Helper().generateCombinations(0, new int[length], 0);

        return result.toArray(new int[0][]);
    }
	
	public int getMultiDimensionalMoveLimit() {
		return multiDimensionalMoveLimit;
	}
	
	public void setMultiDimensionalMoveLimit(int multiDimensionalMoveLimit) {
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
	}
	
	public Measurement getMeasurement() {
		return measurement;
	}
	
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
	
	public boolean getMap(Vector node) {
		return map.get(node);
	}
	
	public boolean getMap(double...components) {
		return map.get(new Vector(components));
	}
	
	public void setMap(boolean value, Vector node) {
		map.put(node, value);
	}
	
	public void setMap(boolean value, double...components) {
		map.put(new Vector(components), value);
	}
}
