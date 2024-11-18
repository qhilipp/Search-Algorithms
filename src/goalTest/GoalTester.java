package goalTest;

@FunctionalInterface
public interface GoalTester<Node> {

	public boolean isGoal(Node node);
	
}
