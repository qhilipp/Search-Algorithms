package searchStrategy;

import java.util.ArrayList;

/**
 * sorts the paths based on their length in descending order
 * @param	<Node>	the node type used by the StateSpace
 */
public class DepthFirst<Node> implements PathSorter<Node> {

	@Override
	public boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating) {
		return newPath.size() > existingPath.size();
	}

}
