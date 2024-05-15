package detector.items;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import bCNU3D.Support3DOther;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.ECAL;
import cnuphys.ced.event.data.FTOF;
import cnuphys.ced.event.data.PCAL;

import com.jme3.scene.Node;
import shapes3D.Item3DImp2;

public class MCHitDrawer3DImp2 extends Item3DImp2 {

	// the event manager
	ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	private static final float POINTSIZE = 3f;
	private CedPanel3DImp2 _cedPanel3D;

	public MCHitDrawer3DImp2(CedPanel3DImp2 panel3D) {
		super(panel3D);
		_cedPanel3D = panel3D;
	}

	//TODO FIX THIS
	public void draw(Node root) {

		if (!_cedPanel3D.showMCTruth()) {
			return;
		}

		if (_panel3D instanceof ForwardPanel3DImp2) { // forward detectors

			// if (showDC()) {
			// }

			if (_cedPanel3D.showFTOF()) {
				showGemcXYZHits(root, FTOF.getInstance().avgX(FTOF.PANEL_1A),
						FTOF.getInstance().avgY(FTOF.PANEL_1A), FTOF.getInstance().avgZ(FTOF.PANEL_1A), null, 0);
				showGemcXYZHits(root, FTOF.getInstance().avgX(FTOF.PANEL_1B),
						FTOF.getInstance().avgY(FTOF.PANEL_1B), FTOF.getInstance().avgZ(FTOF.PANEL_1B), null, 1);
				showGemcXYZHits(root, FTOF.getInstance().avgX(FTOF.PANEL_2), FTOF.getInstance().avgY(FTOF.PANEL_2),
						FTOF.getInstance().avgZ(FTOF.PANEL_2), null, 2);
			}

			if (_cedPanel3D.showECAL()) {
				showGemcXYZHits(root, ECAL.avgX(), ECAL.avgY(), ECAL.avgZ(), ECAL.pid(), 0);
			}

			if (_cedPanel3D.showPCAL()) {
				showGemcXYZHits(root, PCAL.avgX(), PCAL.avgY(), PCAL.avgZ(), PCAL.pid(), 0);
			}

		} /*else if (_panel3D instanceof CentralPanel3D) { // central detectors

		}*/
		//TODO Fix this once i add the central detectors

	}

	// draw all the MC hits at once
	private void showGemcXYZHits(Node root, double x[], double y[], double z[], int pid[], int option) {

		if ((x == null) || (y == null) || (z == null) || (x.length < 1)) {
			return;
		}

		// should not be necessary but be safe
		int len = x.length;
		len = Math.min(len, y.length);
		len = Math.min(len, z.length);

		if (len < 1) {
			return;
		}

		for (int hitIndex = 0; hitIndex < len; hitIndex++) {
			Color truthColor = DetectorItem3DImp2.truthColor(pid, hitIndex);
			float xcm = (float) (x[hitIndex] / 10); // convert mm to cm
			float ycm = (float) (y[hitIndex] / 10); // convert mm to cm
			float zcm = (float) (z[hitIndex] / 10); // convert mm to cm
			Support3DOther.drawPoint(root, xcm, ycm, zcm, Color.black, POINTSIZE + 2, true);
			Support3DOther.drawPoint(root, xcm, ycm, zcm, truthColor, POINTSIZE, true);

		}

	}

	@Override
	public void update(float tpf) {
		// TODO Auto-generated method stub
		
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
