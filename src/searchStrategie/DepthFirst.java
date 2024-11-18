package searchStrategie;

public class DepthFirst<Node> extends SearchStrategy<Node> {

	@Override
	public void add(Node node, double rating) {
		insert(node, rating, 0);
	}

}
