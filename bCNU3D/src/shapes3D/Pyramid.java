package shapes3D;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Pyramid extends Mesh{

	public Pyramid() {
		Vector3f [] vertices = new Vector3f[5];
		vertices[0] = new Vector3f(-1,0,-1);
		vertices[1] = new Vector3f(-1,0,1);
		vertices[2] = new Vector3f(1,0,1);
		vertices[3] = new Vector3f(1,0,-1);
		vertices[4] = new Vector3f(0,2,0);
		
		int[] indexes = {1,4,0, 2,4,1, 3,4,2, 0,4,3, 0,3,1, 3,2,1};
		
		Vector3f [] normals = new Vector3f[6];
		normals[0] = FastMath.computeNormal(vertices[1], vertices[4], vertices[0]);
		normals[1] = FastMath.computeNormal(vertices[2], vertices[4], vertices[1]);
		normals[2] = FastMath.computeNormal(vertices[3], vertices[4], vertices[2]);
		normals[3] = FastMath.computeNormal(vertices[0], vertices[4], vertices[3]);
		normals[4] = FastMath.computeNormal(vertices[0], vertices[3], vertices[1]);
		normals[5] = FastMath.computeNormal(vertices[3], vertices[2], vertices[1]);
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		//setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}
	
}
