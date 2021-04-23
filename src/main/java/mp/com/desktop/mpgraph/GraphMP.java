package mp.com.desktop.mpgraph;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class GraphMP {

	private ArrayList<Vertex> vertices = new ArrayList();
	private HashMap<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	private ArrayList<EdgeTest> edges = new ArrayList<>();

	private Group canvas;

	private ZoomScrollPane scrollPane;

	private Pane cellLayer;

	public GraphMP() {

		canvas = new Group();
		cellLayer = new Pane();

		canvas.getChildren().add(cellLayer);
		scrollPane = new ZoomScrollPane(canvas);

		scrollPane.pannableProperty().set(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

	}

	public ScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public Pane getCellLayer() {
		return this.cellLayer;
	}

	public double getScale() {
		return this.scrollPane.getScaleValue();
	}

	public Vertex getVertexById(String id) {
		return vertexMap.get(id);
	}

	public void addVertex(Vertex v) {
		this.cellLayer.getChildren().add(v);
		this.vertices.add(v);
		this.vertexMap.put(v.getVertexId(), v);
	}

	public void removeVertex(Vertex v) {
		this.cellLayer.getChildren().remove(v);
		this.vertices.remove(v);
		this.vertexMap.remove(v.getVertexId());
	}

	public void addEdges(Vertex v1, Vertex v2) {
		EdgeTest newEdge = new EdgeTest(v1, v2);
		VertexSquare nvs = new VertexSquare(this, "Schema");
		addVertex(nvs);
		nvs.relocate(((v1.getBoundsInParent().getCenterX() + v2.getBoundsInParent().getCenterX()) / 2) - 6,
				((v1.getBoundsInParent().getCenterY() + v2.getBoundsInParent().getCenterY()) / 2) - 6);
		newEdge.getX1().bind(v1.layoutXProperty().add(((VertexSquare) v1).getSquare().getWidth() / 2.0));
		newEdge.getY1().bind(v1.layoutYProperty().add(((VertexSquare) v1).getSquare().getHeight() / 2.0));
//			newEdge.getX2().bind(v2.layoutXProperty().add(((VertexSquare) v2).getSquare().getWidth() / 2.0));
//			newEdge.getY2().bind(v2.layoutYProperty().add(((VertexSquare) v2).getSquare().getHeight() / 2.0));
		newEdge.getX2().bind(nvs.layoutXProperty().add(((VertexSquare) v2).getSquare().getWidth() / 2.0));
		newEdge.getY2().bind(nvs.layoutYProperty().add(((VertexSquare) v2).getSquare().getHeight() / 2.0));

		EdgeTest newEdge1 = new EdgeTest(nvs, v2);
		newEdge1.getX1().bind(nvs.layoutXProperty().add(((VertexSquare) v2).getSquare().getWidth() / 2.0));
		newEdge1.getY1().bind(nvs.layoutYProperty().add(((VertexSquare) v2).getSquare().getHeight() / 2.0));
		newEdge1.getX2().bind(v2.layoutXProperty().add(((VertexSquare) v2).getSquare().getWidth() / 2.0));
		newEdge1.getY2().bind(v2.layoutYProperty().add(((VertexSquare) v2).getSquare().getHeight() / 2.0));
		this.cellLayer.getChildren().add(newEdge1);
		this.edges.add(newEdge1);
		this.cellLayer.getChildren().add(newEdge);
		this.edges.add(newEdge);
	}
}
