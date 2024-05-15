package detector.items;

import java.awt.Color;
import javax.swing.event.ChangeEvent;

import bCNU3D.Support3DOther;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.event.data.DC;
import cnuphys.ced.event.data.DCTdcHit;
import cnuphys.ced.event.data.DCTdcHitList;
import cnuphys.ced.geometry.DCGeometry;
import detector.items.JMonkey3DOverrides.Geometry3DBatchFactory;
import shapes3D.Geometry3D;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;

public class DCSuperLayer3DImp2 extends DetectorItem3DImp2 {

	protected static final Color docaColor = new Color(255, 0, 0, 64);

	private static final boolean frame = true;

	// one based sector [1..6]
	private final int _sector;

	// one based superlayer [1..6]
	private final int _superLayer;

	// the vertices
	public float[] coords = new float[18];

	/**
	 * The owner panel
	 * 
	 * @param panel3d
	 * @param sector     one based sector [1..6]
	 * @param superLayer one based superlayer [1..6]
	 */
	public DCSuperLayer3DImp2(Node root, CedPanel3DImp2 panel3D, int sector, int superLayer) {
		super(panel3D);
		_sector = sector;
		_superLayer = superLayer;
		_geometries = new Geometry3D[5];
		_colorFill = X11Colors.getX11Color("wheat", getVolumeAlpha());
		DCGeometry.superLayerVertices(_sector, _superLayer, coords);
		_parentNode = new Node();

		createShape(_parentNode);
		//createData(_parentNode);
		Geometry3DBatchFactory.optimize(_parentNode);
		_parentNode.setQueueBucket(Bucket.Transparent);
		root.attachChild(_parentNode);
	}

	public void createShape(Node node) {
		Color outlineColor = X11Colors.getX11Color("wheat", getVolumeAlpha());
		_geometries[0] = Support3DOther.drawTriangle(node, coords, 0, 1, 2, outlineColor, 1f, frame);
		_geometries[1] = Support3DOther.drawQuad(node, coords, 1, 4, 3, 0, outlineColor, 1f, frame);
		_geometries[2] = Support3DOther.drawQuad(node, coords, 0, 3, 5, 2, outlineColor, 1f, frame);
		_geometries[3] = Support3DOther.drawQuad(node, coords, 1, 4, 5, 2, outlineColor, 1f, frame);
		_geometries[4] = Support3DOther.drawTriangle(node, coords, 3, 4, 5, outlineColor, 1f, frame);
	}

	public void updateData(Node node, float tpf) {

		float coords[] = new float[6];

		DCTdcHitList hits = DC.getInstance().getTDCHits();
		if ((hits != null) && !hits.isEmpty()) {
			for (DCTdcHit hit : hits) {
				if ((hit.sector == _sector) && (hit.superlayer == _superLayer)) {
					getWire(hit.layer6, hit.wire, coords);
					_tempGeometries.add(Support3DOther.drawLine(node, coords, dgtzColor, WIRELINEWIDTH));
				}
			}
		}
		createTBData(node);
	}

	private void createTBData(Node root) {
	}

	/**
	 * Get the 1-based sector [1..6]
	 * 
	 * @return the 1-based sector [1..6]
	 */
	public int getSector() {
		return _sector;
	}

	/**
	 * Get the 1-based super layer [1..6]
	 * 
	 * @return the 1-based super layer [1..6]
	 */
	public int getSuperLayer() {
		return _superLayer;
	}

	private void getWire(int layer, int wire, float coords[]) {
		org.jlab.geom.prim.Line3D dcwire = DCGeometry.getWire(_sector, _superLayer, layer, wire);
		org.jlab.geom.prim.Point3D p0 = dcwire.origin();
		org.jlab.geom.prim.Point3D p1 = dcwire.end();
		coords[0] = (float) p0.x();
		coords[1] = (float) p0.y();
		coords[2] = (float) p0.z();
		coords[3] = (float) p1.x();
		coords[4] = (float) p1.y();
		coords[5] = (float) p1.z();
	}

	// show DCs?
	@Override
	protected boolean show() {
		boolean showdc = _cedPanel3D.showDC();
		return showdc && _cedPanel3D.showSector(_sector);
	}
	
	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		_colorFill = X11Colors.getX11Color("wheat", getVolumeAlpha());
		super.handleSliderInterrupt(e);
	}

}
