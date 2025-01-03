package searchAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;

import heuristic.Heuristic;
import loopHandling.GraphLoopHandler;
import loopHandling.LoopHandler;
import pathEvaluation.PathEvaluator;
import searchStrategy.SearchStrategy;
import stateSpace.StateSpace;

/**
 * the general search algorithm from the lecture
 * @param 	<Node>	the node type that the StateSpace uses
 */
public class GeneralSearch<Node> {

	private StateSpace<Node> space;
	private SearchStrategy<Node> strategy;
	private PathEvaluator<Node> pathEvaluator;
	private Heuristic<Node> heuristic;
	private LoopHandler<Node> loopHandler = new GraphLoopHandler<>();
	
	/**
	 * @param 	space			the StateSpace that the algorithm searches in
	 * @param 	strategy 		the search strategy that the algorithm uses
	 * @param 	pathEvaluator	the path evaluation that the algorithm uses
	 * @param 	heuristic		the heuristic that the algorithm uses
	 */
	public GeneralSearch(StateSpace<Node> space, SearchStrategy<Node> strategy, PathEvaluator<Node> pathEvaluator, Heuristic<Node> heuristic) {
		this.space = space;
		this.strategy = strategy;
		this.pathEvaluator = pathEvaluator;
		this.heuristic = heuristic;
	}
	
	/**
	 * initializes the search and iterates until a path is found or no further iterations can be made
	 * @return	the first path that is being found that leads to a goal state, null if no path is being found
	 */
	public ArrayList<Node> search() {
		initializeSearch();
		
		while(!strategy.isEmpty()) {
			ArrayList<Node> pathToGoal = iterateSearch();
			if(pathToGoal != null) return pathToGoal;
		}
		
		return null;
	}
	
	public HashMap<Node, Double> minCostToNode = new HashMap<>();
	public HashMap<Node, Double> minCostToVisitedNode = new HashMap<>();
	private ArrayList<Node> startPath = new ArrayList<>();
	
	/**
	 * initializes the search
	 */
	public void initializeSearch() {
		strategy.clear();
		minCostToNode.clear();
		minCostToVisitedNode.clear();
		minCostToNode.put(space.getStart(), 0.0);
		startPath.clear();
		startPath.add(space.getStart());	
		strategy.add(startPath, rate(startPath));
	}
	
	/**
	 * executes one iteration of the search
	 * @return	the first path that is being found that leads to a goal state, null if no path is being found
	 */
	public ArrayList<Node> iterateSearch() {
		ArrayList<Node> path = strategy.get();
		
		minCostToVisitedNode.put(path.getLast(), pathEvaluator.pastCost(space, path));
		
		if(space.isGoal(path.getLast())) return path;
		
		for(Node neighbor : space.getNeighbors(path.getLast())) {
			ArrayList<Node> newPath = new ArrayList<Node>(path);
			newPath.add(neighbor);
			
			double pathCost = pathEvaluator.pastCost(space, newPath);
			
			if(loopHandler.shouldVisitNode(neighbor, newPath, pathCost, minCostToNode)) {
				minCostToNode.put(neighbor, pathCost);
				strategy.add(newPath, rate(newPath));
			}
		}
		
		return null;
	}
	
	/**
	 * combines the past and future cost
	 * @param 	path	the overall cost of the path
	 * @return	the overall cost of the path
	 */
	public double rate(ArrayList<Node> path) {
		return pathEvaluator.pastCost(space, path) + heuristic.futureCost(space, path.getLast());
	}
	
	/**
	 * if the heuristic is not admissible, this function is guaranteed to return false, if the heuristic is admissible and the StateSpace is infinite, this function will not terminate
	 * @return	whether or not the heuristic that the algorithm uses is admissible
	 */
	public boolean isHeuristicAdmissible() {
		Node initialStart = space.getStart();
		UniformSearch<Node> search = new UniformSearch<>(space);
		ArrayList<Node> path = null;
		for(Node node : space.getNodes()) {
			double heuristicCost = heuristic.futureCost(space, node);
			space.setStart(node);
			path = search.search();
			double actualCost = 0;
			for(int i = 0; i < path.size() - 1; i++) actualCost += space.getCost(path.get(i), path.get(i + 1));
			System.out.println(node.toString() + "\t" + heuristicCost + "\t" + actualCost);
			if(actualCost < heuristicCost) return false;
		}
		space.setStart(initialStart);
		return true;
	}
	
	/**
	 * @return	the StateSpace that the algorithm searches in
	 */
	public StateSpace<Node> getStateSpace() {
		return space;
	}
	
	/**
	 * @return	the strategy that the algorithm uses
	 */
	public SearchStrategy<Node> getStrategy() {
		return strategy;
	}
	
	/**
	 * @return	the heuristic that the algorithm uses
	 */
	public Heuristic<Node> getHeuristic() {
		return heuristic;
	}
	
	/**
	 * @return	the path evaluator that the algorithm uses
	 */
	public PathEvaluator<Node> getPathEvaluator() {
		return pathEvaluator;
	}
	
	/**
	 * the loop handler that the algorithm uses
	 * @return
	 */
	public LoopHandler<Node> getLoopHandler() {
		return loopHandler;
	}
}
