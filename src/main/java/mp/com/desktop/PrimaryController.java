package mp.com.desktop;

import java.io.IOException;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import mp.com.desktop.mpgraph.GraphMP;
import mp.com.desktop.mpgraph.Vertex;
import mp.com.desktop.mpgraph.VertexSquare;

public class PrimaryController {
	Vertex vertex1;
	@FXML
	Button btnLock;
	@FXML
	Button btnAdd;
	@FXML
	TextArea txtArea;
	@FXML
	BorderPane borderPane;
	GraphMP graph = new GraphMP();

	@FXML
	private void addVertex() {
		VertexSquare v = new VertexSquare(graph, txtArea.getSelectedText());
		graph.addVertex(v);
		borderPane.setCenter(graph.getScrollPane());
	}

	@FXML
	private void lockUnlockTextArea() throws IOException {
		if (txtArea.isEditable()) {
			txtArea.setEditable(false);
			txtArea.setDisable(true);
		} else {
			txtArea.setEditable(true);
			txtArea.setDisable(false);
		}
	}

}
