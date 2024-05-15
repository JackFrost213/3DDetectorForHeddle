package detector.items;

import java.awt.Color;

import com.jme3.scene.Node;
import bCNU3D.Support3DOther;
import shapes3D.Geometry3D;

public abstract class Paddle3DImp2 {

	// the cached vertices. 3 (xyz) times 8 corners = 24
	protected float[] _coords = new float[24];

	// frame the slab (paddle)?
	protected boolean _frame = true;

	// 1 based layer ID
	protected final int _layerId;

	// 1-based ID
	protected final int _paddleId;

	public Paddle3DImp2(int paddleId) {
		this(1, paddleId);
	}

	public Paddle3DImp2(int layer, int paddleId) {
		_layerId = layer;
		_paddleId = paddleId;
		fillVertices();
	}

	// fill the coords
	protected abstract void fillVertices();

	/**
	 * Set whether we frame the paddle
	 * 
	 * @param frame the new frame flag
	 */
	public void setFrame(boolean frame) {
		_frame = frame;
	}

	/**
	 * Draw the paddle
	 * 
	 * @param drawable the drawable
	 * @param color    the color
	 */
	protected Geometry3D[] drawPaddle(Node node, Color color) {
		Geometry3D[] geometries = new Geometry3D[6];
		// think in terms of a rectangular slab
		geometries[0] = Support3DOther.drawQuad(node, _coords, 0, 1, 2, 3, color, 1f, _frame); // front
		geometries[1] = Support3DOther.drawQuad(node, _coords, 3, 7, 6, 2, color, 1f, _frame); // bottom
		geometries[2] = Support3DOther.drawQuad(node, _coords, 0, 4, 7, 3, color, 1f, _frame); // left
		geometries[3] = Support3DOther.drawQuad(node, _coords, 0, 4, 5, 1, color, 1f, _frame); // top
		geometries[4] = Support3DOther.drawQuad(node, _coords, 1, 5, 6, 2, color, 1f, _frame); // right
		geometries[5] = Support3DOther.drawQuad(node, _coords, 4, 5, 6, 7, color, 1f, _frame); // back
		
		return geometries;
	}
}
