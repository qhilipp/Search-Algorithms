package util;

public class Point implements Position {

	private String name;
	private Vector position;
	
	public Point(String name, double...components) {
		this.name = name;
		this.position = new Vector(components);
	}
	
	@Override
	public Vector getPosition() {
		return position;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != Vector.class) return false;
		Point other = (Point) obj;
		return other.name.equals(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
