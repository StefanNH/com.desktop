package mp.com.desktop.mpgraph;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class EdgeTest extends Group {
	private final double OFFSET_SCALE = 15;
	private final double ARROW_ANGLE = 10;
	private final double ARROW_LENGTH = 4;
	protected Vertex source;
	protected Vertex target;

	private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
	private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
	private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
	private SimpleDoubleProperty y2 = new SimpleDoubleProperty();

	private Polyline line = new Polyline();
	private Polyline arrowhead = new Polyline();

	public EdgeTest(Vertex source, Vertex target) {

		line.setFill(Color.DIMGREY);
		line.setStroke(Color.DIMGREY);
		arrowhead.setFill(Color.DIMGREY);
		arrowhead.setStroke(Color.DIMGREY);
		if (source instanceof VertexSquare) {
			if (target instanceof VertexSquare) {
				this.source = source;
				this.target = target;
				this.x1.set(((VertexSquare) source).getSquare().getLayoutX());
				this.y1.set(((VertexSquare) source).getSquare().getLayoutY());
				this.x2.set(((VertexSquare) target).getSquare().getLayoutX());
				this.y2.set(((VertexSquare) target).getSquare().getLayoutY());
				getChildren().addAll(line, arrowhead);

			}
		}
		for (SimpleDoubleProperty s : new SimpleDoubleProperty[] { this.x1, this.y1, this.x2, this.y2 }) {
			s.addListener((observable, oldVal, newVal) -> updateCoordinates());
		}
	}

	private void updateCoordinates() {
		double[] start = offset(this.x1.get(), this.y1.get(), this.x2.get(), this.y2.get());
		double[] end = offset(this.x2.get(), this.y2.get(), this.x1.get(), this.y1.get());

		line.getPoints().setAll(start[0], start[1], end[0], end[1]);

		double angle = Math.atan2(start[1] - end[1], start[0] - end[0]);
		double x = end[0] - Math.cos(angle - ARROW_ANGLE) * ARROW_LENGTH;
		double y = end[1] - Math.sin(angle - ARROW_ANGLE) * ARROW_LENGTH;
		arrowhead.getPoints().setAll(x, y, end[0], end[1]);
		double x1 = end[0] - Math.cos(angle + ARROW_ANGLE) * ARROW_LENGTH;
		double y1 = end[1] - Math.sin(angle + ARROW_ANGLE) * ARROW_LENGTH;
		// adding forth point x,y will result in triangle arrowhead
		arrowhead.getPoints().addAll(x1, y1, x, y);
	}

	private double[] offset(double x1, double y1, double x2, double y2) {
		double angle = Math.atan2(y2 - y1, x2 - x1);
		return new double[] { x1 + Math.cos(angle) * OFFSET_SCALE, y1 + Math.sin(angle) * OFFSET_SCALE };
	}

	public Vertex getSource() {
		return source;
	}

	public Vertex getTarget() {
		return target;
	}

	public SimpleDoubleProperty getX1() {
		return x1;
	}

	public void setX1(SimpleDoubleProperty x1) {
		this.x1 = x1;
	}

	public SimpleDoubleProperty getY1() {
		return y1;
	}

	public void setY1(SimpleDoubleProperty y1) {
		this.y1 = y1;
	}

	public SimpleDoubleProperty getX2() {
		return x2;
	}

	public void setX2(SimpleDoubleProperty x2) {
		this.x2 = x2;
	}

	public SimpleDoubleProperty getY2() {
		return y2;
	}

	public void setY2(SimpleDoubleProperty y2) {
		this.y2 = y2;
	}

}
