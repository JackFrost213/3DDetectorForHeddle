package bCNU3D;

import java.awt.Color;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jogamp.graph.geom.SVertex;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import shapes3D.Geometry3D;
import shapes3D.Line;
import shapes3D.QuadShape;
import shapes3D.Triangle;

import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class Support3DOther {

	public static GLUT glut = new GLUT();

	private static final byte byteA = (byte) 170;
	private static final byte byteB = (byte) 170;
	private static AssetManager assetManager = Panel3DOther.assetManagerExternal;

	/* a stipple pattern */
	public static byte sd[] = { byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA,
			byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB,
			byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA,
			byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB,
			byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA,
			byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB,
			byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA,
			byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB,
			byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA, byteB, byteA };

	/** for half-tone stipples */
	public static byte halftone[] = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
			(byte) 0x55, (byte) 0x55 };

	/**
	 * Draw a set of points
	 * 
	 * @param drawable the OpenGL drawable
	 * @param coords   the vertices as [x, y, z, x, y, z, ...]
	 * @param color    the color
	 * @param size     the points size
	 */
	public static Geometry3D[] drawPoints(Node root, float coords[], Color color, float size, boolean circular) {
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		// how many points?
		int np = coords.length / 3;
		Geometry3D[] temp = new Geometry3D[np];
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		if (circular) {
			Texture texture = assetManager.loadTexture("Textures/CiricleText.png");
			texture.setWrap(Texture.WrapMode.Repeat);
			mat.setTexture("ColorMap", texture);
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		}
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

		for (int i = 0; i < np; i++) {
			int j = i * 3;
			QuadShape q = new QuadShape();
			Geometry3D geom = new Geometry3D("Point", q);
			if(circular) {
				geom.setQueueBucket(Bucket.Transparent);
				mat.getAdditionalRenderState().setDepthWrite(false);
			}
			BillboardControl bc = new BillboardControl();
			bc.setAlignment(BillboardControl.Alignment.Camera);
			geom.setLocalScale(size);
			geom.setLocalTranslation(new Vector3f(coords[j], coords[j + 1], coords[j + 2]));
			geom.setMaterial(mat);
			geom.addControl(bc);
			geom.updateModelBound();
			temp[i] = geom;
			root.attachChild(geom);
		}
		
		return temp;
	}

	/**
	 * Draw a set of points
	 * 
	 * @param drawable the OpenGL drawable
	 * @param coords   the vertices as [x, y, z, x, y, z, ...]
	 * @param fill     the fill color
	 * @param frame    the frame color
	 * @param size     the points size
	 */
	public static Geometry3D[] drawPoints(Node root, float coords[], Color fill, Color frame, float size, boolean circular) {
		if (frame == null) {
			return drawPoints(root, coords, fill, size, circular);
		} else {
			drawPoints(root, coords, frame, size, circular);
			return drawPoints(root, coords, fill, size - 2, circular);
		}
	}

	/**
	 * Draw a single point using double coordinates
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x        the x coordinate
	 * @param y        the y coordinate
	 * @param z        the z coordinate
	 * 
	 * @param color    the color
	 * @param size     the point's pixel size
	 */
	public static Geometry3D drawPoint(Node root, double x, double y, double z, Color color, float size, boolean circular) {
		return drawPoint(root, (float) x, (float) y, (float) z, color, size, circular);
	}

	/**
	 * Draw a point using float coordinates
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x        the x coordinate
	 * @param y        the y coordinate
	 * @param z        the z coordinate
	 * 
	 * @param color    the color
	 * @param size     the points size
	 * @param circular
	 */
	public static Geometry3D drawPoint(Node root, float x, float y, float z, Color color, float size, boolean circular) {
		
		/*ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		if (circular) {
			Texture texture = assetManager.loadTexture("Textures/CiricleText.png");
			texture.setWrap(Texture.WrapMode.Repeat);
			mat.setTexture("ColorMap", texture);
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		}

		QuadShape q = new QuadShape();
		Geometry3D geom = new Geometry3D("Point", q);
		if(circular) {
			geom.setQueueBucket(Bucket.Transparent);
			mat.getAdditionalRenderState().setDepthWrite(false);
		}
		geom.setLocalScale(size);
		BillboardControl bc = new BillboardControl();
		bc.setAlignment(BillboardControl.Alignment.Camera);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		geom.setLocalTranslation(new Vector3f(x, y, z));
		geom.addControl(bc);
		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);
		
		return geom;*/
		if (circular) {
			return solidSphere(root,x,y,z,size,16,16,color);
		}
		else{
			return solidCube(root,x,y,z,size,color);
		}
	}

	public static final String vshader1 = "void main {\n" + "gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n"
			+ "}";

	public static final String fshader1 = "void main {\n" + "}";

	public static void loadShader(GL2 gl, String vertexShaderString, String fragmentShaderString) {
		int v = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
		int f = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);

		// Compile the vertexShader String into a program.
		String[] vlines = new String[] { vertexShaderString };
		int[] vlengths = new int[] { vlines[0].length() };
		gl.glShaderSource(v, vlines.length, vlines, vlengths, 0);
		gl.glCompileShader(v);

		String[] flines = new String[] { fragmentShaderString };
		int[] flengths = new int[] { flines[0].length() };
		gl.glShaderSource(f, flines.length, flines, flengths, 0);
		gl.glCompileShader(f);

		int shaderprogram = gl.glCreateProgram();
		gl.glAttachShader(shaderprogram, v);
		gl.glAttachShader(shaderprogram, f);
		gl.glLinkProgram(shaderprogram);
		gl.glValidateProgram(shaderprogram);

		// gl.glDeleteShader(v);
		// gl.glDeleteShader(f);
		gl.glUseProgram(shaderprogram);
	}

	public static Geometry3D drawSprite(Node root, Texture texture, float x, float y, float z, float size) {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		texture.setWrap(Texture.WrapMode.EdgeClamp);
		mat.setTexture("ColorMap", texture);

		float ratio = ((float)texture.getImage().getHeight())/texture.getImage().getWidth();
		QuadShape q = new QuadShape();
		Geometry3D geom = new Geometry3D("Sprite", q);
		geom.setLocalScale(new Vector3f(size,size*ratio,size));
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		geom.setLocalTranslation(new Vector3f(x, y, z));
		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);
		
		return geom;
	}

	/**
	 * Draw a wire sphere
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x        x center
	 * @param y        y center
	 * @param z        z enter
	 * @param radius   radius in physical units
	 * @param slices   number of slices
	 * @param stacks   number of strips
	 * @param color    color of wires
	 */
	public static Geometry3D wireSphere(Node root, float x, float y, float z, float radius, int slices, int stacks,
			Color color) {
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Sphere s = new Sphere(slices, stacks, 1);
		Geometry3D geom = new Geometry3D("Sphere", s);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		mat.getAdditionalRenderState().setWireframe(true);
		geom.setLocalScale(radius);
		geom.setLocalTranslation(new Vector3f(x, y, z));
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);

		return geom;
	}

	/**
	 * Draw a solid cube
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x        x center
	 * @param y        y center
	 * @param z        z enter
	 * @param radius   radius in physical units
	 * @param slices   number of slices
	 * @param stacks   number of strips
	 * @param color    color of wires
	 */
	public static Geometry3D solidCube(Node root, float x, float y, float z, float size, Color color) {
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Box b = new Box(1, 1, 1);
		Geometry3D geom = new Geometry3D("Box", b);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		geom.setLocalScale(size);
		geom.setLocalTranslation(new Vector3f(x,y,z));
		if (color.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);
		
		return geom;
	}
	
	/**
	 * Draw a solid sphere
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x        x center
	 * @param y        y center
	 * @param z        z enter
	 * @param radius   radius in physical units
	 * @param slices   number of slices
	 * @param stacks   number of strips
	 * @param color    color of wires
	 */
	public static Geometry3D solidSphere(Node root, float x, float y, float z, float radius, int slices, int stacks,
			Color color) {
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Sphere s = new Sphere(slices, stacks, 1);
		Geometry3D geom = new Geometry3D("Sphere", s);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		geom.setLocalScale(radius);
		geom.setLocalTranslation(new Vector3f(x, y, z));
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);

		return geom;

	}

	/**
	 * @param drawable  the openGL drawable
	 * @param coords    the coordinate array
	 * @param color     the color
	 * @param lineWidth the line width
	 * @param frame     if <code>true</code> frame in slightly darker color
	 */
	public static Geometry3D[] drawQuads(Node root, float coords[], Color color, float lineWidth, boolean frame) {

		return drawQuads(root, coords, color, (frame ? color.darker() : null), lineWidth);
	}

	/**
	 * @param drawable  the openGL drawable
	 * @param coords    the coordinate array
	 * @param color     the color
	 * @param lineWidth the line width
	 * @param frame     if <code>true</code> frame in slightly darker color
	 */
	public static Geometry3D[] drawQuads(Node root, float coords[], Color color, Color lineColor,
			float lineWidth) {
		
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		
		Geometry3D[] quads = null;
		if (lineColor != null) {

			// a quad has four vertices therefor 12 points
			int numQuad = coords.length / 12;
			quads = new Geometry3D[numQuad];
			for (int i = 0; i < numQuad; i++) {
				int j = i * 12;
				Vector3f vertex1 = new Vector3f(coords[j++], coords[j++], coords[j++]);
				Vector3f vertex2 = new Vector3f(coords[j++], coords[j++], coords[j++]);
				Vector3f vertex3 = new Vector3f(coords[j++], coords[j++], coords[j++]);
				Vector3f vertex4 = new Vector3f(coords[j++], coords[j++], coords[j++]);
				
				QuadShape q = new QuadShape(vertex1, vertex2, vertex3, vertex4);
				Geometry3D geom = new Geometry3D("Quad", q);
				Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				mat.setColor("Color", colorRGBA);
				if (colorRGBA.getAlpha() != 1) {
					mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
					geom.setQueueBucket(Bucket.Transparent);
				}

				geom.setMaterial(mat);
				geom.updateModelBound();
				root.attachChild(geom);

				if (lineColor != null) {
					ColorRGBA colorRGBADarker = getColorRGBAFrom255AWTColor(color.darker());
					Geometry3D outline = ShapeGenerator.createOutline(geom, colorRGBADarker, lineWidth);
					geom.setOutline(outline);
					root.attachChild(outline);
				}
				quads[i] = geom;
			}
		}
		return quads;
	}

	/**
	 * @param drawable  the openGL drawable
	 * @param coords    the coordinate array
	 * @param color     the color
	 * @param lineWidth the line width
	 * @param frame     if <code>true</code> frame in slightly darker color
	 */
	public static Geometry3D[] drawQuadsHalfTone(GLAutoDrawable drawable, float coords[], Color color, float lineWidth,
			boolean frame) {

		GL2 gl = drawable.getGL().getGL2();
		gl.glLineWidth(lineWidth);
		gl.glEnable(GL2.GL_POLYGON_STIPPLE);
		gl.glPolygonStipple(halftone, 0);

		gl.glBegin(GL2ES3.GL_QUADS);
		setColor(gl, color);

		int numPoints = coords.length / 3;

		for (int i = 0; i < numPoints; i++) {
			int j = 3 * i;
			gl.glVertex3f(coords[j], coords[j + 1], coords[j + 2]);
		}

		gl.glEnd();
		gl.glDisable(GL2.GL_POLYGON_STIPPLE);

		if (frame) {
			int numQuad = coords.length / 12;
			for (int i = 0; i < numQuad; i++) {
				gl.glBegin(GL.GL_LINE_STRIP);
				setColor(gl, color.darker());

				int j = i * 12;

				gl.glVertex3f(coords[j++], coords[j++], coords[j++]);
				gl.glVertex3f(coords[j++], coords[j++], coords[j++]);
				gl.glVertex3f(coords[j++], coords[j++], coords[j++]);
				gl.glVertex3f(coords[j++], coords[j++], coords[j++]);

				j = i * 12;
				gl.glVertex3f(coords[j++], coords[j++], coords[j++]);

				gl.glEnd();
			}
		}
		return null;

	}

	/**
	 * @param drawable  the openGL drawable
	 * @param coords    the coordinate array
	 * @param index1    index into first vertex
	 * @param index2    index into second vertex
	 * @param index3    index into third vertex
	 * @param index4    index into fourth vertex
	 * @param color     the color
	 * @param lineWidth the line width
	 * @param frame     if <code>true</code> frame in slightly darker color
	 */
	public static Geometry3D drawQuad(Node root, float coords[], int index1, int index2, int index3, int index4,
			Color color, float lineWidth, boolean frame) {

		int i1 = 3 * index1;
		int i2 = 3 * index2;
		int i3 = 3 * index3;
		int i4 = 3 * index4;

		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Vector3f vertex1 = new Vector3f(coords[i1], coords[i1 + 1], coords[i1 + 2]);
		Vector3f vertex2 = new Vector3f(coords[i2], coords[i2 + 1], coords[i2 + 2]);
		Vector3f vertex3 = new Vector3f(coords[i3], coords[i3 + 1], coords[i3 + 2]);
		Vector3f vertex4 = new Vector3f(coords[i4], coords[i4 + 1], coords[i4 + 2]);

		QuadShape q = new QuadShape(vertex1, vertex2, vertex3, vertex4);
		Geometry3D geom = new Geometry3D("Quad", q);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}

		geom.setMaterial(mat);
		geom.updateModelBound();
		root.attachChild(geom);

		if (frame) {
			ColorRGBA colorRGBADarker = getColorRGBAFrom255AWTColor(color.darker());
			Geometry3D outline = ShapeGenerator.createOutline(geom, colorRGBADarker, lineWidth);
			geom.setOutline(outline);
			root.attachChild(outline);
		}

		return geom;
	}

	/**
	 * 
	 * @param drawable  the OpenGL drawable
	 * @param coords    the triangle as [x1, y1, ..., y3, z3]
	 * @param color     the color
	 * @param lineWidth the line width
	 * @param frame     if <code>true</code> frame in slightly darker color
	 * @param lineWidth
	 */
	public static Geometry3D[] drawTriangles(Node root, float coords[], Color color, float lineWidth, boolean frame) {
		int numTriangle = coords.length / 9;
		Geometry3D[] list = new Geometry3D[numTriangle];
		for (int i = 0; i < numTriangle; i++) {
			int j = 3 * i;

			list[i] = drawTriangle(root, coords, j, j + 1, j + 2, color, lineWidth, frame);
		}

		return list;
	}

	/**
	 * Break one triangle into smaller triangles
	 * 
	 * @param coords the triangle as [x1, y1, ..., y3, z3]
	 * @param level  [1..] number of times called recursively. If level is n, get
	 *               4^n triangles
	 * @return all the triangles in a coordinate array
	 */
	public static float[] triangulateTriangle(float coords[], int level) {
		if (level < 1) {
			return coords;
		}
		float tricoords[] = oneToFourTriangle(coords);

		for (int lev = 2; lev <= level; lev++) {
			int numtri = tricoords.length / 9;
			int numNewTri = 4 * numtri;
			float[] tri[] = new float[numtri][];
			float allTris[] = new float[9 * numNewTri];
			for (int i = 0; i < numtri; i++) {
				tri[i] = oneToFourTriangle(tricoords, i);
				System.arraycopy(tri[i], 0, allTris, 36 * i, 36);
			}
			tricoords = allTris;

		}
		return tricoords;
	}

	/**
	 * Break one triangle into smaller triangles
	 * 
	 * @param coords the triangle as [x1, y1, ..., y3, z3]
	 * @param level  [1..] number of times called recursively. If level is n, get
	 *               4^n triangles
	 * @return all the triangles in a coordinate array
	 */
	public static Geometry[] drawTriangulatedTriangles(Node root, float coords[], Color color, float lineWidth,
			boolean frame, int level) {
		float tricoords[] = triangulateTriangle(coords, level);
		Geometry[] temp = drawTriangles(root, tricoords, color, lineWidth, frame);

		return temp;
	}

	/**
	 * Break one triangle into four by connecting the midpoints
	 * 
	 * @param coords the triangle as [x1, y1, ..., y3, z3] starting at index
	 * @param index  to first vertex, where coords is assume to contain a list of
	 *               triangles each one requiring 9 numbers
	 * @return all four triangles in a coordinate array
	 */
	public static float[] oneToFourTriangle(float coords[], int index) {

		Vector3f p[] = new Vector3f[6];

		int i1 = 3 * index;
		int i2 = 3 * (index + 1);
		int i3 = 3 * (index + 2);

		Vector3f firstCorner = new Vector3f(coords[i1], coords[i1 + 1], coords[i1 + 2]);
		Vector3f secondCorner = new Vector3f(coords[i2], coords[i2 + 1], coords[i2 + 2]);
		Vector3f thirdCorner = new Vector3f(coords[i3], coords[i3 + 1], coords[i3 + 2]);

		p[0] = firstCorner;
		p[1] = secondCorner;
		p[2] = thirdCorner;

		p[3] = (p[0].add(p[1])).mult(0.5f);
		p[4] = (p[1].add(p[2])).mult(0.5f);
		p[5] = (p[2].add(p[0])).mult(0.5f);

		float coords4[] = new float[36];

		fillCoords(coords4, 0, p[0], p[3], p[5]);
		fillCoords(coords4, 1, p[1], p[3], p[4]);
		fillCoords(coords4, 2, p[3], p[4], p[5]);
		fillCoords(coords4, 3, p[2], p[4], p[5]); // System.err.println("Found Triangles");
		return coords4;
	}

	/**
	 * Break one triangle into four by connecting the midpoints
	 * 
	 * @param coords the triangle as [x1, y1, ..., y3, z3]
	 * @return all four triangles in a coordinate array
	 */
	public static float[] oneToFourTriangle(float coords[]) {
		return oneToFourTriangle(coords, 0);
	}

	// create a coords array by appending 3D points
	private static void fillCoords(float coords[], int index, Vector3f... p) {

		int size = 3 * p.length;
		int i = size * index;

		for (Vector3f v3f : p) {
			coords[i++] = v3f.x;
			coords[i++] = v3f.y;
			coords[i++] = v3f.z;
		}
	}

	/**
	 * Draw a triangle from a coordinate array
	 * 
	 * @param drawable  the OpenGL drawable
	 * @param coords    a set of points
	 * @param index1    "three index" of start of first corner, which will be the
	 *                  next three entries in the coords array
	 * @param index2    "three index" of start of second corner, which will be the
	 *                  next three entries in the coords array
	 * @param index3    "three index" of start of third corner, which will be the
	 *                  next three entries in the coords array
	 * @param color     the color the fill color
	 * @param lineWidth the line width in pixels (if framed)
	 * @param frame     if <code>true</code> frame in slightly darker color
	 * @param lineWidth
	 */
	public static Geometry3D drawTriangle(Node root, float coords[], int index1, int index2, int index3, Color color,
			float lineWidth, boolean frame) {

		int i1 = 3 * index1;
		int i2 = 3 * index2;
		int i3 = 3 * index3;
		Vector3f firstCorner = new Vector3f(coords[i1], coords[i1 + 1], coords[i1 + 2]);
		Vector3f secondCorner = new Vector3f(coords[i2], coords[i2 + 1], coords[i2 + 2]);
		Vector3f thirdCorner = new Vector3f(coords[i3], coords[i3 + 1], coords[i3 + 2]);

		Triangle t = new Triangle(firstCorner, secondCorner, thirdCorner);
		Geometry3D geom = new Geometry3D("Triangle", t);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		mat.setColor("Color", colorRGBA);
		geom.setLocalTranslation(new Vector3f(0, 0, 0));
		geom.setLocalRotation(new Quaternion());
		geom.setLocalScale(new Vector3f(1, 1, 1));
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			//mat.getAdditionalRenderState().setDepthFunc(TestFunction.Always);
			geom.setQueueBucket(Bucket.Transparent);
			// mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		}
		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);

		if (frame) {
			ColorRGBA colorRGBADarker = getColorRGBAFrom255AWTColor(color.darker());
			Geometry3D outline = ShapeGenerator.createOutline(geom, colorRGBADarker, lineWidth);
			geom.setOutline(outline);
		}

		return geom;
	}

	/**
	 * Draw a triangle from Vector positions
	 * 
	 * @param drawable   the OpenGL drawable
	 * @param lowerLeft  vector representing the position of first corner
	 * @param lowerRight vector representing the position of second corner
	 * @param topCenter  vector representing the position of third corner
	 * @param color      the color the fill color
	 * @param lineWidth  the line width in pixels (if framed)
	 * @param frame      if <code>true</code> frame in slightly darker color
	 * @param lineWidth
	 * @return
	 */
	public static Geometry drawTriangle(Node root, Vector3f lowerLeft, Vector3f lowerRight, Vector3f topCenter,
			Color color, float lineWidth, boolean frame) {

		Triangle t = new Triangle(lowerLeft, lowerRight, topCenter);
		Geometry3D geom = new Geometry3D("Triangle", t);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		mat.setColor("Color", colorRGBA);
		mat.getAdditionalRenderState().setLineWidth(lineWidth);
		geom.setLocalTranslation(new Vector3f(0, 0, 0));
		geom.setLocalRotation(new Quaternion());
		geom.setLocalScale(new Vector3f(1, 1, 1));
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			//mat.getAdditionalRenderState().setDepthWrite(false);
			geom.setQueueBucket(Bucket.Transparent);
			// mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		}
		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);

		// GL2 gl = drawable.getGL().getGL2();

		if (frame) {
			ColorRGBA colorRGBADarker = getColorRGBAFrom255AWTColor(color.darker());
			Geometry outline = ShapeGenerator.createOutline(geom, colorRGBADarker, lineWidth);
			root.attachChild(outline);
		}

		return geom;

	}

	/**
	 * Draw a cone
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x1       x coordinate of center of base
	 * @param y1       y coordinate of center of base
	 * @param z1       z coordinate of center of base
	 * @param x2       x coordinate of tip
	 * @param y2       y coordinate of tip
	 * @param z2       z coordinate of tip
	 * @param radius   radius of base
	 * @param color    color of cone
	 */
	public static void drawCone(GLAutoDrawable drawable, float x1, float y1, float z1, float x2, float y2, float z2,
			float radius, Color color) {

		float vx = x2 - x1;
		float vy = y2 - y1;
		float vz = z2 - z1;
		if (Math.abs(vz) < 1.0e-5) {
			vz = 0.0001f;
		}

		float v = (float) Math.sqrt(vx * vx + vy * vy + vz * vz);
		float ax = (float) (57.2957795 * Math.acos(vz / v));
		if (vz < 0.0)
			ax = -ax;
		float rx = -vy * vz;
		float ry = vx * vz;

		GL2 gl = drawable.getGL().getGL2();
		setColor(gl, color);

		gl.glPushMatrix();
		// draw the cylinder body
		gl.glTranslatef(x1, y1, z1);
		gl.glRotatef(ax, rx, ry, 0f);

		glut.glutSolidCone(radius, v, 20, 20);

		gl.glPopMatrix();
	}

	/**
	 * Draw a 3D tube
	 * 
	 * @param drawable the OpenGL drawable
	 * @param x1       x coordinate of one end
	 * @param y1       y coordinate of one end
	 * @param z1       z coordinate of one end
	 * @param x2       x coordinate of other end
	 * @param y2       y coordinate of other end
	 * @param z2       z coordinate of other end
	 * @param radius   the radius of the tube
	 * @param color    the color of the tube
	 */
	public static Geometry3D drawTube(Node root, float x1, float y1, float z1, float x2, float y2, float z2,
			float radius, Color color) {
		float vx = x2 - x1;
		float vy = y2 - y1;
		float vz = z2 - z1;
		if (Math.abs(vz) < 1.0e-5) {
			vz = 0.0001f;
		}
		float v = (float) Math.sqrt(vx * vx + vy * vy + vz * vz);
		
		Vector3f direction = new Vector3f(x2,y2,z2).subtract(new Vector3f(x1,y1,z1));
		Vector3f directionNormalized = direction.normalize();
		float angle = directionNormalized.angleBetween(new Vector3f(0, 0, 1));
		Quaternion rotation = new Quaternion();
		Vector3f crossProdVec = direction.cross(new Vector3f(0, 0, 1));
		rotation.fromAngleAxis(-angle, crossProdVec);
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Cylinder cylinder = new Cylinder(4, 20, radius, radius, v, true, false); 
		Geometry3D geom = new Geometry3D("Cylinder", cylinder);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		geom.setMaterial(mat);
		geom.updateModelBound();
		geom.setLocalTranslation(new Vector3f((x2+x1)/2, (y1+y2)/2, (z1+z2)/2));
		geom.setLocalRotation(rotation);
		geom.setLocalScale(new Vector3f(1, 1, 1));
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}
		geom.setMaterial(mat);
		geom.updateModelBound();
		
		root.attachChild(geom);

		return geom;
	}

	/**
	 * @param Node      the JMonkey3D Node
	 * @param x1        x coordinate of start
	 * @param y1        y coordinate of start
	 * @param z1        z coordinate of start
	 * @param ux        x component of unit vector direction
	 * @param uy        y component of unit vector direction
	 * @param uz        z component of unit vector direction
	 * @param length    the length of the line
	 * @param color     the color
	 * @param lineWidth the line width
	 */
	public static Geometry3D drawLine(Node root, float x1, float y1, float z1, float ux, float uy, float uz, float length,
			Color color, float lineWidth) {

		float x2 = x1 + length * ux;
		float y2 = y1 + length * uy;
		float z2 = z1 + length * uz;

		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Vector3f start = new Vector3f(x1, y1, z1);
		Vector3f end = new Vector3f(x2, y2, z2);
		Line l = new Line(start, end);
		Geometry3D geom = new Geometry3D("Line", l);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		geom.setMaterial(mat);
		mat.getAdditionalRenderState().setLineWidth(lineWidth);
		geom.updateModelBound();
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}
		root.attachChild(geom);

		return geom;
	}

	/**
	 * Draw a 3D line
	 * 
	 * @param Node      the JMonkey3D Node
	 * @param x1        x coordinate of one end
	 * @param y1        y coordinate of one end
	 * @param z1        z coordinate of one end
	 * @param x2        x coordinate of other end
	 * @param y2        y coordinate of other end
	 * @param z2        z coordinate of other end
	 * @param color     the color
	 * @param lineWidth the line width in pixels
	 */
	public static Geometry3D drawLine(Node root, float x1, float y1, float z1, float x2, float y2, float z2, Color color,
			float lineWidth) {

		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Vector3f start = new Vector3f(x1, y1, z1);
		Vector3f end = new Vector3f(x2, y2, z2);
		Line l = new Line(start, end);
		Geometry3D geom = new Geometry3D("Line", l);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		geom.setMaterial(mat);
		mat.getAdditionalRenderState().setLineWidth(lineWidth);
		geom.updateModelBound();
		if (colorRGBA.getAlpha() != 1) {
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geom.setQueueBucket(Bucket.Transparent);
		}
		root.attachChild(geom);

		return geom;
	}

	/**
	 * Draw a 3D line
	 * 
	 * @param Node      the JMonkey3D Node
	 * @param p0        one end point as [x, y, z]
	 * @param p1        other end point as [x, y, z]
	 * @param color     the color
	 * @param lineWidth the line width in pixels
	 */
	public static Geometry3D drawLine(Node root, float[] p0, float[] p1, Color color, float lineWidth) {

		return drawLine(root, p0[0], p0[1], p0[2], p1[0], p1[1], p1[2], color, lineWidth);
	}

	/**
	 * Draw a two color 3D line
	 * 
	 * @param Node      the JMonkey3D Node
	 * @param p0        one end point as [x, y, z]
	 * @param p1        other end point as [x, y, z]
	 * @param color     the color
	 * @param lineWidth the line width in pixels
	 */
	public static Geometry3D[] drawLine(Node root, float[] p0, float[] p1, Color color1, Color color2, float lineWidth) {

		return drawLine(root, p0[0], p0[1], p0[2], p1[0], p1[1], p1[2], color1, color2, lineWidth);
	}

	/**
	 * Draw a 3D line
	 * 
	 * @param Node      the JMonkey3D Node
	 * @param coords    the line as [x1, y1, z1, x2, y2, z2]
	 * @param color     the color
	 * @param lineWidth the line width in pixels
	 */
	public static Geometry3D drawLine(Node root, float[] coords, Color color, float lineWidth) {

		return drawLine(root, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], color, lineWidth);
	}

	/**
	 * Draw a polyline
	 * 
	 * @param drawable  the OpenGL drawable
	 * @param coords    the vertices as [x, y, z, x, y, z, ...]
	 * @param color     the color
	 * @param lineWidth the line width
	 */
	public static Geometry3D drawPolyLine(Node root, float[] coords, Color color, float lineWidth) {
		int np = coords.length / 3;
		Vector3f[] positions = new Vector3f[np];
		for (int i = 0; i < np; i++) {
			int j = i * 3;
			positions[i] = new Vector3f(coords[j], coords[j + 1], coords[j + 2]);
		}

		Line l = new Line(positions);
		Geometry3D geom = new Geometry3D("PolyLine", l);
		ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		mat.getAdditionalRenderState().setLineWidth(lineWidth);
		geom.setMaterial(mat);
		geom.updateModelBound();

		root.attachChild(geom);
		return geom;

	}

	/**
	 * Draw a two color 3D line
	 * 
	 * @param Node      the JMonkey3D Node
	 * @param gl        the gl context
	 * @param x1        x coordinate of one end
	 * @param y1        y coordinate of one end
	 * @param z1        z coordinate of one end
	 * @param x2        x coordinate of other end
	 * @param y2        y coordinate of other end
	 * @param z2        z coordinate of other end
	 * @param color1    one color
	 * @param color2    other color
	 * @param lineWidth the line width in pixels
	 */
	public static Geometry3D[] drawLine(Node root, float x1, float y1, float z1, float x2, float y2, float z2,
			Color color1, Color color2, float lineWidth) {

		Vector3f start = new Vector3f(x1, y1, z1);
		Vector3f end = new Vector3f(x2, y2, z2);
		Line l = new Line(start, end);
		Geometry3D geomC1 = null;
		Geometry3D geomC2 = null;

		if (color1 != null) {
			geomC1 = new Geometry3D("LineC1", l);
			ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color1);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", colorRGBA);
			Texture texture = assetManager.loadTexture("Textures/DashedLine_128x8.png");
			texture.setWrap(Texture.WrapMode.Repeat);
			mat.setTexture("ColorMap", texture);
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geomC1.setQueueBucket(Bucket.Transparent);
			mat.getAdditionalRenderState().setLineWidth(lineWidth);
			geomC1.setMaterial(mat);
		}
		if (color2 != null) {
			geomC2 = new Geometry3D("LineC2", l);
			ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color2);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", colorRGBA);
			Texture texture = assetManager.loadTexture("Textures/DashedLineNegative_128x8.png");
			texture.setWrap(Texture.WrapMode.Repeat);
			mat.setTexture("ColorMap", texture);
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geomC2.setQueueBucket(Bucket.Transparent);
			mat.getAdditionalRenderState().setLineWidth(lineWidth);
			geomC2.setMaterial(mat);
		}

		if (geomC1 != null) {
			root.attachChild(geomC1);
			geomC1.updateModelBound();
		}

		if (geomC2 != null) {
			root.attachChild(geomC2);
			geomC2.updateModelBound();
		}

		if (geomC1 != null && geomC2 != null) {
			geomC1.setQueueBucket(Bucket.Opaque);
			geomC2.setQueueBucket(Bucket.Opaque);
		}
		return new Geometry3D[] { geomC1, geomC2 };

	}

	/**
	 * Draw a two color polyline
	 * 
	 * @param drawable  the OpenGL drawable
	 * @param coords    the vertices as [x, y, z, x, y, z, ...]
	 * @param color1    one color
	 * @param color2    other color
	 * @param lineWidth the line width in pixels
	 */
	public static Geometry3D[] drawPolyLine(Node root, float[] coords, Color color1, Color color2, float lineWidth) {
		int np = coords.length / 3;
		Vector3f[] positions = new Vector3f[np];
		for (int i = 0; i < np; i++) {
			int j = i * 3;
			positions[i] = new Vector3f(coords[j], coords[j + 1], coords[j + 2]);
		}

		Line l = new Line(positions);
		Geometry3D geomC1 = null;
		Geometry3D geomC2 = null;

		if (color1 != null) {
			geomC1 = new Geometry3D("LineC1", l);
			ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color1);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", colorRGBA);
			Texture texture = assetManager.loadTexture("Textures/DashedLine_128x8.png");
			texture.setWrap(Texture.WrapMode.Repeat);
			mat.setTexture("ColorMap", texture);
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geomC1.setQueueBucket(Bucket.Transparent);
			mat.getAdditionalRenderState().setLineWidth(lineWidth);
			geomC1.setMaterial(mat);
		}
		if (color2 != null) {
			geomC2 = new Geometry3D("LineC2", l);
			ColorRGBA colorRGBA = getColorRGBAFrom255AWTColor(color2);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", colorRGBA);
			Texture texture = assetManager.loadTexture("Textures/DashedLineNegative_128x8.png");
			texture.setWrap(Texture.WrapMode.Repeat);
			mat.setTexture("ColorMap", texture);
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geomC2.setQueueBucket(Bucket.Transparent);
			mat.getAdditionalRenderState().setLineWidth(lineWidth);
			geomC2.setMaterial(mat);
		}

		if (geomC1 != null) {
			root.attachChild(geomC1);
			geomC1.updateModelBound();
		}

		if (geomC2 != null) {
			root.attachChild(geomC2);
			geomC2.updateModelBound();
		}

		if (geomC1 != null && geomC2 != null) {
			geomC1.setQueueBucket(Bucket.Opaque);
			geomC2.setQueueBucket(Bucket.Opaque);
		}

		return new Geometry3D[] { geomC1, geomC2 };

	}

	/**
	 * Set a color based on an awt color
	 * 
	 * @param gl    the graphics context
	 * @param color the awt color
	 */
	public static void setColor(GL2 gl, Color color) {
		color.getRed();
		color.getGreen();
		color.getBlue();
		color.getAlpha();

	}

	/**
	 * Set a color based on an awt color
	 * 
	 * @param gl    the graphics context
	 * @param color the awt color
	 */
	public static ColorRGBA getColorRGBAFrom255AWTColor(Color color) {
		float r = color.getRed() / 255f;
		float g = color.getGreen() / 255f;
		float b = color.getBlue() / 255f;
		float a = color.getAlpha() / 255f;
		return new ColorRGBA(r, g, b, a);

	}

	/**
	 * Get a simple vertex
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return a vertex
	 */
	public static SVertex vertex(float x, float y, float z) {
		return new SVertex(x, y, z, false);
	}

	/**
	 * Convenience method to convert a variable list of floats into a float array.
	 * 
	 * @param v the variable length list of floats
	 * @return the corresponding array
	 */
	public static float[] toArray(float... v) {
		return v;
	}
	
	public static void setAssetManager(AssetManager assetMan) {
		assetManager = assetMan;
	}

}
