package shapes3D;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import detector.items.Scene;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class Geometry3D extends Geometry {

	private Mesh defaultMesh;
	private Quaternion currentRotation;
	private Quaternion prevRotation;
	private Vector3f localScale;
	private Geometry3D outline;
	private Vector3f baseScale;
	private Vector3f basePosition;
	private boolean sceneNotified;

	public Geometry3D(String name, Mesh model) {
		super(name, model);
		defaultMesh = model.deepClone();
		currentRotation = new Quaternion();
		prevRotation = new Quaternion();
		localScale = new Vector3f(1, 1, 1);
		baseScale = this.getLocalScale();
		basePosition = new Vector3f(0,0,0);
		outline = null;
		sceneNotified = false;
	}

	@Override
	public void setLocalScale(float val) {
		setLocalScale(new Vector3f(val,val,val));
	}
	
	@Override
	public void setLocalScale(Vector3f vec) {
		localScale = vec;
		if (outline != null) {
			outline.setLocalScale(vec);
		}
		adjustMesh();
		notifyScene();
		setGlobalScale(this.getGlobalScale());
	}

	@Override
	public void setLocalTranslation(Vector3f vec) {
		super.setLocalTranslation(vec);
		basePosition = vec;
		if (outline != null) {
			outline.setLocalTranslation(vec);
		}
	}
	
	@Override
	public Vector3f getLocalTranslation() {
		return basePosition;
	}

	@Override
	public Vector3f getLocalScale() {
		return localScale;
	}

	@Override
	public Quaternion getLocalRotation() {
		return currentRotation;

	}

	@Override
	public void setLocalRotation(Quaternion rot) {
		currentRotation = rot;
		if (outline != null) {
			outline.setLocalRotation(rot);
		}
		adjustMesh();

	}

	private void adjustMesh() {
		// get vertices of mesh
		FloatBuffer pos = (FloatBuffer) (defaultMesh.getBuffer(Type.Position).getData());
		pos.rewind();
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		while (pos.hasRemaining()) {
			float x = pos.get();
			float y = pos.get();
			float z = pos.get();
			vertices.add(new Vector3f(x, y, z));
		}
		Matrix3f matrix = currentRotation.toRotationMatrix();
		for (int i = 0; i < vertices.size(); i++) {
			vertices.set(i, vertices.get(i).mult(localScale));
			Vector3f temp = matrix.mult(vertices.get(i));
			vertices.set(i, temp);
		}
		prevRotation = currentRotation;
		this.getMesh().setBuffer(Type.Position, 3,
				BufferUtils.createFloatBuffer((vertices.toArray(new Vector3f[vertices.size()]))));
		this.updateModelBound();
	}

	public Vector3f getGlobalScale() {
		return super.getLocalScale();
	}

	public void setGlobalScale(Vector3f vec) {
		super.setLocalScale(vec);
		if (outline != null) {
			outline.setGlobalScale(vec);
		}
	}

	public void setOutline(Geometry3D outline) {
		if (this.outline != null && this.outline.getParent() != null) {
			this.outline.removeFromParent();
		}
		this.outline = outline;
		try {
			this.getParent().attachChild(this.outline);
		} catch (NullPointerException e) {
			System.out.println("Error! You cannot add an outline until you attach the object to a Node");
		}
	}

	public Vector3f getBaseScale() {
		return this.baseScale;
	}
	
	@Override
	public void updateGeometricState() {
		super.updateGeometricState();
		if(!sceneNotified && this.getParent() != null) {
			sceneNotified = true;
			notifyScene();
		}
	}
	
	public void notifyScene() {
		Node n = this.getParent();
		if (n != null) {
			while (!(n instanceof Scene) && n != null) {
				n = n.getParent();
			}
			if(n != null) {
				//Found Scene
				Scene scene = (Scene)n;
				scene.updateSceneScale();
			}
		}
	}
	
	public Geometry3D getOutline() {
		return outline;
	}

	public void setNormalizedTranslation(Vector3f vec) {
		super.setLocalTranslation(vec);
		if (outline != null) {
			outline.setLocalTranslation(vec);
		}
	}
}
