package ui;

import searchAlgorithms.GeneralSearch;
import util.Copyable;
import util.Nameable;
import util.Position;
import util.Rectangle;
import util.Vector;

public class StateSpaceView2D<Node extends Position&Nameable&Copyable> extends StateSpaceView<Node> {

	private static final int viewSize = 6;
	
	public StateSpaceView2D(GeneralSearch<Node> searchAlgorithm) {
		super(
			searchAlgorithm,
			new Rectangle(
				searchAlgorithm.getStateSpace().getStart().getPosition().x() - viewSize / 2,
				searchAlgorithm.getStateSpace().getStart().getPosition().y() - viewSize / 2,
				viewSize,
				viewSize
			)
		);
	}
	
	@Override
	protected Vector spaceToPixel(Vector spacePosition) {
		double translatedX = (spacePosition.getPosition().x() - view.position.x()) / view.size.x() * getWidth();
		double translatedY = (spacePosition.getPosition().y() - view.position.y()) / view.size.y() * getHeight();
		return new Vector(translatedX, translatedY);
	}
	
	@Override
	protected Vector pixelToSpace(Vector pixelPosition) {
		double convertedX = pixelPosition.x() / getWidth() * view.size.x() + view.position.x();
		double convertedY = pixelPosition.y() / getHeight() * view.size.y() + view.position.y();
		return new Vector(convertedX, convertedY);
	}

	@Override
	protected void dragged(Vector pixelDelta) {
		pixelDelta = pixelToSpace(pixelDelta);
		pixelDelta.translate(view.position.scaled(-1));
		view.translate(pixelDelta);
	}

	@Override
	protected void scrolled(double factor, Vector pixelPosition) {
		Vector beforeScale = pixelToSpace(pixelPosition);
		
		view.size.scale(factor);
		
		Vector afterScale = pixelToSpace(pixelPosition);
		Vector translation = beforeScale.translated(afterScale.scaled(-1));
		
		view.position.translate(translation);
	}
	
}
