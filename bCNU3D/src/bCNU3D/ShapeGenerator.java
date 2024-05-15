package bCNU3D;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import shapes3D.Geometry3D;
import shapes3D.Line;
import shapes3D.Points;
import shapes3D.Pyramid;
import shapes3D.QuadShape;
import shapes3D.Triangle;

import java.awt.Font;
import java.nio.FloatBuffer;
import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

public class ShapeGenerator {

	private static AssetManager assetManager = Panel3DOther.assetManagerExternal;

	public static Geometry3D createTriangle(Vector3f position, Vector3f size, Quaternion rotation, ColorRGBA color) {
		Triangle t = new Triangle();
		Geometry3D geom = new Geometry3D("Triangle", t);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setLocalTranslation(position);
		geom.setLocalRotation(rotation);
		geom.setLocalScale(size);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			geom.setQueueBucket(Bucket.Transparent);
			// mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		}
		geom.setMaterial(mat);
		geom.updateModelBound();

		return geom;

	}

	public static Geometry3D createCube(Vector3f position, Vector3f size, Quaternion rotation, ColorRGBA color) {
		Box b = new Box(1, 1, 1);
		Geometry3D geom = new Geometry3D("Box", b);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setLocalScale(size);
		geom.setLocalTranslation(position);
		geom.setLocalRotation(rotation);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		return geom;

	}

	public static Geometry3D createOutline(Geometry g, ColorRGBA color, float lineWidth) {
		// Shallow Clone, so outline shares location and scale properties with parent
		// geometry
		Geometry3D geom = (Geometry3D) g.deepClone();
		geom.setName("Outline");
		Material mat = geom.getMaterial();
		if (color != null)
			mat.setColor("Color", color);
		else
			mat.setColor("Color", g.getMaterial().getParamValue("Color"));
		
		if(geom.getMesh().getClass().getName().contains("QuadShape")) {;
			QuadShape prevMesh = (QuadShape) geom.getMesh();
			geom.setMesh(new QuadShape(prevMesh.lowerLeft,prevMesh.lowerRight,prevMesh.upperLeft,prevMesh.upperRight,true));
		}else {
			mat.getAdditionalRenderState().setWireframe(true);
		}
		mat.getAdditionalRenderState().setLineWidth(lineWidth);
		geom.setQueueBucket(g.getQueueBucket());
		return geom;

	}

	public static Geometry3D createLine(Vector3f start, Vector3f end, ColorRGBA color) {
		Line l = new Line(start, end);
		Geometry3D geom = new Geometry3D("Line", l);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		//geom.setQueueBucket(Bucket.Translucent);
		geom.setQueueBucket(Bucket.Opaque);
		geom.setMaterial(mat);
		geom.updateModelBound();
		return geom;

	}

	public static Geometry3D createPyramid(Vector3f position, Vector3f size, Quaternion rotation, ColorRGBA color) {
		Pyramid p = new Pyramid();
		Geometry3D geom = new Geometry3D("Pyramid", p);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setLocalScale(size);
		geom.setLocalTranslation(position);
		geom.setLocalRotation(rotation);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		return geom;
	}

	public static Geometry3D createSphere(Vector3f position, float radius, ColorRGBA color) {
		Sphere s = new Sphere(16, 16, 1);
		Geometry3D geom = new Geometry3D("Sphere", s);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setLocalScale(radius);
		geom.setLocalTranslation(position);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		return geom;
	}

	public static Geometry3D createPoints(Vector3f[] positions, float radius, ColorRGBA color) {
		Points s = new Points(positions, color);
		Geometry3D geom = new Geometry3D("Points", s);
		//geom.setShadowMode(ShadowMode.Off);
        geom.setQueueBucket(Bucket.Opaque);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		// mat.getAdditionalRenderState().setLineWidth(radius);
		return geom;
	}

	public static Geometry3D createCircle(Vector3f position, float radius, ColorRGBA color) {
		Quad q = new Quad(5,5);
		Geometry3D geom = new Geometry3D("Square", q);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		Texture texture = assetManager.loadTexture("Textures/CiricleText.png");
		texture.setWrap(Texture.WrapMode.Repeat);
		mat.setTexture("ColorMap", texture);
		geom.setLocalScale(radius);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		geom.setLocalTranslation(position);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
			//mat.getAdditionalRenderState().setDepthWrite(false);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		return geom;
	}
	
	public static Node generatePointCloud(FloatBuffer pointCoordinates3d, FloatBuffer colorsRGBA, FloatBuffer sizes) {
		Node result = new Node();

		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		// mat.getAdditionalRenderState()
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Off);
		mat.setBoolean("PointSprite", true);
		mat.setFloat("Quadratic", 0.25f);

		Mesh m = new Mesh();
		m.setMode(Mode.Points);
		m.setBuffer(VertexBuffer.Type.Position, 3, pointCoordinates3d);
		m.setBuffer(VertexBuffer.Type.Color, 4, colorsRGBA);
		m.setBuffer(VertexBuffer.Type.Size, 1, sizes);
		m.setStatic();
		m.updateBound();

		Geometry3D g = new Geometry3D("Point Cloud", m);
		g.setShadowMode(ShadowMode.Off);
		g.setQueueBucket(Bucket.Transparent);
		g.setMaterial(mat);
		g.updateModelBound();

		result.attachChild(g);
		result.updateModelBound();
		return result;
	}
	
	public static BitmapText createString(String s, Vector3f position, float size, ColorRGBA color) {
		BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		fixFont(guiFont);
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize());
		ch.setText(s);
		ch.setColor(color);
		ch.setLocalTranslation(position);
		ch.setLocalScale(size);
		ch.setQueueBucket(Bucket.Transparent);
		//ch.setQueueBucket(Bucket.Translucent);
		return ch;

	}
	
	public static BitmapText createString(String s, Vector3f position, float size, ColorRGBA color, Font font) {
		BitmapFont guiFont = assetManager.loadFont("Fonts/" + font.getFontName()  + "." + font.getSize() + ".fnt");
		fixFont(guiFont);
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize());
		ch.setText(s);
		ch.setColor(color);
		ch.setLocalTranslation(position);
		ch.setLocalScale(size);
		ch.setQueueBucket(Bucket.Transparent);

		return ch;

	}

	public static void fixFont( BitmapFont font ) {
        for( int i = 0; i < font.getPageSize(); i++ ) {
            Material m = font.getPage(i);
            m.setFloat("AlphaDiscardThreshold", 0.1f);
        }
    }
	
	public static Geometry3D createCylinder(Vector3f position, Vector3f size, Quaternion rotation, ColorRGBA color) {
		Cylinder c = new Cylinder(2, 16, 1, 1, true, false);
		Geometry3D geom = new Geometry3D("Cylinder", c);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setLocalScale(size);
		geom.setLocalTranslation(position);
		geom.setLocalRotation(rotation);
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			geom.setQueueBucket(Bucket.Transparent);
			mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		return geom;

	}

	public static Vector3f MIN = new Vector3f(-4.675392f, -3.287754f, 0.0f);
	public static Vector3f MAX = new Vector3f(5.847956f, 2.126556f, 19.251f);
	public static Vector3f CENTER = MAX.add(MIN).mult(0.5f);
	public static Vector3f DELTA = MAX.subtract(CENTER);
	
	public static float[] generatePoints(int numberOfPoints) {
		float[] result = new float[3 * numberOfPoints];
		Random random = new Random();
		for (int i = 0; i < numberOfPoints; i++) {
			result[i * 3] = CENTER.x + DELTA.x * (random.nextFloat() - random.nextFloat());
			result[i * 3 + 1] = CENTER.y + DELTA.y * (random.nextFloat() - random.nextFloat());
			result[i * 3 + 2] = CENTER.z + DELTA.z * (random.nextFloat() - random.nextFloat());
		}
		return result;
	}

	public static float[] generateColors(int numberOfPoints) {
		float[] result = new float[4 * numberOfPoints];
		Random random = new Random();
		for (int i = 0; i < numberOfPoints; i++) {
			result[i * 4] = random.nextFloat();
			result[i * 4 + 1] = 0.5f + 0.5f * random.nextFloat();
			result[i * 4 + 2] = random.nextFloat();
			result[i * 4 + 3] = 1.0f;
		}
		return result;
	}

	public static void setAssetManager(AssetManager assetMan) {
		assetManager = assetMan;
	}
	
}
