package net.hokuspokus.wott.client;

import java.util.Vector;
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
			//g.removeFromParent();
			g.addController(new LimbExplodeController(g));
			/*
	        SpatialTransformer st = new SpatialTransformer(1);

	        st.setObject(g,0,-1);
	        //st.setObject(s,1,0);

	        Quaternion x0=new Quaternion();
	        x0.fromAngleAxis(0,new Vector3f(0,1,0));
	        Quaternion x90=new Quaternion();
	        x90.fromAngleAxis((float) (Math.PI/2),new Vector3f(1,0,0));
	        Quaternion x180=new Quaternion();
	        x180.fromAngleAxis((float) (Math.PI),new Vector3f(0,1,0));
	        Quaternion x270=new Quaternion();
	        x270.fromAngleAxis((float) (3*Math.PI/2),new Vector3f(0,0,1));

	        st.setRotation(0,0,x0);
	        st.setRotation(0,1,x90);
	        st.setRotation(0,2,x180);
	        st.setRotation(0,3,x270);
	        st.setRotation(0,5,x0);
	        
	        st.setPosition(0,0,new Vector3f().set(g.getLocalTranslation()));
	        //st.setPosition(1,2,new Vector3f(0,0,-5));
	        st.setPosition(0,5,
	        		new Vector3f().set(g.getLocalTranslation())
	        		.addLocal(new Vector3f(FastMath.rand.nextFloat()*20-10,FastMath.rand.nextFloat()*20-10,5)
	        		.divideLocal(g.getParent().getWorldScale())));

	        st.interpolateMissing();
			g.addController(st);
			//explodeNode.attachChild(g);
			*/
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
		geom.getLocalRotation().addLocal(_tmpAng.set(angular).multLocal(time));
	}
	
}
