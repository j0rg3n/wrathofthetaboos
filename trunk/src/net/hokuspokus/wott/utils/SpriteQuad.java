package net.hokuspokus.wott.utils;

import java.nio.FloatBuffer;

import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.scene.batch.TriangleBatch;
import com.jme.util.geom.BufferUtils;

public class SpriteQuad extends TriMesh {

	private static final long serialVersionUID = 1L;

    public SpriteQuad() {
        
    }
    
	/**
	 * Constructor creates a new <code>SpriteQuad</code> object. That data for the
	 * <code>SpriteQuad</code> is not set until a call to <code>initialize</code>
	 * is made.
	 * 
	 * @param name
	 *            the name of this <code>SpriteQuad</code>.
	 */
	public SpriteQuad(String name) {
		super(name);
	}

	/**
	 * Constructor creates a new <code>SpriteQuade</code> object with the provided
	 * width and height.
	 * 
	 * @param name
	 *            the name of the <code>SpriteQuad</code>.
	 * @param width
	 *            the width of the <code>SpriteQuad</code>.
	 * @param height
	 *            the height of the <code>SpriteQuad</code>.
	 */
	public SpriteQuad(String name, float width, float height) {
		super(name);
		initialize(width, height);
	}

	/**
	 * <code>resize</code> changes the width and height of the given SpriteQuad by
	 * altering its vertices.
	 * 
	 * @param width
	 *            the new width of the <code>SpriteQuad</code>.
	 * @param height
	 *            the new height of the <code>SpriteQuad</code>.
	 */
	public void resize(float width, float height) {
        TriangleBatch batch = getBatch(0);
		batch.getVertexBuffer().clear();
		putVertices(width, height, batch);
	}

	/**
	 * 
	 * <code>initialize</code> builds the data for the <code>SpriteQuad</code>
	 * object.
	 * 
	 * 
	 * @param width
	 *            the width of the <code>SpriteQuad</code>.
	 * @param height
	 *            the height of the <code>SpriteQuad</code>.
	 */
	public void initialize(float width, float height) {
        TriangleBatch batch = getBatch(0);
		batch.setVertexCount(4);
		batch.setVertexBuffer(BufferUtils.createVector3Buffer(batch.getVertexCount()));
		batch.setNormalBuffer(BufferUtils.createVector3Buffer(batch.getVertexCount()));
        FloatBuffer tbuf = BufferUtils.createVector2Buffer(batch.getVertexCount());
        setTextureBuffer(0,tbuf);
	    batch.setTriangleQuantity(2);
	    batch.setIndexBuffer(BufferUtils.createIntBuffer(batch.getTriangleCount() * 3));

		putVertices(width, height, batch);

		batch.getNormalBuffer().put(0).put(0).put(1);
		batch.getNormalBuffer().put(0).put(0).put(1);
		batch.getNormalBuffer().put(0).put(0).put(1);
		batch.getNormalBuffer().put(0).put(0).put(1);

        
		tbuf.put(0).put(1);
        tbuf.put(0).put(0);
        tbuf.put(1).put(0);
        tbuf.put(1).put(1);

	    batch.getIndexBuffer().put(0);
	    batch.getIndexBuffer().put(1);
	    batch.getIndexBuffer().put(2);
	    batch.getIndexBuffer().put(0);
	    batch.getIndexBuffer().put(2);
	    batch.getIndexBuffer().put(3);
	}

	private void putVertices(float width, float height, TriangleBatch batch) {
		batch.getVertexBuffer().put(0).put(height).put(0);
		batch.getVertexBuffer().put(0).put(0).put(0);
		batch.getVertexBuffer().put(width).put(0).put(0);
		batch.getVertexBuffer().put(width).put(height).put(0);
	}

	/**
	 * <code>getCenter</code> returns the center of the <code>SpriteQuad</code>.
	 * 
	 * @return Vector3f the center of the <code>SpriteQuad</code>.
	 */
	public Vector3f getCenter() {
		return worldTranslation;
	}
}
