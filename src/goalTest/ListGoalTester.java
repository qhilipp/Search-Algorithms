package goalTest;

import java.util.ArrayList;
import java.util.Arrays;

public class ListGoalTester<Node> implements GoalTester<Node> {

	private ArrayList<Node> goals;
	
	public ListGoalTester(Node...goals) {
		this.goals = new ArrayList<>(Arrays.asList(goals));
	}
	
	public ListGoalTester(ArrayList<Node> goals) {
		this.goals = goals;
	}
	
	@Override
	public boolean isGoal(Node node) {
		for(Node goal : goals) {
			if(goal.equals(node)) return true;
		}
		return false;
	}

}