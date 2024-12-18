package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JPanel;

import stateSpace.StateSpace;
import util.Measurement;
import util.Nameable;
import util.Position;
import util.Rectangle;
import util.Vector;

public class StateSpaceView<Node extends Position & Nameable> extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	private StateSpace<Node> space;
	private Rectangle view = new Rectangle(-2, -2, 4, 4);;
	private HashSet<Node> cachedNodes = new HashSet<>();
	private Vector dragStartLocation = null;
	private Font nodeFont = new Font("Arial", Font.PLAIN, 16);
	private SSVListener<Node> listener = null;
	private int nodesOnScreen = 0;
	
	public StateSpaceView(StateSpace<Node> space) {
//		setSpace(space);
		
		setSize(600, 600);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	
	@Override
	public void paint(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;
		
		nodesOnScreen = 0;
		
		for(Node node : cachedNodes) {
			Vector translatedOvalPosition = spaceToPixel(node.getPosition());
			translatedOvalPosition.translate(-getNodeSize() / 2, -getNodeSize() / 2);
			Vector translatedOvalCenter = spaceToPixel(node.getPosition());
			
			if(!isInBounds(translatedOvalPosition)) continue;

			g.setColor(Color.WHITE);
			g.fillOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), getNodeSize(), getNodeSize());
			g.setColor(Color.BLACK);
			
			for(Node neighbor : space.getNeighbors(node)) {
				Vector translatedNeighborCenter = spaceToPixel(neighbor.getPosition());
				Vector neighborOffset = interpolate(translatedNeighborCenter, translatedOvalCenter, getNodeSize() / 2);
				Vector nodeOffset = interpolate(translatedOvalCenter, translatedNeighborCenter, getNodeSize() / 2);
				int[] xPos = new int[3];
				int[] yPos = new int[3];
				xPos[0] = (int) neighborOffset.x();
				yPos[0] = (int) neighborOffset.y();
				Vector triangleBase = interpolate(translatedOvalCenter, translatedNeighborCenter, getNodeSize() / 1.5);
				
//				g.fillPolygon(null, null, ABORT);
				g.drawLine((int) nodeOffset.x(), (int) nodeOffset.y(), (int) neighborOffset.x(), (int) neighborOffset.y());
			}
			
			Vector textSize = getStringSize(g, node.getName());
			Vector textPosition = translatedOvalCenter.translated(textSize.x() * -0.5, textSize.y() * 0.25);
			g.drawString(node.getName(), (int) textPosition.x(), (int) textPosition.y());
			g.drawOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), getNodeSize(), getNodeSize());
			
			if(space.isGoal(node)) {
				int offset = 2;
				g.drawOval((int) translatedOvalPosition.x() + offset, (int) translatedOvalPosition.y() + offset, getNodeSize() - 2 * offset, getNodeSize() - 2 * offset);
			}
			
			nodesOnScreen++;
		}
	}
	
	private Vector interpolate(Vector a, Vector b, double length) {
		double x = length / a.distance(b, Measurement.EUCLIDEAN);
		Vector direction = b.translated(a.scaled(-1));
		return a.translated(direction.scaled(x));
	}
	
	private Vector getStringSize(Graphics g, String text) {
		FontMetrics fm = g.getFontMetrics(nodeFont);
		
		int stringWidth = fm.stringWidth(text);
		int stringHeight = fm.getHeight();
		
		return new Vector(stringWidth, stringHeight);
	}
	
	private void cacheNodes() {
		ArrayList<Node> nodes = new ArrayList<>();
		nodes.addAll(cachedNodes);
		if(!nodes.contains(space.getStart())) {
			nodes.add(space.getStart());
		}
		cachedNodes.clear();
		cachedNodes.add(nodes.getFirst());
		
		while(!nodes.isEmpty()) {
			Node node = nodes.remove(0);
			cachedNodes.add(node);
			
			for(Node neighbor : space.getNeighbors(node)) {
				Vector translatedNeighborPosition = spaceToPixel(neighbor.getPosition());
				
				if(isInBounds(translatedNeighborPosition) && !cachedNodes.contains(neighbor)) {
					nodes.add(neighbor);
				}
			}
		}
	}
	
	private boolean isInBounds(Vector point) {
		return point.x() >= -getNodeSize() && point.x() <= getWidth() + getNodeSize() && point.y() >= -getNodeSize() && point.y() <= getHeight() + getNodeSize();
	}
	
	private Vector spaceToPixel(Position position) {
		double translatedX = (position.getPosition().x() - view.x()) / view.width() * getWidth();
		double translatedY = (position.getPosition().y() - view.y()) / view.height() * getHeight();
		return new Vector(translatedX, translatedY);
	}
		
	private Vector pixelToSpace(double x, double y) {
		double convertedX = x / getWidth() * view.width() + view.x();
		double convertedY = y / getHeight() * view.height() + view.y();
		return new Vector(convertedX, convertedY);
	}
	
	private int getNodeSize() {
		return (int) (nodeFont.getSize() * 2.5);
	}
	
	public StateSpace<Node> getSpace() {
		return space;
	}
	
	public void setSpace(StateSpace<Node> space) {
		if(space.getStart().getPosition().getDimensions() != 2) {
			return;
		}
		this.space = space;
		cacheNodes();
	}
	
	public void setSSVListener(SSVListener<Node> listener) {
		this.listener = listener;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(listener == null) return;
		for(Node node : cachedNodes) {
			if(spaceToPixel(node).distance(new Vector(e.getX(), e.getY()), Measurement.EUCLIDEAN) < getNodeSize() / 2) {
				listener.nodeSelected(node);
				return;
			}
		}
		listener.nodeSelected(null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragStartLocation = new Vector(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragStartLocation = null;
		cacheNodes();
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		Vector delta = new Vector(-e.getX(), -e.getY()).translated(dragStartLocation);
		delta = pixelToSpace(delta.x(), delta.y());
		delta.translate(view.position.scaled(-1));
		view.translate(delta);
		dragStartLocation = new Vector(e.getX(), e.getY());
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double scaleFactor = Math.min(1.05, 1 + -e.getPreciseWheelRotation() / 30);
		if(nodesOnScreen > 100 && scaleFactor > 1) return;
		
		Vector beforeScale = pixelToSpace(e.getX(), e.getY());
		
		view.size.scale(scaleFactor);
		
		Vector afterScale = pixelToSpace(e.getX(), e.getY());
		Vector translation = beforeScale.translated(afterScale.scaled(-1));
		
		view.position.translate(translation);
		
		if(e.getUnitsToScroll() == 0) cacheNodes();
		
		repaint();
	}
	
}
