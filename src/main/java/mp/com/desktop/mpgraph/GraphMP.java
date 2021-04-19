package mp.com.desktop.mpgraph;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class GraphMP {
	private ArrayList<Vertex> vertices = new ArrayList();

	private Group canvas;

	private ZoomScrollPane scrollPane;


	private Pane cellLayer;

	public GraphMP() {


        canvas = new Group();
        cellLayer = new Pane();

        canvas.getChildren().add(cellLayer);


        scrollPane = new ZoomScrollPane(canvas);
       
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
	
	public void addVertex(Vertex v) {
		this.cellLayer.getChildren().add(v);
		this.vertices.add(v);
	}
	public void removeVertex(Vertex v) {
		this.cellLayer.getChildren().remove(v);
		this.vertices.remove(v);
	}
}
