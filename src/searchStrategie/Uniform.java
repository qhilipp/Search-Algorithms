package searchStrategie;

public class Uniform<Node> extends SearchStrategy<Node> {

	@Override
	public void add(Node node, double rating) {
		for(int i = 0; i < list.size(); i++) {
			if(rating <= get(i).rating) {
				insert(node, rating, i);
				return;
			}
		}
		insert(node, rating, -1);
	}

}
