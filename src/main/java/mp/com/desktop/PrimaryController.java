package mp.com.desktop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import mp.com.desktop.mpgraph.GraphMP;
import mp.com.desktop.mpgraph.VertexSquare;

public class PrimaryController {
	@FXML
	Button btnLock;
	@FXML
	Button btnImport;
	@FXML
	Button btnExport;
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

	@FXML
	private void handleDragOver(DragEvent de) {
		if (de.getDragboard().hasFiles()) {
			de.acceptTransferModes(TransferMode.ANY);
		}
		de.consume();
	}

	@FXML
	private void handleDropTxtFiles(DragEvent de) throws IOException {
		if (de.getDragboard().hasFiles()) {
			if (de.getDragboard().getFiles().get(0).getName().toLowerCase().endsWith(".txt")) {
				File f = de.getDragboard().getFiles().get(0);
				BufferedReader br = new BufferedReader(new FileReader(f));
				String res;
				while ((res = br.readLine()) != null) {
					txtArea.appendText(res + "\n");
				}
				br.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("File exception");
				alert.setContentText("Please select text files only");
				alert.showAndWait();
			}

		}
		de.consume();
	}

	@FXML
	private void openTxtFile() throws IOException {
		FileChooser fch = new FileChooser();
		fch.setTitle("Open File");
		File chosen = fch.showOpenDialog(null);
		if (chosen != null) {
			if (chosen.getName().endsWith(".txt")) {
				BufferedReader br = new BufferedReader(new FileReader(chosen));
				String res;
				while ((res = br.readLine()) != null) {
					txtArea.appendText(res + "\n");
				}
				br.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("File exception");
				alert.setContentText("Please select text files only");
				alert.showAndWait();
			}
		}
	}

	@FXML
	private void saveTxtFile() throws IOException {
		FileChooser fch = new FileChooser();
		fch.getExtensionFilters().addAll(new ExtensionFilter("Text files", "*.txt"));
		fch.setInitialFileName("file.txt");
		File f = fch.showSaveDialog(null);
		if(f != null) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(txtArea.getText());
			bw.close();
		}
		
	}
}
