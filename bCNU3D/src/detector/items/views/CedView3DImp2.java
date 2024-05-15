package detector.items.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jlab.io.base.DataEvent;

import com.jme3.math.Vector3f;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.util.PrintUtilities;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.IClasIoEventListener;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.IAccumulationListener;
import cnuphys.lund.SwimTrajectoryListener;
import cnuphys.swim.Swimming;
import detector.items.CedPanel3DImp2;
import detector.items.MainDisplay3D;
import detector.items.Scene;

public abstract class CedView3DImp2 extends BaseView
		implements IClasIoEventListener, SwimTrajectoryListener, IAccumulationListener, ActionListener {

	// the menu bar
	private final JMenuBar _menuBar;

	// the event manager
	private final ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// the 3D panel
	public final CedPanel3DImp2 _panel3D;

	// for appending event number to the titile
	private static final String evnumAppend = "  (Seq Event# ";

	protected Scene scene;
	public MainDisplay3D mainDisplay;
	private boolean hasJMonkeyCanvas = false;

	// menu
	private JMenuItem _printMenuItem;
	private JMenuItem _pngMenuItem;
	private JMenuItem _refreshItem;

	/**
	 * Create a 3D view
	 * 
	 * @param title
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 * @param xDist
	 * @param yDist
	 * @param zDist
	 */
	public CedView3DImp2(String title, MainDisplay3D display, float angleX, float angleY, float angleZ, float xDist,
			float yDist, float zDist) {
		super(PropertySupport.TITLE, title, PropertySupport.ICONIFIABLE, true, PropertySupport.MAXIMIZABLE, true,
				PropertySupport.CLOSABLE, true, PropertySupport.RESIZABLE, true, PropertySupport.VISIBLE, true);
		System.out.println("STARTING DISPLAY");
		setupDisplay(display, angleX, angleY, angleZ, xDist, yDist, zDist);

		_eventManager.addClasIoEventListener(this, 2);

		// listen for trajectory changes
		Swimming.addSwimTrajectoryListener(this);

		_menuBar = new JMenuBar();
		setJMenuBar(_menuBar);
		addMenus();

		setLayout(new BorderLayout(1, 1));

		_panel3D = make3DPanel(angleX, angleY, angleZ, xDist, yDist, zDist);

		addViewToDisplay(display);

		add(_panel3D, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(1), BorderLayout.WEST);
		// pack();
		this.setSize(854, 800);
		AccumulationManager.getInstance().addAccumulationListener(this);

	}

	private void addViewToDisplay(MainDisplay3D display) {
		synchronized (display.getThreadLock()) {
			while (!display.started()) {
				try {
					display.getThreadLock().wait();
				} catch (Exception e) {

				}
			}
		}

		display.addCedView(this);
		display.setCurrentScene(scene);

	}

	// make the 3d panel
	protected abstract CedPanel3DImp2 make3DPanel(float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist);

	// add the menus
	private void addMenus() {
		JMenu actionMenu = new JMenu("ced3D");
		_printMenuItem = new JMenuItem("Print...");
		_printMenuItem.addActionListener(this);
		actionMenu.add(_printMenuItem);

		_pngMenuItem = new JMenuItem("Save as PNG...");
		_pngMenuItem.addActionListener(this);
		actionMenu.add(_pngMenuItem);

		_refreshItem = new JMenuItem("Refresh");
		_refreshItem.addActionListener(this);
		actionMenu.add(_refreshItem);

		_menuBar.add(actionMenu);
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		if (!_eventManager.isAccumulating()) {
			fixTitle(event);
			_panel3D.refreshQueued();
		}
	}

	@Override
	public void openedNewEventFile(String path) {
		_panel3D.refreshQueued();
	}

	/**
	 * Change the event source type
	 * 
	 * @param source the new source: File, ET
	 */
	@Override
	public void changedEventSource(ClasIoEventManager.EventSourceType source) {
	}

	@Override
	public void accumulationEvent(int reason) {
		switch (reason) {
		case AccumulationManager.ACCUMULATION_STARTED:
			break;

		case AccumulationManager.ACCUMULATION_CANCELLED:
			fixTitle(_eventManager.getCurrentEvent());
			_panel3D.refresh();
			break;

		case AccumulationManager.ACCUMULATION_FINISHED:
			fixTitle(_eventManager.getCurrentEvent());
			_panel3D.refresh();
			break;
		}
	}

	@Override
	public void trajectoriesChanged() {
		if (!_eventManager.isAccumulating()) {
			_panel3D.refreshQueued();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();
		if (source == _printMenuItem) {
			PrintUtilities.printComponent(_panel3D);
		} else if (source == _pngMenuItem) {
			GraphicsUtilities.saveAsPng(_panel3D);
		} else if (source == _refreshItem) {
			_panel3D.refresh();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (_panel3D != null) {
			_panel3D.requestFocus();
		}
	}

	@Override
	public void refresh() {
		if (_panel3D != null) {
			_panel3D.refresh();
		}
	}

	/**
	 * Fix the title of the view after an event arrives. The default is to append
	 * the event number.
	 * 
	 * @param event the new event
	 */
	protected void fixTitle(DataEvent event) {
		String title = getTitle();
		int index = title.indexOf(evnumAppend);
		if (index > 0) {
			title = title.substring(0, index);
		}

		int seqNum = _eventManager.getSequentialEventNumber();
		int trueNum = _eventManager.getTrueEventNumber();
		if (seqNum > 0) {
			if (trueNum > 0) {
				setTitle(title + evnumAppend + seqNum + "  True event# " + trueNum + ")");
			} else {
				setTitle(title + evnumAppend + seqNum + ")");
			}
		}
	}

	public void setupDisplay(MainDisplay3D display, float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist) {
		mainDisplay = display;
		scene = new Scene(this.getTitle());
		scene.setName("CedViewScene");
		scene.setScale(1, 1, 1);
		scene.setSegments(0.5f, 0.5f, 0.5f);
		scene.setZoom(30);
		scene.setCameraLocationRaw(new Vector3f(xDist, yDist, zDist));
	}

	public MainDisplay3D getMainDisplay() {
		return mainDisplay;
	}

	/**
	 * Tests whether this listener is interested in events while accumulating
	 * 
	 * @return <code>true</code> if this listener is NOT interested in events while
	 *         accumulating
	 */
	@Override
	public boolean ignoreIfAccumulating() {
		return true;
	}

	public void update(float tpf) {
		if (!scene.isSceneInFocus() && this.isSelected() && !this.hasJMonkeyCanvas && mainDisplay.sceneAboutToChange == 0 && !mainDisplay.updatingSceneDataAlready) {
			mainDisplay.setCurrentScene(scene);
		}
		
		if (_panel3D != null) {
			_panel3D.update(tpf);
		}
	}

	public boolean getHasJMonkeyCanvas(){
		return hasJMonkeyCanvas;
	}
	
	public void setHasJMonkeyCanvas(boolean has){
		hasJMonkeyCanvas = has;
	}
	
	public Scene getScene() {
		return scene;
	}
}
