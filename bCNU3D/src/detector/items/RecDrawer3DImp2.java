package detector.items;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.event.ChangeEvent;

import org.jlab.io.base.DataEvent;

import com.jme3.scene.Node;
import bCNU3D.Support3DOther;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.RECCalorimeter;
import cnuphys.ced.frame.CedColors;
import cnuphys.lund.LundId;
import shapes3D.Item3DImp2;

public class RecDrawer3DImp2 extends Item3DImp2 {

	// the event manager
	ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// the current event
	private DataEvent _currentEvent;
	private DataEvent _previousEvent;
	private boolean interruptTriggered = false;

	private static final float POINTSIZE = 5f;
	private CedPanel3DImp2 _cedPanel3D;

	public RecDrawer3DImp2(Node root, CedPanel3DImp2 panel3D) {
		super(panel3D);
		_cedPanel3D = panel3D;
		_parentNode = new Node();

		root.attachChild(_parentNode);
	}

	@Override
	public void update(float tpf) {
		_currentEvent = _eventManager.getCurrentEvent();
		if (_currentEvent == null && _previousEvent != null) {
			_previousEvent = null;
			_parentNode.detachAllChildren();
		}
		if (_currentEvent == null) {
			return;
		}

		if (interruptTriggered || !(_currentEvent.equals(_previousEvent))) {
			if (_panel3D instanceof ForwardPanel3DImp2) { // forward detectors
				// show any data from REC::Calorimiter?
				_parentNode.detachAllChildren();
				if (((ForwardPanel3DImp2) _panel3D).showRecCal()) {
					_previousEvent = _currentEvent;
					interruptTriggered = false;
					showEconCalorimeter(_parentNode);
				}
			}
		}
	}

	// show data from REC::Calorimeter
	private void showEconCalorimeter(Node root) {

		RECCalorimeter recCal = RECCalorimeter.getInstance();
		if (recCal.isEmpty()) {
			return;
		}

		for (int i = 0; i < recCal.count; i++) {

			float radius = recCal.getRadius(recCal.energy[i]);
			LundId lid = recCal.getLundId(i);

			if ((recCal.layer[i] <= 3) && _cedPanel3D.showPCAL()) {
				Support3DOther.drawPoint(root, recCal.x[i], recCal.y[i], recCal.z[i], Color.black, POINTSIZE, true);

				if (radius > 0) {
					Color color = (lid == null) ? CedColors.RECEcalFill : lid.getStyle().getTransparentFillColor();
					Support3DOther.solidSphere(root, recCal.x[i], recCal.y[i], recCal.z[i], radius, 40, 40, color);
				}
			} else if ((recCal.layer[i] > 3) && _cedPanel3D.showECAL()) {
				Support3DOther.drawPoint(root, recCal.x[i], recCal.y[i], recCal.z[i], Color.black, POINTSIZE, true);
				if (radius > 0) {
					Color color = (lid == null) ? CedColors.RECEcalFill : lid.getStyle().getTransparentFillColor();
					Support3DOther.solidSphere(root, recCal.x[i], recCal.y[i], recCal.z[i], radius, 40, 40, color);
				}
			}
		} // end for

	}

	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCheckBoxInterrupt(ActionEvent e) {
		AbstractButton o = (AbstractButton) e.getSource();
		String name = o.getText();
		if (name.equals(CedPanel3DImp2.SHOW_PCAL) || name.equals(CedPanel3DImp2.SHOW_ECAL)
				|| name.equals(CedPanel3DImp2.SHOW_REC_CAL)) {
			interruptTriggered = true;
		}

	}

}
