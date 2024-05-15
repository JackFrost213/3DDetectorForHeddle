package item3D;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.jme3.scene.Node;
import bCNU3D.Panel3D;
import bCNU3D.Support3DOther;
import shapes3D.Item3DImp2;

public class CylinderImp2 extends Item3DImp2 {

	private float _radius;
	private float _x1;
	private float _y1;
	private float _z1;
	private float _x2;
	private float _y2;
	private float _z2;
	private Color _color;

	public CylinderImp2(Panel3D panel3D, float x1, float y1, float z1, float x2, float y2, float z2, float radius,
			Color color) {
		super(panel3D);
		_radius = radius;
		_x1 = x1;
		_y1 = y1;
		_z1 = z1;
		_x2 = x2;
		_y2 = y2;
		_z2 = z2;
		_color = color;
	}

	public CylinderImp2(Panel3D panel3D, float data[], Color color) {
		this(panel3D, data[0], data[1], data[2], data[3], data[4], data[5], data[6], color);
	}

	public void draw(Node root) {
		Support3DOther.drawTube(root, _x1, _y1, _z1, _x2, _y2, _z2, _radius, _color);
	}

	public void reset(float x1, float y1, float z1, float x2, float y2, float z2) {
		_x1 = x1;
		_y1 = y1;
		_z1 = z1;
		_x2 = x2;
		_y2 = y2;
		_z2 = z2;
	}

	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCheckBoxInterrupt(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float tpf) {
		// TODO Auto-generated method stub
		
	}

}
