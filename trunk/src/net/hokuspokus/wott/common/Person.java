package net.hokuspokus.wott.common;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Person
{
	private static int nextId = 0;
	public enum PersonType
	{
		MAN,
		WOMAN
	}
	
	Player owner;
	PersonType type;
	Vector2f pos;
	final int id;
	Spatial geometry;
	
	public Person(Player owner, PersonType type)
	{
		this.owner = owner;
		this.type = type;
		this.id = nextId++;
		this.geometry = owner.createNode(type);
		
		pos = new Vector2f((float)(Math.random() * 100),
				(float)(Math.random() * 100));
	}

	public Spatial getGeometry()
	{
		return geometry;
	}

	public Vector2f getPos() {
		return pos;
	}
	
	
}
