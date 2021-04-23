package mp.com.desktop.mpgraph;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;

public class EdgeTest extends Group {
	protected Vertex source;
	protected Vertex target;

	private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
	private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
	private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
	private SimpleDoubleProperty y2 = new SimpleDoubleProperty();

	private Polyline line = new Polyline();

	public EdgeTest(Vertex source, Vertex target) {
		if (source instanceof VertexSquare) {
			if (target instanceof VertexSquare) {
				this.source = source;
				this.target = target;
				this.x1.set(((VertexSquare) source).getSquare().getLayoutX());
				this.y1.set(((VertexSquare) source).getSquare().getLayoutY());
				this.x2.set(((VertexSquare) target).getSquare().getLayoutX());
				this.y2.set(((VertexSquare) target).getSquare().getLayoutY());
				getChildren().add(line);


			}
		}
		for (SimpleDoubleProperty s : new SimpleDoubleProperty[] { this.x1, this.y1, this.x2, this.y2 }) {
			s.addListener((observable, oldVal, newVal) -> updateCoordinates());
		}
	}

	private void updateCoordinates() {
		line.getPoints().setAll(x1.get(), y1.get(), x2.get(), y2.get());
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
