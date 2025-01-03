package goalTest;

/**
 * tells the StateSpace whether a given Node is a goal or not
 * @param 	<Node> 	the Node type used by the StateSpace
 */
@FunctionalInterface
public interface GoalTester<Node> {

	/**
	 * 
	 * @param 	node 	the node that you want to test
	 * @return			whether the node is a goal or not
	 */
	public boolean isGoal(Node node);
	
}
