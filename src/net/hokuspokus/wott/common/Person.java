package net.hokuspokus.wott.common;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Arrow;

public class Person
{
	public static final float ZONE = .1f;
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
	private Arrow cellPointer;
	
	public Person(Player owner, PersonType type)
	{
		this.owner = owner;
		this.type = type;
		this.id = nextId++;
		this.geometry = owner.createNode(type);
		this.cellPointer = new Arrow("uha", 1.0f, .05f);
	}
	
	

	public PersonType getType() {
		return type;
	}



	public Spatial getGeometry()
	{
		return geometry;
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos.set(pos);
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity.set(velocity);
	}

	/**
	 * Distance from center of this person to center of other person.
	 */
	public float getDistance(Person p2) {
		return getPos().subtract(p2.getPos()).length();
	}

	public Spatial getCellPointerGeometry() {
		return cellPointer;
	}

	public Player getOwner()
	{
		return owner;
	}



	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
}
