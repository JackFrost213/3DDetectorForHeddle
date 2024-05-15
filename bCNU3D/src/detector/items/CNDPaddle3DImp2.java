package detector.items;

import cnuphys.ced.geometry.CNDGeometry;

public class CNDPaddle3DImp2 extends Paddle3DImp2 {

	/**
	 * @param layer    the layer [1,2,3]
	 * @param paddleId 1-based paddle Id [1..48]
	 */
	public CNDPaddle3DImp2(int layer, int paddleId) {
		super(layer, paddleId);
	}

	@Override
	protected void fillVertices() {
		CNDGeometry.paddleVertices(_layerId, _paddleId, _coords);
	}

}
