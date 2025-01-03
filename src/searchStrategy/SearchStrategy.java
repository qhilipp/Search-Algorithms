package searchStrategy;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * determines when GeneralSearch visits which node according to the PathSorters
 * PathSorters that appear earlier in the list have higher importance
 * @param <Node>
 */
public class SearchStrategy<Node> {

	private ArrayList<PathRating> list = new ArrayList<>();
	private ArrayList<PathRating> history = new ArrayList<>();
	private ArrayList<PathSorter<Node>> pathSorters = new ArrayList<>();
	
	/**
	 * @param 	pathSorters	the PathSorters that are being used
	 */
	public SearchStrategy(ArrayList<PathSorter<Node>> pathSorters) {
		this.pathSorters = pathSorters;
	}
	
	/**
	 * @param 	pathSorters	the PathSorters that are being used
	 */
	public SearchStrategy(PathSorter<Node>...pathSorters) {
		for(PathSorter<Node> pathSorter : pathSorters) this.pathSorters.add(pathSorter);
	}
		
	/**
	 * returns the next path along with its rating and removes it
	 * @return	the next path
	 */
	public ArrayList<Node> get() {
		return list.removeFirst().path;
	}
	
	/**
	 * returns the next path along with its rating without removing it
	 * @return	the next path
	 */
	public PathRating read() {
		if(isEmpty()) return null;
		return list.getFirst();
	}
	
	/**
	 * returns the path along with its rating at the given index
	 * @param 	index the index of the desired path and rating
	 * @return	the path and rating
	 */
	public PathRating get(int index) {
		return list.get(index);
	}
	
	/**
	 * @return whether or not the list of paths is empty
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	/**
	 * removes all paths
	 */
	public void clear() {
		list.clear();
		history.clear();
	}
	
	/**
	 * inserts the given path and the rating into the list of paths at the correct position according to the PathSorters
	 * @param 	path
	 * @param 	rating
	 */
	public void add(ArrayList<Node> path, double rating) {
		insert(path, rating, list);
		insert(path, rating, history);
	}
	
	/**
	 * inserts the given path and rating into the given list at the correct position according to the PathSorters
	 * @param 	path	the path that is being added
	 * @param 	rating	the rating that is being added
	 * @param 	list	the list that the path and rating should be added to
	 */
	private void insert(ArrayList<Node> path, double rating, ArrayList<PathRating> list) {
		list.add(new PathRating(path, rating));
		for(PathSorter<Node> pathSorter : pathSorters.reversed()) {
			list.sort(new Comparator<PathRating>() {
				@Override
				public int compare(SearchStrategy<Node>.PathRating o1, SearchStrategy<Node>.PathRating o2) {
					return pathSorter.shouldPrecede(o1.path, o1.rating, o2.path, o2.rating) ? -1 : 1;
				}
			});
		}
	}
	
	/**
	 * @return all pahts and ratings that have ever been added
	 */
	public ArrayList<PathRating> getHistory() {
		return history;
	}
	
	@Override
	public String toString() {
		String[] strs = new String[list.size()];
		for(int i = 0; i < strs.length; i++) strs[i] = list.get(i).path.toString();
		return "[" + String.join(", ", strs) + "]";
	}
	
	/**
	 * a helper class that combines a path and rating
	 */
	public class PathRating {
		public ArrayList<Node> path;
		public double rating;
		
		/**
		 * @param 	node
		 * @param 	rating
		 */
		PathRating(ArrayList<Node> node, double rating) {
			this.path = node;
			this.rating = rating;
		}
	}
	
}
