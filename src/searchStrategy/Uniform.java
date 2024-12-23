package searchStrategy;

public class Uniform<Node> extends SearchStrategy<Node> {

	@Override
	public boolean shouldBeBehind(Node newPath, double newRating, Node existingPath, double existingRating) {
		return newRating > existingRating;
	}

}
