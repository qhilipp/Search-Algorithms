package searchStrategy;

import java.util.ArrayList;

public class Uniform<Node> implements PathSorter<Node> {

	@Override
	public boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating) {
		return newRating <= existingRating;
	}

}
