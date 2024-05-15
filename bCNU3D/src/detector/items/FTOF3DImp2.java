package detector.items;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;

import cnuphys.lund.X11Colors;
import detector.items.JMonkey3DOverrides.Geometry3DBatchFactory;
import shapes3D.Geometry3D;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;

public class FTOF3DImp2 extends DetectorItem3DImp2 {

	// private int drawList = -1;

	// individual paddles indices are PANEL_1A, PANEL_1B, PANEL_2 (0,1,2)
	// and are child items
	private FTOFPanel3DImp2 _panels[];
	private ArrayList<Geometry3D> geometriesComplex = new ArrayList<Geometry3D>();

	// one based sector [1..6]
	private final int _sector;

	/**
	 * The 3D FTOF
	 * 
	 * @param panel3d the 3D panel owner
	 * @param sector  the 1-based sector [1..6]
	 */
	public FTOF3DImp2(Node root, CedPanel3DImp2 panel3d, int sector) {
		super(panel3d);
		_sector = sector;
		_geometries = new Geometry3D[0];
		_colorFill = X11Colors.getX11Color("Light Sky Blue", getVolumeAlpha());
		// add the three panels as child items
		_panels = new FTOFPanel3DImp2[3];
		for (int panelId = 0; panelId < 3; panelId++) {
			_panels[panelId] = new FTOFPanel3DImp2(panel3d, sector, panelId);
			addChild(_panels[panelId]);
		}

		_parentNode = new Node();
		_parentNode.setQueueBucket(Bucket.Transparent);
		createShape(_parentNode);
		Geometry3DBatchFactory.optimize(_parentNode);
		root.attachChild(_parentNode);
	}

	@Override
	public void createShape(Node node) {
		//GeometryBatchFactory.optimize(root);
		Geometry3D[] geometriesTemp = new Geometry3D[0];
		Color outlineColor = X11Colors.getX11Color("Light Sky Blue", getVolumeAlpha());
		for (FTOFPanel3DImp2 panel : _panels) {
			for (int paddleId = 1; paddleId <= panel.getPaddleCount(); paddleId++) {
				geometriesTemp = panel.getPaddle(paddleId).drawPaddle(node, outlineColor);
				for (Geometry3D geo : geometriesTemp) {
					geometriesComplex.add(geo);
				}
			}
		}
		_geometries = new Geometry3D[geometriesComplex.size()];
		int index = 0;
		for (Geometry3D geoTemp : geometriesComplex) {
			_geometries[index] = geoTemp;
			index++;
		}

		// gl.glCallList(drawList);

		// if (drawList < 0) {
		// System.err.println("Creating drawlist for FTOF sector " + _sector);
		// drawList = gl.glGenLists(1);
		// gl.glNewList(drawList, GL2.GL_COMPILE);
		// for (FTOFPanel3D panel : _panels) {
		// for (int paddleId = 1; paddleId <= panel.getPaddleCount();
		// paddleId++) {
		// panel.getPaddle(paddleId)
		// .drawPaddle(drawable, outlineColor);
		// }
		// }
		// gl.glEndList();
		// }
		//
		// gl.glCallList(drawList);

	}

	/**
	 * Create a set of same color and size points for use on a Panel3D.
	 * 
	 * @param panel3D   the owner 3D panel
	 * @param coords    the points as [x1, y1, z1, ..., xn, yn, zn]
	 * @param color     the color of the points
	 * @param pointSize the drawing size of the points
	 */
//	public PointSet3D(Panel3D panel3D, float[] coords, Color color,
//			float pointSize, boolean circular) {

	@Override
	public void updateData(Node root, float tpf) {

//		// arggh this sector array is zero based
//		int sector[] = FTOF.getInstance().reconSector();
//		if (sector == null) {
//			return;
//		}
//		float recX[] = FTOF.getInstance().reconX();
//		float recY[] = FTOF.getInstance().reconY();
//		float recZ[] = FTOF.getInstance().reconZ();
//
//		int numHits = sector.length;
//		// System.err.println("FTOF DRAWDATA NUM HITS: " + numHits);
//
//		float xyz[] = new float[3];
//		for (int i = 0; i < numHits; i++) {
//			if (_sector == sector[i]) {
//				xyz[0] = recX[i];
//				xyz[1] = recY[i];
//				xyz[2] = recZ[i];
//
//				 Support3D.drawPoints(drawable, xyz, Color.cyan, Color.black,
//				 10, true);
//
//			}
//		}
	}

	// show FTOFs?
	@Override
	protected boolean show() {
		boolean showtof = _cedPanel3D.showFTOF();
		return showtof && _cedPanel3D.showSector(_sector);
	}

	/**
	 * Get the sector [1..6]
	 * 
	 * @return the sector 1..6
	 */
	public int getSector() {
		return _sector;
	}
	
	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		_colorFill = X11Colors.getX11Color("Light Sky Blue", getVolumeAlpha());
		super.handleSliderInterrupt(e);
	}

}
