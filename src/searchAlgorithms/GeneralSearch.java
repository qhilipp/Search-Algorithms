package searchAlgorithms;

import java.util.ArrayList;

import searchStrategie.SearchStrategy;
import stateSpace.StateSpace;

public class GeneralSearch<Node> {

	private final StateSpace<Node> space;
	private final Class<SearchStrategy<NodePath>> strategy;
	
	public GeneralSearch(StateSpace<Node> space, Class<SearchStrategy<NodePath>> strategy) {
		this.space = space;
		this.strategy = strategy;
	}
	
	public ArrayList<Node> search() {
		SearchStrategy<NodePath> strategy = null;
		
		try {
			strategy = this.strategy.newInstance();
		} catch(Exception e) {}
		
		ArrayList<Node> startPath = new ArrayList<>();
		startPath.add(space.getStart());
		
		strategy.add(new NodePath(space.getStart(), startPath));
		
		while(!strategy.isEmpty()) {
			NodePath nodePath = strategy.get();
			
			if(space.isGoal(nodePath.node)) return nodePath.path;
			
			for(Node neighbor : space.getNeighbors(nodePath.node)) {
				ArrayList<Node> newPath = new ArrayList<Node>(nodePath.path);
				newPath.add(neighbor);
				
				strategy.add(new NodePath(neighbor, newPath));
			}
		}
		
		return null;
	}
	
	class NodePath {
		Node node;
		ArrayList<Node> path;
		
		NodePath(Node node, ArrayList<Node> path) {
			this.node = node;
			this.path = path;
		}
		
		@Override
		public String toString() {
			return node.toString();
		}
	}
	
}
