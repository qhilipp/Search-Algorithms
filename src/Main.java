import goalTest.ListGoalTester;
import searchAlgorithms.AStarSearch;
import searchAlgorithms.GeneralSearch;
import stateSpace.Cartesian;
import stateSpace.Graph;
import stateSpace.StateSpace;
import ui.StateSpaceWindow;
import util.NamedVector;

public class Main {

	public static void main(String[] args) {
		new StateSpaceWindow<>(cartesianSearch());
	}
	
	static GeneralSearch<NamedVector> cartesianSearch() {
		NamedVector[] goals = { new NamedVector(4, 5) };
		boolean[][] map = {
				{ false, false, false, false, false, false, false },
				{ false,  true,  true,  true,  true,  true, false },
				{ false,  true,  true, false, false,  true, false },
				{ false,  true, false,  true,  true,  true, false },
				{ false,  true, false,  true,  true,  true, false },
				{ false,  true, false,  true, false,  true, false },
				{ false,  true, false,  true, false,  true, false },
				{ false,  true, false,  true, false,  true, false },
				{ false,  true, false,  true, false,  true, false },
				{ false, false, false, false, false, false, false },
		};
		Cartesian<NamedVector> space = new Cartesian<>(new NamedVector(0, 0), 1, new ListGoalTester<>(goals));
		return AStarSearch.autoHeuristic(space);
	}
	
	static GeneralSearch<NamedVector> graphSearch() {
		NamedVector s = new NamedVector("S", 0, 0);
		NamedVector a = new NamedVector("A", 1, -1);
		NamedVector b = new NamedVector("B", 1, 0);
		NamedVector c = new NamedVector("C", 1, 1);
		NamedVector d = new NamedVector("D", 2, 0);
		NamedVector e = new NamedVector("E", 2, 1);
		NamedVector f = new NamedVector("F", 3, 0);
		NamedVector g = new NamedVector("G", 4, 0);
		
		Graph<NamedVector> space = new Graph<>(s, a, b, c, d, e, f, g);
		space.connect(s, a, 6);
		space.connect(s, b, 2);
		space.connect(s, c, 1);
		space.connect(a, d, 3);
		space.connect(a, g, 20);
		space.connect(b, d, 2);
		space.connect(b, e, 6);
		space.connect(c, b, 3);
		space.connect(c, e, 6);
		space.connect(c, f, 4);
		space.connect(d, f, 5);
		space.connect(e, g, 2);
		space.connect(f, g, 1);
		space.setGoal(g);
		
		AStarSearch<NamedVector> search = new AStarSearch<>(space, 
				(StateSpace<NamedVector> stateSpace, NamedVector node) -> {
					if(node.equals(s)) return 6;
					if(node.equals(a)) return 8;
					if(node.equals(b)) return 6;
					if(node.equals(c)) return 5;
					if(node.equals(d)) return 4;
					if(node.equals(e)) return 2;
					if(node.equals(f)) return 1;
					return 0;
				}
		);
		
		return search;
	}
}
