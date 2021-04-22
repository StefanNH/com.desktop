package mp.com.desktop.mpgraph;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class GraphMP {

	private ArrayList<Vertex> vertices = new ArrayList();
	private HashMap<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	private ArrayList<Edge> edges = new ArrayList<>();

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
			Edge newEdge = new Edge(v1, v2);
			this.cellLayer.getChildren().add(newEdge);
			this.edges.add(newEdge);
	}
}
