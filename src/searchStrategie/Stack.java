package searchStrategie;

import java.util.ArrayList;

public class Stack<Node> implements SearchStrategy<Node> {

	private ArrayList<Node> list = new ArrayList<>();
	
	@Override
	public void add(Node node) {
		list.add(node);
	}

	@Override
	public Node get() {
		return list.removeLast();
	}
	
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

}
