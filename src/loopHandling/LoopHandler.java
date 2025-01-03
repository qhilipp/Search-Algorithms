package loopHandling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * an interface that the StateSpace uses to decide whether or to visit a given node 
 * @param <Node>
 */
public interface LoopHandler<Node> {

	/**
	 * @param 	node			the node that is either being visited or not
	 * @param 	path			the potential path that is being used to visit that node
	 * @param 	pathCost		the cost of the potential path
	 * @param 	minCostToNode	the minimum cost for each visited node
	 * @return					whether or not the node should be visited
	 */
	public boolean shouldVisitNode(Node node, ArrayList<Node> path, double pathCost, HashMap<Node, Double> minCostToNode);
	
}
