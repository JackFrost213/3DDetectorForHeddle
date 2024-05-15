package detector.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jme3.math.Vector3f;

import cnuphys.ced.geometry.DCGeometry;
import detector.items.views.ForwardView3DImp2;

public class TestRunner {

	public static void main(String[] args) {
		DCGeometry.initialize();
		MainDisplay3D _3Ddisplay = new MainDisplay3D();
		System.out.println("Running Test Run");
		ForwardView3DImp2 _forward3DView = new ForwardView3DImp2("Scene 1", _3Ddisplay);
		_forward3DView.setName("View 1");
		System.out.println("Created View 1");
		ForwardView3DImp2 _forward3DView2 = new ForwardView3DImp2("Scene 2", _3Ddisplay);
		_forward3DView2.setName("View 2");
		_forward3DView2.getScene().setCameraLocationRaw(new Vector3f(500,0,0));
		System.out.println("Created View 2");
		JFrame frame = new JFrame();
		frame.add(_forward3DView);
		frame.add(_forward3DView2);
		frame.setBackground(Color.BLUE);
		frame.setTitle("bCNU 3D Panel Test using JMonkeyLibrary");
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.BLUE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setSize(new Dimension(800,500));
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				_3Ddisplay.stop();
			}
		});
		
		/*
		while(true) {
			System.out.println("Panel 1: " + _forward3DView.isSelected());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Panel 2: " + _forward3DView2.isSelected());
		}*/
	}
}
