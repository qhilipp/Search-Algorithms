package searchAlgorithms;

import java.util.ArrayList;

import heuristic.Heuristic;
import pathEvaluation.PathEvaluator;
import searchStrategy.SearchStrategy;
import stateSpace.StateSpace;

public class GeneralSearch<Node> {

	private StateSpace<Node> space;
	private SearchStrategy<ArrayList<Node>> strategy;
	private PathEvaluator<Node> pathEvaluator;
	private Heuristic<Node> heuristic;
	
	public GeneralSearch(StateSpace<Node> space, SearchStrategy<ArrayList<Node>> strategy, PathEvaluator<Node> pathEvaluator, Heuristic<Node> heuristic) {
		this.space = space;
		this.strategy = strategy;
		this.pathEvaluator = pathEvaluator;
		this.heuristic = heuristic;
	}
	
	public ArrayList<Node> search() {
		strategy.clear();
		
		ArrayList<Node> startPath = new ArrayList<>();
		startPath.add(space.getStart());
		
		strategy.add(startPath, rate(startPath));
		
		while(!strategy.isEmpty()) {
			ArrayList<Node> path = strategy.get();
			
			if(space.isGoal(path.getLast())) return path;
			
			for(Node neighbor : space.getNeighbors(path.getLast())) {
				ArrayList<Node> newPath = new ArrayList<Node>(path);
				newPath.add(neighbor);
				
				strategy.add(newPath, rate(newPath));
			}
			for(Node node : strategy.get()) {
				System.out.println(node);
			}
		}
		
		return null;
	}
	
	private double rate(ArrayList<Node> path) {
		return pathEvaluator.pastCost(space, path) + heuristic.futureCost(space, path.getLast());
	}
}
