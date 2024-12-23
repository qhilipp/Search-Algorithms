package searchStrategy;

import java.util.ArrayList;

public abstract class SearchStrategy<Path> {

	private ArrayList<PathRating> list = new ArrayList<>();
	private ArrayList<PathRating> history = new ArrayList<>();
	
	public abstract boolean shouldBeBehind(Path newPath, double newRating, Path existingPath, double existingRating);
	
	public Path get() {
		return list.removeFirst().path;
	}
	
	public PathRating read() {
		if(isEmpty()) return null;
		return list.getFirst();
	}
	
	public PathRating get(int index) {
		return list.get(index);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public void clear() {
		list.clear();
		history.clear();
	}
	
	public void add(Path path, double rating) {
		if(isEmpty()) {
			list.add(new PathRating(path, rating));
//			history.add(new PathRating(path, rating));
		}
		boolean addedToList = false, addedToHistory = false;
		for(int i = 0; i < list.size(); i++) {
			if(!shouldBeBehind(path, rating, list.get(i).path, list.get(i).rating)) {
				list.add(i, new PathRating(path, rating));
				addedToList = true;
				break;
			}
		}
		if(!addedToList) list.add(new PathRating(path, rating));
//		for(int i = 0; i < history.size(); i++) {
//			if(!shouldBeBehind(path, rating, history.get(i).path, history.get(i).rating)) {
//				history.add(i, new PathRating(path, rating));
//				addedToHistory = true;
//				break;
//			}
//		}
//		if(!addedToHistory) history.add(new PathRating(path, rating));
		
	}
	
	public ArrayList<PathRating> getHistory() {
		return history;
	}
	
	@Override
	public String toString() {
		String[] strs = new String[list.size()];
		for(int i = 0; i < strs.length; i++) strs[i] = list.get(i).path.toString();
		return "[" + String.join(", ", strs) + "]";
	}
	
	public class PathRating {
		public Path path;
		public double rating;
		
		PathRating(Path node, double rating) {
			this.path = node;
			this.rating = rating;
		}
	}
	
}
