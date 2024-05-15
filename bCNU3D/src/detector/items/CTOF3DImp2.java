package detector.items;

import java.awt.Color;
import java.util.ArrayList;

import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import bCNU3D.Support3DOther;
import cnuphys.ced.event.data.CTOF;
import cnuphys.ced.event.data.TdcAdcHit;
import cnuphys.ced.event.data.TdcAdcHitList;
import cnuphys.lund.X11Colors;
import detector.items.JMonkey3DOverrides.Geometry3DBatchFactory;
import shapes3D.Geometry3D;

public class CTOF3DImp2 extends DetectorItem3DImp2 {

	// child layer items
	private CTOFPaddle3DImp2 _paddles[];

	/**
	 * The 3D CND
	 * 
	 * @param panel3d the 3D panel owner
	 */
	public CTOF3DImp2(Node node, CedPanel3DImp2 panel3D) {
		super(panel3D);

		_parentNode = new Node();
		_geometries = null;
		_panel3D = panel3D;
		_paddles = new CTOFPaddle3DImp2[48];
		for (int paddleId = 1; paddleId <= 48; paddleId++) {
			_paddles[paddleId - 1] = new CTOFPaddle3DImp2(paddleId);
		}
		createShape(_parentNode);
		Geometry3DBatchFactory.optimize(_parentNode);
		_parentNode.setQueueBucket(Bucket.Transparent);
		_parentNode.updateModelBound();
		node.attachChild(_parentNode);

	}

	@Override
	public void createShape(Node node) {

		Color noHitColor = X11Colors.getX11Color("Dodger blue", getVolumeAlpha());
		Color hitColor = X11Colors.getX11Color("red", getVolumeAlpha());
		TdcAdcHitList hits = CTOF.getInstance().getHits();
		_colorFill = noHitColor;
		ArrayList<Geometry3D> geometriesTemp = new ArrayList<Geometry3D>();
		for (int paddleId = 1; paddleId <= 48; paddleId++) {

			TdcAdcHit hit = null;
			if ((hits != null) && !hits.isEmpty()) {
				hit = hits.get(0, 0, paddleId);
			}

			Geometry3D[] temp = _paddles[paddleId - 1].drawPaddle(node, hit == null ? noHitColor : hitColor);
			for (Geometry3D e : temp) {
				geometriesTemp.add(e);
			}

		}

		_geometries = new Geometry3D[geometriesTemp.size()];
		for (int x = 0; x < geometriesTemp.size(); x++) {
			_geometries[x] = geometriesTemp.get(x);
		}

	}

	@Override
	public void updateOpacity() {

		if (this.isVisible()) {
			Color noHitColor = X11Colors.getX11Color("Dodger blue", getVolumeAlpha());
			Color hitColor = X11Colors.getX11Color("red", getVolumeAlpha());
			TdcAdcHitList hits = CTOF.getInstance().getHits();
			TdcAdcHit hit = null;
			for (int paddleId = 1; paddleId <= 48; paddleId++) {
				if ((hits != null) && !hits.isEmpty()) {
					hit = hits.get(0, 0, paddleId);
				}
					Color color = hit == null ? noHitColor : hitColor;
					Color darkerColor = color.darker();
					ColorRGBA colorRGBA = Support3DOther.getColorRGBAFrom255AWTColor(color);
					ColorRGBA colorDarker = Support3DOther.getColorRGBAFrom255AWTColor(darkerColor);
					for (int x = 0; x < 6; x++) {
						_geometries[paddleId * x].getMaterial().setColor("Color", colorRGBA);
						_geometries[paddleId * x].getOutline().getMaterial().setColor("Color", colorDarker);
					}
				
			}
			if (_tempGeometries != null) {
				for (Geometry3D geometry : _tempGeometries) {
					if (geometry != null) {
						Color color = geometry.getMaterial().getParamValue("Color");
						ColorRGBA colorRGBA2 = Support3DOther.getColorRGBAFrom255AWTColor(color);
						colorRGBA2.setAlpha(getVolumeAlpha());
						ColorRGBA colorDarker2 = Support3DOther.getColorRGBAFrom255AWTColor(color.darker());
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
	public void updateData(Node node, float tpf) {
		// Children handle drawing

	}

	@Override
	protected boolean show() {
		return _cedPanel3D.showCTOF();
	}

}
