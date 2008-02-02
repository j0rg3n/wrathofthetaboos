package net.hokuspokus.wott.common;

import com.jme.math.Vector3f;
import com.jme.scene.Geometry;

public class TabooDisplay {

	Vector3f position;
	
	Geometry shape;

	public TabooDisplay(Vector3f position, Geometry shape) {

		this.position = position;
		this.shape = shape;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Geometry getGeometry() {
		return shape;
	}

	
	
}
