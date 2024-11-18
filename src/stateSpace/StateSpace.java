package stateSpace;

import java.util.ArrayList;

public interface StateSpace<Node> {
	public Node getStart();
	public ArrayList<Node> getNeighbors(Node node);
	public boolean isGoal(Node node);
	public double getCost(Node from, Node to);
	public double futureCost(Node node);
}
