package ui;

public interface SSEListener<Node> {

	public void nextIteration();
	public void continueSearch();
	public void select(Node node);
	public void reset();
	public void setSearchSpeed(int speed);
	
}
