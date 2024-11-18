package heuristic;

@FunctionalInterface
public interface Heuristic<Node> {
	public double futureCost(Node node);
}
