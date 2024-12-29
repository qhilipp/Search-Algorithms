package util;

import java.util.Arrays;

public class Vector implements Position, Nameable, Copyable {
	protected double[] components;
	
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
	
	public void translate(double... components) {
		translate(new Vector(components));
	}
	
	public void translate(Vector delta) {
		if(delta.getDimensions() != getDimensions()) return;
		for(int i = 0; i < getDimensions(); i++) set(i, get(i) + delta.get(i));
	}
	
	public Vector translated(double...components) {
		return translated(new Vector(components));
	}
	
	public Vector translated(Vector delta) {
		Vector clone = (Vector) this.copy();
		clone.translate(delta);
		return clone;
	}
	
	public void scale(double factor) {
		for(int i = 0; i < getDimensions(); i++) set(i, get(i) * factor);
	}
	
	public Vector scaled(double factor) {
		Vector clone = (Vector) this.copy();
		clone.scale(factor);
		return clone;
	}
	
	public static double[][] matrixMultiplication(double[][] a, double[][] b) {
		double[][] result = new double[a.length][b[0].length];
		for(int i = 0; i < result.length; i++) {
			for(int j = 0; j < result[0].length; j++) {
				for(int k = 0; k < a[0].length; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return result;
	}
	
	public void rotate(Vector rotation) {
		if(rotation.getDimensions() == 3) {
			double[][] xRotation = new double[][] {
				{ 1, 0, 0},
				{ 0, Math.cos(rotation.x()), -Math.sin(rotation.x()) },
				{ 0, Math.sin(rotation.x()), Math.cos(rotation.x()) }
			};
			double[][] yRotation = new double[][] {
				{ Math.cos(rotation.y()), 0, Math.sin(rotation.y()) },
				{ 0, 1, 0 },
				{ -Math.sin(rotation.y()), 0, Math.cos(rotation.y()) }
			};
			double[][] zRotation = new double[][] {
				{ Math.cos(rotation.z()), -Math.sin(rotation.z()), 0 },
				{ Math.sin(rotation.z()), Math.cos(rotation.z()), 0 },
				{ 0, 0, 1 }
			};
			rotate(yRotation, xRotation, zRotation);
		}
	}
	
	private void rotate(double[][]...matricies) {
		double[][] componentsMatrix = new double[getDimensions()][1];
		for(int i = 0; i < componentsMatrix.length; i++) {
			componentsMatrix[i][0] = components[i];
		}
		for(double[][] matrix : matricies) {			
			componentsMatrix = matrixMultiplication(matrix, componentsMatrix);
		}
		for(int i = 0; i < componentsMatrix.length; i++) {
			components[i] = componentsMatrix[i][0];
		}
		
	}
	
	public Vector rotated(Vector rotation) {
		Vector clone = (Vector) this.copy();
		clone.rotate(rotation);
		return clone;
	}
	
	public void interpolate(Vector to, double length) {
		double x = length / distance(to, Measurement.EUCLIDEAN);
		Vector direction = to.translated(scaled(-1));
		translate(direction.scaled(x));
	}
	
	public Vector interpolated(Vector to, double length) {
		Vector clone = (Vector) this.copy();
		clone.interpolate(to, length);
		return clone;
	}
	
	public void normalize(Measurement measurement) {
		scale(1 / getLength(measurement));
	}
	
	public Vector normalized(Measurement measurement) {
		Vector clone = (Vector) this.copy();
		clone.normalize(measurement);
		return clone;
	}
	
	public double getLength(Measurement measurement) {
		return distance(new Vector(getDimensions()), measurement);
	}
	
	public void setLength(Measurement measurement, double length) {
		normalize(measurement);
		scale(length);
	}
	
	public double x() {
		return get(0);
	}
	
	public double y() {
		return get(1);
	}
	
	public double z() {
		return get(2);
	}
	
	@Override
	public Vector getPosition() {
		return new Vector(components);
	}
	
	@Override
	public void setPosition(Vector position) {
		this.components = position.components.clone();
	}
	
	@Override
	public String getName() {
		String[] components = new String[this.components.length];
		for(int i = 0; i < components.length; i++) components[i] = (this.components[i] % 1 == 0) ? ((int) this.components[i] + "") : (this.components[i] + "");
		return String.join(", ", components);
	}
	
	@Override
	public void setName(String name) {}
	
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
		for(int i = 0; i < components.length; i++) components[i] = ((int) (this.components[i] * 100)) / 100.0 + "";
		return "(" + String.join(", ", components) + ")";
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(components);
	}

	@Override
	public Object copy() {
		return new Vector(components);
	}

}
