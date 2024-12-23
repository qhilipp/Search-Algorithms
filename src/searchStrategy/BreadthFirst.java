package searchStrategy;

public class BreadthFirst<Node> extends SearchStrategy<Node> {

	@Override
	public boolean shouldBeBehind(Node newPath, double newRating, Node existingPath, double existingRating) {
		return true;
	}
	
}
