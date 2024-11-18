import searchAlgorithms.GeneralSearch;
import searchStrategie.Uniform;
import stateSpace.Cartesian;
import stateSpace.Graph;
import util.Vector;

public class Main {

	public static void main(String[] args) {
		Cartesian space = new Cartesian(2, new Vector(3, 4));
		Graph<String> graph = new Graph("S", "A", "B", "C", "D", "E", "F", "G");
		graph.connect("S", "A", 6);
		graph.connect("S", "B", 2);
		graph.connect("S", "C", 1);
		graph.connect("A", "D", 3);
		graph.connect("A", "G", 20);
		graph.connect("B", "D", 2);
		graph.connect("B", "E", 6);
		graph.connect("C", "B", 3);
		graph.connect("C", "E", 6);
		graph.connect("D", "F", 5);
		graph.connect("E", "G", 2);
		graph.connect("F", "G", 1);
		graph.setGoal("G");
		GeneralSearch<String> search = new GeneralSearch(graph, Uniform.class);
		for(String v : search.search()) {
			System.out.println(v + " ");
		}
	}

}
