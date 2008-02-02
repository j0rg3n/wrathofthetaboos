package net.hokuspokus.wott.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import jmetest.renderer.TestSimpleBoneAnimation;

import com.jme.animation.SkinNode;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.MaxToJme;

import net.hokuspokus.wott.client.WrathOfTaboo;
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
	
	public Node createNode(PersonType type)
	{
		Node r1 = null;
		try
		{
			
			
			
			URL model = Player.class.getClassLoader().getResource(type == PersonType.WOMAN ? "3d gfx/Kvinde.3DS" : "3d gfx/Mand2.3DS");
			MaxToJme C1 = new MaxToJme();
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			C1.convert(new BufferedInputStream(model.openStream()), BO);
            
			r1 = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			r1.setLocalScale(.010f);
			
	 /*       TextureState ts = WrathOfTaboo.getInstance().getdisplay().getRenderer().createTextureState();
	        ts.setTexture(TextureManager.loadTexture(TestSimpleBoneAnimation.class
	                .getClassLoader().getResource(type == PersonType.WOMAN ? "2d gfx/CITROEN_.JPG" : "2d gfx/CITROEN_.JPG"),
	                Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, 0.0f, true));
	        r1.setRenderState(ts);*/
/*
	        MaterialState ms = WrathOfTaboo.getInstance().getdisplay().getRenderer().createMaterialState();
	        ms.setSpecular(new ColorRGBA(0.9f, 0.9f, 0.9f, 1));
	        ms.setShininess(10);
	        b.setRenderState(ms);*/
			
			// Node r = new Node("parent stuff");
			// r.attachChild(C1.get(new
			// BufferedInputStream(modelToLoad.openStream()), BO));
			// r.setLocalScale(.1f);
			//r1.setLocalScale(.1f);
			//if (r1.getChild(0).getControllers().size() != 0)
			//	r1.getChild(0).getController(0).setSpeed(20);
            
			Quaternion temp = new Quaternion();
			temp.fromAngleAxis(FastMath.PI / 2, new Vector3f(-1, 0, 0));
			r1.setLocalRotation(temp);
			
			//r1.setLocalTranslation(new Vector3f(10, 0, 0));
			// rootNode.attachChild(r);
			// rootNode.attachChild(r1);
		}
		catch (IOException e)
		{
			System.err.println("Failed to load Max file");
		}
		
		return r1;
	}

	public Iterable<Person> getPopulation()
	{
		return people;
	}
}
