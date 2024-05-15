package detector.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Canvas3D extends JPanel {

	private BufferedImage image;

	public void setBufferedImage(BufferedImage image) {
		this.image = image;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(image, 0,0,this.getWidth(), this.getHeight(), null);
		}
	}
}
