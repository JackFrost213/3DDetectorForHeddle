package shapes3D;

import java.awt.Color;
import java.awt.Font;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import bCNU3D.DoubleFormat;
import bCNU3D.ShapeGenerator;
import bCNU3D.Support3DOther;

public class Axis extends Node {

	private float startMagnitude, endMagnitude, length;
	private float numOfSegments;
	private float deltaX;
	private Vector3f direction;
	private boolean setup;
	private ColorRGBA primaryColor, secondaryColor, wordColor;

	public Axis(Vector3f direction, float length) {
		this(direction, 0.5f, 1, length);
	}

	public Axis(Vector3f direction, float numOfSegments, float deltaX, float length) {
		this(direction, numOfSegments, deltaX, 0, length);
	}

	public Axis(Vector3f direction, float numOfSegments, float deltaX, float startMag, float endMag) {
		this.direction = direction.normalize();
		this.numOfSegments = numOfSegments;
		this.deltaX = deltaX;
		this.startMagnitude = startMag;
		this.endMagnitude = endMag;
		this.primaryColor = ColorRGBA.Black;
		this.secondaryColor = ColorRGBA.Black;
		this.wordColor = ColorRGBA.Black;
		this.length = Math.abs(endMag - startMag);
		setupAxis();
	}

	private void setupAxis() {
		setup = true;
		Geometry axisLine = ShapeGenerator.createLine(direction.mult(startMagnitude), direction.mult(endMagnitude),
				ColorRGBA.Black);
		axisLine.setName("axisLine");
		this.attachChild(axisLine);

		setSegments(direction.mult(numOfSegments));
		setup = false;
	}

	public void update(Camera cam) {
		for (Spatial word : ((Node) this.getChild("axisLabels")).getChildren()) {
			word.lookAt(cam.getLocation(), cam.getUp());
			word.setLocalScale(cam.getLocation().distance(word.getLocalTranslation()) / 800);
		}
	}

	public void setSegments(Vector3f s) {
		if (numOfSegments != s.dot(direction) || setup) {
			numOfSegments = s.dot(direction);

			if (this.getChild("axisMiniLines") != null) {
				this.getChild("axisMiniLines").removeFromParent();
				this.getChild("axisLabels").removeFromParent();
			}

			Node axisMiniLines = new Node("axisMiniLines");
			Node axisLabels = new Node("axisLabels");
			this.attachChild(axisMiniLines);
			this.attachChild(axisLabels);

			float angle = direction.angleBetween(new Vector3f(1, 0, 0));
			Quaternion rotation = new Quaternion();
			Vector3f crossProdVec = direction.cross(new Vector3f(1, 0, 0));
			rotation.fromAngleAxis(-angle, crossProdVec);
			boolean has0BeenCovered = false;
			for (double x = startMagnitude * numOfSegments; x <= endMagnitude * numOfSegments; x++) {
				if(x == 0) {
					has0BeenCovered = true;
				}
				drawMiniLineAt(x,axisMiniLines,axisLabels,rotation);
			}
			
			if(!has0BeenCovered) {
				drawMiniLineAt(0,axisMiniLines,axisLabels,rotation);
			}
		}
	}

	private void drawMiniLineAt(double x, Node axisMiniLines, Node axisLabels, Quaternion rotation) {
		Vector3f start = new Vector3f((float) ((x / numOfSegments)), (float) (-.25), 0);
		Vector3f end = new Vector3f((float) ((x / numOfSegments)), (float) (.25), 0);
		start = rotation.toRotationMatrix().mult(start);
		end = rotation.toRotationMatrix().mult(end);
		Geometry miniLine = ShapeGenerator.createLine(start, end, secondaryColor);
		axisMiniLines.attachChild(miniLine);
		float textVal = (float) ((x * deltaX) / (numOfSegments));
		String text = "";
		if (DoubleFormat.isInteger(textVal)) {
			text = String.valueOf((int) textVal);
		} else {
			textVal = (float) ((double) (Math.round(textVal * 100)) / 100);
			text = String.valueOf(textVal);
		}
		Vector3f position = new Vector3f((float) (end.x), (float) (end.y), (float) (end.z));
		BitmapText word = ShapeGenerator.createString(text, position, 0.025f, wordColor);
		word.setName("Word");
		axisLabels.attachChild(word);
	}
	
	public void setDelta(Vector3f coordinateSize) {
		int counter = 0;
		deltaX = coordinateSize.dot(direction);
		for (Spatial e : ((Node) this.getChild("axisLabels")).getChildren()) {
			BitmapText temp = (BitmapText) e;
			double val = counter;
			
			if(!temp.getText().equals("0")) {
			val = startMagnitude * deltaX + counter * deltaX / (numOfSegments);
			val = (double) (Math.round(val * 100)) / 100;
			if (DoubleFormat.isInteger(val)) {
				temp.setText(String.valueOf((int) val));
			} else {
				temp.setText(String.valueOf(val));
			}
			}
			counter++;
		}
	}

	public void setStartMag(float l) {
		startMagnitude = l;
	}

	public void setFont(Font font) {
		/*List<Spatial> children = ((Node) this.getChild("axisLabels")).getChildren();
		for (int x = 0; x < children.size(); x++) {
			BitmapText word = ((BitmapText) children.get(x));
			BitmapText newWord = ShapeGenerator.createString(word.getText(), word.getLocalTranslation(), 0.025f, word.getColor(), font);
			newWord.setName("Word");
		}*/
		
		//TODO Finish this eventually

		
	}

	public void setColor(Color c1, Color c2) {
		primaryColor = Support3DOther.getColorRGBAFrom255AWTColor(c1);
		((Geometry) this.getChild("axisLine")).getMaterial().setColor("Color", primaryColor);
		secondaryColor = Support3DOther.getColorRGBAFrom255AWTColor(c2);
		for (Spatial miniLines : ((Node) this.getChild("axisMiniLines")).getChildren()) {
			((Geometry)miniLines).getMaterial().setColor("Color", secondaryColor);
		}
	}

	public void setWordColor(Color c) {
		wordColor = Support3DOther.getColorRGBAFrom255AWTColor(c);
		for (Spatial word : ((Node) this.getChild("axisLabels")).getChildren()) {
			((BitmapText) word).setColor(wordColor);
		}
		
	}
}
