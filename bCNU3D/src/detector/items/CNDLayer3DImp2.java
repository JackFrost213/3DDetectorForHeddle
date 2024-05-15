package detector.items;

import java.awt.Color;
import cnuphys.lund.X11Colors;
import detector.items.JMonkey3DOverrides.Geometry3DBatchFactory;
import shapes3D.Geometry3D;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;

public class CNDLayer3DImp2 extends DetectorItem3DImp2 {

	// 1-based layer 1..3
	private final int _layer;

	// the paddles
	private CNDPaddle3DImp2 _paddles[];

	public CNDLayer3DImp2(Node node, CedPanel3DImp2 panel3DImp2, int layer) {
		super(panel3DImp2);
		_layer = layer;

		_parentNode = new Node();
		_paddles = new CNDPaddle3DImp2[48];
		for (int paddleId = 1; paddleId <= 48; paddleId++) {
			_paddles[paddleId - 1] = new CNDPaddle3DImp2(layer, paddleId);
		}
		createShape(_parentNode);
		Geometry3DBatchFactory.optimize(_parentNode);
		_parentNode.setQueueBucket(Bucket.Transparent);
		_parentNode.updateModelBound();
		node.attachChild(_parentNode);
	}

	public Geometry3D[] getGeometries(){
		return _geometries;
	}
	
	@Override
	public void createShape(Node node) {
		Color outlineColor = X11Colors.getX11Color("Medium Spring Green", getVolumeAlpha());
		_colorFill = outlineColor;
		_geometries = new Geometry3D[48*6];
		for (int paddleId = 1; paddleId <= 48; paddleId++) {
			Geometry3D[] paddleGeoms = getPaddle(paddleId).drawPaddle(node, outlineColor);
			int x = 0;
			for(Geometry3D temp : paddleGeoms) {
				_geometries[paddleId+x] = temp;
				x++;
			}
		}
	}

	@Override
	public void updateData(Node node, float tpf) {
	}

	@Override
	protected boolean show() {
		switch (_layer) {
		case 1:
			return _cedPanel3D.showCNDLayer1();

		case 2:
			return _cedPanel3D.showCNDLayer2();

		case 3:
			return _cedPanel3D.showCNDLayer3();
		}
		return false;
	}

	/**
	 * Get the 3DImp2 Paddle
	 * 
	 * @param paddleId the paddle Id [..48]
	 * @return the 3DImp2 paddle
	 */
	public CNDPaddle3DImp2 getPaddle(int paddleId) {
		if ((paddleId < 1) || (paddleId > 48)) {
			return null;
		}

		return _paddles[paddleId - 1];
	}
}
