package shapes3D;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.jme3.math.Vector3f;

public class Line extends Mesh {

	public Line(Vector3f start, Vector3f end) {
		setMode(Mesh.Mode.Lines);
		Vector3f[] vertices = new Vector3f[2];
		vertices[0] = start;
		vertices[1] = end;

		int[] indexes = { 0, 1 };

		vertices[1].subtract(vertices[0]).length();

		Vector3f[] texCoords = vertices;

		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.TexCoord, 3, BufferUtils.createFloatBuffer(texCoords));
		setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}

	public Line(Vector3f[] positions) {
		setMode(Mesh.Mode.LineStrip);
		Vector3f[] vertices = positions;
		Vector3f[] texCoords = vertices;
		
		int[] indexes = new int[positions.length];
		for (int x = 0; x < positions.length; x++) {
			indexes[x] = x;
		}
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.TexCoord, 3, BufferUtils.createFloatBuffer(texCoords));
		setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}

}
