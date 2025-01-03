package goalTest;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * a GoalTester that specifies the goals with a list of goal nodes
 * @param <Node>
 */
public class ListGoalTester<Node> implements GoalTester<Node> {

	private ArrayList<Node> goals;
	
	/**
	 * all given nodes are goals, all other are not
	 * @param 	goals	the nodes that are goals
	 */
	public ListGoalTester(Node...goals) {
		this.goals = new ArrayList<>(Arrays.asList(goals));
	}
	
	/**
	 * all given nodes are goals, all other are not
	 * @param 	goals	the nodes that are goals
	 */
	public ListGoalTester(ArrayList<Node> goals) {
		this.goals = goals;
	}
	
	/**
	 * add a node to the list of goals
	 * @param 	goal	the node that is being added to the goals 
	 */
	public void add(Node goal) {
		goals.add(goal);
	}
	
	/**
	 * remove a node from the list of goals
	 * @param 	goal	the node that is being removed from the goals
	 */
	public void remove(Node goal) {
		goals.remove(goal);
	}
	
	/**
	 * return all goal nodes
	 * @return	the goal nodes
	 */
	public ArrayList<Node> getGoals() {
		return goals;
	}
	
	@Override
	public boolean isGoal(Node node) {
		for(Node goal : goals) {
			if(goal.equals(node)) return true;
		}
		return false;
	}

}
