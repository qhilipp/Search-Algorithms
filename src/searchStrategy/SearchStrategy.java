package searchStrategy;

import java.util.ArrayList;

public abstract class SearchStrategy<Node> {

	private ArrayList<NodeRating> list = new ArrayList<>();	
	
	public abstract void add(Node node, double rating);
	
	public Node read() {
		return list.get(0).node;
	}
	
	public Node get() {
		return list.removeFirst().node;
	}
	
	public NodeRating get(int index) {
		return list.get(index);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public void clear() {
		list.clear();
	}
	
	protected void insert(Node node, double rating, int index) {
		list.add(index < 0 ? list.size() + index + 1 : index, new NodeRating(node, rating));
	}
	
	public ArrayList<NodeRating> getList() {
		return list;
	}
	
	@Override
	public String toString() {
		String[] strs = new String[list.size()];
		for(int i = 0; i < strs.length; i++) strs[i] = list.get(i).node.toString();
		return "[" + String.join(", ", strs) + "]";
	}
	
	public class NodeRating {
		public Node node;
		public double rating;
		
		NodeRating(Node node, double rating) {
			this.node = node;
			this.rating = rating;
		}
	}
	
}
