import searchAlgorithms.GeneralSearch;
import searchStrategie.*;
import stateSpace.Cartesian;
import util.Vector;

public class Main {

	public static void main(String[] args) {
		Cartesian space = new Cartesian(2, new Vector(3, 4));
		GeneralSearch<Vector> search = new GeneralSearch(space, Queue.class);
		for(Vector v : search.search()) {
			System.out.println(v + " ");
		}
	}

}
