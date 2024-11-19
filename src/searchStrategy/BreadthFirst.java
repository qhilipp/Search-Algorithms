package searchStrategy;

public class BreadthFirst<Node> extends SearchStrategy<Node> {
	
	@Override
	public void add(Node node, double rating) {
		insert(node, rating, -1);
	}
	
}
