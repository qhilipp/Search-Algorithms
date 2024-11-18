package searchStrategie;

public interface SearchStrategy<Node> {

	public void add(Node node);
	public Node get();
	public boolean isEmpty();
	
}
