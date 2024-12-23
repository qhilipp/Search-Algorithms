package searchStrategy;

import java.util.ArrayList;

public class Alphabetical<Node> implements PathSorter<Node> {

	boolean ascending = false;
	
	public Alphabetical() {}
	
	public Alphabetical(boolean ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public boolean shouldPrecede(ArrayList<Node> newPath, double newRating, ArrayList<Node> existingPath, double existingRating) {
		return newPath.getLast().toString().compareTo(existingPath.getLast().toString()) < 0 == ascending;
	}

}
