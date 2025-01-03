package searchStrategy;

import java.util.ArrayList;

/**
 * an interface used to sort paths
 * @param	<Node>	the node type used by the StateSpace
 */
public interface PathSorter<Node> {

	/**
	 * @param	newPath			the path that is being added to the list
	 * @param	newRating		the rating of the path that is being added to the list
	 * @param	existingPath	the path that the new path is getting compared with and that is already in the list
	 * @param	existingRating	the rating of the path that is already in the list
	 * @return	whether or not the newPath should precede the existing path in the list
	 */
	public abstract boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating);
	
}
