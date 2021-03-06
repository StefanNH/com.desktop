package mp.com.desktop.mpgraph;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class AtomVertex extends Vertex {

	private double x = 0;
	private double y = 0;
	private Label lb = new Label();
	private GraphMP graph;
	private ContextMenu contextMenu = new ContextMenu();
	private Circle circle = new Circle();
	// create menuitems
	private MenuItem menuItem1 = new MenuItem("Change content");
	private MenuItem menuItem2 = new MenuItem("Delete vertex");
	private MenuItem menuItem3 = new MenuItem("Close");
	private Rectangle square = new Rectangle(20, 20);

	public AtomVertex(GraphMP g, String strContent) {
		super();
		graph = g;

		lb.setText(strContent);
		lb.setLayoutX(square.getLayoutX() + 22);

		square.setStroke(Color.GRAY);
		square.setFill(Color.GRAY);
		square.setArcHeight(5);
		square.setArcWidth(5);
		square.setOnMousePressed(e -> onMousePressedHandler(e));
		square.setOnMouseDragged(e -> onMouseDraggedHandler(e));
		square.setOnMouseReleased(e -> onMouseReleasedHandler(e));
		square.setOnDragOver(e -> {
			if (e.getDragboard().hasString()) {
				e.acceptTransferModes(TransferMode.ANY);
				e.consume();
			}
		});
		square.setOnDragDropped(e -> {
			String str = e.getDragboard().getString();
			graph.addEdges(graph.getVertexById(str), getCurrent());
			e.consume();
		});

		circle.setFill(Color.BLACK);
		circle.setCenterX(10);
		circle.setRadius(4);
		circle.setOnDragDetected(e -> {
			Dragboard db = circle.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(this.getVertexId());
			db.setContent(content);
			e.consume();
		});

		menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TextInputDialog db = new TextInputDialog();
				db.setTitle("Edit content");
				db.setHeaderText("Do you want to edit the content of Node ID: " + getVertexId());
				db.getEditor().setText(lb.getText());
				db.showAndWait();
				lb.setText(db.getEditor().getText());
			}
		});
		menuItem2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				removeThisNode();
			}
		});

		// add menu items to menu
		contextMenu.getItems().add(menuItem1);
		contextMenu.getItems().add(menuItem2);
		contextMenu.getItems().add(menuItem3);
		setOnMousePressed(eh -> {
			eh.consume();
			if (eh.isSecondaryButtonDown() && !contextMenu.isShowing()) {
				contextMenu.show(this, eh.getScreenX(), eh.getScreenY());
			} else if (contextMenu.isShowing()) {
				contextMenu.hide();
			}
		});

		getChildren().add(lb);
		getChildren().add(square);
		getChildren().add(circle);
	}

	private void onMousePressedHandler(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			Node node;
			Node srs = (Node) event.getSource();
			if (srs instanceof Rectangle) {
				node = srs.getParent();
				double scale = graph.getScale();
				toFront();
				x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
				y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();
			}
		}
	}

	private void onMouseDraggedHandler(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			event.consume();
			Node node;
			Node srs = (Node) event.getSource();
			if (srs instanceof Rectangle) {
				node = srs.getParent();
				double offsetX = event.getScreenX() + x;
				double offsetY = event.getScreenY() + y;

				// adjust the offset in case we are zoomed
				double scale = graph.getScale();

				offsetX /= scale;
				offsetY /= scale;

				node.relocate(offsetX, offsetY);
			}
		}
	}

	private void onMouseReleasedHandler(MouseEvent event) {

	}

	public Rectangle getSquare() {
		return this.square;
	}

	protected Vertex getCurrent() {
		return this;
	}

	private void removeThisNode() {
		graph.removeEdge(getCurrent());
		graph.removeVertex(getCurrent());
	}

	public String getContent() {
		return lb.getText();
	}
}
