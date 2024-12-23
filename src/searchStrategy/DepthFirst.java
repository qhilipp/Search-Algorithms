package searchStrategy;

public class DepthFirst<Node> extends SearchStrategy<Node> {

	@Override
	public boolean shouldBeBehind(Node newPath, double newRating, Node existingPath, double existingRating) {
		return false;
	}

}
