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

public class VertexSquare extends Vertex {

	private double x = 0;
	private double y = 0;
	private Label lb = new Label();
	private GraphMP graph;
	private ContextMenu contextMenu = new ContextMenu();
	private Circle circle = new Circle();
	// create menuitems
	private MenuItem menuItem1 = new MenuItem("Vertex item 1");
	private MenuItem menuItem2 = new MenuItem("Vertex item 2");
	private MenuItem menuItem3 = new MenuItem("Vertex item 3");
	private Rectangle square = new Rectangle(20, 20);

	public VertexSquare(GraphMP g, String strContent) {
		super();
		graph = g;
		
		lb.setText(strContent);
		lb.setLayoutX(square.getLayoutX() + 22);
		
		square.setStroke(Color.GRAY);
		square.setFill(Color.GRAY);
		square.setArcHeight(3);
		square.setArcWidth(3);
		square.setOnMousePressed(onMousePressedEventHandler);
		square.setOnMouseDragged(onMouseDraggedEventHandler);
		square.setOnMouseReleased(onMouseReleasedEventHandler);
		square.setOnDragOver(e->{
			if(e.getDragboard().hasString()) {
				e.acceptTransferModes(TransferMode.ANY);
			}
		});
		square.setOnDragDropped(e->{
			String str = e.getDragboard().getString();
			System.out.println(this.getVertexId());
			System.out.println(str);
			graph.addEdges(graph.getVertexById(str), getCurrent());
		});
		
		circle.setFill(Color.DARKRED);
        circle.setCenterX(10);
        circle.setRadius(3);
        circle.setOnDragDetected(e->{
        	Dragboard db = circle.startDragAndDrop(TransferMode.ANY);
        	ClipboardContent content = new ClipboardContent();
            content.putString(this.getVertexId());
            System.out.println(this.getVertexId());
            db.setContent(content);
            e.consume();
        });

		menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TextInputDialog db = new TextInputDialog();
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

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
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
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

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
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

		}
	};
	protected Vertex getCurrent() {
		return this;
	}
	
	private void removeThisNode() {
		graph.removeVertex(this);
	}

}
