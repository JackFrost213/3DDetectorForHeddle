package detector.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import cnuphys.magfield.MagneticFields;
import detector.items.views.CedView3DImp2;
import shapes3D.Axis;
import shapes3D.Item3DImp2;

public class ForwardPanel3DImp2 extends CedPanel3DImp2 {

	// cm
	private final float xymax = 600f;
	private final float zmax = 600f;
	private final float zmin = -100f;

	private static final Color _torusColor = new Color(0, 255, 255, 16);
	private static final Color _solenoidColor = new Color(255, 128, 255, 16);

	private static final String _cbaLabels[] = { SHOW_VOLUMES, SHOW_TRUTH, SHOW_SECTOR_1, SHOW_SECTOR_2, SHOW_SECTOR_3,
			SHOW_SECTOR_4, SHOW_SECTOR_5, SHOW_SECTOR_6, SHOW_DC, SHOW_ECAL, SHOW_PCAL, SHOW_FTOF, SHOW_HB_CROSS,
			SHOW_TB_CROSS, SHOW_AIHB_CROSS, SHOW_AITB_CROSS, SHOW_HB_TRACK, SHOW_TB_TRACK, SHOW_AIHB_TRACK,
			SHOW_AITB_TRACK, SHOW_CVT_TRACK, SHOW_REC_TRACK, SHOW_REC_CAL, SHOW_MAP_EXTENTS };

	public ForwardPanel3DImp2(CedView3DImp2 view, float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist) {
		super(view, angleX, angleY, angleZ, xDist, yDist, zDist, 0.92f, 0.92f, 0.92f, _cbaLabels);
	}

	/**
	 * Create the initial items
	 */
	@Override
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

		CrossDrawer3DImp2 cdraw = new CrossDrawer3DImp2(scalableNode, this);
		addItem(cdraw);
		// trajectory drawer
		TrajectoryDrawer3DImp2 trajDrawer = new TrajectoryDrawer3DImp2(scalableNode, this);
		addItem(trajDrawer);

		// mc hit drawer
		MCHitDrawer3DImp2 mchd = new MCHitDrawer3DImp2(this);
		addItem(mchd);

		// recon drawer
		RecDrawer3DImp2 rd = new RecDrawer3DImp2(scalableNode, this);
		addItem(rd);

		// dc super layers
		for (int sector = 1; sector <= 6; sector++) {
			for (int superlayer = 1; superlayer <= 6; superlayer++) {
				DCSuperLayer3DImp2 dcsl = new DCSuperLayer3DImp2(scalableNode, this, sector, superlayer);
				addItem(dcsl);
				
			}
		}
		
		// tof paddles
		for (int sector = 1; sector <= 6; sector++) {
			addItem(new FTOF3DImp2(scalableNode, this, sector));
		}

		for (int sector = 1; sector <= 6; sector++) {
			for (int view = 1; view <= 3; view++) {
				PCALViewPlane3DImp2 pcalvp = new PCALViewPlane3DImp2(scalableNode, this, sector, view);
				addItem(pcalvp);
			}
		}

		for (int sector = 1; sector <= 6; sector++) {
			for (int stack = 1; stack <= 2; stack++) {
				for (int view = 1; view <= 3; view++) {
					ECViewPlane3DImp2 ecvp = new ECViewPlane3DImp2(scalableNode, this, sector, stack, view);
					addItem(ecvp);
				}

			}
		}
		
		if (MagneticFields.getInstance().hasActiveSolenoid())
			 { System.out.println("Adding 3D Solenoid Boundary"); 
			 addItem(new FieldBoundaryImp2(scalableNode, this, MagneticFields.getInstance().getSolenoid(), _solenoidColor)); 
			 } 
		if (MagneticFields.getInstance().hasActiveTorus()) {
			 System.out.println("Adding 3D Torus Boundary"); 
			 addItem(new FieldBoundaryImp2(scalableNode, this, MagneticFields.getInstance().getTorus(), _torusColor)); 
			 }
		
		this.handleSliderInterrupt(null);
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
	
	/*@Override
	public void handleNextButtonPressed(ActionEvent e) {
		super.handleNextButtonPressed(e);
		for (Item3DImp2 detectorItem : _itemList2) {
			detectorItem.updateData();
		}
	}*/

	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		for (Item3DImp2 detectorItem : _itemList2) {
			detectorItem.handleSliderInterrupt(e);
		}
	}

	@Override
	public void handleCheckBoxInterrupt(ActionEvent e) {
		for (Item3DImp2 detectorItem : _itemList2) {
			detectorItem.handleCheckBoxInterrupt(e);
		}
	}
}
