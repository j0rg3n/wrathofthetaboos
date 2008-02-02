package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

public class Board {

	/**
	 * List of still living pieces.
	 */
	List<Person> living = new ArrayList<Person>();

	public void update()
	{
		for (Person p : living) {
			p.getGeometry().setLocalTranslation(p.getPos().x, 0, p.getPos().y);
		}
	}

	public void addPiece(Person person) {

		living.add(person);
	}	
}
