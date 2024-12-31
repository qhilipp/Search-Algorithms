package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import searchAlgorithms.GeneralSearch;
import util.Copyable;
import util.Measurement;
import util.Nameable;
import util.Position;
import util.Vector;

public class StateSpaceView3D<Node extends Position&Nameable&Copyable> extends StateSpaceView<Node> implements KeyListener {
	
	private HashSet<Character> keys = new HashSet<>();
	private Vector rotation = new Vector(0, 0, 0);
	private double cameraVelocity = 0.04;
	private double viewDistance = 4.5;
	
	public StateSpaceView3D(GeneralSearch<Node> searchAlgorithm) {
		super(
			searchAlgorithm,
			new Vector(
				searchAlgorithm.getStateSpace().getStart().getPosition().x(),
				searchAlgorithm.getStateSpace().getStart().getPosition().y(),
				searchAlgorithm.getStateSpace().getStart().getPosition().z()
			),
			new Vector(3, 3)
		);
		addKeyListener(this);
		
		visibilityDepth = 0;
		initialize();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					updateKeyInputs();
					try {
						Thread.sleep(50);
					} catch(Exception e) {}
				}
			}
			
		}).start();
	}

	@Override
	protected Vector spaceToPixel(Vector spacePosition) {
		Vector cameraWorldPosition = spaceToCamera(spacePosition);
		double z = Math.abs(cameraWorldPosition.getPosition().z());
		Vector pixelPosition = new Vector(
			(cameraWorldPosition.getPosition().x() / z + size.x() / 2) / size.x() * getWidth(),
			(cameraWorldPosition.getPosition().y() / z + size.y() / 2) / size.y() * getHeight()
		);
		return pixelPosition;
	}
	
	private Vector spaceToCamera(Vector spacePosition) {
		return spacePosition.translated(position.scaled(-1)).rotated(rotation.scaled(-1));
	}
	
	@Override
	protected double getNodeVisibility(Node node) {
		double distance = spaceToCamera(node.getPosition()).getLength(Measurement.EUCLIDEAN);
		double fraction = Math.min(1, Math.max(0, distance - 1) / viewDistance);
		return 1 - fraction;
	}

	@Override
	protected void dragged(Vector pixelDelta) {
		pixelDelta.scale(0.01);
		rotation.translate(new Vector(pixelDelta.y(), -pixelDelta.x(), 0));
		rotation.set(0, rotation.x() % (2 * Math.PI));
		rotation.set(1, rotation.y() % (2 * Math.PI));
		double maxViewAngle = Math.PI * 0.45;
		if(rotation.x() > maxViewAngle) rotation.set(0, maxViewAngle);
		if(rotation.x() < -maxViewAngle) rotation.set(0, -maxViewAngle);
	}

	@Override
	protected void scrolled(double factor, Vector pixelPosition) {
		position.translate(getCameraDirection().scaled(cameraVelocity * factor));
	}
	
	@Override
	protected boolean isVisible(Node node) {
		Vector cameraPosition = spaceToCamera(node.getPosition());
		return super.isVisible(node) && cameraPosition.getLength(Measurement.EUCLIDEAN) <= viewDistance && cameraPosition.z() > 0.05;
	}
	
	@Override
	protected double drawOrder(Node node) {
		return -spaceToCamera(node.getPosition()).z();
	}
	
	private void updateKeyInputs() {
//		if(keys.contains('w')) position.translate(getCameraDirection().scaled(cameraVelocity));
//		if(keys.contains('s')) position.translate(getCameraDirection().scaled(-cameraVelocity));
//		if(keys.contains('d')) 
//		if(keys.contains('a')) 
		repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keys.add(e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.remove(e.getKeyChar());
	}
	
	private Vector getCameraDirection() {
		Vector direction = new Vector(0, 0, 1).rotated(rotation);
		direction.set(1, Math.cos(rotation.y()) * direction.y());
		return direction;
	}
	
}
