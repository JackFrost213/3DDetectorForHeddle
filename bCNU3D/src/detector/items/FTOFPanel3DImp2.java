package detector.items;

import com.jme3.scene.Node;
import cnuphys.ced.event.data.FTOF;
import cnuphys.ced.event.data.TdcAdcHit;
import cnuphys.ced.event.data.TdcAdcHitList;
import cnuphys.ced.geometry.FTOFGeometry;

public class FTOFPanel3DImp2 extends DetectorItem3DImp2 {

	// individual paddles
	private FTOFPaddle3DImp2 _paddles[];

	// one based sector [1..6]
	private final int _sector;

	// "superlayer" [PANEL_1A, PANEL_1B, PANEL_2] (0, 1, 2)
	private final int _panelId;

	/**
	 * An FTOF Panel 3D item
	 * 
	 * @param panel3d the owner graphical panel
	 * @param sector  the sector 1..6
	 * @param panelId the super layer [PANEL_1A, PANEL_1B, PANEL_2] (0, 1, 2)
	 */
	public FTOFPanel3DImp2(CedPanel3DImp2 panel3D, int sector, int panelId) {
		super(panel3D);
		_sector = sector;
		_panelId = panelId;

		_paddles = new FTOFPaddle3DImp2[FTOFGeometry.numPaddles[panelId]];
		for (int paddleId = 1; paddleId <= _paddles.length; paddleId++) {
			_paddles[paddleId - 1] = new FTOFPaddle3DImp2(sector, panelId, paddleId);
		}
	}

	/**
	 * Get the number of paddles
	 * 
	 * @return the number of paddles
	 */
	public int getPaddleCount() {
		return _paddles.length;
	}

	/**
	 * Get the paddle
	 * 
	 * @param paddleId the 1-based index
	 * @return the paddle
	 */
	public FTOFPaddle3DImp2 getPaddle(int paddleId) {
		return _paddles[paddleId - 1];
	}

	
	public void createShape(Node root) {
	}

	public void updateData(Node root, float tpf) {

		// draw tdc adc hits
		TdcAdcHitList hits = FTOF.getInstance().getTdcAdcHits();
		if (!hits.isEmpty()) {
			byte layer = (byte) (_panelId + 1);
			for (TdcAdcHit hit : hits) {
				if ((hit.sector == _sector) && (hit.layer == layer)) {
					getPaddle(hit.component).drawPaddle(root, hits.adcColor(hit));
				}
			}
		}

//
//
//		int pid[] = FTOF.getInstance().pid(_superLayer);
//		int sector[] = FTOF.getInstance().sector(_superLayer);
//		int paddleId[] = FTOF.getInstance().paddle(_superLayer);;
//		double x[] = FTOF.getInstance().avgX(_superLayer);
//		double y[] = FTOF.getInstance().avgY(_superLayer);
//		double z[] = FTOF.getInstance().avgZ(_superLayer);
//
//		if (paddleId == null) {
//			Log.getInstance().warning("null paddleId array in FTOFPanel3D");
//			return;
//		}
//
//		for (int i = 0; i < hitCount; i++) {
//			if (sector[i] == _sector) {
//				try {
//					if (showMCTruth() && (pid != null)) {
//						Color color = truthColor(pid, i);
//						getPaddle(paddleId[i]).drawPaddle(drawable, color);
//						// convert mm to cm
//						double xcm = x[i] / 10;
//						double ycm = y[i] / 10;
//						double zcm = z[i] / 10;
//						drawMCPoint(drawable, xcm, ycm, zcm, color);
//
//					} else {
//						getPaddle(paddleId[i]).drawPaddle(drawable, dgtzColor);
//					}
//				} catch (ArrayIndexOutOfBoundsException oob) {
//					System.err.println("sector: " + _sector);
//					System.err.println("superLayer: " + _superLayer);
//					System.err.println("paddleId: " + paddleId[i]);
//					oob.printStackTrace();
//				}
//			}
//		}
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
		return _panelId;
	}

	// show FTOFs?
	@Override
	protected boolean show() {
		return _cedPanel3D.showFTOF();
	}

}
