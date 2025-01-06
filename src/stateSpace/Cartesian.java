package stateSpace;

import java.util.ArrayList;
import java.util.HashMap;

import goalTest.GoalTester;
import util.Measurement;
import util.Nameable;
import util.Position;
import util.Vector;
import util.Copyable;

/**
 * a potentially infinite graph of nodes arranged in an n-dimensional Cartesian space
 * @param	<Node>	the node type of the StateSpace
 */
public class Cartesian<Node extends Position&Nameable&Copyable> extends StateSpace<Node> {

	private int multiDimensionalMoveLimit;
	private Measurement measurement = Measurement.EUCLIDEAN;
	private HashMap<Vector, Boolean> map = new HashMap<>();
	
	/**
	 * @param	start						the starting node
	 * @param	multiDimensionalMoveLimit	the maximum number of dimensions two neighboring nodes can be different from each other to still have a connection between each other
	 * @param 	goalTester					the goal tester
	 * @param 	map							a 3-dimensional boolean array indicating which nodes can have neighbors and which cannot
	 */
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
	
	/**
	 * @param	start						the starting node
	 * @param	multiDimensionalMoveLimit	the maximum number of dimensions two neighboring nodes can be different from each other to still have a connection between each other
	 * @param 	goalTester					the goal tester
	 * @param 	map							a 2-dimensional boolean array indicating which nodes can have neighbors and which cannot
	 */
	public Cartesian(Node start, int multiDimensionalMoveLimit, GoalTester<Node> goalTester, boolean[][] map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				this.map.put(new Vector(y, x), map[x][y]);
			}
		}
	}
	
	/**
	 * @param	start						the starting node
	 * @param	multiDimensionalMoveLimit	the maximum number of dimensions two neighboring nodes can be different from each other to still have a connection between each other
	 * @param 	goalTester					the goal tester
	 * @param 	map							a HashMap indicating which nodes can have neighbors and which cannot
	 */
	public Cartesian(Node start, int multiDimensionalMoveLimit, GoalTester<Node> goalTester, HashMap<Vector, Boolean> map) {
		super(start, goalTester);
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
		this.map = map;
	}
	
	/**
	 * @param	start						the starting node
	 * @param	multiDimensionalMoveLimit	the maximum number of dimensions two neighboring nodes can be different from each other to still have a connection between each other
	 * @param 	goalTester					the goal tester
	 */
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
	
	/**
	 * @return	the number of dimensions that the StateSpace has
	 */
	public int getDimensions() {
		return getStart().getPosition().getDimensions();
	}
	
	/**
	 * a helper function for the neighbor calculation
	 * @param 	length	the length of the sub-arrays
	 * @return	the array of all sub-arrays of the specified length
	 */
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
	
	/**
	 * @return	the maximum number of dimensions two neighboring nodes can be different from each other to still have a connection between each other
	 */
	public int getMultiDimensionalMoveLimit() {
		return multiDimensionalMoveLimit;
	}
	
	/**
	 * sets the maximum number of dimensions two neighboring nodes can be different from each other to still have a connection between each other
	 * @param multiDimensionalMoveLimit	the new value
	 */
	public void setMultiDimensionalMoveLimit(int multiDimensionalMoveLimit) {
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
	}
	
	/**
	 * @return	the measurement that is being used for distance calculations
	 */
	public Measurement getMeasurement() {
		return measurement;
	}
	
	/**
	 * sets the measurement that is being used for distance calculations
	 * @param measurement	the new value
	 */
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
	
	/**
	 * @param	position	the position of the node of which the state is requested
	 * @return	whether or not the node at the given position
	 */
	public boolean isEnabled(Vector position) {
		return map.containsKey(position) && map.get(position);
	}
	
	/**
	 * toggles whether or not the given node is enabled
	 * @param 	node	the node that is being toggled
	 */
	public void toggleEnabled(Node node) {
		map.put(node.getPosition(), !isEnabled(node.getPosition()));
	}
	
	/**
	 * enables or disables the given node according to the given boolean
	 * @param 	value	whether or not the given node should be enabled
	 * @param 	node	the node that should be modified
	 */
	public void setEnabled(boolean value, Node node) {
		map.put(node.getPosition(), value);
	}
	
	/**
	 * enables or disables the node with the given position components according to the given boolean
	 * @param 	value		whether or not the given node should be enabled
	 * @param 	components	the components of the position of the node that should be modified
	 */
	public void setEnabled(boolean value, double...components) {
		map.put(new Vector(components), value);
	}
}
