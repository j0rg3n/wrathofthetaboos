package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Board {

	/**
	 * List of still living pieces.
	 */
	List<Person> living = new ArrayList<Person>();
	private int width;
	private int height;

	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public void update()
	{
		for (Person p : living) {
			p.getGeometry().setLocalTranslation(p.getPos().x, 0, p.getPos().y);
		}
	}

	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}

	public Spatial getTile(int x, int y)
	{
		Spatial tile = new Box("tile_"+x+","+y, new Vector3f(0.02f, 0.1f, 0.02f), new Vector3f(0.98f, 0.1f, 0.98f));
		tile.setLocalTranslation(x, 0, y);
		return tile;
	}
	
	public void addPiece(Person person) {

		living.add(person);
	}	
}
