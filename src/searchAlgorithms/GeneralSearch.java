package searchAlgorithms;

import java.util.ArrayList;

import heuristic.Heuristic;
import searchStrategie.SearchStrategy;
import stateSpace.StateSpace;

public class GeneralSearch<Node> {

	private StateSpace<Node> space;
	private SearchStrategy<ArrayList<Node>> strategy;
	private Heuristic<Node> heuristic;
	
	public GeneralSearch(StateSpace<Node> space, SearchStrategy<ArrayList<Node>> strategy, Heuristic<Node> heuristic) {
		this.space = space;
		this.strategy = strategy;
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
		}
		
		return null;
	}
	
	private double rate(ArrayList<Node> path) {
		return pastCost(path) + heuristic.futureCost(space, path.getLast());
	}
	
	private double pastCost(ArrayList<Node> path) {
		double cost = 0;
		for(int i = 0; i < path.size() - 1; i++) {
			cost += space.getCost(path.get(i), path.get(i + 1));
		}
		return cost;
	}
}
