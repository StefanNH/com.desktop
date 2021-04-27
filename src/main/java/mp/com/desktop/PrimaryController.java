package mp.com.desktop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import mp.com.desktop.mpgraph.Edge;
import mp.com.desktop.mpgraph.GraphMP;
import mp.com.desktop.mpgraph.GraphMP.LAYOUTS;
import mp.com.desktop.mpgraph.Vertex;
import mp.com.desktop.mpgraph.VertexSchema;
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
		if (f != null) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(txtArea.getText());
			bw.close();
		}

	}

	@FXML
	private void exportJSONhandler() throws IOException {
		FileChooser fch = new FileChooser();
		fch.getExtensionFilters().addAll(new ExtensionFilter("Export JSON", "*.json"));
		fch.setInitialFileName("default.json");
		File f = fch.showSaveDialog(null);
		if (f != null) {
			JsonArray verticesJSON = new JsonArray();
			JsonArray edgesJSON = new JsonArray();
			for (Vertex v : graph.getVertices()) {
				JsonObject vJSON = new JsonObject();
				if (v instanceof VertexSchema) {
					vJSON.put("type", "schema");
				} else {
					vJSON.put("type", "node");
				}
				vJSON.put("id", v.getVertexId());
				vJSON.put("data", v.getContent());
				verticesJSON.add(vJSON);
			}
			for (Edge e : graph.getEdges()) {
				JsonObject eJSON = new JsonObject();
				eJSON.put("source", e.getSource().getVertexId());
				eJSON.put("target", e.getTarget().getVertexId());
				edgesJSON.add(eJSON);
			}
			JsonObject data = new JsonObject();
			data.put("vertices", verticesJSON);
			data.put("edges", edgesJSON);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(data.toJson());
			bw.close();
		}
	}

	@FXML
	private void importJSONhandler() throws IOException, JsonException {
		FileChooser fch = new FileChooser();
		fch.setTitle("Import JSON File");
		File chosen = fch.showOpenDialog(null);
		if (chosen != null) {
			if (chosen.getName().endsWith(".json")) {
				graph.cleanUp();
				BufferedReader br = new BufferedReader(new FileReader(chosen));
				JsonObject parser = (JsonObject) Jsoner.deserialize(br);
				JsonArray verArray = (JsonArray) parser.get("vertices");
				JsonArray edgeArray = (JsonArray) parser.get("edges");
				for (Object vr : verArray) {
					JsonObject vertex = (JsonObject) vr;
					String verType = (String) vertex.get("type");
					if (verType.equals("node")) {
						String id = (String) vertex.get("id");
						String data = (String) vertex.get("data");
						VertexSquare vs = new VertexSquare(graph, data);
						vs.setVertexId(id);
						graph.addVertex(vs);
					} else if (verType.equals("schema")) {
						String id = (String) vertex.get("id");
						String data = (String) vertex.get("data");
						VertexSchema vsch = new VertexSchema(graph, data);
						vsch.setVertexId(id);
						graph.addVertex(vsch);
					}
				}
				for(Object er : edgeArray) {
					JsonObject edge = (JsonObject) er;
					String source = (String) edge.get("source");
					String target = (String) edge.get("target");
					graph.addEdges(graph.getVertexById(source), graph.getVertexById(target));
				}
				graph.setLayot(LAYOUTS.RANDOM);
				br.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("File exception");
				alert.setContentText("Please select text files only");
				alert.showAndWait();
			}
		}
	}
}
