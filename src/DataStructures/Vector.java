package DataStructures;

public class Vector {
	double[] components;
	
	public Vector(int dimensions) {
		this.components = new double[dimensions];
	}
	
	public Vector(double[] components) {
		this.components = components.clone();
	}
	
	public int getDimensions() {
		return components.length;
	}
	
	public double get(int dimension) {
		return components[dimension];
	}
	
	public double getEuclideanDistance(Vector other, Measurement measurement) {
		if(other.getDimensions() != this.getDimensions()) return -1;
		double dist = 0;
		switch(measurement) {
		case EUCLIDEAN: 
			for(int i = 0; i < getDimensions(); i++) {
				dist += Math.pow(get(i) - other.get(i), 2);
			}
			dist = Math.sqrt(dist);
			break;
		case MANHATTAN: 
			for(int i = 0; i < getDimensions(); i++) {
				dist += Math.abs(get(i) - other.get(i));
			}
			break;
		case UNIFORM:
			dist = 1;
		case HAMMING:
			for(int i = 0; i < getDimensions(); i++) {
				dist += get(i) == other.get(i) ? 0 : 1;
			}
			break;
		}
		return dist;
	}
}
