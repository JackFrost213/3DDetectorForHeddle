package shapes3D;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Triangle extends Mesh{

	public Triangle() {
		Vector3f [] vertices = new Vector3f[3];
		vertices[0] = new Vector3f(-1,0,0);
		vertices[1] = new Vector3f(1,0,0);
		vertices[2] = new Vector3f(0,1,0);
		int[] indexes = {0,1,2,2,1,0};
		
		Vector3f [] normals = new Vector3f[2];
		normals[0] = FastMath.computeNormal(vertices[0], vertices[1], vertices[2]);
		normals[1] = FastMath.computeNormal(vertices[2], vertices[1], vertices[0]);
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}
	
	public Triangle(Vector3f lowerLeft, Vector3f lowerRight, Vector3f topMiddle) {
		Vector3f [] vertices = new Vector3f[3];
		vertices[0] = lowerLeft;
		vertices[1] = lowerRight;
		vertices[2] = topMiddle;
		int[] indexes = {0,1,2,2,1,0};
		
		Vector3f [] normals = new Vector3f[2];
		normals[0] = FastMath.computeNormal(vertices[0], vertices[1], vertices[2]);
		normals[1] = FastMath.computeNormal(vertices[2], vertices[1], vertices[0]);
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}
	
}
