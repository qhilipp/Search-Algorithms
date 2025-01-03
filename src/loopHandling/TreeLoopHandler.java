package loopHandling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * always visit the node
 * @param <Node>
 */
public class TreeLoopHandler<Node> implements LoopHandler<Node> {

	@Override
	public boolean shouldVisitNode(Node node, ArrayList<Node> path, double pathCost, HashMap<Node, Double> minCostToNode) {
		return true;
	}

}
