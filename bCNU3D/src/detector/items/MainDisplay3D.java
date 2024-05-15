package detector.items;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import bCNU3D.ShapeGenerator;
import bCNU3D.Support3DOther;
import detector.items.views.CedView3DImp2;

public class MainDisplay3D extends SimpleApplication {

	// background default color used for r, g and b
	public static final float BGFEFAULT = 0.9804f * 255f;

	// the actual components of the background
	private float _bgRed = BGFEFAULT / 255f;
	private float _bgGreen = BGFEFAULT / 255f;
	private float _bgBlue = BGFEFAULT / 255f;

	// private static ForwardDetectorGUI GUI;

	public static AssetManager assetManagerExternal;

	public JPanel outputPanel;

	private ArrayList<CedView3DImp2> cedViews;

	private ChaseCamera camera;

	private Scene currentScene;

	private Geometry lockPoint;

	private int width = 1920, height = 1080;

	private final Object threadLock = new Object();
	public boolean updatingSceneDataAlready = false;
	ScreenshotAppState screenShotState;
	private boolean isStarted = false;
	private boolean requestSettingCanvasVisible = false;
	public int sceneAboutToChange = 0;
	private Scene pendingScene = null;

	private boolean allCedViewsAdded = false;

	private boolean updateLockPoint = false;
	private boolean panMode = false;

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
	public MainDisplay3D() {
		cedViews = new ArrayList<CedView3DImp2>();
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
	public MainDisplay3D(CedView3DImp2 cedView) {
		cedViews = new ArrayList<CedView3DImp2>();
		cedViews.add(cedView);
		init();
	}

	public MainDisplay3D(float bgRed, float bgGreen, float bgBlue) {
		// TODO FINISH THIS CONSTRUCTOR
	}

	private void init() {
		this.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.setResolution(width, height);
		settings.setFullscreen(false);
		settings.setVSync(true);
		settings.setSamples(8);
		settings.setGammaCorrection(true);
		this.setSettings(settings);
		this.setPauseOnLostFocus(false);
		this.createCanvas();
		
		int canvasWidth = settings.getWidth() - settings.getWidth() / 4;
		int canvasHeight = settings.getHeight() - settings.getHeight() / 4;
		JmeCanvasContext context = (JmeCanvasContext) this.getContext();
		context.setSystemListener(this);
		Canvas canvas = context.getCanvas();
		Dimension dim = new Dimension(canvasWidth, canvasHeight);
		
		canvas.setSize(dim);
		canvas.setBackground(new Color((int) BGFEFAULT, (int) BGFEFAULT, (int) BGFEFAULT, 255));

		outputPanel = new JPanel();
		outputPanel.setLayout(null);
		Dimension dim2 = new Dimension(settings.getWidth(), settings.getHeight() + 35);
		outputPanel.setPreferredSize(dim2);
		outputPanel.setSize(dim2);
		outputPanel.add(canvas);
		this.startCanvas(true);
		System.out.println("FINISHED STARTING DISPLAY");
	}

	// the openGL version and renderer strings
	protected String _versionStr;

	@Override
	public void simpleInitApp() {
		viewPort.setBackgroundColor(new ColorRGBA(_bgRed, _bgGreen, _bgBlue, 1f));
		screenShotState = new ScreenshotAppState("ScreenShots/");
		screenShotState.setIsNumbered(false);
		this.stateManager.attach(screenShotState);
		assetManagerExternal = assetManager;
		Support3DOther.setAssetManager(assetManager);
		ShapeGenerator.setAssetManager(assetManager);
		setupCamera();
		setupHUD();
		isStarted = true;
		synchronized (threadLock) {
			threadLock.notifyAll();
		}
	}

	private void setupCamera() {
		flyCam.setEnabled(false);
		lockPoint = ShapeGenerator.createCube(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Quaternion(),
				new ColorRGBA(0, 0, 0, 0f));
		rootNode.attachChild(lockPoint);
		camera = new ChaseCamera(cam, lockPoint, inputManager);
		camera.setRotationSpeed(5);
		camera.setInvertVerticalAxis(true);
		camera.setMaxVerticalRotation((float) (90f * (Math.PI / 180)));
		camera.setMinVerticalRotation((float) (-89f * (Math.PI / 180)));
		camera.setDefaultDistance(10);

		inputManager.addMapping("Left Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(actionListener, "Left Click");

	}

	final private ActionListener actionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean value, float tpf) {
			System.out.println(name + " = " + value);
			System.out.println(getInputManager().getCursorPosition());
		}
	};

	private void setupHUD() {
		setDisplayStatView(false);
		setDisplayFps(true);
		/*
		 * GUI.guiScaleX.setText(String.valueOf(deltaX));
		 * GUI.guiScaleY.setText(String.valueOf(deltaY));
		 * GUI.guiScaleZ.setText(String.valueOf(deltaZ));
		 * GUI.guiRotX.setText(String.valueOf(rotX));
		 * GUI.guiRotY.setText(String.valueOf(rotY));
		 * GUI.guiRotZ.setText(String.valueOf(rotZ));
		 * GUI.guiTickX.setText(String.valueOf(numOfSegmentsX));
		 * GUI.guiTickY.setText(String.valueOf(numOfSegmentsY));
		 * GUI.guiTickZ.setText(String.valueOf(numOfSegmentsZ));
		 */

	}

	@Override
	public void simpleUpdate(float tpf) {

		handleSceneChange(tpf);
		if (allCedViewsAdded) {

			if (panMode) {
				camera.setRotationSpeed(0);
				inputManager.setCursorVisible(true);
			} else {
				camera.setRotationSpeed(5);
			}
			if (updateLockPoint) {
				lockPoint.setLocalTranslation(getCurrentScene().getCameraLocation());
				updateLockPoint = false;
			}

			for (CedView3DImp2 cedView : cedViews) {

				cedView.update(tpf);
				// Always call this. This checks and adjusts the scale of all the geometries
				// when any of the
				// scene parameters are changed
				cedView.getScene().update(tpf, cam);
			}
		}
	}

	int buffer = 2;

	public void handleSceneChange(float tpf) {
		if (sceneAboutToChange == 3) {
			saveOldCameraData();
			currentScene = pendingScene;

			updateSceneData();
			sceneAboutToChange = 4;
		}

		if (sceneAboutToChange == 4 && !updatingSceneDataAlready) {
			if (rootNode.getChild("CedViewScene") != null) {
				rootNode.getChild("CedViewScene").removeFromParent();
			}
			rootNode.attachChild(getCurrentScene());
			pendingScene = null;

			// change scene
			camera.setDefaultDistance(getCurrentScene().getZoom());
			lockPoint.setLocalTranslation(getCurrentScene().getCameraLocation());
			camera.setDefaultHorizontalRotation((float) (getCurrentScene().getCameraRotation().getX()));
			camera.setDefaultVerticalRotation((float) (getCurrentScene().getCameraRotation().getY()));
			sceneAboutToChange = 0;
		}

		if (requestSettingCanvasVisible) {
			if (buffer <= 0) {
				for (CedView3DImp2 cedView : cedViews) {
					if (getCurrentScene().equals(cedView.getScene())) {
						Canvas canvas = (Canvas) cedView._panel3D.getJMonkey3DPanel().getComponent(0);
						canvas.setVisible(true);
					}
				}
				requestSettingCanvasVisible = false;
				buffer = 2;
			}
			buffer--;
		}

		if (sceneAboutToChange == 1) {
			if (getCurrentScene() != null) {
				screenShotState.setFileName(getCurrentScene().getCedName());
			}
			screenShotState.takeScreenshot();
		}

		if (sceneAboutToChange >= 1 && sceneAboutToChange < 3) {
			sceneAboutToChange += 1;
		}
	}

	/**
	 * Add an item to the list. Note that this does not initiate a redraw.
	 * 
	 * @param item the item to add.
	 */
	public void addItem(Geometry item) {
		Node scalableNode = ((Node) rootNode.getChild("Scalable"));
		scalableNode.attachChild(item);
	}

	/**
	 * Remove an item from the list. Note that this does not initiate a redraw.
	 * 
	 * @param item the item to remove.
	 */
	public void removeItem(Geometry item) {
		item.removeFromParent();
	}

	public Canvas getCanvas() {
		JmeCanvasContext context = (JmeCanvasContext) this.getContext();
		Canvas canvas = context.getCanvas();
		return canvas;
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

	public void addCedView(CedView3DImp2 cedView3DImp2) {
		cedViews.add(cedView3DImp2);
	}

	public void setCurrentScene(int i) {
		if (sceneAboutToChange == 0) {
			sceneAboutToChange = 1;
			pendingScene = cedViews.get(i).getScene();
		}
	}

	public void setCurrentScene(Scene n) {
		if (sceneAboutToChange == 0) {
			sceneAboutToChange = 1;
			pendingScene = n;
		}
	}

	private void saveOldCameraData() {
		if (getCurrentScene() != null) {
			getCurrentScene()
					.setCameraRotation(new Vector3f(camera.getHorizontalRotation(), camera.getVerticalRotation(), 0f));
			getCurrentScene().setCameraLocationRaw(lockPoint.getLocalTranslation());
			getCurrentScene().setZoom(camera.getDistanceToTarget());
		}
	}

	private void updateSceneData() {
		updatingSceneDataAlready = true;
		System.out.println("Updating Scene");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Canvas canvas = null;
				for (CedView3DImp2 cedView : cedViews) {
					if (!getCurrentScene().equals(cedView.getScene())) {
						cedView.getScene().setSceneInFocus(false);
						if (cedView.getHasJMonkeyCanvas()) {
							canvas = (Canvas) cedView._panel3D.getJMonkey3DPanel().getComponent(0);
							canvas.setVisible(false);
							Canvas3D temp = (Canvas3D) cedView._panel3D.getJMonkey3DPanel().getComponent(1);
							temp.setBufferedImage(ImageLoader.readImage(cedView.getScene().getCedName()));
							JPanel JMonkeyPanel = cedView._panel3D.getJMonkey3DPanel();
							Dimension dim = new Dimension(JMonkeyPanel.getWidth(), JMonkeyPanel.getHeight());
							temp.setSize(dim);
							cedView.setHasJMonkeyCanvas(false);

						}
					}

				}
				for (CedView3DImp2 cedView : cedViews) {
					if (getCurrentScene().equals(cedView.getScene())) {
						try {
							JPanel JMonkeyPanel = cedView._panel3D.getJMonkey3DPanel();
							JMonkeyPanel.add(canvas, 0);
							
							//TODO For some reason, JMonkey3D seems to scale canvas width and height by the windows text icon size
							//setting. You need to somehow read in what the windows setting is set to and adjust how much you scale by
							//For example, for a 125% view, you set canvasWidth = width + width/4
							int canvasWidth = (int)JMonkeyPanel.getWidth();
							int canvasHeight = (int)JMonkeyPanel.getHeight();
							Dimension dim = new Dimension(canvasWidth, canvasHeight);
							canvas.setSize(dim);
							cedView.setHasJMonkeyCanvas(true);
						} catch (Exception e) {
							System.out.println("ALREADY ADDED");
							// this means it was already added
						}
						cedView.getScene().setSceneInFocus(true);
					}
				}

				updatingSceneDataAlready = false;
				requestSettingCanvasVisible = true;
			}
		});

	}

	public Object getThreadLock() {
		return threadLock;
	}

	public boolean started() {
		return isStarted;
	}

	public Geometry getLockPoint() {
		return lockPoint;
	}

	public void setRotX(float x) {
		getCurrentScene().setRotX(x);
		updateLockPoint = true;
	}

	public void setRotY(float y) {
		getCurrentScene().setRotY(y);
		updateLockPoint = true;
	}

	public void setRotZ(float z) {
		getCurrentScene().setRotZ(z);
		updateLockPoint = true;
	}

	public void setPanMode(boolean s) {
		panMode = s;
	}

	public void setAllCedViewsAdded(boolean in) {
		allCedViewsAdded = in;
	}

	public Scene getCurrentScene() {
		return currentScene;
	}
}
