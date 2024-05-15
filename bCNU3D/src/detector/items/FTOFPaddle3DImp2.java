package detector.items;

import java.awt.Color;
import bCNU3D.Support3DOther;
import cnuphys.ced.geometry.FTOFGeometry;
import shapes3D.Geometry3D;

import com.jme3.scene.Node;

public class FTOFPaddle3DImp2 {

	// one based sector [1..6]
	private final int _sector;

	// "superlayer" [PANEL_1A, PANEL_1B, PANEL_2] (0, 1, 2)
	private final int _superLayer;

	// 1 -based paddle Id
	private final int _paddleId;

	// the cached vertices
	private float[] _coords = new float[24];

	// frame the paddle?
	private static boolean _frame = true;

	/**
	 * @param sector     1-based sector
	 * @param superLayer the "superlayer" [PANEL_1A, PANEL_1B, PANEL_2] (0, 1, 2)
	 * @param paddleId   1-based paddle Id
	 */
	public FTOFPaddle3DImp2(int sector, int superLayer, int paddleId) {
		_sector = sector;
		_superLayer = superLayer;
		_paddleId = paddleId;

		// get and cache the vertices
		FTOFGeometry.paddleVertices(sector, superLayer, paddleId, _coords);
	}

	/**
	 * Get the sector [1..6]
	 * 
	 * @return the sector 1..6
	 */
	public int getSector() {
		return _sector;
	}

	/**
	 * Get the superlayer [PANEL_1A, PANEL_1B, PANEL_2] (0, 1, 2)
	 * 
	 * @return the superlayer [PANEL_1A, PANEL_1B, PANEL_2] (0, 1, 2)
	 */
	public int getSuperLayer() {
		return _superLayer;
	}

	/**
	 * Get the 1-based paddleId 1..
	 * 
	 * @return the paddle Id
	 */
	public int getPaddleId() {
		return _paddleId;
	}

	/**
	 * Draw the paddle
	 * 
	 * @param drawable the drawable
	 * @param color    the color
	 */
	protected Geometry3D[] drawPaddle(Node root, Color color) {
		Geometry3D[] geometries = new Geometry3D[6];
		geometries[0] = Support3DOther.drawQuad(root, _coords, 0, 1, 2, 3, color, 1f, _frame);
		geometries[1] = Support3DOther.drawQuad(root, _coords, 3, 7, 6, 2, color, 1f, _frame);
		geometries[2] = Support3DOther.drawQuad(root, _coords, 0, 4, 7, 3, color, 1f, _frame);
		geometries[3] = Support3DOther.drawQuad(root, _coords, 0, 4, 5, 1, color, 1f, _frame);
		geometries[4] = Support3DOther.drawQuad(root, _coords, 1, 5, 6, 2, color, 1f, _frame);
		geometries[5] = Support3DOther.drawQuad(root, _coords, 4, 5, 6, 7, color, 1f, _frame);
		return geometries;
	}

}
