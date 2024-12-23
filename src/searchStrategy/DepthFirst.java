package searchStrategy;

import java.util.ArrayList;

public class DepthFirst<Node> implements PathSorter<Node> {

	@Override
	public boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating) {
		return newPath.size() > existingPath.size();
	}

}
