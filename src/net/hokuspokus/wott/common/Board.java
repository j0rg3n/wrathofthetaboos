package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.common.TabooSelector.TABOO;

import com.jme.math.Quaternion;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Board {

	private static final float TILESIZE = 1.0f;
	public static final boolean SHOW_CELL_MEMBERSHIP = false;
	
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

	
	private float t;
	public void update()
	{
		t += 0.2f;
		
		applyForce(10);

		Set<Person> violators = new HashSet<Person>();
		violators.addAll(getTabooViolators(TABOO.MAN));
		
		for (Person p : living) {
			float silliness = violators.contains(p) ? (float)Math.sin(t) * 0.05f : 0;
			
			p.getGeometry().setLocalTranslation(
					getTileCenterPos(p.getPos().x, p.getPos().y)
					.add(0, .5f + silliness , 0));
			
			// Reduce force...
			Vector2f newVelocity = p.getVelocity().mult(Math.min(0.90f, (float) Math.pow(0.50f, p.getVelocity().length())));
			if (newVelocity.length() < .01f) {
				newVelocity.set(0,0);
				//p.getGeometry().getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
			}
			else {
				p.getGeometry().getLocalRotation().fromAngles(-FastMath.HALF_PI, newVelocity.getAngle()-FastMath.HALF_PI, 0); //
			}
			p.setVelocity(newVelocity);
		}
		
		if (SHOW_CELL_MEMBERSHIP) {
			// Make cells point to the people inside
			Map<Vector2f, List<Person>> cellBuckets = getCellBuckets();
			Vector2f cellPos = new Vector2f();
			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					cellPos.set(x, y);
					List<Person> persons = cellBuckets.get(cellPos);
					if (persons != null) {
						for (Person p : persons) {
							//p.getCellPointerGeometry()
							//.setLocalRotation(new Quaternion(new float[]{ 0, (float)Math.random(), -(float)(Math.PI / 2) }));
							p.getCellPointerGeometry()
							.setLocalTranslation(getTileCenterPos(x, y).add(0, .5f, 0));
						}
					}
				}
			}
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
				if (p.getPos().x > width) {
					p.getPos().x = width;
					p.getVelocity().x = -Math.abs(p.getVelocity().x);
				}
				if (p.getPos().y < 0) {
					p.getPos().y = 0;
					p.getVelocity().y = Math.abs(p.getVelocity().y);
				}
				if (p.getPos().y > height) {
					p.getPos().y = height;
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
	
	public List<Person> getTabooViolators(TabooSelector.TABOO taboo) {

		Map<Vector2f, List<Person>> cellBuckets = getCellBuckets();
		
		// Process each cell
		List<Person> violators = new LinkedList<Person>();
		for (Entry<Vector2f, List<Person>> e : cellBuckets.entrySet()) {

			List<Person> persons = e.getValue();
			boolean isViolator = false;
			switch (taboo) {
			case PLENTY:
				isViolator = persons.size() >= 3;
				break;
			case MAN:
			case WOMAN:
				if (persons.size() == 1) {
					if (persons.get(0).getType() == PersonType.MAN) {
						isViolator = taboo == TABOO.MAN;
					} else {
						isViolator = taboo == TABOO.WOMAN;
					}
				}
				break;
			case WOMANWOMAN:
			case MANMAN:
			case MANWOMAN:
				if (persons.size() == 2) {
					if (persons.get(0).getType() == persons.get(1).getType()) {
						if (persons.get(0).getType() == PersonType.MAN) {
							isViolator = taboo == TABOO.MANMAN;
						} else {
							isViolator = taboo == TABOO.WOMANWOMAN;
						}
					} else {
						isViolator = taboo == TABOO.MANWOMAN;
					}
				}
				break;
			}
			
			if (isViolator) {
				violators.addAll(persons);
			}
		}
		
		return violators;
	}

	public Map<Vector2f, List<Person>> getCellBuckets() {
		// Gather in cells to ease processing
		Map<Vector2f, List<Person>> cellBuckets = new HashMap<Vector2f, List<Person>>();
		for (Person p : living) {
			
			Vector2f cell = new Vector2f((int)(p.getPos().x + .5f), 
					(int)(p.getPos().y + .5f));
			
			List<Person> persons = cellBuckets.get(cell);
			if (persons == null) {
				persons = new LinkedList<Person>();
				cellBuckets.put(cell, persons);
			}
			
			persons.add(p);
		}
		return cellBuckets;
	}

	public Iterable<Person> getLiving()
	{
		return living;
	}	
}