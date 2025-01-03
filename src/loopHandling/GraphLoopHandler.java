package loopHandling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * only visits nodes that have not been visited already
 * @param 	<Node>	the node type being used by the StateSpace
 */
public class GraphLoopHandler<Node> implements LoopHandler<Node> {

	@Override
	public boolean shouldVisitNode(Node node, ArrayList<Node> path, double pathCost, HashMap<Node, Double> minCostToNode) {
		return !minCostToNode.containsKey(node);
	}

}
