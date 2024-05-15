package detector.items;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.jme3.scene.Node;
import bCNU3D.Support3DOther;
import cnuphys.ced.event.data.Cross;
import cnuphys.ced.event.data.CrossList;
import cnuphys.ced.event.data.HBCrosses;
import cnuphys.ced.event.data.TBCrosses;
import cnuphys.ced.frame.CedColors;
import shapes3D.Item3DImp2;

public class CrossDrawer3DImp2 extends Item3DImp2 {

	protected static final float CROSS_LEN = 30f; // in cm
	protected static final double COS_TILT = Math.cos(Math.toRadians(25.));
	protected static final double SIN_TILT = Math.sin(Math.toRadians(25.));

	private CedPanel3DImp2 _cedPanel3D;
	
	public CrossDrawer3DImp2(Node root, CedPanel3DImp2 cedPanel) {
		super(cedPanel);
		_cedPanel3D = cedPanel;
		_parentNode = new Node();
		
		root.attachChild(_parentNode);
		
	}

	@Override
	public void update(float tpf) {
		_parentNode.detachAllChildren();
		if (_cedPanel3D.showHBCross()) {
			CrossList list = HBCrosses.getInstance().getCrosses();
			drawCrossList(list, CedColors.HB_COLOR);
		}
		if (_cedPanel3D.showTBCross()) {
			CrossList list = TBCrosses.getInstance().getCrosses();
			drawCrossList(list, CedColors.TB_COLOR);
		}
		if (_cedPanel3D.showAIHBCross()) {
			CrossList list = HBCrosses.getInstance().getCrosses();
			drawCrossList(list, CedColors.AIHB_COLOR);
		}
		if (_cedPanel3D.showAITBCross()) {
			CrossList list = TBCrosses.getInstance().getCrosses();
			drawCrossList(list, CedColors.AITB_COLOR);
		}

	}

	// draw a cross list
	private void drawCrossList(CrossList list, Color color) {
		if ((list == null) || list.isEmpty()) {
			return;
		}

		for (Cross cross : list) {
			if (cross != null) {
				createCross(cross, color);
			}
		}
	}

	private void createCross(Cross cross, Color color) {

		float[] p3d0 = new float[3];
		float[] p3d1 = new float[3];

		tiltedToSector(cross.x, cross.y, cross.z, p3d0);
		float x = p3d0[0];
		float y = p3d0[1];
		float z = p3d0[2];

		float tx = cross.x + CROSS_LEN * cross.ux;
		float ty = cross.y + CROSS_LEN * cross.uy;
		float tz = cross.z + CROSS_LEN * cross.uz;
		tiltedToSector(tx, ty, tz, p3d1);

		Support3DOther.drawLine(_parentNode, x, y, z, p3d1[0], p3d1[1], p3d1[2], Color.black, 3f);
		Support3DOther.drawLine(_parentNode, x, y, z, p3d1[0], p3d1[1], p3d1[2], Color.gray, 1f);

		Support3DOther.drawPoint(_parentNode, x, y, z, Color.black, 13, true);
		Support3DOther.drawPoint(_parentNode, x, y, z, color, 11, true);

	}

	/**
	 * Convert tilted sector coordinates to sector coordinates. The two vectors can
	 * be the same in which case it is overwritten.
	 * 
	 * @param tiltedXYZ will hold the tilted coordinates
	 * @param sectorXYZ the sector coordinates
	 */
	public void tiltedToSector(float tiltx, float tilty, float tiltz, float[] sectorXYZ) {

		double sectx = tiltx * COS_TILT + tiltz * SIN_TILT;
		double secty = tilty;
		double sectz = -tiltx * SIN_TILT + tiltz * COS_TILT;

		sectorXYZ[0] = (float) sectx;
		sectorXYZ[1] = (float) secty;
		sectorXYZ[2] = (float) sectz;
	}

	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCheckBoxInterrupt(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
