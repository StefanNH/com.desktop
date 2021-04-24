package mp.com.desktop.mpgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class GraphMP {

	private ArrayList<Vertex> vertices = new ArrayList<>();
	private HashMap<String, Vertex> vertexMap = new HashMap<>();
	private ArrayList<Edge> edges = new ArrayList<>();

	// undo functionality
	private Stack<Vertex> removedVertices = new Stack<>();
	private Stack<Edge> removedEdges = new Stack<>();

	ContextMenu contextMenu = new ContextMenu();
	// create menuitems
	MenuItem menuItem1 = new MenuItem("Add vertex");
	MenuItem menuItem2 = new MenuItem("Add schema");
	MenuItem menuItem3 = new MenuItem("Undo");
	MenuItem menuItem4 = new MenuItem("close");

	private Group canvas;

	private ZoomScrollPane scrollPane;

	private Pane vertexLayer;

	public GraphMP() {

		canvas = new Group();
		vertexLayer = new Pane();

		canvas.getChildren().add(vertexLayer);
		scrollPane = new ZoomScrollPane(canvas);
		// add menu items to menu
		contextMenu.getItems().add(menuItem1);
		contextMenu.getItems().add(menuItem2);
		contextMenu.getItems().add(menuItem3);
		contextMenu.getItems().add(menuItem4);
		scrollPane.setOnMousePressed(eh -> {
			if (eh.isSecondaryButtonDown() && !contextMenu.isShowing()) {
				contextMenu.show(canvas, eh.getScreenX(), eh.getScreenY());
			} else if (contextMenu.isShowing()) {
				contextMenu.hide();
			}
		});

		menuItem1.setOnAction(e -> {
			addVertex(new VertexSquare(this, ""));
		});
		menuItem2.setOnAction(e -> {
			addVertex(new VertexSchema(this, ""));
		});
		menuItem3.setOnAction(e -> {
			undoRemoved();
		});

		scrollPane.pannableProperty().set(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

	}

	public ScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public Pane getVertexLayer() {
		return this.vertexLayer;
	}

	public double getScale() {
		return this.scrollPane.getScaleValue();
	}

	public void removeEdge(Vertex v) {
		// enhanced for loop triggers concurrency exception
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext();) {
			Edge value = iterator.next();
			if (value.getSource().equals(v) || value.getTarget().equals(v)) {
				iterator.remove();
				edges.remove(value);
				vertexLayer.getChildren().remove(value);
				removedEdges.add(value);
			}
		}
	}

	public Vertex getVertexById(String id) {
		return vertexMap.get(id);
	}

	public void addVertex(Vertex v) {
		vertexLayer.getChildren().add(v);
		vertices.add(v);
		vertexMap.put(v.getVertexId(), v);
	}

	public void removeVertex(Vertex v) {
		vertexLayer.getChildren().remove(v);
		vertices.remove(v);
		vertexMap.remove(v.getVertexId());
		removedVertices.add(v);
	}

	public void undoRemoved() {
		if (removedVertices != null || removedVertices.size() > 0) {
			Vertex v = removedVertices.pop();
			vertexLayer.getChildren().add(v);
			vertices.add(v);
			while (removedEdges.lastElement().getSource().equals(v)
					|| removedEdges.lastElement().getTarget().equals(v)) {
				Edge e = removedEdges.pop();
				edges.add(e);
				vertexLayer.getChildren().add(e);
				if (removedEdges.isEmpty()) {
					break;
				}
			}
		}
	}

	public void addEdges(Vertex v1, Vertex v2) {
		if (v1 instanceof VertexSquare) {
			if (v2 instanceof VertexSquare) {
				VertexSchema nvs = new VertexSchema(this, "");
				Edge newEdge = new Edge(v1, nvs);
				addVertex(nvs);
				nvs.relocate(((v1.getBoundsInParent().getCenterX() + v2.getBoundsInParent().getCenterX()) / 2) - 6,
						((v1.getBoundsInParent().getCenterY() + v2.getBoundsInParent().getCenterY()) / 2) - 6);
				newEdge.getX1().bind(v1.layoutXProperty().add(((VertexSquare) v1).getSquare().getWidth() / 2.0));
				newEdge.getY1().bind(v1.layoutYProperty().add(((VertexSquare) v1).getSquare().getHeight() / 2.0));

				newEdge.getX2().bind(nvs.layoutXProperty().add(nvs.getSquare().getWidth() / 2.0));
				newEdge.getY2().bind(nvs.layoutYProperty().add(nvs.getSquare().getHeight() / 2.0));

				Edge newEdge1 = new Edge(nvs, v2);
				newEdge1.getX1().bind(nvs.layoutXProperty().add(nvs.getSquare().getWidth() / 2.0));
				newEdge1.getY1().bind(nvs.layoutYProperty().add(nvs.getSquare().getHeight() / 2.0));
				newEdge1.getX2().bind(v2.layoutXProperty().add(((VertexSquare) v2).getSquare().getWidth() / 2.0));
				newEdge1.getY2().bind(v2.layoutYProperty().add(((VertexSquare) v2).getSquare().getHeight() / 2.0));
				vertexLayer.getChildren().add(newEdge);
				edges.add(newEdge);
				vertexLayer.getChildren().add(newEdge1);
				edges.add(newEdge1);
			} else if (v2 instanceof VertexSchema) {
				if (v1 instanceof VertexSquare) {
					Edge newEdge = new Edge(v1, v2);
					newEdge.getX1().bind(v1.layoutXProperty().add(((VertexSquare) v1).getSquare().getWidth() / 2.0));
					newEdge.getY1().bind(v1.layoutYProperty().add(((VertexSquare) v1).getSquare().getHeight() / 2.0));
					newEdge.getX2().bind(v2.layoutXProperty().add(((VertexSchema) v2).getSquare().getWidth() / 2.0));
					newEdge.getY2().bind(v2.layoutYProperty().add(((VertexSchema) v2).getSquare().getHeight() / 2.0));
					vertexLayer.getChildren().add(newEdge);
					edges.add(newEdge);
				} else if (v1 instanceof VertexSchema) {
					Edge newEdge = new Edge(v1, v2);
					newEdge.getX1().bind(v1.layoutXProperty().add(((VertexSchema) v1).getSquare().getWidth() / 2.0));
					newEdge.getY1().bind(v1.layoutYProperty().add(((VertexSchema) v1).getSquare().getHeight() / 2.0));
					newEdge.getX2().bind(v2.layoutXProperty().add(((VertexSchema) v2).getSquare().getWidth() / 2.0));
					newEdge.getY2().bind(v2.layoutYProperty().add(((VertexSchema) v2).getSquare().getHeight() / 2.0));
					vertexLayer.getChildren().add(newEdge);
					edges.add(newEdge);
				}
			}
		} else if (v1 instanceof VertexSchema) {
			if (v2 instanceof VertexSquare) {
				Edge newEdge = new Edge(v1, v2);
				newEdge.getX1().bind(v1.layoutXProperty().add(((VertexSchema) v1).getSquare().getWidth() / 2.0));
				newEdge.getY1().bind(v1.layoutYProperty().add(((VertexSchema) v1).getSquare().getHeight() / 2.0));
				newEdge.getX2().bind(v2.layoutXProperty().add(((VertexSquare) v2).getSquare().getWidth() / 2.0));
				newEdge.getY2().bind(v2.layoutYProperty().add(((VertexSquare) v2).getSquare().getHeight() / 2.0));
				vertexLayer.getChildren().add(newEdge);
				edges.add(newEdge);
			} else if (v2 instanceof VertexSchema) {
				Edge newEdge = new Edge(v1, v2);
				newEdge.getX1().bind(v1.layoutXProperty().add(((VertexSchema) v1).getSquare().getWidth() / 2.0));
				newEdge.getY1().bind(v1.layoutYProperty().add(((VertexSchema) v1).getSquare().getHeight() / 2.0));
				newEdge.getX2().bind(v2.layoutXProperty().add(((VertexSchema) v2).getSquare().getWidth() / 2.0));
				newEdge.getY2().bind(v2.layoutYProperty().add(((VertexSchema) v2).getSquare().getHeight() / 2.0));
				vertexLayer.getChildren().add(newEdge);
				edges.add(newEdge);
			}
		}
	}
}
