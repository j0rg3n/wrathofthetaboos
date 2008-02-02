package net.hokuspokus.wott.common;

import java.io.File;
import java.io.IOException;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.utils.TextureUtil;
import com.jme.animation.SpatialTransformer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Quad;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

public class Player
{
	private static final boolean USE_PLACEHOLDER = false;
	
	public enum PlayerColor
	{
		BLUE,
		RED,
		GREEN
	}
	
	public Quad bigIcon, smallIcon;
	
	private PlayerColor color;
	
	public Player(PlayerColor color, String bigIconPath, String smallIconPath) {
		this.color = color;
		this.bigIcon = TextureUtil.getTransparentQuad("icon", bigIconPath, 300, 300);
		this.smallIcon = TextureUtil.getTransparentQuad("icon", smallIconPath, 170, 170);
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
				if(type == PersonType.MAN)
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
	            	File file = new File("ressources/3d gfx/Bobbing2.jme");
	            	SimpleResourceLocator locator = new SimpleResourceLocator(file.getParentFile().toURI());
	                ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
					r1 = (Node) BinaryImporter.getInstance().load(file);
					r1.getLocalScale().set(0.02f, 0.02f, 0.02f);
				}
				else
				{
	            	File file = new File("ressources/3d gfx/mini_negerkvinde02.jme");
	            	SimpleResourceLocator locator = new SimpleResourceLocator(file.getParentFile().toURI());
	                ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
					r1 = (Node) BinaryImporter.getInstance().load(file);
					r1.getLocalScale().set(0.22f, 0.22f, 0.22f);
				}
				
				/*
				MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
				ms.setColorMaterial(MaterialState.CM_NONE);
				r1.setRenderState(ms);
				r1.updateRenderState();
				
*/				
				TextureUtil.getInstance().setTexture(r1, "/ressources/2d gfx/player_"+color+".jpg");
/*
				for (Controller c : r1.getControllers())
				{
					System.out.println(" ");
					System.out.println(c);
					System.out.println(" ");
				}
				System.out.println();

				for (Spatial c : r1.getChildren())
				{
					for (Controller con : c.getControllers())
					{
						System.out.println(" ");
						System.out.println(con);
						System.out.println(" ");
					}
				}
*/
				SpatialTransformer kc;
				if (r1 != null && r1.getControllerCount() >= 1 && r1.getController(0) != null)
				{
					kc = (SpatialTransformer) r1.getController(0);
					kc.setSpeed(10);
					kc.setRepeatType(Controller.RT_WRAP);
				}
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
