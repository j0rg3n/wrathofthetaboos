package net.hokuspokus.wott.common;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Person
{
	public static final float ZONE = .6f;
	private static int nextId = 0;
	public enum PersonType
	{
		MAN,
		WOMAN
	}
	
	Player owner;
	PersonType type;
	Vector2f pos = new Vector2f();
	Vector2f velocity = new Vector2f();
	final int id;
	Spatial geometry;
	
	public Person(Player owner, PersonType type)
	{
		this.owner = owner;
		this.type = type;
		this.id = nextId++;
		this.geometry = owner.createNode(type);
	}

	public Spatial getGeometry()
	{
		return geometry;
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	/**
	 * Distance from center of this person to center of other person.
	 */
	public float getDistance(Person p2) {
		return getPos().subtract(p2.getPos()).length();
	}
}
