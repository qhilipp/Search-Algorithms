package searchStrategy;

import java.util.ArrayList;

/**
 * sorts the paths based on their rating in ascending order
 * @param <Node>
 */
public class Uniform<Node> implements PathSorter<Node> {

	@Override
	public boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating) {
		return newRating <= existingRating;
	}

}
