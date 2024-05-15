package detector.items.views;

import detector.items.CedPanel3DImp2;
import detector.items.FTCalPanel3DImp2;
import detector.items.MainDisplay3D;

public class FTCalView3DImp2 extends CedView3DImp2 {

	public static final float xdist = 0f;
	public static final float ydist = 0f;
	public static final float zdist = 0f;

	private static final float thetax = 0f;
	private static final float thetay = 90f;
	private static final float thetaz = 90f;

	public FTCalView3DImp2(MainDisplay3D display) {
		super("FTCal 3D View", display, thetax, thetay, thetaz, xdist, ydist, zdist);
	}

	public FTCalView3DImp2(String title, MainDisplay3D display) {
		super(title, display, thetax, thetay, thetaz, xdist, ydist, zdist);
	}
	
	@Override
	protected CedPanel3DImp2 make3DPanel(float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		scene.setScale(10, 10, 10);
		return new FTCalPanel3DImp2(this, angleX, angleY, angleZ, xDist, yDist, zDist);
	}

}
