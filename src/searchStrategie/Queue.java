package searchStrategie;

import java.util.ArrayList;

public class Queue<Node> implements SearchStrategy<Node> {

	private ArrayList<Node> list = new ArrayList<>();
	
	@Override
	public void add(Node node) {
		list.add(node);
	}

	@Override
	public Node get() {
		return list.removeFirst();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	@Override
	public String toString() {
		String str = "";
		for(Node node : list) {
			str += node.toString() + ", ";
		}
		return str;
	}
	
}
