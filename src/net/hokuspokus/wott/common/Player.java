package net.hokuspokus.wott.common;

import java.io.File;
import java.io.IOException;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.utils.NodeUtils;
import net.hokuspokus.wott.utils.TextureUtil;
import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.RenderState;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

public class Player
{
	private static final boolean USE_PLACEHOLDER = false;
	
	public enum PlayerColor
	{
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
			
			if(type == PersonType.MAN)
			{
				r1 = (Node) NodeUtils.loadNode("ressources/3d gfx/negermand_"+color.name().toLowerCase()+".jme");
				r1.getLocalScale().set(0.22f, 0.22f, 0.22f);
				r1.getLocalRotation().fromAngles(-FastMath.HALF_PI, FastMath.HALF_PI, 0); //
			}
			else
			{
				r1 = (Node) NodeUtils.loadNode("ressources/3d gfx/negerkvinde_"+color.name().toLowerCase()+".jme");
				r1.getLocalScale().set(0.22f, 0.22f, 0.22f);
				r1.getLocalRotation().fromAngles(-FastMath.HALF_PI, FastMath.HALF_PI, 0); //
			}
				
			SpatialTransformer kc;
			if (r1 != null && r1.getControllerCount() >= 1 && r1.getController(0) != null)
			{
				kc = (SpatialTransformer) r1.getController(0);
				kc.setSpeed(10);
				kc.setRepeatType(Controller.RT_WRAP);
			}

			return r1;
		}
	}

	public PlayerColor getColor()
	{
		return color;
	}
}
