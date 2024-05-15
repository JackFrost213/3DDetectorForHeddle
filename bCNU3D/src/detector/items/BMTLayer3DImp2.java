package detector.items;

import java.awt.Color;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import bCNU3D.Support3DOther;
import cnuphys.ced.event.data.AdcHit;
import cnuphys.ced.event.data.AdcHitList;
import cnuphys.ced.event.data.BMT;
import cnuphys.ced.event.data.BMTCrosses;
import cnuphys.ced.event.data.Cross2;
import cnuphys.ced.event.data.CrossList2;
import cnuphys.ced.geometry.BMTGeometry;
import cnuphys.ced.geometry.bmt.Constants;
import cnuphys.lund.X11Colors;
import detector.items.JMonkey3DOverrides.Geometry3DBatchFactory;
import shapes3D.Geometry3D;

public class BMTLayer3DImp2 extends DetectorItem3DImp2 {

	protected static final Color outlineHitColor = new Color(0, 255, 64, 24);

	protected static final float CROSS_LEN = 3f; // in cm
	protected static final Color crossColor = X11Colors.getX11Color("dark orange");

	// the 1=based sect
	private int _sector;

	// the 1-based layer
	private int _layer;

	public BMTLayer3DImp2(Node node, CedPanel3DImp2 panel3D, int sector, int layer) {
		super(panel3D);
		_sector = sector;
		_layer = layer;
		
		_parentNode = new Node();
		_geometries = null;
		createShape(_parentNode);
		Geometry3DBatchFactory.optimize(_parentNode);
		_parentNode.setQueueBucket(Bucket.Transparent);
		node.attachChild(_parentNode);
	}

	@Override
	public void createShape(Node root) {
		float coords6[] = new float[6];
		int region = (_layer + 1) / 2 - 1; // region index (0...2) 0=layers 1&2, 1=layers 3&4, 2=layers 5&6

		Color color = (BMTGeometry.getGeometry().isZLayer(_layer) ? X11Colors.getX11Color("gray", getVolumeAlpha())
				: X11Colors.getX11Color("light green", getVolumeAlpha()));
		_colorFill = color;
		if (BMTGeometry.getGeometry().isZLayer(_layer)) {
			int numStrips = Constants.getCRZNSTRIPS()[region];
			_geometries = new Geometry3D[numStrips];
			for (int strip = 1; strip <= numStrips; strip++) {
				BMTGeometry.getGeometry().getCRZEndPoints(_sector, _layer, strip, coords6);
				if (!Float.isNaN(coords6[0])) {
					_geometries[strip-1] = Support3DOther.drawLine(root, coords6, color, 2f);
				}
			}
		}
	}

	@Override
	public void updateData(Node root, float tpf) {

		float coords6[] = new float[6];
//		float coords36[] = new float[36];
//		boolean drawOutline = false;

		AdcHitList hits = BMT.getInstance().getADCHits();
		if ((hits != null) && !hits.isEmpty()) {
			for (AdcHit hit : hits) {
				if (hit != null) {
					int sector = hit.sector;
					int layer = hit.layer;
					if ((_sector == sector) && (_layer == layer)) {
//						drawOutline = true;
						int strip = hit.component;

						if (showHits()) {
							if (BMTGeometry.getGeometry().isZLayer(layer)) {
								BMTGeometry.getGeometry().getCRZEndPoints(sector, layer, strip, coords6);

								if (!Float.isNaN(coords6[0])) {
									Support3DOther.drawLine(root, coords6, dgtzColor, STRIPLINEWIDTH);
								}
							}
						}

					} // match sector and layer
				} // hit not null
			} // loop on hits
		} // hits not null

//
//		if (drawOutline) { // if any hits, draw it once
//			BSTGeometry.getLayerQuads(_sector, _layer, coords36);
//			Support3D.drawQuads(drawable, coords36, outlineHitColor, 1f, true);
//		}

		// reconstructed crosses?
		if (_cedPanel3D.showReconCrosses()) {

			// BMT
			CrossList2 crosses = BMTCrosses.getInstance().getCrosses();
			int len = (crosses == null) ? 0 : crosses.size();
			for (int i = 0; i < len; i++) {
				Cross2 cross = crosses.elementAt(i);
				if (cross != null) {
					// no longer convert (already in cm)
					float x1 = cross.x;
					float y1 = cross.y;
					float z1 = cross.z;

					Support3DOther.drawLine(root, x1, y1, z1, cross.ux, cross.uy, cross.uz, CROSS_LEN, crossColor, 3f);
					Support3DOther.drawLine(root, x1, y1, z1, cross.ux, cross.uy, cross.uz, (float) (1.1 * CROSS_LEN),
							Color.black, 1f);

					drawCrossPoint(root, x1, y1, z1, crossColor);
				}
			} // bmt

		}
	}

	// show BMT?
	@Override
	protected boolean show() {
		switch (_layer) {
		case 1:
			return _cedPanel3D.showBMTLayer1();
		case 2:
			return _cedPanel3D.showBMTLayer2();
		case 3:
			return _cedPanel3D.showBMTLayer3();
		case 4:
			return _cedPanel3D.showBMTLayer4();
		case 5:
			return _cedPanel3D.showBMTLayer5();
		case 6:
			return _cedPanel3D.showBMTLayer6();
		}
		return false;
	}

	// show strip hits?
	protected boolean showHits() {
		return show() && _cedPanel3D.showBMTHits();
	}
}
