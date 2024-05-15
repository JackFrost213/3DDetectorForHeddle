package detector.items;

import java.util.ArrayList;

import com.jme3.scene.Node;
import shapes3D.Geometry3D;

/**
 * 3D version of the central neutron detector
 * 
 * @author heddle
 *
 */
public class CND3DImp2 extends DetectorItem3DImp2 {

	// child layer items
	private CNDLayer3DImp2 _layers[];
	private CedPanel3DImp2 _panel3D;

	/**
	 * The 3D CND
	 * 
	 * @param panel3d the 3D panel owner
	 */
	public CND3DImp2(Node node, CedPanel3DImp2 panel3D) {
		super(panel3D);

		_parentNode = new Node();
		_geometries = null;
		_panel3D = panel3D;
		createShape(_parentNode);
		node.attachChild(_parentNode);
	}

	@Override
	public void createShape(Node node) {
		// add the three layers as child items
		_layers = new CNDLayer3DImp2[3];
		ArrayList<Geometry3D> geometriesTemp = new ArrayList<Geometry3D>();
		for (int layer = 1; layer <= 3; layer++) {
			_layers[layer - 1] = new CNDLayer3DImp2(node, _panel3D, layer);
			addChild(_layers[layer - 1]);
			for(Geometry3D e : _layers[layer - 1].getGeometries()) {
				geometriesTemp.add(e);
			}
		}
		
		_geometries = new Geometry3D[geometriesTemp.size()];
		for(int x = 0; x<geometriesTemp.size();x++) {
			_geometries[x] = geometriesTemp.get(x);
		}
	}

	@Override
	public void updateData(Node node, float tpf) {
		// Children handle drawing

	}

	@Override
	protected boolean show() {
		return _cedPanel3D.showCND();
	}

}
