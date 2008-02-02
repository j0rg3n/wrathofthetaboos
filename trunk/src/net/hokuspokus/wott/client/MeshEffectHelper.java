package net.hokuspokus.wott.client;

import java.util.Vector;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class MeshEffectHelper
{
	
	public static void explodeNode(final Node explodeNode)
	{
		Node parent = explodeNode.getParent();
		explodeNode.removeFromParent();
		
		// Extract meshes
		final Vector<Geometry> meshes = new Vector<Geometry>();
		recursivelyAddMeshes(explodeNode, meshes);
		explodeNode.detachAllChildren();
		
		explodeNode.setLocalTranslation(explodeNode.getLocalTranslation());
		explodeNode.addController(new Controller()
		{
			float total_time = 0;
			@Override
			public void update(float time)
			{
				total_time += time;
				if(total_time > 2)
				{
					explodeNode.removeFromParent();
				}
			}
		});
		for(Geometry g : meshes)
		{
			g.removeFromParent();
			g.addController(new LimbExplodeController(g));
			explodeNode.attachChild(g);
		}
		parent.attachChild(explodeNode);
		explodeNode.updateRenderState();
	}

	private static void recursivelyAddMeshes(Node meshes_node, Vector<Geometry> meshes)
	{
		if(meshes_node.getChildren() != null)
		{
			for(Spatial s : meshes_node.getChildren())
			{
				if(s == null)
					continue;
				if(s instanceof Geometry)
				{
					meshes.add((Geometry) s);
				}
				else if (s instanceof Node)
				{
					recursivelyAddMeshes((Node) s, meshes);
				}
			}
		}
	}
}

class LimbExplodeController extends Controller
{
	private Vector3f velocity;
	private Geometry geom;
	private Quaternion angular;
	private static Vector3f _tmpVec = new Vector3f();
	private static Quaternion _tmpAng = new Quaternion();

	public LimbExplodeController(Geometry g)
	{
		this.geom = g;
		this.velocity = new Vector3f((float)(Math.random()-0.5),1,(float)(Math.random()-0.5));
		this.velocity.normalizeLocal().multLocal((float) (4+Math.random()*2));
		this.angular = new Quaternion();
		angular.fromAngles(FastMath.TWO_PI*FastMath.rand.nextFloat(), FastMath.TWO_PI*FastMath.rand.nextFloat(), FastMath.TWO_PI*FastMath.rand.nextFloat());
	}

	@Override
	public void update(float time)
	{
		geom.getLocalTranslation().addLocal(_tmpVec.set(velocity).multLocal(time));
		geom.getLocalRotation().multLocal(_tmpAng.set(angular).multLocal(time));
	}
	
}
