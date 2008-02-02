package net.hokuspokus.wott.common;

import com.jme.math.Vector2f;

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
	
	public Person(Player owner, PersonType type)
	{
		this.owner = owner;
		this.type = type;
		this.id = nextId++;
	}
}
