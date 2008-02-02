package net.hokuspokus.wott.common;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import net.hokuspokus.wott.client.MeshEffectHelper;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.common.TabooSelector.TABOO;
import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.effects.particles.ParticleGeometry;

public class Board {

	private static final float TILESIZE = 1.0f;
	public static final boolean SHOW_CELL_MEMBERSHIP = false;
	
	/**
	 * List of still living pieces.
	 */
	List<Person> living = new ArrayList<Person>();
	private Set<Person> violators = new HashSet<Person>();

	private int width;
	private int height;

	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public void markViolators(TABOO taboo) {
		violators.clear();
		violators.addAll(getTabooViolators(taboo));	
	}
	
	private float t;
	public void update()
	{
		t += 0.2f;
		
		applyForce(10);

		for (Person p : living) {
			
			float silliness = violators.contains(p) ? (float)Math.sin(t) * 0.05f : 0;
			
			p.getGeometry().setLocalTranslation(
					getTilePos(p.getPos().x, p.getPos().y)
					.add(0, .05f + silliness , 0));
			
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
		
		Vector2f repulsionVector = new Vector2f();

		for (int i = 0; i < steps; ++i) {
			
			// Apply one stepth of velocity plus repulsive forces.
			for (Person p : living) {
				p.getPos().addLocal(p.getVelocity().divide(steps));
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
							
							repulsionVector.set(p1.getPos());
							repulsionVector.subtractLocal(p2.getPos());
							repulsionVector.normalizeLocal();
							repulsionVector.multLocal(repulsion / 2.0f);
							
							p1.getPos().addLocal(repulsionVector);
							p2.getPos().subtractLocal(repulsionVector);
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
				new Vector3f( 0, -0.2f, 0), 
				new Vector3f( TILESIZE,  0.0f,  TILESIZE));
		tile.setLocalTranslation(getTilePos(x, y));
		TextureUtil.getInstance().setTexture(tile, "/ressources/2d gfx/tile1.jpg");
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
			case MIX:
			case MEN:
			case WOMEN:
				// Note: This also covers a single man and a single woman, but in that case,
				// we'll just jump directly to the MANWOMAN test below, so the extra check is
				// benign.
				if (persons.size() >= 2) {
					if (isAllSameType(taboo, persons)) {
						if (persons.get(0).getType() == PersonType.MAN) {
							isViolator = taboo == TABOO.MEN;
						} else {
							isViolator = taboo == TABOO.WOMEN;
						}
					} else {
						isViolator = taboo == TABOO.MIX;
					}
				}
				break;
			case MANWOMAN:
				if (persons.size() == 2) {
					isViolator = persons.get(0).getType() != persons.get(1).getType();
				}
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
			}
			
			if (isViolator) {
				violators.addAll(persons);
			}
		}
		
		return violators;
	}

	private boolean isAllSameType(TabooSelector.TABOO taboo, List<Person> persons) {

		PersonType prevPersonType = persons.get(0).getType();
		for (int i = 1; i < persons.size(); ++i) {
			Person p = persons.get(i);
			if (p.getType() != prevPersonType) {
				return false;
			}
			prevPersonType = p.getType();
		}
		return true;
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
	
	public void killViolators() {
		for (Person p : violators) {           

			living.remove(p);
			
            try
			{
            	File file = new File("ressources/2d gfx/death3.jme");
            	SimpleResourceLocator locator = new SimpleResourceLocator(file.getParentFile().toURI());
                ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
				Spatial obj = (Spatial) BinaryImporter.getInstance().load(file);
				
				obj.setLocalScale(0.0100f);
				obj.getLocalTranslation().set(p.getGeometry().getLocalTranslation());
				
				p.getGeometry().getParent().attachChild(obj);

				if(obj instanceof Node)
				{
	                for (Spatial child : ((Node)obj).getChildren()) {
	                    if (child instanceof ParticleGeometry) {
	                        ((ParticleGeometry) child).forceRespawn();
	                    }
	                }
				}
				else
				{
					((ParticleGeometry) obj).forceRespawn();
				}
				//BOO: obj.setLocalScale(0.01f);
				obj.updateRenderState();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			
			//p.getGeometry().removeFromParent();
			MeshEffectHelper.explodeNode((Node) p.getGeometry());
		}
	}

	public int getRoundTime() {
		return living.size();
	}
	
	public boolean isDone() {
		if (living.isEmpty()) {
			return true;
		} 
		
		return getWinner() != null;
	}

	public Player getWinner() {
		if (living.isEmpty()) {
			throw new NoSuchElementException("Noone left alive.");
		}
		
		Player prevPlayer =  living.get(0).getOwner();
		for (Person p : living) {
			if (prevPlayer != p.getOwner()) {
				return null;
			}
		}
		
		return prevPlayer;
	}	
}
