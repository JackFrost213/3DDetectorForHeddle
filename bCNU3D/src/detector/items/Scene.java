package detector.items;

import java.util.Vector;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import item3D.Item3D;
import shapes3D.Axis;
import shapes3D.Geometry3D;

public class Scene extends Node {
	protected float _xscale = 1.0f;
	protected float _yscale = 1.0f;
	protected float _zscale = 1.0f;

	// view rotation angles (degrees)
	private float _view_rotx;
	private float _view_roty;
	private float _view_rotz;

	// the list of 3D items to be drawn
	protected Vector<Item3D> _itemList = new Vector<Item3D>();

	private Vector3f coordinateSize;
	private Vector3f numOfSegments;
	private Vector3f prevCoordinateSize;
	private Vector3f prevNumOfSegments;
	private Vector3f cameraLocation;
	private Vector3f cameraRotation;

	private float numOfSegmentsX = 0.5f;
	private float numOfSegmentsY = 0.5f;
	private float numOfSegmentsZ = 0.5f;
	private float deltaX = 1f;
	private float deltaY = 1f;
	private float deltaZ = 1f;
	private float rotX = 0f;
	private float rotY = 0f;
	private float rotZ = 0f;
	private float zoom = 0f;

	private boolean sceneInFocus;
	private String cedName;

	public Scene(String name) {
		_view_rotx = 0 % 180;
		_view_roty = 0 % 180;
		_view_rotz = 0;
		sceneInFocus = false;
		cedName = name;
		init();
	}

	/*
	 * The panel that holds the 3D objects
	 * 
	 * @param angleX the initial x rotation angle in degrees
	 * 
	 * @param angleY the initial y rotation angle in degrees
	 * 
	 * @param angleZ the initial z rotation angle in degrees
	 * 
	 * @param xdist move viewpoint left/right
	 * 
	 * @param ydist move viewpoint up/down
	 * 
	 * @param zdist the initial viewer z distance should be negative
	 */
	public Scene(String name, float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		rotX = xDist;
		rotY = yDist;
		rotZ = zDist;
		_view_rotx = angleX % 180;
		_view_roty = angleY % 180;
		_view_rotz = angleZ;
		cedName = name;
		init();
	}

	public void init() {
		coordinateSize = new Vector3f(getScaleX(), deltaY, getScaleZ());
		Node scalableNode = new Node("Scalable");
		this.attachChild(scalableNode);
		Node axes = new Node("Axes");
		this.attachChild(axes);
		coordinateSize = new Vector3f(getScaleX(), deltaY, getScaleZ());
		prevCoordinateSize = new Vector3f(1, 1, 1);
		prevNumOfSegments = new Vector3f(0.5f, 0.5f, 0.5f);
		cameraLocation = new Vector3f(rotX, rotY, rotZ);
		cameraRotation = new Vector3f(_view_rotx, _view_roty, _view_rotz);
		// createInitialItems();

		// setupCamera(lockPoint);
	}

	public void update(float tpf, Camera cam) {

		for (Spatial s : ((Node) this.getChild("Axes")).getChildren()) {
			Axis axis = (Axis) s;
			axis.update(cam);
		}

		if (!cameraLocation.equals(new Vector3f(rotX, rotY, rotZ))) {
			cameraLocation.set(rotX, rotY, rotZ);
		}
		
		if (!prevNumOfSegments.equals(new Vector3f(numOfSegmentsX, numOfSegmentsY, numOfSegmentsZ))) {
			prevNumOfSegments = new Vector3f(numOfSegmentsX, numOfSegmentsY, numOfSegmentsZ);
			for (Spatial s : ((Node) this.getChild("Axes")).getChildren()) {
				Axis axis = (Axis) s;
				axis.setSegments(prevNumOfSegments);
			}
		}

		if (!coordinateSize.equals(prevCoordinateSize)) {
			rotX = rotX * prevCoordinateSize.x;
			rotY = rotY * prevCoordinateSize.y;
			rotZ = rotZ * prevCoordinateSize.z;

			rotX = rotX / coordinateSize.x;
			rotY = rotY / coordinateSize.y;
			rotZ = rotZ / coordinateSize.z;

			updateSceneScale();
			prevCoordinateSize = coordinateSize;
		}
	}

	public void updateSceneScale() {
		for (Spatial s : ((Node) this.getChild("Axes")).getChildren()) {
			Axis axis = (Axis) s;
			axis.setDelta(coordinateSize);
		}

		scaleGeometries((Node) this.getChild("Scalable"));
	}
	
	private void scaleGeometries(Node parent) {
		for (Spatial g : parent.getChildren()) {
			if (g instanceof Node) {
				scaleGeometries((Node) g);
			} else {
				if (!g.getName().equals("Outline")) {
					if (g instanceof Geometry3D) {
						Geometry3D geom = (Geometry3D) g;
						updateGeometry(geom);
					}
					
				}
			}
		}
	}

	public void setScale(float xscale, float yscale, float zscale) {
		deltaX = xscale;
		deltaY = yscale;
		deltaZ = zscale;
		coordinateSize = new Vector3f(xscale, yscale, zscale);
	}

	public void setSegments(float xscale, float yscale, float zscale) {
		numOfSegmentsX = xscale;
		numOfSegmentsY = yscale;
		numOfSegmentsZ = zscale;
		numOfSegments = new Vector3f(xscale, yscale, zscale);
	}

	public Vector3f getCameraLocation() {
		return cameraLocation;
	}

	public float get_view_rotx() {
		return _view_rotx;
	}

	public float get_view_roty() {
		return _view_roty;
	}

	/**
	 * Set rotation angle about x
	 * 
	 * @param angle about x (degrees)
	 */
	public void setRotationX(float angle) {
		_view_rotx = angle;
		cameraRotation.setX(_view_rotx);
	}

	/**
	 * Set rotation angle about y
	 * 
	 * @param angle about y (degrees)
	 */
	public void setRotationY(float angle) {
		_view_roty = angle;
		cameraRotation.setY(_view_roty);
	}

	/**
	 * Set rotation angle about z
	 * 
	 * @param angle about z (degrees)
	 */
	public void setRotationZ(float angle) {
		_view_rotz = angle;
		cameraRotation.setZ(_view_rotz);
	}

	/**
	 * Get the rotation about x
	 * 
	 * @return the rotation about x (degrees)
	 */
	public float getRotationX() {
		return _view_rotx;
	}

	/**
	 * Get the rotation about y
	 * 
	 * @return the rotation about y (degrees)
	 */
	public float getRotationY() {
		return _view_roty;
	}

	/**
	 * Get the rotation about z
	 * 
	 * @return the rotation about z (degrees)
	 */
	public float getRotationZ() {
		return _view_rotz;
	}

	// Getters and Setters
	public void setRotX(float x) {
		rotX = x / coordinateSize.x;
		updateCamera();
	}

	public void setRotY(float y) {
		rotY = y / coordinateSize.y;
		updateCamera();
	}

	public void setRotZ(float z) {
		rotZ = z / coordinateSize.z;
		updateCamera();
	}
	
	public void updateCamera() {
		cameraLocation.set(rotX, rotY, rotZ);
	}
	
	/*
	 * public void setDistance(float xDist, float yDist, float zDist) {
	 * System.out.println(this.cedName + " changed Camera Location");
	 * setRotX(xDist); setRotY(yDist); setRotZ(zDist); }
	 */

	public Vector3f getCameraRotation() {
		return cameraRotation;
	}

	public void setSceneInFocus(boolean focus) {
		sceneInFocus = focus;
	}

	public boolean isSceneInFocus() {
		return sceneInFocus;
	}

	public void setCameraRotation(Vector3f rotation) {
		cameraRotation = rotation;

	}

	public String getCedName() {
		return this.cedName;
	}

	public void setZoom(float z) {
		zoom = z;
	}

	public float getZoom() {
		return zoom;
	}

	public void setCameraLocationRaw(Vector3f vector3f) {
		cameraLocation.set(vector3f);
		rotX = cameraLocation.x;
		rotY = cameraLocation.y;
		rotZ = cameraLocation.z;

	}

	public void updateGeometry(Geometry3D geometry3d) {
		if(geometry3d.getName() == "Cube2") {
			System.out.println(geometry3d.getLocalTranslation());
		}
		geometry3d.setGlobalScale(geometry3d.getBaseScale().divide(coordinateSize));
		geometry3d.setNormalizedTranslation(geometry3d.getLocalTranslation().divide(coordinateSize));
	}

	public float getScaleX() {
		return deltaX;
	}

	public float getScaleZ() {
		return deltaZ;
	}

	public float getScaleY() {
		return deltaY;
	}
}
