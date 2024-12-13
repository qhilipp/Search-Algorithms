package util;

public class Rectangle {

	public Vector position, size;
	
	public Rectangle(double x, double y, double width, double height) {
		this.position = new Vector(x, y);
		this.size = new Vector(width, height);
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
