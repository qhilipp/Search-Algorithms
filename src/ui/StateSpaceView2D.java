package ui;

import searchAlgorithms.GeneralSearch;
import util.Copyable;
import util.Nameable;
import util.Position;
import util.Rectangle;
import util.Vector;

public class StateSpaceView2D<Node extends Position & Nameable & Copyable> extends StateSpaceView<Node> {

	public StateSpaceView2D(GeneralSearch<Node> searchAlgorithm) {
		super(searchAlgorithm,
				new Vector(searchAlgorithm.getStateSpace().getStart().getPosition().x() - viewSize / 2,
						searchAlgorithm.getStateSpace().getStart().getPosition().y() - viewSize / 2),
				new Vector(viewSize, viewSize));
		initialize();
	}

	@Override
	protected Vector spaceToPixel(Vector spacePosition) {
		double translatedX = (spacePosition.getPosition().x() - position.x()) / size.x() * getWidth();
		double translatedY = (spacePosition.getPosition().y() - position.y()) / size.y() * getHeight();
		return new Vector(translatedX, translatedY);
	}

	protected Vector pixelToSpace(Vector pixelPosition) {
		double convertedX = pixelPosition.x() / getWidth() * size.x() + position.x();
		double convertedY = pixelPosition.y() / getHeight() * size.y() + position.y();
		return new Vector(convertedX, convertedY);
	}

	@Override
	protected void dragged(Vector pixelDelta) {
		pixelDelta = pixelToSpace(pixelDelta);
		pixelDelta.translate(position.scaled(-1));
		position.translate(pixelDelta);
	}

	@Override
	protected void scrolled(double delta, Vector pixelPosition) {
		Vector beforeScale = pixelToSpace(pixelPosition);
		
		double factor = Math.min(1.08, 1 + -delta / 30);
		
		size.scale(factor);

		Vector afterScale = pixelToSpace(pixelPosition);
		Vector translation = beforeScale.translated(afterScale.scaled(-1));

		position.translate(translation);
	}

}
