package searchStrategy;

import java.util.ArrayList;

public interface PathSorter<Node> {

	public abstract boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating);
	
}
