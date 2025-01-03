package loopHandling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * only visits nodes if they have not been visited already or the new path is cheaper than the old one 
 * @param 	<Node>	the node type being used by the StateSpace
 */
public class SmartGraphLoopHandler<Node> implements LoopHandler<Node> {

	@Override
	public boolean shouldVisitNode(Node node, ArrayList<Node> path, double pathCost, HashMap<Node, Double> minCostToNode) {
		return !minCostToNode.containsKey(node) || minCostToNode.get(node) > pathCost;
	}

}
