package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import net.hokuspokus.wott.common.Person.PersonType;

public class Player
{
	private static final int MANCOUNT = 15;
	private static final int WOMANCOUNT = 15;
	
	List<Person> people = new ArrayList<Person>();

	public Player() {
		
		for (int i = 0; i < MANCOUNT; ++i) {
			people.add(new Person(this, PersonType.MAN));
		}

		for (int i = 0; i < WOMANCOUNT; ++i) {
			people.add(new Person(this, PersonType.WOMAN));
		}
	}
}
