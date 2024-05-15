package bCNU3D;

import item3D.Item3D;
import net.wcomohundro.jme3.csg.CSGGeometry;
import net.wcomohundro.jme3.csg.CSGShape;
import shapes3D.Geometry3D;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import adapter3D.KeyAdapter3D;
import adapter3D.MouseAdapter3D;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import shapes3D.Axis;

public class Panel3DOther extends SimpleApplication {

	// background default color used for r, g and b
	public static final float BGFEFAULT = 0.9804f * 255f;

	// the actual components of the background
	private float _bgRed = BGFEFAULT;
	private float _bgGreen = BGFEFAULT;
	private float _bgBlue = BGFEFAULT;

	// different modes of operation
	public static enum DrawMode {
		MANUAL, ANIMATOR
	};

	protected final DrawMode _drawMode = DrawMode.MANUAL;

	protected float _xscale = 1.0f;
	protected float _yscale = 1.0f;
	protected float _zscale = 1.0f;

	// view rotation angles (degrees)
	private float _view_rotx;
	private float _view_roty;
	private float _view_rotz;

	private ChaseCamera camera;

	// distance in front of the screen
	private float _zdist;

	// x and y translation
	private float _xdist;
	private float _ydist;

	// the list of 3D items to be drawn
	protected Vector<Item3D> _itemList = new Vector<Item3D>();

	// listen for mouse events
	protected MouseAdapter3D _mouseAdapter;

	// listen for key events
	protected KeyAdapter3D _keyAdapter;

	protected String _rendererStr;

	private static Vector3f coordinateSize;
	private Vector3f prevCoordinateSize;
	private Vector3f prevNumOfSegments;

	private static Panel3DGUI GUI;
	private Geometry lockPoint;

	public static float numOfSegmentsX = 0.5f;
	public static float numOfSegmentsY = 0.5f;
	public static float numOfSegmentsZ = 1.5f;
	public static float deltaX = 1f;
	public static float deltaY = 1f;
	public static float deltaZ = 1f;
	private static float rotX = 0;
	private static float rotY = 0;
	private static float rotZ = 0;

	public static AssetManager assetManagerExternal;

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
	public Panel3DOther(float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		_xdist = xDist;
		_ydist = yDist;
		_zdist = zDist;
		_view_rotx = angleX % 180;
		_view_roty = angleY % 180;
		_view_rotz = angleZ;
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
	public Panel3DOther(float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist, float bgRed,
			float bgGreen, float bgBlue) {

	}

	// the openGL version and renderer strings
	protected String _versionStr;

	/**
	 * Create the initial items
	 */
	public void createInitialItems() {
		float xymax = 600f;
		float zmax = 600f;
		float zmin = -100f;
		// Axes3D axes = new Axes3D(this, -xymax, xymax, -xymax, xymax, zmin, zmax,
		// null, Color.darkGray, 1f, 7, 7,
		// 8, Color.black, Color.blue, new Font("SansSerif", Font.PLAIN, 11), 0);
		// addItem(axes);

		// add some triangles

		// addItem(new Triangle3D(this,
		// 0f, 0f, 0f, 100f, 0f, -100f, 50f, 100, 100f, new Color(255,
		// 0, 0, 64), 2f, true));

		// addItem(new Triangle3D(this, 500f, 0f, -200f, -500f, 500f, 0f, 0f, -100f,
		// 500f, new Color(255, 0, 0, 64), 1f,
		// true));

		Node scalableNode = ((Node) rootNode.getChild("Scalable"));
		Node overlappingTransparent = new Node();
		overlappingTransparent.setName("overlappingTransparent");
		Geometry3D triangle = ShapeGenerator.createTriangle(new Vector3f(5, 0, 5), new Vector3f(3, 1, 4),
				new Quaternion(), ColorRGBA.Red);
		// addItem(triangle);
		scalableNode.attachChild(triangle);

		// addItem(new Triangle3D(this, 0f, 500f, 0f, -300f, -500f, 500f, 0f, -100f,
		// 500f, new Color(0, 0, 255, 64), 2f,
		// true));
		Quaternion rotation = new Quaternion();
		rotation.fromAngleAxis((float) (_view_rotx / (2 * Math.PI)), new Vector3f(1, 0, 0));
		Geometry3D triangle2 = ShapeGenerator.createTriangle(new Vector3f(0, 0, 0), new Vector3f(3, 5, 3), rotation,
				new ColorRGBA(0, 255 / 255, 0, 1f));
		triangle2.setName("TriangleS");
		scalableNode.attachChild(triangle2);
		// addItem(triangle2);

		ShapeGenerator.createTriangle(new Vector3f(0, 1.7f, 2.01f), new Vector3f(1, 2f, 1),
				rotation, new ColorRGBA(0, 255, 0, 0.78f));
		// overlappingTransparent.attachChild(triangle4);
		rotation.fromAngleAxis((float) (90 * (Math.PI / 180)), new Vector3f(1, 0, 0));
		rotation.fromAngleAxis((float) (90 * (Math.PI / 180)), new Vector3f(0, 0, 1));
		ShapeGenerator.createTriangle(new Vector3f(-2.11f, 0.3f, 0f), new Vector3f(1, 2f, 1),
				rotation, new ColorRGBA(0, 255 / 255, 0, 0.78f));

		Quaternion rotation3 = new Quaternion();
		rotation3.fromAngleAxis((float) (_view_rotx / (2 * Math.PI)), new Vector3f(1, 0, 0));
		Geometry3D cylinder = ShapeGenerator.createCylinder(new Vector3f(4, 2, 5), new Vector3f(0.5f, 0.5f, 5),
				rotation3, new ColorRGBA(255 / 255, 255 / 255, 0, 0.5f));
		// addItem(cylinder);
		scalableNode.attachChild(cylinder);
		Geometry3D cylinderOutline = ShapeGenerator.createOutline(cylinder, ColorRGBA.Black, 2);
		cylinder.setOutline(cylinderOutline);

		// addItem(new Triangle3D(this, 0f, 0f, 500f, 0f, -400f, -500f, 500f, -100f,
		// 500f, new Color(0, 255, 0, 64), 2f,
		// true));
		Quaternion rotation4 = new Quaternion();
		rotation4.fromAngleAxis((float) (-45 * (Math.PI / 180)), new Vector3f(1, 0, 0));
		Geometry triangle3 = ShapeGenerator.createTriangle(new Vector3f(0, 0, 10), new Vector3f(5, 5, 5), rotation4,
				new ColorRGBA(0, 128 / 255, 0, 0.7f));
		// addItem(triangle3);
		scalableNode.attachChild(triangle3);

		// addItem(new Cylinder(this, 0f, 0f, 0f, 300f, 300f, 300f, 50f, new Color(0,
		// 255, 255, 128)));

		Geometry3D cube = ShapeGenerator.createCube(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2), new Quaternion(),
				new ColorRGBA(0, 0, 255 / 255, .32f));
		//cube.setMaterial(assetManager.loadMaterial("Interface/Logo/Logo.j3m"));
		cube.setName("Cube1");
		// addItem(cube);

		Geometry3D cubeOutline = ShapeGenerator.createOutline(cube, ColorRGBA.Red, 20);
		overlappingTransparent.attachChild(cube);
		cube.setOutline(cubeOutline);

		Geometry cube2 = ShapeGenerator.createCube(new Vector3f(3.5f, 0, 0), new Vector3f(1, 1, 1), new Quaternion(),
				new ColorRGBA(0, 0, 0, 0.9f));
		cube2.setName("Cube2");
		overlappingTransparent.attachChild(cube2);
		// addItem(cube2);

		scalableNode.attachChild(overlappingTransparent);

		Quaternion rotation2 = new Quaternion();
		rotation2.fromAngleAxis((float) (-45 * (Math.PI / 180)), new Vector3f(1, 0, 0));
		Geometry3D pyramid = ShapeGenerator.createPyramid(new Vector3f(1.5f, 0, 0), new Vector3f(2, 2, 2), rotation2,
				new ColorRGBA(127 / 255, 0, 255 / 255, 1f));
		pyramid.setName("Pyr");
		pyramid.setGlobalScale(new Vector3f(1, 1, 1));
		pyramid.setLocalScale(new Vector3f(1, 2, 1));
		// addItem(pyramid);
		scalableNode.attachChild(pyramid);
		// temp.attachChild(pyramid);
		Geometry3D pyrOutline = ShapeGenerator.createOutline(pyramid, new ColorRGBA(70 / 255, 0, 170 / 255, 1f), 2);
		// addItem(pyrOutline);
		pyramid.setOutline(pyrOutline);
		// Get Collision Test
		CSGShape g = new CSGShape("cubeMesh", cube.getMesh());
		g.setLocalTranslation(cube.getLocalTranslation());
		// g.setLocalScale(cube.getLocalScale());
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", ColorRGBA.Black);
		g.setMaterial(mat);
		// addItem(g);

		// System.out.println(triangle3.getMesh().getTriangleCount());
		CSGShape g2 = new CSGShape("w2", pyramid.getMesh());
		g2.setLocalTranslation(pyramid.getLocalTranslation());
		g2.setMaterial(mat);
		// rootNode.attachChild(g2);

		CSGGeometry CSG_obj = new CSGGeometry("Geom");
		Material mats = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mats.setColor("Color", ColorRGBA.Brown);
		CSG_obj.setMaterial(mats);
		CSG_obj.setLocalTranslation(new Vector3f(6.5f, 0, 0));
		// addItem(CSG_obj);
		scalableNode.attachChild(CSG_obj);
		// Geometry csgOutline = ShapeGenerator.createOutline(CSG_obj, ColorRGBA.Black,
		// 2);
		// addItem(csgOutline);
		CSG_obj.addShape(g);
		CSG_obj.addShape(g2, CSGGeometry.CSGOperator.INTERSECTION);
		CSG_obj.regenerate();

		Geometry CSG_obj_outline = CSG_obj.clone(true);
		CSG_obj_outline.setMaterial(mat);
		// addItem(CSG_obj_outline);
		scalableNode.attachChild(CSG_obj_outline);

		CSGGeometry CSG_obj2 = new CSGGeometry("Pyr2");
		CSG_obj2.setMaterial(pyramid.getMaterial());
		// CSG_obj2.setLocalTranslation(pyramid.getLocalTranslation());
		// addItem(CSG_obj2);
		// Geometry csgOutline = ShapeGenerator.createOutline(CSG_obj, ColorRGBA.Black,
		// 2);
		// addItem(csgOutline);
		CSG_obj2.addShape(g2);
		CSG_obj2.regenerate();
		// pyramid.setMesh(CSG_obj2.getMesh());
		// Geometry3D temp3 = pyraWmid;
		// temp3.setMeshScale(new Vector3f(2,2,2));
		// addItem((Geometry)temp3);

		Support3DOther.wireSphere(scalableNode, 1, 2, 10, 3, 50, 50, new Color(255, 0, 0, 255));

		Geometry3D testTriangle = (Geometry3D) Support3DOther.drawTriangle(scalableNode, new Vector3f(2, 0, -7),
				new Vector3f(3, 0, -7), new Vector3f(3, 2, -7), new Color(255, 0, 0, 255), 1, false);
		// coordinateObjects.add(testTriangle);
		testTriangle.setOutline(ShapeGenerator.createOutline(testTriangle, ColorRGBA.Black, 3));

		Geometry[] triangulatedTriangles = Support3DOther.drawTriangulatedTriangles(scalableNode,
				new float[] { 2, 0, -5, 3, 0, -5, 3, 2, -5 }, new Color(255, 0, 0, 255), 2, false, 1);
		for (int x = 0; x < triangulatedTriangles.length; x++) {
			Geometry temp = ShapeGenerator.createOutline(triangulatedTriangles[x], ColorRGBA.Black, 3);
			temp.setName("OutlineB");
			// addItem(temp);
			scalableNode.attachChild(temp);
			// coordinateObjects.add(triangulatedTriangles[x]);
		}

		Support3DOther.drawPolyLine(scalableNode,
				new float[] { 3, 3, 3, 4, 4, 4, 1, 2, 3, 6, 7, 3, 2, 7, 5 }, new Color(0, 0, 255, 255),
				new Color(255, 0, 0, 255), 8);

		Support3DOther.drawLine(scalableNode, new float[] { 3, 8, 3 }, new float[] { 8, 8, 8 },
				new Color(0, 0, 0, 1), null, 2);

		Geometry3D quad = Support3DOther.drawQuad(scalableNode, new float[] { -1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0 },
				0, 1, 2, 3, new Color(0, 255, 0, 255), 2, true);
		quad.setLocalTranslation(new Vector3f(-15, 4, 0));
		quad.setLocalScale(new Vector3f(5, 5, 5));
		// coordinateObjects.add(quad);
		// Cube cube = new Cube(this, 0.25f, 0.25f, 0.25f, 0.5f,
		// Color.yellow);
		// addItem(cube);

		// System.err.println("test with " + num + " lines.");
		// Line3D.lineItemTest(this, num);

		// Cube.cubeTest(this, 40000);

		// point set test
		int numBigPnt = 1000;
		int numSmallPnt = 100000;

		float pntSize = 10 * 100;
		float coords[] = new float[3 * numBigPnt];
		new ColorRGBA(255, 191, 0, 0.5f);
		float[] points = ShapeGenerator.generatePoints(numSmallPnt);
		float[] colors = ShapeGenerator.generateColors(numSmallPnt);
		float[] sizes = new float[numSmallPnt];

		for (int i = 0; i < numBigPnt; i++) {
			int j = i * 3;
			float x = (float) (-xymax + 2 * xymax * Math.random());
			float y = (float) (-xymax + 2 * xymax * Math.random());
			float z = (float) (zmin + (zmax - zmin) * Math.random());
			coords[j] = x;
			coords[j + 1] = y;
			coords[j + 2] = z;
			/*
			 * Geometry3D temp2 = Support3DOther.drawPoint(scalableNode, x, y, z, new
			 * Color(255, 191, 0), pntSize / 100, true);
			 */

			Support3DOther.solidSphere(scalableNode, x, y, z, pntSize / 100, 16, 16, new Color(255, 191, 0, 155));
		}
		Texture texture = assetManager.loadTexture("Textures/CNU LOGO.jpg");
		Support3DOther.drawSprite(scalableNode, texture, 0, 10, 0.5f, 3);

		for (int x = 0; x < sizes.length; x++) {
			sizes[x] = 20;
			// colors[x] = ColorRGBA.White;
		}

		Node cloud = ShapeGenerator.generatePointCloud(BufferUtils.createFloatBuffer(points),
				BufferUtils.createFloatBuffer(colors), BufferUtils.createFloatBuffer(sizes));
		scalableNode.attachChild(cloud);

		ShapeGenerator.createCube(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Quaternion(),
				ColorRGBA.Red);

		// Add Coordinate System

		Node axes = new Node("Axes");
		rootNode.attachChild(axes);
		int length = 20;

		// Line from 0,0,0 to 0,1,0
		Axis yAxisTemp = new Axis(new Vector3f(0, 1, 0), 0.5f, 1, length);
		axes.attachChild(yAxisTemp);

		// Line from 0,0,0 to 0,0,1
		Axis zAxisTemp = new Axis(new Vector3f(0, 0, 1), 0.5f, 1, length);
		axes.attachChild(zAxisTemp);

		// Line from 0,0,0 to 1,0,0
		Axis xAxisTemp = new Axis(new Vector3f(1, 0, 0), 0.5f, 1, length);
		axes.attachChild(xAxisTemp);
	}

	public void setScale(float xscale, float yscale, float zscale) {
		_xscale = xscale;
		_yscale = yscale;
		_zscale = zscale;
	}

	@Override
	public void simpleInitApp() {

		viewPort.setBackgroundColor(new ColorRGBA(_bgRed, _bgGreen, _bgBlue, 1f));
		// fpp = new FilterPostProcessor(assetManager);
		// fpp.addFilter(new CartoonEdgeFilter());
		// viewPort.addProcessor(fpp);
		assetManagerExternal = assetManager;

		coordinateSize = new Vector3f(deltaX, deltaY, deltaZ);
		prevCoordinateSize = new Vector3f(1, 1, 1);
		prevNumOfSegments = new Vector3f(0.5f, 0.5f, 0.5f);
		Node scalableNode = new Node("Scalable");
		rootNode.attachChild(scalableNode);

		createInitialItems();

		// NewTransparentComparator comparator = new NewTransparentComparator();
		// viewPort.getQueue().setGeometryComparator(Bucket.Transparent, comparator);
		lockPoint = ShapeGenerator.createCube(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Quaternion(),
				new ColorRGBA(0, 0, 0, 0f));
		((Node) rootNode.getChild("Scalable")).attachChild(lockPoint);

		setupCamera(lockPoint);
		// setupHUD();
	}

	private void setupCamera(Geometry lockPoint) {
		flyCam.setEnabled(false);
		cam.setLocation(new Vector3f(_xdist, _ydist, _zdist));
		// cam.setFrustumPerspective(45.0f, ((float) cam.getWidth()) / ((float)
		// cam.getHeight()), 0.05f, 100.0f);
		camera = new ChaseCamera(cam, lockPoint, inputManager);
		camera.setRotationSpeed(5);
		camera.setInvertVerticalAxis(true);
		camera.setDefaultHorizontalRotation((float) (_view_rotx * (Math.PI / 180)));
		camera.setDefaultVerticalRotation((float) (_view_roty * (Math.PI / 180)));
		camera.setMaxVerticalRotation((float) (90f * (Math.PI / 180)));
		camera.setMinVerticalRotation((float) (-89f * (Math.PI / 180)));
	}

	private void setupHUD() {
		setDisplayStatView(true);
		// setDisplayFps(false);
		GUI.guiScaleX.setText(String.valueOf(deltaX));
		GUI.guiScaleY.setText(String.valueOf(deltaY));
		GUI.guiScaleZ.setText(String.valueOf(deltaZ));
		GUI.guiRotX.setText(String.valueOf(rotX));
		GUI.guiRotY.setText(String.valueOf(rotY));
		GUI.guiRotZ.setText(String.valueOf(rotZ));
		GUI.guiTickX.setText(String.valueOf(numOfSegmentsX));
		GUI.guiTickY.setText(String.valueOf(numOfSegmentsY));
		GUI.guiTickZ.setText(String.valueOf(numOfSegmentsZ));
	}

	@Override
	public void simpleUpdate(float tpf) {

		Geometry3D temp2 = (Geometry3D) rootNode.getChild("Pyr");
		Quaternion q = new Quaternion();
		q.fromAngleAxis((float) (tpf * 20 * (Math.PI / 180)), new Vector3f(1, 0, 0));
		temp2.setLocalRotation(temp2.getLocalRotation().mult(q));

		// Always call this. This checks and adjusts the scale of all the geometries
		// when any of the
		// scene parameters are changed
		adjustScale();
	}

	public void adjustScale() {

		for (Spatial s : ((Node) rootNode.getChild("Axes")).getChildren()) {
			Axis axis = (Axis) s;
			axis.update(cam);
		}
		coordinateSize = new Vector3f(deltaX, deltaY, deltaZ);
		if (!lockPoint.getLocalTranslation().equals(new Vector3f(rotX, rotY, rotZ))) {
			lockPoint.setLocalTranslation(rotX, rotY, rotZ);
		}
		if (!prevNumOfSegments.equals(new Vector3f(numOfSegmentsX, numOfSegmentsY, numOfSegmentsZ))) {
			prevNumOfSegments = new Vector3f(numOfSegmentsX, numOfSegmentsY, numOfSegmentsZ);
			for (Spatial s : ((Node) rootNode.getChild("Axes")).getChildren()) {
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

			for (Spatial s : ((Node) rootNode.getChild("Axes")).getChildren()) {
				Axis axis = (Axis) s;
				axis.setDelta(coordinateSize);
			}

			scaleGeometries((Node) rootNode.getChild("Scalable"));
			prevCoordinateSize = coordinateSize;
		}
	}

	private void scaleGeometries(Node parent) {
		for (Spatial g : parent.getChildren()) {
			if (g instanceof Node) {
				scaleGeometries((Node) g);
			} else {
				if (!g.getName().equals("Outline")) {
					if (g instanceof Geometry3D) {
						Geometry3D geom = (Geometry3D) g;
						geom.setGlobalScale(geom.getGlobalScale().mult(prevCoordinateSize));
						geom.setGlobalScale(geom.getGlobalScale().divide(coordinateSize));
					} else {
						g.setLocalScale(g.getLocalScale().mult(prevCoordinateSize));
						g.setLocalScale(g.getLocalScale().divide(coordinateSize));
					}
					g.setLocalTranslation(g.getLocalTranslation().mult(prevCoordinateSize));
					g.setLocalTranslation(g.getLocalTranslation().divide(coordinateSize));
				}
			}
		}
	}

	/**
	 * Set rotation angle about x
	 * 
	 * @param angle about x (degrees)
	 */
	public void setRotationX(float angle) {
		_view_rotx = angle;
	}

	/**
	 * Set rotation angle about y
	 * 
	 * @param angle about y (degrees)
	 */
	public void setRotationY(float angle) {
		_view_roty = angle;
	}

	/**
	 * Set rotation angle about z
	 * 
	 * @param angle about z (degrees)
	 */
	public void setRotationZ(float angle) {
		_view_rotz = angle;
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

	/**
	 * Change the x distance to move in or out
	 * 
	 * @param dx the change in x
	 */
	public void deltaX(float dx) {
		_xdist += dx;
		// refresh();
	}

	/**
	 * Change the y distance to move in or out
	 * 
	 * @param dy the change in y
	 */
	public void deltaY(float dy) {
		_ydist += dy;
		// refresh();
	}

	/**
	 * Change the z distance to move in or out
	 * 
	 * @param dz the change in z
	 */
	public void deltaZ(float dz) {
		_zdist += dz;
		// refresh();
	}

	/**
	 * Add an item to the list. Note that this does not initiate a redraw.
	 * 
	 * @param item the item to add.
	 */
	public void addItem(Geometry item) {
		rootNode.attachChild(item);
	}

	/**
	 * Remove an item from the list. Note that this does not initiate a redraw.
	 * 
	 * @param item the item to remove.
	 */
	public void removeItem(Geometry item) {
		item.removeFromParent();
	}

	/**
	 * This gets the z step used by the mouse and key adapters, to see how fast we
	 * move in or in in response to mouse wheel or up/down arrows. It should be
	 * overridden to give something sensible. like the scale/100;
	 * 
	 * @return the z step (changes to zDist) for moving in and out
	 */
	public float getZStep() {
		return 0.1f;
	}

	/**
	 * Main program for testing. Put the panel on JFrame,
	 * 
	 * @param arg
	 */
	public static void main(String arg[]) {
		// final JFrame testFrame = new JFrame("bCNU 3D Panel Test using
		// JMonkeyLibrary");
		Panel3DOther app = createPanel3D();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1440, 920);
		settings.setFullscreen(false);
		settings.setVSync(true);
		settings.setSamples(8);
		settings.setRenderer(AppSettings.LWJGL_OPENGL45);
		settings.setGammaCorrection(true);
		settings.setTitle("bCNU 3D Panel Test using JMonkeyLibrary");
		app.setSettings(settings);
		app.setPauseOnLostFocus(false);
		app.createCanvas();

		int canvasWidth = (int) (settings.getWidth() - settings.getWidth() / 5.25);
		int canvasHeight = settings.getHeight() - settings.getHeight() / 5;
		JmeCanvasContext context = (JmeCanvasContext) app.getContext();
		context.setSystemListener(app);
		Canvas canvas = context.getCanvas();
		Dimension dim = new Dimension(settings.getWidth(), (int) (settings.getHeight()));
		canvas.setSize(dim);
		canvas.setBackground(Color.BLACK);

		JPanel test = new JPanel();
		JFrame frame = new JFrame();
		frame.setTitle("bCNU 3D Panel Test using JMonkeyLibrary");
		frame.setLayout(null);
		test.setLayout(null);
		Dimension dim2 = new Dimension(canvasWidth, canvasHeight + 35);
		frame.setPreferredSize(dim2);
		test.setPreferredSize(dim2);
		frame.setSize(dim2);
		test.setSize(dim2);

		// JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				app.stop();
			}
		});
		// frame.getContentPane().add(panel);
		GUI = new Panel3DGUI();
		GUI.setLocation(frame.getWidth() - GUI.getWidth() - 10, 0);
		app.setupHUD();
		test.add(GUI);
		test.add(canvas);

		frame.add(test);
		// frame.pack();
		frame.getContentPane().setBackground(Color.BLUE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				app.startCanvas(true);
			}
		});
	}

	private static Panel3DOther createPanel3D() {

		final float xdist = -100f;
		final float ydist = 0f;
		final float zdist = -1600f;

		final float thetax = 45f;
		final float thetay = 45f;
		final float thetaz = 45f;
		return new Panel3DOther(thetax, thetay, thetaz, xdist, ydist, zdist);
	}

	// Getters and Setters
	public static void setRotX(float x) {
		rotX = x / coordinateSize.x;
	}

	public static void setRotY(float y) {
		rotY = y / coordinateSize.y;
	}

	public static void setRotZ(float z) {
		rotZ = z / coordinateSize.z;
	}

	/**
	 * Print the panel. No default implementation.
	 */
	public void print() {
	}

	/**
	 * Snapshot of the panel. No default implementation.
	 */
	public void snapshot() {
	}

}
