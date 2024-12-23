package searchStrategy;

import java.util.ArrayList;
import java.util.Comparator;

public class SearchStrategy<Node> {

	private ArrayList<PathRating> list = new ArrayList<>();
	private ArrayList<PathRating> history = new ArrayList<>();
	private ArrayList<PathSorter<Node>> pathSorters = new ArrayList<>();
	
	public SearchStrategy(ArrayList<PathSorter<Node>> pathSorters) {
		this.pathSorters = pathSorters;
	}
	
	public SearchStrategy(PathSorter<Node>...pathSorters) {
		for(PathSorter<Node> pathSorter : pathSorters) this.pathSorters.add(pathSorter);
	}
		
	public ArrayList<Node> get() {
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
	
	public void add(ArrayList<Node> path, double rating) {
		insert(path, rating, list);
		insert(path, rating, history);
	}
	
	private void insert(ArrayList<Node> path, double rating, ArrayList<PathRating> list) {
		list.add(new PathRating(path, rating));
		for(PathSorter<Node> pathSorter : pathSorters) {
			list.sort(new Comparator<PathRating>() {
				@Override
				public int compare(SearchStrategy<Node>.PathRating o1, SearchStrategy<Node>.PathRating o2) {
					return pathSorter.shouldPrecede(o1.path, o1.rating, o2.path, o2.rating) ? -1 : 1;
				}
			});
		}
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
		public ArrayList<Node> path;
		public double rating;
		
		PathRating(ArrayList<Node> node, double rating) {
			this.path = node;
			this.rating = rating;
		}
	}
	
}
