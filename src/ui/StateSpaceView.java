package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JPanel;

import searchAlgorithms.GeneralSearch;
import searchStrategy.SearchStrategy;
import util.Copyable;
import util.Measurement;
import util.Nameable;
import util.Position;
import util.Vector;

public abstract class StateSpaceView<Node extends Position&Nameable&Copyable> extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {

	private GeneralSearch<Node> searchAlgorithm;
	private HashSet<Node> cachedNodes = new HashSet<>();
	private Vector dragStartLocation = null;
	private SSVListener<Node> listener = null;
	private int nodesOnScreen = 0;
	private ArrayList<Node> path = new ArrayList<>();
	private Node selected = null;
	private Dimension oldSize;
	private int searchDelay = 100;
	
	protected Vector position, size;
	protected static final int viewSize = 6;
	protected Font nodeFont = new Font("Arial", Font.PLAIN, 15);
	protected int visibilityDepth = 1;
	
	public StateSpaceView(GeneralSearch<Node> searchAlgorithm, Vector position, Vector size) {
		setSize(700, 700);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		oldSize = getSize();
		
		this.searchAlgorithm = searchAlgorithm;
		this.position = position;
		this.size = size;
	}
	
	protected abstract Vector spaceToPixel(Vector spacePosition);
	protected abstract void dragged(Vector pixelDelta);
	protected abstract void scrolled(double delta, Vector pixelPosition);
	protected double drawOrder(Node node) {
		return 1;
	}
	
	protected void drawNode(Graphics2D g, Node node) {		
		Vector translatedOvalPosition = spaceToPixel(node.getPosition());
		translatedOvalPosition.translate(-getNodeSize() / 2, -getNodeSize() / 2);
		Vector translatedOvalCenter = spaceToPixel(node.getPosition());
		
		Vector nameSize = getStringSize(g, node.getName());
		Vector namePosition = translatedOvalCenter.translated(nameSize.x() * -0.5, nameSize.y() * 0.25);
		
		Color fillColor = getNodeFillColor(node);
		
		g.setStroke(new BasicStroke(1));
		g.setColor(fillColor);
		g.fillOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), getNodeSize(), getNodeSize());	

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(node.equals(selected) ? 3 : 1));
		
		g.drawString(node.getName(), (int) namePosition.x(), (int) namePosition.y());
		g.drawOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), getNodeSize(), getNodeSize());
		
		if(searchAlgorithm.getStateSpace().isGoal(node)) {
			int offset = 2;
			g.drawOval((int) translatedOvalPosition.x() + offset, (int) translatedOvalPosition.y() + offset, getNodeSize() - 2 * offset, getNodeSize() - 2 * offset);
		}
	}
	
	protected void drawArrow(Graphics2D g, Node from, Node to) {
		if(arrowIsInPath(to, from)) return;
		
		boolean highlightArrow = arrowIsInPath(from, to);
		
		Vector translatedNodeCenter = spaceToPixel(from.getPosition());
		Vector translatedNeighborCenter = spaceToPixel(to.getPosition());

		drawArrow(g, translatedNodeCenter, translatedNeighborCenter, highlightArrow);
	}
	
	protected void drawArrow(Graphics2D g, Vector from, Vector to, boolean highlighted) {
		Vector[] triangleCorners = getTriangle(from, to, highlighted ? 2 : 1);
		
		int[] xPos = new int[3];
		int[] yPos = new int[3];
		
		xPos[0] = (int) triangleCorners[0].x();
		yPos[0] = (int) triangleCorners[0].y();
		xPos[1] = (int) triangleCorners[1].x();
		yPos[1] = (int) triangleCorners[1].y();
		xPos[2] = (int) triangleCorners[2].x();
		yPos[2] = (int) triangleCorners[2].y();
		
		g.setColor(highlighted ? Color.magenta : Color.BLACK);
		g.setStroke(highlighted ? new BasicStroke(3) : new BasicStroke(1));
		g.fillPolygon(xPos, yPos, xPos.length);
		
		Vector neighborOffset = to.interpolated(from, getNodeSize() * 0.75);
		Vector nodeOffset = from.interpolated(to, getNodeSize() / 2);
				
		g.drawLine((int) nodeOffset.x(), (int) nodeOffset.y(), (int) neighborOffset.x(), (int) neighborOffset.y());
	}
	
	@Override
	public void paint(Graphics g0) {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, getWidth(), getHeight());
		nodesOnScreen = 0;
		
		TreeMap<Double, ArrayList<Node>> treeMap = new TreeMap<>();
		for(Node node : cachedNodes) {
			double index = drawOrder(node);
			if(!treeMap.containsKey(index)) treeMap.put(index, new ArrayList<>());
			treeMap.get(index).add(node);
		}
		
		for(Entry<Double, ArrayList<Node>> entry : treeMap.entrySet()) {
			for(Node node : entry.getValue()) {				
				if(!isInBounds(node)) continue;
				
				for(Node neighbor : searchAlgorithm.getStateSpace().getNeighbors(node)) {
					drawArrow(g, node, neighbor);
				}
				
				drawNode(g, node);
				
				nodesOnScreen++;
			}
		}
		
		g0.drawImage(img, 0, 0, null);
	}
	
	private Color getNodeFillColor(Node node) {
		SearchStrategy<Node>.PathRating currentPathRating = searchAlgorithm.getStrategy().read();
		if(path.isEmpty() && currentPathRating != null && currentPathRating.path.getLast().equals(node)) return Color.magenta;
		if(path.contains(node)) return Color.magenta;
		if(searchAlgorithm.minCostToVisitedNode.containsKey(node)) return new Color(100, 237, 113);
		if(searchAlgorithm.minCostToNode.containsKey(node)) return new Color(101, 201, 235);
		return Color.white;
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
		
		Vector base = to.interpolated(from, height);
		Vector direction = from.translated(to.scaled(-1));
		Vector perpendicularDirection = new Vector(-direction.y(), direction.x());
		perpendicularDirection.setLength(Measurement.EUCLIDEAN, width);
		
		Vector a = base.translated(perpendicularDirection).interpolated(from, getNodeSize() / 2);
		Vector b = base.translated(perpendicularDirection.scaled(-1)).interpolated(from, getNodeSize() / 2);
		
		return new Vector[] { to.interpolated(from, getNodeSize() / 2), a, b };
	}
	
	private Vector getStringSize(Graphics g, String text) {
		FontMetrics fm = g.getFontMetrics(nodeFont);
		
		int stringWidth = fm.stringWidth(text);
		int stringHeight = fm.getHeight();
		
		return new Vector(stringWidth, stringHeight);
	}
	
	protected void cacheNodes() {
		ArrayList<Node> nodes = new ArrayList<>();
		nodes.addAll(cachedNodes);
		
		if(nodes.isEmpty()) {
			nodes.add(searchAlgorithm.getStateSpace().getStart());
		}
		
		cachedNodes.clear();
		
		while(!nodes.isEmpty()) {
			Node node = nodes.remove(0);
			cachedNodes.add(node);
			
			for(Node neighbor : searchAlgorithm.getStateSpace().getNeighbors(node)) {				
				if(isInBounds(neighbor) && !cachedNodes.contains(neighbor)) {
					nodes.add(neighbor);
				}
			}
		}
		
		repaint();
	}
	
	private boolean isInBounds(Node node) {
		return isInBounds(node, visibilityDepth);
	}
	
	private boolean isInBounds(Node node, int depth) {
		if(depth > 0) {
			for(Node neighbor : searchAlgorithm.getStateSpace().getNeighbors(node)) {
				if(isInBounds(neighbor, depth-1)) return true;
			}
		}
		return isVisible(node);
	}
	
	protected boolean isVisible(Node node) {		
		Vector position = spaceToPixel(node.getPosition());
		return  position.x() >= 0 &&
				position.x() <= getWidth() &&
				position.y() >= 0 &&
				position.y() <= getHeight();
	}
	
	private int getNodeSize() {
		return (int) (nodeFont.getSize() * 3);
	}
	
	public void select(Node node) {
		selected = node;
		repaint();
	}
	
	public void setSSVListener(SSVListener<Node> listener) {
		this.listener = listener;
	}
	
	public void nextIteration() {
		ArrayList<Node> path = searchAlgorithm.iterateSearch();
		if(path != null) {
			this.path = path;
			if(listener != null) listener.hasPathChanged(!this.path.isEmpty());
		}
		repaint();
	}
	
	public void initialize() {
		cacheNodes();
		searchAlgorithm.initializeSearch();
		path.clear();
		if(listener != null) listener.hasPathChanged(!this.path.isEmpty());
		repaint();
	}

	public void continueSearch() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(path.isEmpty()) {
					nextIteration();
					try {
						Thread.sleep(searchDelay);
					} catch(Exception e) {}
				}
			}	
		}).start();
	}
	
	public void setSearchDelay(int delay) {
		searchDelay = delay;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(listener == null) return;
		for(Node node : cachedNodes) {
			if(spaceToPixel(node.getPosition()).distance(new Vector(e.getX(), e.getY()), Measurement.EUCLIDEAN) < getNodeSize() / 2) {
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
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		Vector pixelDelta = new Vector(-e.getX(), -e.getY()).translated(dragStartLocation);
		dragged(pixelDelta);
		dragStartLocation = new Vector(e.getX(), e.getY());
		cacheNodes();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double scaleFactor = Math.min(1.08, 1 + -e.getPreciseWheelRotation() / 30);
		if(nodesOnScreen > 200 && scaleFactor > 1) return;
		
		scrolled(e.getPreciseWheelRotation(), new Vector(e.getX(), e.getY()));
		
		if(e.getUnitsToScroll() == 0) cacheNodes();
		
		repaint();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		double widthDelta = getWidth() / oldSize.getWidth();
		double heightDelta = getHeight() / oldSize.getHeight();
		size.set(0, size.get(0) * widthDelta);
		size.set(1, size.get(1) * heightDelta);
		oldSize = getSize();
		cacheNodes();
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}
	
}
