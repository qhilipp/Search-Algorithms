package searchAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;

import heuristic.Heuristic;
import loopHandling.GraphLoopHandler;
import loopHandling.LoopHandler;
import pathEvaluation.PathEvaluator;
import searchStrategy.SearchStrategy;
import stateSpace.StateSpace;

public class GeneralSearch<Node> {

	private StateSpace<Node> space;
	private SearchStrategy<Node> strategy;
	private PathEvaluator<Node> pathEvaluator;
	private Heuristic<Node> heuristic;
	private LoopHandler<Node> loopHandler = new GraphLoopHandler<>();
	
	public GeneralSearch(StateSpace<Node> space, SearchStrategy<Node> strategy, PathEvaluator<Node> pathEvaluator, Heuristic<Node> heuristic) {
		this.space = space;
		this.strategy = strategy;
		this.pathEvaluator = pathEvaluator;
		this.heuristic = heuristic;
	}
	
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
	public void initializeSearch() {
		strategy.clear();
		minCostToNode.clear();
		minCostToVisitedNode.clear();
		minCostToNode.put(space.getStart(), 0.0);
		startPath.clear();
		startPath.add(space.getStart());	
		strategy.add(startPath, rate(startPath));
	}
	
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
	
	public double rate(ArrayList<Node> path) {
		return pathEvaluator.pastCost(space, path) + heuristic.futureCost(space, path.getLast());
	}
	
	public boolean isHeuristicAdmissible() {
		ArrayList<Node> nodes = space.getNodes();
		Node initialStart = space.getStart();
		UniformSearch<Node> search = new UniformSearch<>(space);
		ArrayList<Node> path = null;
		for(Node node : nodes) {
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
	
	public StateSpace<Node> getStateSpace() {
		return space;
	}
	
	public SearchStrategy<Node> getStrategy() {
		return strategy;
	}
	
	public Heuristic<Node> getHeuristic() {
		return heuristic;
	}
	
	public PathEvaluator<Node> getPathEvaluator() {
		return pathEvaluator;
	}
	
	public LoopHandler<Node> getLoopHandler() {
		return loopHandler;
	}
}
