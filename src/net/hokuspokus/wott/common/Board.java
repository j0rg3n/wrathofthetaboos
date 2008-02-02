package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Board {

	private static final float TILESIZE = 1.0f;
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
			p.getGeometry().setLocalTranslation(
					getTileCenterPos((int)p.getPos().x, (int)p.getPos().y)
					.add(0, .5f, 0));
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
	
	public static Vector3f getTilePos(int x, int y) {
		return new Vector3f(x * TILESIZE, 0, y * TILESIZE);
	}

	public static Vector3f getTileCenterPos(int x, int y) {
		return new Vector3f((x + .5f) * TILESIZE, 0, (y + .5f) * TILESIZE);
	}

	public Spatial getTile(int x, int y)
	{
		Spatial tile = new Box("tile_"+x+","+y, 
				new Vector3f(0.02f * TILESIZE, 0.2f * TILESIZE, 0.02f * TILESIZE), 
				new Vector3f(0.98f * TILESIZE, 0.1f * TILESIZE, 0.98f * TILESIZE));
		tile.setLocalTranslation(getTilePos(x, y));
		return tile;
	}
	
	public void addPiece(Person person) {

		living.add(person);
		
		person.setPos(new Vector2f((int)(Math.random() * width),
				(int)(Math.random() * height)));
	}	
}
