package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	private int fontSize = 15;
	private Graphics2D g;
	
	protected Vector position, size;
	protected static final int viewSize = 6;
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
	protected double getNodeVisibility(Node node) {
		return 1;
	}
	
	private void drawNode(Node node) {
		int nodeSize = getNodeSize(node);
		
		Vector translatedOvalPosition = spaceToPixel(node.getPosition());
		translatedOvalPosition.translate(-nodeSize / 2, -nodeSize / 2);
		Vector translatedOvalCenter = spaceToPixel(node.getPosition());
		
		Vector namePosition = translatedOvalCenter.translated(nodeSize * -0.5, 0);
		
		Color fillColor = getDistanceColor(getNodeFillColor(node), getNodeVisibility(node));
		
		g.setStroke(new BasicStroke(1));
		g.setColor(fillColor);
		g.fillOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), nodeSize, nodeSize);

		g.setColor(getDistanceColor(Color.black, getNodeVisibility(node)));
		g.setStroke(new BasicStroke(node.equals(selected) ? 3 : 1));
		
		g.setFont(getNodeFont(node));
		g.drawString(node.getName(), (int) namePosition.x(), (int) namePosition.y());
		g.drawOval((int) translatedOvalPosition.x(), (int) translatedOvalPosition.y(), nodeSize, nodeSize);
		
		if(searchAlgorithm.getStateSpace().isGoal(node)) {
			int offset = 2;
			g.drawOval((int) translatedOvalPosition.x() + offset, (int) translatedOvalPosition.y() + offset, nodeSize - 2 * offset, nodeSize - 2 * offset);
		}
	}
	
	private void drawArrow(Node from, Node to) {
		if(arrowIsInPath(to, from)) return;
		
		boolean highlighted = arrowIsInPath(from, to);
		
		Vector fromPosition = spaceToPixel(from.getPosition());
		Vector toPosition = spaceToPixel(to.getPosition());
		
		Vector[] triangleCorners = getTriangle(from, to, highlighted ? 2 : 1);
		
		int[] xPos = new int[3];
		int[] yPos = new int[3];
		
		xPos[0] = (int) triangleCorners[0].x();
		yPos[0] = (int) triangleCorners[0].y();
		xPos[1] = (int) triangleCorners[1].x();
		yPos[1] = (int) triangleCorners[1].y();
		xPos[2] = (int) triangleCorners[2].x();
		yPos[2] = (int) triangleCorners[2].y();
		
		g.setColor(highlighted ? getDistanceColor(Color.magenta, getNodeVisibility(from)) : getDistanceColor(Color.black, getNodeVisibility(from)));
		g.setStroke(highlighted ? new BasicStroke(3) : new BasicStroke(1));
		g.fillPolygon(xPos, yPos, xPos.length);
		
		Vector neighborOffset = toPosition.interpolated(fromPosition, getNodeSize(to) * 0.75);
		Vector nodeOffset = fromPosition.interpolated(toPosition, getNodeSize(from) / 2);
		
		if(neighborOffset.getLength(Measurement.MANHATTAN) > 5000 || nodeOffset.getLength(Measurement.MANHATTAN) > 5000) return;
		
		g.drawLine((int) nodeOffset.x(), (int) nodeOffset.y(), (int) neighborOffset.x(), (int) neighborOffset.y());
	}
	
	@Override
	public void paint(Graphics g0) {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		g = img.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
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
					drawArrow(node, neighbor);
				}
				
				drawNode(node);
				
				nodesOnScreen++;
			}
		}
		
		g0.drawImage(img, 0, 0, null);
	}
	
	private Color getDistanceColor(Color color, double distance) {
		Color background = getBackground();
		
		int red = background.getRed() - (int) ((background.getRed() - color.getRed()) * distance);
		int green = background.getGreen() - (int) ((background.getGreen() - color.getGreen()) * distance);
		int blue = background.getBlue() - (int) ((background.getBlue() - color.getBlue()) * distance);
		
		return new Color(red, green, blue);
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
	
	private Vector[] getTriangle(Node from, Node to, double size) {
		double nodeSize = getNodeSize(to);
		double width = nodeSize / 10 * size;
		double height = nodeSize / 4 * size;
		
		Vector fromPosition = spaceToPixel(from.getPosition());
		Vector toPosition = spaceToPixel(to.getPosition());
		
		Vector base = toPosition.interpolated(fromPosition, height);
		Vector direction = fromPosition.translated(toPosition.scaled(-1));
		Vector perpendicularDirection = new Vector(-direction.y(), direction.x());
		perpendicularDirection.setLength(Measurement.EUCLIDEAN, width);
		
		Vector a = base.translated(perpendicularDirection).interpolated(fromPosition, nodeSize / 2);
		Vector b = base.translated(perpendicularDirection.scaled(-1)).interpolated(fromPosition, nodeSize / 2);
		
		return new Vector[] { toPosition.interpolated(fromPosition, nodeSize / 2), a, b };
	}
	
	private Font getNodeFont(Node node) {
		float fontSize = (float) Math.max(5, this.fontSize * getNodeVisibility(node));
		return new Font("Arial", Font.PLAIN, 1).deriveFont(fontSize);
	}
	
	private int getNodeSize(Node node) {
		FontMetrics fm = g.getFontMetrics(getNodeFont(node));
		
		int padding = 2;		
		int stringWidth = fm.stringWidth(node.getName());
		
		return stringWidth + 2 * padding;
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
			if(spaceToPixel(node.getPosition()).distance(new Vector(e.getX(), e.getY()), Measurement.EUCLIDEAN) < getNodeSize(node) / 2) {
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
