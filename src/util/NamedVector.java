package util;

public class NamedVector implements Position, Nameable {

	private String name = null;
	private Vector position;
	
	public NamedVector(double...components) {
		this.position = new Vector(components);
	}
	
	public NamedVector(String name, double...components) {
		this.name = name;
		this.position = new Vector(components);
	}
	
	@Override
	public Vector getPosition() {
		return position;
	}
	
	@Override
	public String getName() {
		if(name == null) return position.toString();
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != Vector.class) return false;
		NamedVector other = (NamedVector) obj;
		return other.name.equals(name);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
