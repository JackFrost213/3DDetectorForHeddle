package detector.items;

import cnuphys.ced.geometry.CTOFGeometry;

public class CTOFPaddle3DImp2 extends Paddle3DImp2 {

	/**
	 * @param paddleId 1-based paddle Id [1..48]
	 */
	public CTOFPaddle3DImp2(int paddleId) {
		super(paddleId);
	}

	@Override
	protected void fillVertices() {
		CTOFGeometry.paddleVertices(_paddleId, _coords);
	}
}
