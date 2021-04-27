package mp.com.desktop.mpgraph;

import java.util.UUID;

import javafx.scene.layout.Pane;

public class Vertex extends Pane {
	private String vertexId;
	private String content;
	private UUID uuid = UUID.randomUUID();

	public Vertex() {
		this.vertexId = uuid.toString();
	}

	public String getVertexId() {
		return this.vertexId;
	}

	public void setVertexId(String id) {
		this.vertexId = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
