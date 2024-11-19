package stateSpace;

import java.util.ArrayList;

import goalTest.GoalTester;
import util.Measurement;
import util.Vector;

public class Cartesian extends StateSpace<Vector> {

	private final int dimensions;
	private final int multiDimensionalMoveLimit;
	private final Measurement measurement = Measurement.EUCLIDEAN;
	
	public Cartesian(int dimensions, int multiDimensionalMoveLimit, GoalTester<Vector> goalTester) {
		super(goalTester);
		this.dimensions = dimensions;
		this.multiDimensionalMoveLimit = multiDimensionalMoveLimit;
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
				for(int distro = 0; distro < Math.pow(2, dimensionDifferences); distro++) {
					int distroCopy = distro;
					Vector neighbor = (Vector) node.clone();
					for(int index : subset) {
						int offset = distroCopy % 2 == 0 ? -1 : 1;
						neighbor.set(index, node.get(index) + offset);
						distroCopy /= 2;
					}
					neighbors.add(neighbor);
				}
			}
		}
		return neighbors;
	}

	@Override
	public double getCost(Vector from, Vector to) {
		return from.distance(to, measurement);
	}
	
	public int[][] generateSubsets(int length) {
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
}
