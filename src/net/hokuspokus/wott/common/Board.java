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
		applyForce(10);
		
		for (Person p : living) {
			p.getGeometry().setLocalTranslation(
					getTileCenterPos(p.getPos().x, p.getPos().y)
					.add(0, .5f, 0));
			
			// Reduce force...
			Vector2f newVelocity = p.getVelocity().mult(0.98f);
			if (newVelocity.length() < .01f) {
				newVelocity = new Vector2f();
			}
			p.setVelocity(newVelocity);
		}
	}

	private void applyForce(int steps) {

		for (int i = 0; i < steps; ++i) {
			
			// Apply one stepth of velocity plus repulsive forces.
			for (Person p : living) {
				p.setPos(p.getPos().add(p.getVelocity().divide(steps)));
			}

			// Apply repulsion between all touching pairs and from the walls. 
			for (int j = 0; j < living.size(); ++j) {
				Person p1 = living.get(j);
				
				// Good, old, O(n^2) collision detection
				for (int k = 0; k < j; ++k) {
					Person p2 = living.get(k);
					if (p1 != p2) {
						float repulsion = Person.ZONE * 2 - p1.getDistance(p2);
						if (repulsion > 0) {
							Vector2f repulsionVector = 
								p1.getPos()
								.subtract(p2.getPos())
								.normalize()
								.mult(repulsion / 2.0f);
							p1.setPos(p1.getPos().add(repulsionVector));
							p2.setPos(p2.getPos().subtract(repulsionVector));
						}
					}
				}
			}

			// Bounce off walls, too.
			for (Person p : living) {

				if (p.getPos().x < 0) {
					p.getPos().x = 0;
					p.getVelocity().x = Math.abs(p.getVelocity().x);
				}
				if (p.getPos().x > width - 1) {
					p.getPos().x = width - 1;
					p.getVelocity().x = -Math.abs(p.getVelocity().x);
				}
				if (p.getPos().y < 0) {
					p.getPos().y = 0;
					p.getVelocity().y = Math.abs(p.getVelocity().y);
				}
				if (p.getPos().y > height - 1) {
					p.getPos().y = height - 1;
					p.getVelocity().y = -Math.abs(p.getVelocity().y);
				}
			}
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
	
	public static Vector3f getTilePos(float x, float y) {
		return new Vector3f(x * TILESIZE, 0, y * TILESIZE);
	}

	public static Vector3f getTileCenterPos(float x, float y) {
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

		float u = 0.1f;
		person.setVelocity(new Vector2f((float)(Math.random() * 2 * u - u), 
				(float)(Math.random() * 2 * u - u)));
	}	
}
