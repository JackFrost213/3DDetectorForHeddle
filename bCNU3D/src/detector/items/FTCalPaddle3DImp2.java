package detector.items;

import java.awt.Color;

import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import bCNU3D.Support3DOther;
import cnuphys.ced.event.data.AdcHit;
import cnuphys.ced.event.data.AdcHitList;
import cnuphys.ced.event.data.CTOF;
import cnuphys.ced.event.data.FTCAL;
import cnuphys.ced.event.data.TdcAdcHit;
import cnuphys.ced.event.data.TdcAdcHitList;
import cnuphys.ced.geometry.FTCALGeometry;
import cnuphys.lund.X11Colors;
import detector.items.JMonkey3DOverrides.Geometry3DBatchFactory;
import shapes3D.Geometry3D;

public class FTCalPaddle3DImp2 extends DetectorItem3DImp2 {

	// paddle ID
	private int _id;

	// the cached vertices
	private float[] _coords = new float[24];

	// frame the paddle?
	private static boolean _frame = true;

	public FTCalPaddle3DImp2(Node root, CedPanel3DImp2 panel3D, int id) {
		super(panel3D);
		_id = id;
		_parentNode = new Node();
		_geometries = null;
		FTCALGeometry.paddleVertices(_id, _coords);
		createShape(_parentNode);
		Geometry3DBatchFactory.optimize(_parentNode);
		_parentNode.setQueueBucket(Bucket.Transparent);
		root.attachChild(_parentNode);
	}

	@Override
	public void createShape(Node node) {
		Color noHitColor = X11Colors.getX11Color("Dodger blue", getVolumeAlpha());
		Color hitColor = X11Colors.getX11Color("red", getVolumeAlpha());

		AdcHitList hits = FTCAL.getInstance().getHits();
		AdcHit hit = null;
		if ((hits != null) && !hits.isEmpty()) {
			hit = hits.get(1, 0, _id);
		}
		_geometries = new Geometry3D[6];
		Color color = (hit == null) ? noHitColor : hitColor;
		_geometries[0] = Support3DOther.drawQuad(node, _coords, 0, 1, 2, 3, color, 1f, _frame);
		_geometries[1] = Support3DOther.drawQuad(node, _coords, 3, 7, 6, 2, color, 1f, _frame);
		_geometries[2] = Support3DOther.drawQuad(node, _coords, 0, 4, 7, 3, color, 1f, _frame);
		_geometries[3] = Support3DOther.drawQuad(node, _coords, 0, 4, 5, 1, color, 1f, _frame);
		_geometries[4] = Support3DOther.drawQuad(node, _coords, 1, 5, 6, 2, color, 1f, _frame);
		_geometries[5] = Support3DOther.drawQuad(node, _coords, 4, 5, 6, 7, color, 1f, _frame);
	}

	@Override
	public void updateData(Node node, float tpf) {
	}

	@Override
	public void updateOpacity() {

		if (this.isVisible()) {
			Color noHitColor = X11Colors.getX11Color("Dodger blue", getVolumeAlpha());
			Color hitColor = X11Colors.getX11Color("red", getVolumeAlpha());
			TdcAdcHitList hits = CTOF.getInstance().getHits();
			TdcAdcHit hit = null;
			if ((hits != null) && !hits.isEmpty()) {
				hit = hits.get(1, 0, _id);
			}
			Color color = hit == null ? noHitColor : hitColor;
			Color darkerColor = color.darker();
			ColorRGBA colorRGBA = Support3DOther.getColorRGBAFrom255AWTColor(color);
			ColorRGBA colorDarker = Support3DOther.getColorRGBAFrom255AWTColor(darkerColor);
			for (int x = 0; x < 6; x++) {
				_geometries[x].getMaterial().setColor("Color", colorRGBA);
				_geometries[x].getOutline().getMaterial().setColor("Color", colorDarker);
			}

			if (_tempGeometries != null) {
				for (Geometry3D geometry : _tempGeometries) {
					if (geometry != null) {
						Color color2 = geometry.getMaterial().getParamValue("Color");
						ColorRGBA colorRGBA2 = Support3DOther.getColorRGBAFrom255AWTColor(color2);
						colorRGBA2.setAlpha(getVolumeAlpha());
						ColorRGBA colorDarker2 = Support3DOther.getColorRGBAFrom255AWTColor(color2.darker());
						colorDarker2.setAlpha(getVolumeAlpha());
						geometry.getMaterial().setColor("Color", colorRGBA2);
						if (geometry.getOutline() != null) {
							geometry.getOutline().getMaterial().setColor("Color", colorDarker2);
						}
					}
				}
			}
			_parentNode.setCullHint(CullHint.Never);
		} else {
			_parentNode.setCullHint(CullHint.Always);
		}
	}

	@Override
	protected boolean show() {
		return true;
	}

}
