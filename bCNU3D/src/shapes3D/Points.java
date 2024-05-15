package shapes3D;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class Points extends Mesh{

	public Points(Vector3f[] lineVerticies, ColorRGBA pointColor) {
        setMode(Mode.Points);
        setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));
        setStatic();
        updateBound();
        updateCounts();
	}
}
