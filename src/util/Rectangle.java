package util;

public class Rectangle {

	public Vector position, size;
	
	public Rectangle(double...components) {
		double[] positionComponents = new double[components.length / 2];
		double[] sizeComponents = new double[components.length / 2];
		for(int i = 0; i < components.length / 2; i++) {
			positionComponents[i] = components[i];
			sizeComponents[i] = components[sizeComponents.length + i];
		}
		this.position = new Vector(positionComponents);
		this.size = new Vector(sizeComponents);
	}
	
	public double x() {
		return position.get(0);
	}
	
	public double y() {
		return position.get(1);
	}
	
	public double width() {
		return size.get(0);
	}
	
	public double height() {
		return size.get(1);
	}
	
	public void translate(Vector delta) {
		position.translate(delta);
	}
	
}
