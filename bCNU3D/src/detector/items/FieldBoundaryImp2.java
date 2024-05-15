package detector.items;

import java.awt.Color;
import java.util.ArrayList;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import bCNU3D.Panel3D;
import bCNU3D.ShapeGenerator;
import bCNU3D.Support3DOther;
import cnuphys.bCNU.graphics.colorscale.ColorScaleModel;
import cnuphys.ced.item.MagFieldItem;
import cnuphys.magfield.FieldProbe;
import cnuphys.magfield.MagneticField;
import cnuphys.magfield.MagneticFieldChangeListener;
import cnuphys.magfield.MagneticFields;
import cnuphys.magfield.Torus;
import item3D.CylinderImp2;
import shapes3D.Geometry3D;

public class FieldBoundaryImp2 extends CylinderImp2 implements MagneticFieldChangeListener {

	private CedPanel3DImp2 _cedPanel;
	private MagneticField _field;
	private ColorScaleModel _model;
	private boolean currentlyDrawn;

	public FieldBoundaryImp2(Node root, Panel3D panel3D, MagneticField field, Color color) {
		super(panel3D, getBoundaryData(field), color);
		_field = field;
		_cedPanel = (CedPanel3DImp2) panel3D;
		_parentNode = new Node();
		_parentNode.setQueueBucket(Bucket.Transparent);
		currentlyDrawn = false;
		root.attachChild(_parentNode);
		MagneticFields.getInstance().addMagneticFieldChangeListener(this);

		if (_field instanceof Torus) {
			_model = MagFieldItem._colorScaleModelTorus;
		} else {
			_model = MagFieldItem._colorScaleModelSolenoid;
		}

	}

	private static float[] getBoundaryData(MagneticField field) {

		float zmin = (float) (field.getZCoordinate().getMin());
		float zmax = (float) (field.getZCoordinate().getMax());
		float radius = (float) (field.getRCoordinate().getMax());

		float x1 = 0f;
		float y1 = 0f;
		float z1 = zmin;

		float x2 = 0f;
		float y2 = 0f;
		float z2 = zmax;

		// TODO add offsets

		float dx = (float) (field.getShiftX());
		float dy = (float) (field.getShiftY());
		float dz = (float) (field.getShiftZ());

		x1 += dx;
		y1 += dy;
		z1 += dz;

		x2 += dx;
		y2 += dy;
		z2 += dz;

		float data[] = { x1, y1, z1, x2, y2, z2, radius };
		return data;
	}

	private void drawMapPoints(Node root) {
//		public PointSet3D(Panel3D panel3D, float[] coords, Color color,
//				float pointSize, boolean circular) {

		FieldProbe probe = FieldProbe.factory(_field);

		float zmin = (float) (_field.getZCoordinate().getMin());
		float zmax = (float) (_field.getZCoordinate().getMax());
		float radius = (float) (_field.getRCoordinate().getMax());

		float radsq = radius * radius;

		float step = 10; // cm
		float x1 = -radius;
		float y1 = -radius;
		float z1 = zmin;
		float x2 = radius; // radius
		float y2 = radius; // radius
		float z2 = zmax;

		float dx = (float) (_field.getShiftX());
		float dy = (float) (_field.getShiftY());
		float dz = (float) (_field.getShiftZ());

		float x = x1;

		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		ArrayList<ColorRGBA> colors = new ArrayList<ColorRGBA>();

		double bmax = -1;
		int count = 0;
		while (x < (x2 + 0.001)) {
			float y = y1;
			while (y < (y2 + 0.001)) {

				if ((x * x + y * y) < radsq) {

					float z = z1;
					while (z < (z2 + 0.001)) {

						double bmag = probe.fieldMagnitude(x + dx, y + dy, z + dz) / 10;

						bmax = Math.max(bmax, bmag);

						Color color = _model.getColor(bmag);

						int alpha = 24 + 5 * ((int) bmag);

						Color acolor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);

						if (!_model.isTooSmall(color)) {
							count++;
							System.out.println(count);
							// System.out.println("draw at [" + x + ", " + y + ", " + z + "] color: " +
							// color);
							positions.add(new Vector3f(x, y, z));
							colors.add(Support3DOther.getColorRGBAFrom255AWTColor(acolor));
						}
						z += step;
					}
				}
				y += step;
			}
			x += step;
		}

		Vector3f[] positionsN = new Vector3f[positions.size()];
		ColorRGBA[] colorsN = new ColorRGBA[colors.size()];
		float[] sizes = new float[colors.size()];
		for (int g = 0; g < sizes.length; g++) {
			sizes[g] = 1;
			positionsN[g] = positions.get(g);
			colorsN[g] = new ColorRGBA(colors.get(g).getRed(),colors.get(g).getBlue()/2,colors.get(g).getGreen()/2,colors.get(g).getAlpha()*3);
		}
		//Node points = ShapeGenerator.generatePointCloud(BufferUtils.createFloatBuffer(positionsN),
				//BufferUtils.createFloatBuffer(colorsN), BufferUtils.createFloatBuffer(sizes));
		Geometry3D points = ShapeGenerator.createPoints(positionsN, radius, colorsN[0]);
		root.attachChild(points);
	}

	@Override
	public void update(float tpf) {
		if (!_cedPanel.showMapExtents() && currentlyDrawn) {
			_parentNode.detachAllChildren();
			System.out.println("DELETED");
			currentlyDrawn = false;
		}

		if (_cedPanel.showMapExtents() && !currentlyDrawn) {
			_parentNode.detachAllChildren();
			currentlyDrawn = true;
			//drawMapPoints(_parentNode);
			super.draw(_parentNode);
		}
	}

	@Override
	public void magneticFieldChanged() {
		float data[] = getBoundaryData(_field);
		reset(data[0], data[1], data[2], data[3], data[4], data[5]);
		_cedPanel.refreshQueued();
	}

}
