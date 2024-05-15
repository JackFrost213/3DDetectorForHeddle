package detector.items;

import java.awt.Color;
import java.awt.Font;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import cnuphys.ced.geometry.FTCALGeometry;
import detector.items.views.CedView3DImp2;
import shapes3D.Axis;

public class FTCalPanel3DImp2 extends CedPanel3DImp2 {

	// dimension of this panel are in cm
	// private final float xymax = 20f;
	// private final float zmax = 215f;
	// private final float zmin = 0f;
	// dimension of this panel are in cm
	private final float xymax = 50f;
	private final float zmax = 50f;
	private final float zmin = -50f;

	// labels for the check box
	private static final String _cbaLabels[] = { SHOW_VOLUMES, SHOW_TRUTH };

	public FTCalPanel3DImp2(CedView3DImp2 view, float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist) {
		super(view, angleX, angleY, angleZ, xDist, yDist, zDist, BGFEFAULT, BGFEFAULT, BGFEFAULT, _cbaLabels);
	}

	public void createInitialItems(Scene rootNode) {
		float xymax_scaled = xymax / rootNode.getScaleX();
		float zmax_scaled = zmax / rootNode.getScaleZ();
		float zmin_scaled = zmin / rootNode.getScaleY();

		Node scalableNode = ((Node) rootNode.getChild("Scalable"));
		Node axes = ((Node) rootNode.getChild("Axes"));
		// coordinate axes
		Axis yAxis = new Axis(new Vector3f(0, 1, 0), 1f, 1, -xymax_scaled, xymax_scaled);
		yAxis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		yAxis.setColor(Color.black, Color.darkGray);
		yAxis.setWordColor(Color.GREEN);
		axes.attachChild(yAxis);

		// Line from 0,0,0 to 0,0,1
		Axis zAxis = new Axis(new Vector3f(0, 0, 1), 1f, 1, zmin_scaled, zmax_scaled);
		zAxis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		zAxis.setColor(Color.black, Color.darkGray);
		zAxis.setWordColor(Color.green);
		axes.attachChild(zAxis);

		// Line from 0,0,0 to 1,0,0
		Axis xAxis = new Axis(new Vector3f(1, 0, 0), 0.5f, 1, -xymax_scaled, xymax_scaled);
		xAxis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		xAxis.setColor(Color.black, Color.darkGray);
		xAxis.setWordColor(Color.GREEN);
		axes.attachChild(xAxis);
		
		// coordinate axes
		/*Axes3D axes = new Axes3D(this, -xymax, xymax, -xymax, xymax, zmin, zmax, null, FTCALGeometry.FTCAL_Z0,
				Color.darkGray, 1f, 6, 6, 6, Color.black, X11Colors.getX11Color("Dark Green"),
				new Font("SansSerif", Font.PLAIN, 12), 0);
		addItem(axes);
		*/
		// trajectory drawer
		TrajectoryDrawer3DImp2 trajDrawer = new TrajectoryDrawer3DImp2(scalableNode, this);
		addItem(trajDrawer);

		for (int id : FTCALGeometry.getGoodIds()) {
			FTCalPaddle3DImp2 paddle = new FTCalPaddle3DImp2(scalableNode, this, id);
			addItem(paddle);
		}

	}

	/**
	 * This gets the z step used by the mouse and key adapters, to see how fast we
	 * move in or in in response to mouse wheel or up/down arrows. It should be
	 * overridden to give something sensible. like the scale/100;
	 * 
	 * @return the z step (changes to zDist) for moving in and out
	 */
	@Override
	public float getZStep() {
		return (zmax - zmin) / 50f;
	}

}
