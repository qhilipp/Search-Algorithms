package ui;

import java.awt.BasicStroke;
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

import searchAlgorithms.AStarSearch;
import searchAlgorithms.GeneralSearch;
import stateSpace.StateSpace;
import util.Copyable;
import util.Measurement;
import util.Nameable;
import util.Position;
import util.Rectangle;
import util.Vector;

public class StateSpaceView<Node extends Position&Nameable&Copyable> extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	private StateSpace<Node> space;
	private GeneralSearch<Node> searchAlgorithm;
	private Rectangle view;
	private HashSet<Node> cachedNodes = new HashSet<>();
	private Vector dragStartLocation = null;
	private Font nodeFont = new Font("Arial", Font.PLAIN, 15);
	private SSVListener<Node> listener = null;
	private int nodesOnScreen = 0;
	private ArrayList<Node> path = new ArrayList<>();
	private Node selected = null;
	
	public StateSpaceView(StateSpace<Node> space) {
		setSize(600, 600);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		int size = 6;
		view = new Rectangle(space.getStart().getPosition().x() - size / 2, space.getStart().getPosition().y() - size / 2, size, size);
		
		setSpace(space);
		searchAlgorithm = AStarSearch.autoHeuristic(space);
		searchAlgorithm.initializeSearch();
//		path = searchAlgorithm.search();
	}
	
	@Override
	public void paint(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;
		g.setStroke(new BasicStroke(1));
		
		nodesOnScreen = 0;
		
		for(Node node : cachedNodes) {
			Vector translatedOvalPosition = spaceToPixel(node.getPosition());
			translatedOvalPosition.translate(-getNodeSize() / 2, -getNodeSize() / 2);
			Vector translatedOvalCenter = spaceToPixel(node.getPosition());
			
			if(!isInBounds(node)) continue;

			Color fillColor = path.contains(node) ? Color.GREEN : Color.WHITE;
			
			g.setColor(fillColor);
			g.fillOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), getNodeSize(), getNodeSize());	
			
			for(Node neighbor : space.getNeighbors(node)) {
				boolean highlightArrow = arrowIsInPath(node, neighbor);
				
				Vector translatedNeighborCenter = spaceToPixel(neighbor.getPosition());
				Vector neighborOffset = interpolate(translatedNeighborCenter, translatedOvalCenter, getNodeSize() * 0.75);
				Vector nodeOffset = interpolate(translatedOvalCenter, translatedNeighborCenter, getNodeSize() / 2);

				Vector[] triangleCorners = getTriangle(translatedOvalCenter, translatedNeighborCenter, highlightArrow ? 2 : 1);
				
				int[] xPos = new int[3];
				int[] yPos = new int[3];
				
				xPos[0] = (int) triangleCorners[0].x();
				yPos[0] = (int) triangleCorners[0].y();
				xPos[1] = (int) triangleCorners[1].x();
				yPos[1] = (int) triangleCorners[1].y();
				xPos[2] = (int) triangleCorners[2].x();
				yPos[2] = (int) triangleCorners[2].y();
				
				g.setColor(highlightArrow ? Color.GREEN : Color.BLACK);
				g.setStroke(highlightArrow ? new BasicStroke(3) : new BasicStroke(1));
				g.fillPolygon(xPos, yPos, xPos.length);
				
				if(arrowIsInPath(neighbor, node)) continue;
				
				g.drawLine((int) nodeOffset.x(), (int) nodeOffset.y(), (int) neighborOffset.x(), (int) neighborOffset.y());
			}
			
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(node.equals(selected) ? 3 : 1));
			
			Vector nameSize = getStringSize(g, node.getName());
			Vector namePosition = translatedOvalCenter.translated(nameSize.x() * -0.5, nameSize.y() * 0.25);
			
			g.drawString(node.getName(), (int) namePosition.x(), (int) namePosition.y());
			g.drawOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), getNodeSize(), getNodeSize());
			
			if(space.isGoal(node)) {
				int offset = 2;
				g.drawOval((int) translatedOvalPosition.x() + offset, (int) translatedOvalPosition.y() + offset, getNodeSize() - 2 * offset, getNodeSize() - 2 * offset);
			}
			
			nodesOnScreen++;
		}
	}
	
	private boolean arrowIsInPath(Node from, Node to) {
		for(int i = 0; i < path.size() - 1; i++) {
			if(path.get(i).equals(from) && path.get(i + 1).equals(to)) return true;
		}
		return false;
	}
	
	private Vector[] getTriangle(Vector from, Vector to, double size) {
		double width = getNodeSize() / 10 * size;
		double height = getNodeSize() / 4 * size;
		
		Vector base = interpolate(to, from, height);
		Vector direction = from.translated(to.scaled(-1));
		Vector perpendicularDirection = new Vector(-direction.y(), direction.x());
		perpendicularDirection.setLength(Measurement.EUCLIDEAN, width);
		
		Vector a = interpolate(base.translated(perpendicularDirection), from, getNodeSize() / 2);
		Vector b = interpolate(base.translated(perpendicularDirection.scaled(-1)), from, getNodeSize() / 2);
		
		return new Vector[] { interpolate(to, from, getNodeSize() / 2), a, b };
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
		
		if(nodes.isEmpty()) {
			nodes.add(space.getStart());
		}
		
		cachedNodes.clear();
		
		while(!nodes.isEmpty()) {
			Node node = nodes.remove(0);
			cachedNodes.add(node);
			
			for(Node neighbor : space.getNeighbors(node)) {				
				if(isInBounds(neighbor) && !cachedNodes.contains(neighbor)) {
					nodes.add(neighbor);
				}
			}
		}
	}
	
	private boolean isInBounds(Node node) {
		return isInBounds(node, 1);
	}
	
	private boolean isInBounds(Node point, int depth) {
		if(depth > 0) {
			for(Node neighbor : space.getNeighbors(point)) {
				if(isInBounds(neighbor, depth-1)) return true;
			}
		}
		return  point.getPosition().x() >= view.x() &&
				point.getPosition().x() <= view.x() + view.width() &&
				point.getPosition().y() >= view.y() &&
				point.getPosition().y() <= view.y() + view.height();
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
		return (int) (nodeFont.getSize() * 3);
	}
	
	public StateSpace<Node> getSpace() {
		return space;
	}
	
	public void select(Node node) {
		selected = node;
		repaint();
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
	
	public void nextIteration() {
		ArrayList<Node> path = searchAlgorithm.iterateSearch();
		if(path != null) this.path = path;
		repaint();
	}

	public void continueSearch() {
		new Thread(new Runnable() {
			@Override
			public void run() {				
				while(path.isEmpty()) {
					nextIteration();
					try {
						Thread.sleep(100);
					} catch(Exception e) {}
				}
			}	
		}).start();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(listener == null) return;
		for(Node node : cachedNodes) {
			if(spaceToPixel(node).distance(new Vector(e.getX(), e.getY()), Measurement.EUCLIDEAN) < getNodeSize() / 2) {
				listener.nodeSelected(node);
				selected = node;
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
		double scaleFactor = Math.min(1.08, 1 + -e.getPreciseWheelRotation() / 30);
		if(nodesOnScreen > 200 && scaleFactor > 1) return;
		
		Vector beforeScale = pixelToSpace(e.getX(), e.getY());
		
		view.size.scale(scaleFactor);
		
		Vector afterScale = pixelToSpace(e.getX(), e.getY());
		Vector translation = beforeScale.translated(afterScale.scaled(-1));
		
		view.position.translate(translation);
		
		if(e.getUnitsToScroll() == 0) cacheNodes();
		
		repaint();
	}
	
}
