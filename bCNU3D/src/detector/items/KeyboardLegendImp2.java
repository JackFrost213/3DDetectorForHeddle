package detector.items;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bCNU3D.Panel3D;
import cnuphys.bCNU.graphics.component.CommonBorder;

@SuppressWarnings("serial")
public class KeyboardLegendImp2 extends JPanel {

	private int rotateMode = 0;
	private Panel3D _panel3D;
	private JTextField guiRotX, guiRotY, guiRotZ;

	/**
	 * Legend on 3D views
	 */
	public KeyboardLegendImp2(Panel3D panel) {
		_panel3D = panel;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel coordinates = new JPanel();
		JLabel xLabel = new JLabel("X:");	
		JLabel yLabel = new JLabel("Y:");
		JLabel zLabel = new JLabel("Z:");
		guiRotX = new JTextField();
		guiRotY = new JTextField();
		guiRotZ = new JTextField();
		guiRotX.setPreferredSize(new Dimension(30,20));
		guiRotY.setPreferredSize(new Dimension(30,20));
		guiRotZ.setPreferredSize(new Dimension(30,20));
		
		guiRotX.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiRotXPropertyChange(e);
            }
        });

        guiRotY.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiRotYPropertyChange(e);
            }
        });
        
        guiRotZ.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiRotZPropertyChange(e);
            }
        });
        Scene scene = ((CedPanel3DImp2)_panel3D).getView().getScene();
        guiRotX.setText(String.valueOf(scene.getCameraLocation().getX()/scene._xscale));
        guiRotY.setText(String.valueOf(scene.getCameraLocation().getY()/scene._yscale));
        guiRotZ.setText(String.valueOf(scene.getCameraLocation().getZ()/scene._zscale));
		coordinates.add(xLabel);
		coordinates.add(guiRotX);
		coordinates.add(yLabel);
		coordinates.add(guiRotY);
		coordinates.add(zLabel);
		coordinates.add(guiRotZ);
		
		JLabel info = new JLabel();
		info.setText("Pivot Point (Rotates Around)");
		JLabel extraSpace = new JLabel();
		
		//TODO make VRMode button start JMonkey3D's VR mode. You will have to adjust how the camera moves.
		JButton VRMode = new JButton("Activate VR MODE");
		this.add(info);
		this.add(coordinates);
		this.add(extraSpace);
		this.add(VRMode);
		setBorder(new CommonBorder("Keyboard Actions"));
//		validate();
	}
	
	public int getRotateMode() {
		return rotateMode;
	}
	
	private void guiRotXPropertyChange(KeyEvent evt) {       
    	try {
    		((CedPanel3DImp2)_panel3D).getView().mainDisplay.setRotX(Float.valueOf(guiRotX.getText()));
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }                                        

    private void guiRotYPropertyChange(KeyEvent evt) {       
    	try {
    		((CedPanel3DImp2)_panel3D).getView().mainDisplay.setRotY(Float.valueOf(guiRotY.getText()));
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }   
    
    private void guiRotZPropertyChange(KeyEvent evt) {       
    	try {
    		((CedPanel3DImp2)_panel3D).getView().mainDisplay.setRotZ(Float.valueOf(guiRotZ.getText()));
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }    
}
