package mp.com.desktop.mpgraph;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
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

public class SchemaVertex extends Vertex {
	private final String[] SCHEMA_OPTIONS = new String[] { "Support", "Conflict", "Argument from Sign",
			"Argument from an Exceptional", "Argument from Analogy", "Argument from Bias",
			"Argument from Cause to Effect", "Argument from Correlation to Causes", "Argument from Established Rule",
			"Argument from Evidence to a Hypothesis", "Argument from Falsification to a Hypothesis",
			"Argument from Example", "Argument from Commitment", "Circumstantial Argument Against the Person",
			"Argument from Popular Practice", "Argument from Popularity", "Argument from Position to Know",
			"Argument from Expert Opinion", "Argument from Precedent", "Argument from Consequences",
			"Argument from Waste", "Causal Slippery Slope Argument" };

	private double x = 0;
	private double y = 0;
	private Label lb = new Label();
	private GraphMP graph;
	private ContextMenu contextMenu = new ContextMenu();
	private Circle circle = new Circle();
	// create menuitems
	private MenuItem menuItem1 = new MenuItem("Change schema");
	private MenuItem menuItem2 = new MenuItem("Delete schema");
	private MenuItem menuItem3 = new MenuItem("Close");
	private Rectangle square = new Rectangle(20, 20);

	public SchemaVertex(GraphMP g, String strContent) {
		super();
		graph = g;
		if (strContent == null || strContent.equals("")) {
			lb.setText("Schema");
		} else {
			lb.setText(strContent);
		}
		lb.setLayoutX(25);
		lb.setLayoutY(15);

		square.setStroke(Color.ORANGE);
		square.setFill(Color.ORANGE);
		square.setRotate(45);
		if (lb.getText().equals(SCHEMA_OPTIONS[0])) {
			getSquare().setFill(Color.LIMEGREEN);
			getSquare().setStroke(Color.LIMEGREEN);
		} else if (lb.getText().equals(SCHEMA_OPTIONS[1])) {
			getSquare().setFill(Color.RED);
			getSquare().setStroke(Color.RED);
		} else {
			getSquare().setFill(Color.ORANGE);
			getSquare().setStroke(Color.ORANGE);
		}
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
				ChoiceDialog<String> db = new ChoiceDialog<>(SCHEMA_OPTIONS[0], SCHEMA_OPTIONS);
				db.setTitle("Schema");
				db.setHeaderText("Choose schema");
				db.showAndWait();
				String res = db.getResult();
				if (res.equals(SCHEMA_OPTIONS[0])) {
					getSquare().setFill(Color.LIMEGREEN);
					getSquare().setStroke(Color.LIMEGREEN);
				} else if (res.equals(SCHEMA_OPTIONS[1])) {
					getSquare().setFill(Color.RED);
					getSquare().setStroke(Color.RED);
				} else {
					getSquare().setFill(Color.ORANGE);
					getSquare().setStroke(Color.ORANGE);
				}
				lb.setText(res);
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
