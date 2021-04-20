package mp.com.desktop.mpgraph;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group{
	protected Vertex source;
    protected Vertex target;

    Line line;

    public Edge(Vertex source, Vertex target) {

        this.source = source;
        this.target = target;


        line = new Line();

        line.startXProperty().bind(source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(target.layoutXProperty().add(target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(target.layoutYProperty().add(target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add(line);

    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }
}
