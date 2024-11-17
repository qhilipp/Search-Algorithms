package DataStructures;

import java.util.ArrayList;

public class Cartesian implements StateSpace<Vector> {

	private final int dimensions;
	private final int multiDimensionalMoveLimit;
	private final GoalTester goalTester;
	private final Measurement measurement = Measurement.EUCLIDEAN;
	
	public Cartesian(int dimensions, GoalTester goalTester) {
		this.dimensions = dimensions;
		this.multiDimensionalMoveLimit = dimensions;
		this.goalTester = goalTester;
	}
	
	@Override
	public Vector getStart() {
		return new Vector(this.dimensions);
	}

	@Override
	public ArrayList<Vector> getNeighbors(Vector node) {
		ArrayList<Vector> neighbors = new ArrayList<>();
		for(int dimensionDifferences = 1; dimensionDifferences <= multiDimensionalMoveLimit; dimensionDifferences++) {
			for(int[] subset : generateSubsets(dimensionDifferences)) {
				Vector neighbor = (Vector) node.clone();
				for(int index : subset) {
					neighbor.set(index, node.get(index) + 1);
					neighbor.set(index, node.get(index) - 1);
				}
				neighbors.add(neighbor);
			}
		}
		return neighbors;
	}

	@Override
	public boolean isGoal(Vector node) {
		return goalTester.test(node);
	}

	@Override
	public double getCost(Vector from, Vector to) {
		return from.distance(to, measurement);
	}
	
	private int[][] generateSubsets(int length) {
        ArrayList<int[]> result = new ArrayList<>();

        class Helper {
            void generateCombinations(int start, int[] current, int index) {
                if(index == length) {
                    result.add(current.clone());
                    return;
                }

                for(int i = start; i < dimensions; i++) {
                    current[index] = i;
                    generateCombinations(i + 1, current, index + 1);
                }
            }
        }

        new Helper().generateCombinations(0, new int[length], 0);

        return result.toArray(new int[0][]);
    }

	@FunctionalInterface
	public interface GoalTester {
		public boolean test(Vector vector);
	}
	
}
