package util;

public class NamedVector extends Vector {

	private String name = null;
	
	public NamedVector(double...components) {
		super(components);
	}
	
	public NamedVector(String name, double...components) {
		super(components);
		this.name = name;
	}
	
	@Override
	public String getName() {
		if(name == null) return super.getName();
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
		if(obj.getClass() != NamedVector.class) return false;
		NamedVector other = (NamedVector) obj;
		return other.getName().equals(getName());
	}
	
	@Override
	public Object copy() {
		return new NamedVector(name, components);
	}
	
}
