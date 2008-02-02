package net.hokuspokus.wott.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.hokuspokus.wott.common.Person.PersonType;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Arrow;
import com.jme.scene.state.RenderState;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.converters.MaxToJme;
import net.hokuspokus.wott.utils.TextureUtil;

public class Player
{
	private static final boolean USE_PLACEHOLDER = false;
	
	public enum PlayerColor
	{
		BLUE,
		RED,
		GREEN
	}
	
	private PlayerColor color;
	
	public Player(PlayerColor color) {
		this.color = color;
	}
	public Node createNode(PersonType type)
	{
		if (USE_PLACEHOLDER) {
			Node r1 = new Node();
			r1.attachChild(new Arrow("u", .5f, .1f));
			return r1;
		} else {
			Node r1 = null;
			try
			{
				if(type == PersonType.WOMAN)
				{
					/*
					URL model = Player.class.getClassLoader().getResource("ressources/3d gfx/" + (type == PersonType.WOMAN ? "mini_negerkvinde.3DS" : "Mand2.3DS"));
	
					MaxToJme C1 = new MaxToJme();
					ByteArrayOutputStream BO = new ByteArrayOutputStream();
					C1.convert(new BufferedInputStream(model.openStream()), BO);
		            
					r1 = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
					if(type != PersonType.WOMAN)
						r1.setLocalScale(.010f);
					else
						r1.getLocalScale().set(Vector3f.UNIT_XYZ);
					*/
					//r1 = new Node();
	            	File file = new File("ressources/3d gfx/Bobbing.jme");
	            	SimpleResourceLocator locator = new SimpleResourceLocator(file.getParentFile().toURI());
	                ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
					r1 = (Node) BinaryImporter.getInstance().load(file);
					r1.getLocalScale().set(0.02f, 0.02f, 0.02f);
				}
				else
				{
	            	File file = new File("ressources/3d gfx/mini_negermand3_collada.jme");
	            	SimpleResourceLocator locator = new SimpleResourceLocator(file.getParentFile().toURI());
	                ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
					r1 = (Node) BinaryImporter.getInstance().load(file);
					r1.getLocalScale().set(0.02f, 0.02f, 0.02f);
				}
				
				//TextureUtil.clearRenderStateRecursively(r1, RenderState.RS_TEXTURE);
				TextureUtil.getInstance().setTexture(r1, "/ressources/2d gfx/player_"+color+".jpg");
				
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
	            
				//Quaternion temp = new Quaternion();
				//temp.fromAngleAxis(FastMath.PI / 2, new Vector3f(-1, 0, 0));
				//r1.setLocalRotation(temp);
				
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
	}

	public PlayerColor getColor()
	{
		return color;
	}
}
