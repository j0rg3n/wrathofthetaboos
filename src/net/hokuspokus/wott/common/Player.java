package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Box;

import net.hokuspokus.wott.common.Person.PersonType;

public class Player
{
	private static final int MANCOUNT = 15;
	private static final int WOMANCOUNT = 15;
	
	private ColorRGBA color;
	List<Person> people = new ArrayList<Person>();

	public Player(ColorRGBA color) {
		this.color = color;
		for (int i = 0; i < MANCOUNT; ++i) {
			people.add(new Person(this, PersonType.MAN));
		}

		for (int i = 0; i < WOMANCOUNT; ++i) {
			people.add(new Person(this, PersonType.WOMAN));
		}
	}

	public Spatial createNode(PersonType type)
	{
		//return new Box("Player:"+type, new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
		Arrow arrow = new Arrow("Player:"+type, 1, .5f);
		arrow.setDefaultColor(color);
		return arrow;
	}

	public Iterable<Person> getPopulation()
	{
		return people;
	}
}