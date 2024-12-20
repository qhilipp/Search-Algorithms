package stateSpace;

import java.util.ArrayList;
import java.util.HashMap;

import goalTest.GoalTester;
import util.Measurement;
import util.Nameable;
import util.Position;
import util.Vector;
import util.Copyable;

public class Cartesian<Node extends Position&Nameable&Copyable> extends StateSpace<Node> {

	private int multiDimensionalMoveLimit;
	private Measurement measurement = Measurement.EUCLIDEAN;
	private HashMap<Vector, Boolean> map = new HashMap<>();
	
	public Cartesian(Node start, int multiDimensionalMoveLimit, GoalTester<Node> goalTester, boolean[][][] map) {
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
	
	public Cartesian(Node start, int multiDimensionalMoveLimit, GoalTester<Node> goalTester, boolean[][] map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				this.map.put(new Vector(y, x), map[x][y]);
			}
		}
	}
	
	public Cartesian(Node start, int multiDimensionalMoveLimit, GoalTester<Node> goalTester, HashMap<Vector, Boolean> map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		this.map = map;
	}
	
	public Cartesian(Node start, int multiDimensionalMoveLimit, GoalTester<Node> goalTester) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
	}

	@Override
	public ArrayList<Node> getNeighbors(Node node) {
		ArrayList<Node> neighbors = new ArrayList<>();
		for(int dimensionDifferences = 1; dimensionDifferences <= multiDimensionalMoveLimit; dimensionDifferences++) {
			for(int[] subset : generateSubsets(dimensionDifferences)) {
				for(int distro = 0; distro < Math.pow(2, dimensionDifferences); distro++) {
					int distroCopy = distro;
					Vector neighborPosition = node.getPosition();
					for(int index : subset) {
						int offset = distroCopy % 2 == 0 ? -1 : 1;
						neighborPosition.set(index, node.getPosition().get(index) + offset);
						distroCopy /= 2;
					}
					Node neighbor = (Node) node.copy();
					neighbor.setPosition(neighborPosition);
					if(!map.containsKey(neighbor.getPosition()) || map.get(neighbor.getPosition())) neighbors.add(neighbor);
				}
			}
		}
		return neighbors;
	}

	@Override
	public double getCost(Node from, Node to) {
		return from.getPosition().distance(to.getPosition(), measurement);
	}
	
	public int getDimensions() {
		return getStart().getPosition().getDimensions();
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
	
	public boolean getMap(Vector position) {
		return map.containsKey(position) && map.get(position);
	}
	
	public void toggleMap(Node node) {
		map.put(node.getPosition(), !getMap(node.getPosition()));
	}
	
	public void setMap(boolean value, Node node) {
		map.put(node.getPosition(), value);
	}
	
	public void setMap(boolean value, double...components) {
		map.put(new Vector(components), value);
	}
}
