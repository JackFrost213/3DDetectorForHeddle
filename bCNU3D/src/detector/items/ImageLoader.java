package detector.items;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	
	public static BufferedImage readImage(String name) {
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("ScreenShots/"+name+".png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return img;
	}
}
