package util;

import java.util.Arrays;

public class Vector implements Position {
	private double[] components;
	
	public Vector(int dimensions) {
		this.components = new double[dimensions];
	}
	
	public Vector(double... components) {
		this.components = components.clone();
	}
	
	public int getDimensions() {
		return components.length;
	}
	
	public double get(int dimension) {
		return components[dimension];
	}
	
	public void set(int dimension, double value) {
		components[dimension] = value;
	}
	
	public double distance(Vector other, Measurement measurement) {
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
	
	@Override
	public Vector getPosition() {
		return this;
	}
	
	@Override
	public Object clone() {
		return new Vector(components);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != Vector.class) return false;
		Vector other = (Vector) obj;
		if(getDimensions() != other.getDimensions()) return false;
		for(int i = 0; i < getDimensions(); i++) {
			if(get(i) != other.get(i)) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String[] components = new String[this.components.length];
		for(int i = 0; i < components.length; i++) components[i] = this.components[i] + "";
		return "(" + String.join(", ", components) + ")";
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(components);
	}
}
