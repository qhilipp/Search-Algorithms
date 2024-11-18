package searchAlgorithms;

import java.util.ArrayList;

import searchStrategie.SearchStrategy;
import stateSpace.StateSpace;

public class GeneralSearch<Node> {

	private final StateSpace<Node> space;
	private final Class<SearchStrategy<ArrayList<Node>>> strategy;
	
	public GeneralSearch(StateSpace<Node> space, Class<SearchStrategy<ArrayList<Node>>> strategy) {
		this.space = space;
		this.strategy = strategy;
	}
	
	public ArrayList<Node> search() {
		SearchStrategy<ArrayList<Node>> strategy = null;
		
		try {
			strategy = this.strategy.newInstance();
		} catch(Exception e) {}
		
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
		return pastCost(path) + futureCost(path.getLast());
	}
	
	private double pastCost(ArrayList<Node> path) {
		double cost = 0;
		for(int i = 0; i < path.size() - 1; i++) {
			cost += space.getCost(path.get(i), path.get(i + 1));
		}
		return cost;
	}
	
	private double futureCost(Node node) {
		return 0;
	}
	
}
