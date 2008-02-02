package net.hokuspokus.wott.client;

import java.util.Vector;

import net.hokuspokus.wott.utils.NodeUtils;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class MeshEffectHelper
{
	
	public static void explodeNode(final Node explodeNode, Camera cam)
	{
		Node parent = explodeNode.getParent();
		explodeNode.removeFromParent();
		
		// Extract meshes
		//NodeUtils.removeControllers(explodeNode);
		final Vector<Geometry> meshes = new Vector<Geometry>();
		recursivelyAddMeshes(explodeNode, meshes);
		//explodeNode.detachAllChildren();
		
		explodeNode.setLocalTranslation(explodeNode.getLocalTranslation());
		explodeNode.addController(new Controller()
		{
			float total_time = 0;
			@Override
			public void update(float time)
			{
				total_time += time;
				if(total_time > 20)
				{
					explodeNode.removeFromParent();
				}
			}
		});
		for(Geometry g : meshes)
		{
			g.clearControllers();
			g.addController(new LimbExplodeController(g));
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
	private Vector3f worldScale = new Vector3f();
	private int bounceCount = 0;
	private static Vector3f _tmpVec = new Vector3f();
	private static Quaternion _tmpAng = new Quaternion();

	public LimbExplodeController(Geometry g)
	{
		this.geom = g;
		
		this.worldScale.set(g.getParent().getWorldScale());
		this.velocity = new Vector3f().set(g.getLocalTranslation())
        		.addLocal(new Vector3f(FastMath.rand.nextFloat()*2-1f,FastMath.rand.nextFloat()*2-1f,5.5f+FastMath.rand.nextFloat()*2)
        		.divideLocal(worldScale));

		this.angular = new Quaternion();
		angular.fromAngles(
				FastMath.TWO_PI*FastMath.rand.nextFloat(), 
				FastMath.TWO_PI*FastMath.rand.nextFloat(), 
				FastMath.TWO_PI*FastMath.rand.nextFloat());
	}

	@Override
	public void update(float time)
	{
		velocity.z -= time * 9.82f / worldScale.z;
		geom.getLocalTranslation().addLocal(_tmpVec.set(velocity).multLocal(time));
		//geom.updateGeometricState(time, false);
		if(velocity.z < 0 && geom.getLocalTranslation().z < 0 && bounceCount++ < 4)
		{
			velocity.z = -velocity.z * 0.60f;
		}
		geom.getLocalRotation().addLocal(_tmpAng.set(angular).multLocal(time));
	}
	
}
