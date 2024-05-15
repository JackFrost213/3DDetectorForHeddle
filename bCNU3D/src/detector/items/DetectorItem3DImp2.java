package detector.items;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;

import shapes3D.Geometry3D;
import shapes3D.Item3DImp2;
import bCNU3D.Support3DOther;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;

import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

public abstract class DetectorItem3DImp2 extends Item3DImp2 {

	protected static final Color dgtzColor = new Color(255, 0, 0);
	protected static final float MC_POINTSIZE = 3f;
	protected static final float CROSS_POINTSIZE = 5f;
	protected static final Color cosmicColor = Color.lightGray;
	
	protected static final float STRIPLINEWIDTH = 10f;
	protected static final float WIRELINEWIDTH = 3f;

	// the event manager
	ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	protected CedPanel3DImp2 _cedPanel3D;
	protected Color _colorFill;

	public DetectorItem3DImp2(CedPanel3DImp2 panel3D) {
		super(panel3D);
		_cedPanel3D = panel3D;
	}

	/**
	 * Update the boundary
	 * 
	 * @param tpf the time between screen refreshes
	 */
	public abstract void createShape(Node root);

	/**
	 * Update the data
	 * 
	 * @param tpf the time between screen refreshes
	 */
	public abstract void updateData(Node root, float tpf);
	
	@Override
	public void update(float tpf) {
	}
	
	@Override		
	public void updateData() {

		/*if (!show()) {
			return;
		}*/

		//if (_cedPanel3D.showVolumes() && (getVolumeAlpha() > 2)) {
			//createShape(root);
		//}

		if (!_eventManager.isAccumulating()) {
			//#Fix this later
			_tempGeometries = new ArrayList<Geometry3D>();
			if(_parentNode.getChild("TempData") != null) {
				_parentNode.detachChildNamed("TempData");
			}
			Node temp = new Node("TempData");
			updateData(temp, 0);
			temp.setQueueBucket(Bucket.Transparent);
			_parentNode.attachChild(temp);
			_parentNode.updateGeometricState();
		}

	}

	// an overall show
		protected abstract boolean show();
	
	/**
	 * Get the alpha value used for drawing detector outlines
	 * 
	 * @return the alpha used for drawing detector outlines
	 */
	protected int getVolumeAlpha() {
		return ((CedPanel3DImp2) _panel3D).getVolumeAlpha();
	}

	/**
	 * Obtain the MC truth color, which corresponds to the LundId
	 * 
	 * @param pid   the particle lund id array
	 * @param index index into the array
	 * @return the truth color
	 */
	protected static Color truthColor(int pid[], int index) {

		if ((pid == null) || (index < 0) || (index >= pid.length)) {
			return Color.black;
		}

		LundId lid = LundSupport.getInstance().get(pid[index]);
		if (lid == null) {
			return dgtzColor;
		}

		return lid.getStyle().getFillColor();
	}

	/**
	 * Obtain the MC truth color, which corresponds to the LundId
	 * 
	 * @param lundId
	 * @return the truth color
	 */
	protected static Color truthColor(int lundId) {

		LundId lid = LundSupport.getInstance().get(lundId);
		if (lid == null) {
			return dgtzColor;
		}

		return lid.getStyle().getFillColor();
	}

	public void updateOpacity() {
		Color outlineColor;
		if(_colorFill == null)
		   outlineColor = new Color(255,255,255,getVolumeAlpha());
		else
		   outlineColor = new Color(_colorFill.getRed(),_colorFill.getGreen(),_colorFill.getBlue(),getVolumeAlpha());
		
		ColorRGBA colorRGBA = Support3DOther.getColorRGBAFrom255AWTColor(outlineColor);
		ColorRGBA colorDarker = Support3DOther.getColorRGBAFrom255AWTColor(outlineColor.darker());
		if (this.isVisible() && _geometries != null) {
			for (Geometry3D geometry : _geometries) {
				if(geometry != null) {
					geometry.getMaterial().setColor("Color", colorRGBA);
					if(geometry.getOutline() != null) {
						geometry.getOutline().getMaterial().setColor("Color", colorDarker);
					}
				}
			}
			
			if(_tempGeometries != null) {
			 for (Geometry3D geometry : _tempGeometries) {
				if(geometry != null) {
					Color color = geometry.getMaterial().getParamValue("Color");
					ColorRGBA colorRGBA2 = Support3DOther.getColorRGBAFrom255AWTColor(color);
					colorRGBA2.setAlpha(getVolumeAlpha());
					ColorRGBA colorDarker2 = Support3DOther.getColorRGBAFrom255AWTColor(color.darker());
					colorDarker2.setAlpha(getVolumeAlpha());
					geometry.getMaterial().setColor("Color", colorRGBA2);
					if(geometry.getOutline() != null) {
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
	public void handleSliderInterrupt(ChangeEvent e) {
		updateOpacity();
	}
	
	@Override
	public void handleCheckBoxInterrupt(ActionEvent e) {
		if(!show() || !_cedPanel3D.showVolumes()) {
			this.setVisible(false);
			
		}
		else {
			this.setVisible(true);
		}
		updateOpacity();	
	}

	
	/**
	 * Draw a MC 3D point
	 * 
	 * @param drawable
	 * @param xcm
	 * @param ycm
	 * @param zcm
	 * @param truthColor
	 */
	protected void drawMCPoint(Node root, double xcm, double ycm, double zcm, Color truthColor) {
		_tempGeometries.add(Support3DOther.drawPoint(root, xcm, ycm, zcm, Color.black, MC_POINTSIZE + 2, true));
		_tempGeometries.add(Support3DOther.drawPoint(root, xcm, ycm, zcm, truthColor, MC_POINTSIZE, true));

	}

	protected void drawCrossPoint(Node root, double xcm, double ycm, double zcm, Color crossColor) {
		_tempGeometries.add(Support3DOther.drawPoint(root, xcm, ycm, zcm, Color.red, CROSS_POINTSIZE + 2, true));
		_tempGeometries.add(Support3DOther.drawPoint(root, xcm, ycm, zcm, crossColor, CROSS_POINTSIZE, true));

	}

}
