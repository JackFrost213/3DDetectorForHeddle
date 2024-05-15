package detector.items;

import shapes3D.Axis;
import shapes3D.Item3DImp2;

import java.awt.Color;
import java.awt.Font;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import cnuphys.ced.geometry.BSTGeometry;
import detector.items.views.CedView3DImp2;

public class CentralPanel3DImp2 extends CedPanel3DImp2 {

	// dimension of this panel are in cm
	private final float xymax = 50f;
	private final float zmax = 50f;
	private final float zmin = -50f;

	// labels for the check box
	private static final String _cbaLabels[] = { SHOW_VOLUMES, SHOW_TRUTH, SHOW_BST, SHOW_BST_LAYER_1, SHOW_BST_LAYER_2,
			SHOW_BST_LAYER_3, SHOW_BST_LAYER_4, SHOW_BST_LAYER_5, SHOW_BST_LAYER_6, SHOW_BST_LAYER_7, SHOW_BST_LAYER_8,
			SHOW_BST_HITS, SHOW_BMT, SHOW_BMT_LAYER_1, SHOW_BMT_LAYER_2, SHOW_BMT_LAYER_3, SHOW_BMT_LAYER_4,
			SHOW_BMT_LAYER_5, SHOW_BMT_LAYER_6, SHOW_BMT_HITS, SHOW_CTOF, SHOW_CND, SHOW_CND_LAYER_1, SHOW_CND_LAYER_2,
			SHOW_CND_LAYER_3, SHOW_RECON_CROSSES, SHOW_TB_TRACK, SHOW_HB_TRACK, SHOW_CVT_TRACK, SHOW_COSMIC };

	/**
	 * 
	 * @param view
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 * @param xDist
	 * @param yDist
	 * @param zDist
	 */
	public CentralPanel3DImp2(CedView3DImp2 view, float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist) {
		super(view, angleX, angleY, angleZ, xDist, yDist, zDist, BGFEFAULT, BGFEFAULT, BGFEFAULT, _cbaLabels);
	}

	@Override
	public void createInitialItems(Scene rootNode) {
		float xymax_scaled = xymax / rootNode.getScaleX();
		float zmax_scaled = zmax / rootNode.getScaleZ();
		float zmin_scaled = zmin / rootNode.getScaleY();
		
		// coordinate axes
		Node scalableNode = ((Node) rootNode.getChild("Scalable"));
		Node axes = ((Node) rootNode.getChild("Axes"));
		// coordinate axes
		Axis yAxis = new Axis(new Vector3f(0, 1, 0), 0.5f, 1, -xymax_scaled, xymax_scaled);
		yAxis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		yAxis.setColor(Color.black, Color.darkGray);
		yAxis.setWordColor(Color.black);
		axes.attachChild(yAxis);

		// Line from 0,0,0 to 0,0,1
		Axis zAxis = new Axis(new Vector3f(0, 0, 1), 0.5f, 1, zmin_scaled, zmax_scaled);
		zAxis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		zAxis.setColor(Color.black, Color.darkGray);
		zAxis.setWordColor(Color.black);
		axes.attachChild(zAxis);

		// Line from 0,0,0 to 1,0,0
		Axis xAxis = new Axis(new Vector3f(1, 0, 0), 0.5f, 1, -xymax_scaled, xymax_scaled);
		xAxis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		xAxis.setColor(Color.black, Color.darkGray);
		xAxis.setWordColor(Color.black);
		axes.attachChild(xAxis);
		
		
		//Axes3D axes = new Axes3D(this, -xymax, xymax, -xymax, xymax, zmin, zmax, null, Color.darkGray, 1f, 6, 6, 6,
		//		Color.black, X11Colors.getX11Color("Dark Green"), new Font("SansSerif", Font.PLAIN, 12), 0);
		//addItem(axes);

		// trajectory drawer
		TrajectoryDrawer3DImp2 trajDrawer = new TrajectoryDrawer3DImp2(scalableNode, this);
		addItem(trajDrawer);

		// mc hit drawer
		// MCHitDrawer3D mchd = new MCHitDrawer3D(this);
		// addItem(mchd);

		// svt panels
		for (int layer = 1; layer <= 6; layer++) {
			// for (int layer = 1; layer <= 8; layer++) {
			// geom service uses 0-based superlayer [0,1,2,3] and layer [0,1]
			int supl = ((layer - 1) / 2); // 0, 1, 2, 3

			int numSect = BSTGeometry.sectorsPerSuperlayer[supl];

			for (int sector = 1; sector <= numSect; sector++) {
				BSTPanel3DImp2 svt = new BSTPanel3DImp2(scalableNode, this, sector, layer);
				addItem(svt);
			}
		}
		
		// BMT
		for (int sector = 1; sector <= 3; sector++) {
			for (int layer = 1; layer <= 6; layer++) {
				BMTLayer3DImp2 bmt = new BMTLayer3DImp2(scalableNode, this, sector, layer);
				addItem(bmt);
			}
		}

		
		// cnd
		addItem(new CND3DImp2(scalableNode, this));

		// ctof
		addItem(new CTOF3DImp2(scalableNode, this));
		
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
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		for (Item3DImp2 detectorItem : _itemList2) {
			detectorItem.update(tpf);
			if(detectorItem.getChildren() != null) {
				for (Item3DImp2 detectorItem2 : detectorItem.getChildren()) {
					detectorItem2.update(tpf);
			}
		 }
	  }
	}

}
