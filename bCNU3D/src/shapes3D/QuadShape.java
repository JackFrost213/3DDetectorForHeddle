package shapes3D;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class QuadShape extends Mesh{
	final public Vector3f lowerLeft,lowerRight,upperRight,upperLeft;
	
	public QuadShape() {
		Vector3f [] vertices = new Vector3f[4];
		lowerLeft = vertices[0] = new Vector3f(-1,-1,0); //lowerLeft
		lowerRight = vertices[1] = new Vector3f(1,-1,0); //lowerRight
		upperRight = vertices[2] = new Vector3f(1,1,0); //upperRight
		upperLeft = vertices[3] = new Vector3f(-1,1,0); //upperLeft
		
		int[] indexes = {3,1,0, 1,2,3, 0,1,3, 3,2,1};
		
		Vector3f [] normals = new Vector3f[2];
		
		Vector2f[] texCoords = new Vector2f[4];
		texCoords[0] = new Vector2f(0,0);
		texCoords[1] = new Vector2f(1,0);
		texCoords[2] = new Vector2f(1,1);
		texCoords[3] = new Vector2f(0,1);
		
		normals[0] = FastMath.computeNormal(vertices[0], vertices[1], vertices[2]);
		normals[1] = FastMath.computeNormal(vertices[2], vertices[1], vertices[0]);
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}
	
	public QuadShape(Vector3f lowerLeft, Vector3f lowerRight, Vector3f upperLeft, Vector3f upperRight) {
		this(lowerLeft,lowerRight,upperRight,upperLeft,false);
	}
	
	public QuadShape(Vector3f lowerLeft, Vector3f lowerRight, Vector3f upperLeft, Vector3f upperRight, boolean outline) {
		this.lowerLeft = lowerLeft;
		this.lowerRight = lowerRight;
		this.upperRight = upperRight;
		this.upperLeft = upperLeft;
		if(outline) {
			this.setMode(Mesh.Mode.LineStrip);
			Vector3f [] vertices = new Vector3f[5];
			vertices[0] = lowerLeft;
			vertices[1] = lowerRight;
			vertices[2] = upperRight;
			vertices[3] = upperLeft;
			vertices[4] = lowerLeft;


			int[] indexes = {0,1,2,3,4};
			
			setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
			setBuffer(Type.Index,    1, BufferUtils.createIntBuffer(indexes));
			
		}else {
			Vector3f [] vertices = new Vector3f[4];
			vertices[0] = lowerLeft;
			vertices[1] = lowerRight;
			vertices[2] = upperRight;
			vertices[3] = upperLeft;

			int[] indexes = {3,1,0, 1,2,3, 0,1,3, 3,2,1};
			
			Vector3f [] normals = new Vector3f[2];
			
			Vector2f[] texCoords = new Vector2f[4];
			texCoords[0] = new Vector2f(0,0);
			texCoords[1] = new Vector2f(1,0);
			texCoords[2] = new Vector2f(1,1);
			texCoords[3] = new Vector2f(0,1);
			
			normals[0] = FastMath.computeNormal(vertices[0], vertices[1], vertices[2]);
			normals[1] = FastMath.computeNormal(vertices[2], vertices[1], vertices[0]);
			setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
			setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
			setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
			setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
			updateBound();
		}
	}
}
