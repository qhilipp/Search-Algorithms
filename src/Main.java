import searchAlgorithms.AStarSearch;
import stateSpace.Graph;
import util.Point;
import util.Vector;

public class Main {

	public static void main(String[] args) {
		Vector[] goals = { new Vector(3, 4) };
//		Cartesian space = new Cartesian(2, 2, new ListGoalTester<>(goals));
		Point s = new Point("S", 0, 0);
		Point a = new Point("A", 1, -1);
		Point b = new Point("B", 1, 0);
		Point c = new Point("C", 1, 1);
		Point d = new Point("D", 2, 0);
		Point e = new Point("E", 2, 1);
		Point f = new Point("F", 3, 0);
		Point g = new Point("G", 4, 0);
		Graph<Point> space = new Graph<>(s, a, b, c, d, e, f, g);
		space.connect(s, a, 6);
		space.connect(s, b, 2);
		space.connect(s, c, 1);
		space.connect(a, d, 3);
		space.connect(a, g, 20);
		space.connect(b, d, 2);
		space.connect(b, e, 6);
		space.connect(c, b, 3);
		space.connect(c, e, 6);
		space.connect(d, f, 5);
		space.connect(e, g, 2);
		space.connect(f, g, 1);
		space.setGoal(g);
		AStarSearch<Point> search = new AStarSearch<>(space);
		for(Object o : search.search()) {
			System.out.println(o + " ");
		}
	}

}
