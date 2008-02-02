package net.hokuspokus.wott.utils;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.SharedMesh;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

public class NodeUtils
{

	public static final int NODE_STRUCTURE_MATERIALS = 1;
	public static final int NODE_STRUCTURE_TRANS     = 2;
	public static final int NODE_CULLMODE            = 4;
	public static final int NODE_RENDERSTATES 		 = 8;
	public static final int NODE_BOUNDING_VOLUMES    = 16;
	private static Hashtable<String, Node> cached_nodes = new Hashtable<String, Node>();


	/**
	 * This method returns the first node which name contains the given string, using a DFS.
	 * 
	 * @param node
	 * @param i 
	 * @param name
	 * @return
	 */
	public static Spatial getFirstChildSubNamed(Node node, String substr, int type_mask)
	{
		return getFirstChildSubNamed(node, substr, type_mask, null);
	}
	
	/**
	 * This method returns the first node which name contains the given string, using a DFS.
	 * 
	 * @param node
	 * @param i 
	 * @param name
	 * @param cl
	 * @return
	 */
	public static Spatial getFirstChildSubNamed(Node node, String substr, int type_mask, Class<?> cl)
	{
		if(node.getChildren() == null)
			return null;
		for(Object o : node.getChildren())
		{
			Spatial s = (Spatial)o;
			//System.out.println("Looking at: "+s.getName()+":"+s.getClass().getName());
			if(s.getName().contains(substr) && (s.getType() & type_mask) != 0 && (cl == null || cl.isInstance(s)))
				return s;
			if((s.getType() & SceneElement.NODE) != 0)
			{
				Spatial res = getFirstChildSubNamed((Node)s, substr, type_mask, cl);
				if(res != null)
					return res;
			}
		}
		
		return null;
	}
	
	
	public static boolean pruneNodes(Node n)
	{
		// This was an empty node
		for(int i = 0; i < n.getQuantity(); i++)
		{
			Spatial s = n.getChild(i);
			if(s != null && ((s.getType() & SceneElement.NODE) != 0))
			{
				if(pruneNodes((Node)s))
					i--;
			}
		}
		if(n.getQuantity() == 0)
		{
			n.removeFromParent();
			return true;
		}
		return false;
	}

	/**
	 * This method prints the structure of a node.
	 * 
	 * @param node
	 */
	public static void printNodeStructure(Spatial s)
	{
		printNodeStructure(s, 0);
	}
	
	public static void printNodeStructure(Spatial s, int flags)
	{	
		printNodeStructure(s, flags, "");
	}
	
	private static void printNodeStructure(Spatial s, int flags, String indent)
	{	
		System.out.print(indent+s.getName()+"["+s.getClass().getName()+"]");
		if((flags & NODE_STRUCTURE_MATERIALS) != 0)
		{
			System.out.print(" [RenderStates: ");
			System.out.print("(Mat:"+s.getRenderState(RenderState.RS_MATERIAL)+")");
			System.out.print("(Tex:"+s.getRenderState(RenderState.RS_TEXTURE)+")");
			System.out.print("(Light:"+s.getRenderState(RenderState.RS_LIGHT)+")");
			System.out.print("]");
		}
		if((flags & NODE_STRUCTURE_TRANS) != 0)
		{
			System.out.print(" (");
			System.out.print("S:"+s.getLocalScale()+",");
			System.out.print("R:"+s.getLocalRotation()+",");
			System.out.print("T:"+s.getLocalTranslation()+"");
			System.out.print(")");
		}
		if((flags & NODE_CULLMODE) != 0)
		{
			System.out.print(" (");
			System.out.print("CullMode:"+s.getCullMode());
			System.out.print(")");
		}
		if((flags & NODE_RENDERSTATES) != 0)
		{
			System.out.print(" (");
			System.out.print("Texture:"+s.getRenderState(RenderState.RS_TEXTURE));
			System.out.print(")");
		}
		
		if((flags & NODE_BOUNDING_VOLUMES) != 0)
		{
			System.out.print(" (");
			switch(s.getWorldBound().getType())
			{
			case BoundingVolume.BOUNDING_BOX:
				System.out.print("aabb "+ ((BoundingBox)s.getWorldBound()).xExtent+" "+
						 				  ((BoundingBox)s.getWorldBound()).yExtent+" "+
						 				  ((BoundingBox)s.getWorldBound()).zExtent+" ---||--- center "+
						 				  ((BoundingBox)s.getWorldBound()).getCenter());
				break;
			case BoundingVolume.BOUNDING_SPHERE:
				System.out.print("sphere "+ ((BoundingSphere)s.getWorldBound()).getRadius()+" ---||--- center "+
											((BoundingSphere)s.getWorldBound()).getCenter());
				break;
			default :
				System.out.print("some other bounding box");
			break;
			}
			System.out.print(")");
			
		}
		
		
		System.out.println("");
		
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			if(node.getChildren() != null)
			{
				for(Object o : node.getChildren())
				{
					Spatial sc = (Spatial)o;
					//System.out.println("Looking at: "+s.getName()+":"+s.getClass().getName());
					printNodeStructure(sc, flags, indent+"  ");
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	static public <T> T getChildNodeOfType(Spatial s, Class cl)
	{
		if(s == null)
			return null;
		if(cl.isInstance(s))
			return (T)s;

		if((s.getType() & SceneElement.NODE) != 0)
		{
			for(Object o : ((Node)s).getChildren())
			{
				Spatial cs = (Spatial)o;
				T res = (T)getChildNodeOfType(cs, cl);
				if(res != null)
					return res;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	static public <T> T getParentNodeOfType(Spatial s, Class cl)
	{
		if(s == null)
			return null;
		if(cl.isInstance(s))
			return (T)s;
		
		if(s.getParent() == null)
			return null;
		return (T)getParentNodeOfType(s.getParent(), cl);
	}


	/**
	 * This method will recursively extract all nodes that match the type-pattern.
	 * 
	 * @param node
	 * @param geometry
	 * @return
	 */
	public static Vector<Spatial> extractNodes(Spatial s, int node_type)
	{
		Vector<Spatial> res = new Vector<Spatial>();
		if((s.getType() & node_type) != 0)
		{
			res.add(s);
		}
		
		// If we are a node, recurse
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node n = (Node)s;
			for(Object o : n.getChildren())
			{
				if(o != null)
					res.addAll(extractNodes((Spatial)o, node_type));
			}
		}
		
		return res;
	}


	/**
	 * Will recusively add Bounding volumes to all geometries down the tree.
	 * @param node
	 * @param bounding_box
	 */
	public static void addBoundingVolumes(Spatial s, int bound_type)
	{
		if((s.getType() & SceneElement.SHARED_MESH) != 0)
		{
			switch(bound_type)
			{
			case BoundingVolume.BOUNDING_BOX:
			{
				((SharedMesh)s).getTarget().setModelBound(new BoundingBox());
			}
				break;
			case BoundingVolume.BOUNDING_OBB:
				((SharedMesh)s).getTarget().setModelBound(new OrientedBoundingBox());
				break;
			case BoundingVolume.BOUNDING_SPHERE:
			{
				((SharedMesh)s).getTarget().setModelBound(new BoundingSphere());
			}
				break;
			}
			((SharedMesh)s).getTarget().updateModelBound();
			((SharedMesh)s).getTarget().updateWorldBound();
			((SharedMesh)s).updateModelBound();
			((SharedMesh)s).updateWorldBound();
		}
		else if((s.getType() & SceneElement.GEOMETRY) != 0)
		{
			switch(bound_type)
			{
			case BoundingVolume.BOUNDING_BOX:
				((Geometry)s).setModelBound(new BoundingBox());
				break;
			case BoundingVolume.BOUNDING_OBB:
				((Geometry)s).setModelBound(new OrientedBoundingBox());
				break;
			case BoundingVolume.BOUNDING_SPHERE:
				((Geometry)s).setModelBound(new BoundingSphere());
				break;
			}
			((Geometry)s).updateModelBound();
			((Geometry)s).updateWorldBound();
		}
		else if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			for(Object o : node.getChildren())
			{
				Spatial sc = (Spatial)o;
				addBoundingVolumes(sc, bound_type);
			}
			s.updateWorldBound();
		}
	}
	
	public static Spatial loadNode(String nodefile)
	{
		Node retval = cached_nodes.get(nodefile);
		try
		{
	    	File file = new File(nodefile);
	    	SimpleResourceLocator locator = new SimpleResourceLocator(file.getParentFile().toURI());
	        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
			Spatial s = (Spatial) BinaryImporter.getInstance().load(file);
			ResourceLocatorTool.removeResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
			if(s instanceof Node)
			{
				retval = (Node) s;
			}
			else
			{
				return s;
			}
			cached_nodes.put(nodefile, retval);
		}
		catch(IOException e)
		{
			return null;
		}
		return new SharedNode("cachedNode", retval);
	}
	
	
	/**
	 * Will recusively set the render-queue mode on a hierachy of nodes.
	 * @param node
	 * @param bounding_box
	 */
	public static void setRenderQueueMode(Spatial s, int render_queue_mode)
	{
		s.setRenderQueueMode(render_queue_mode);
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			for(Object o : node.getChildren())
			{
				Spatial sc = (Spatial)o;
				setRenderQueueMode(sc, render_queue_mode);
			}
		}
	}
	
	public static void setRenderState(Spatial s, RenderState renderstate)
	{
		s.setRenderState(renderstate);
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			for(Object o : node.getChildren())
			{
				Spatial sc = (Spatial)o;
				setRenderState(sc, renderstate);
			}
		}
	}
	
	public static void clearRenderState(Spatial s, int render_state_mode)
	{
		s.clearRenderState(render_state_mode);
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			if(node.getChildren() != null)
			{
				for(Object o : node.getChildren())
				{
					Spatial sc = (Spatial)o;
					clearRenderState(sc, render_state_mode);
				}
			}
		}
	}
	
	public static void removeBoundingVolumes(Spatial s)
	{
		s.setModelBound(null);
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			if(node.getChildren() != null)
			{
				for(Object o : node.getChildren())
				{
					Spatial sc = (Spatial)o;
					removeBoundingVolumes(sc);
				}
			}
		}
	}
	
	public static void setCullMode(Spatial s, int mode)
	{
		s.setCullMode(mode);
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			for(Object o : node.getChildren())
			{
				Spatial sc = (Spatial)o;
				setCullMode(sc, mode);
			}
		}
	}

	public static void removeAllControllers(Spatial s)
	{
		s.clearControllers();
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			if(node.getChildren() != null)
			{
				for(Object o : node.getChildren())
				{
					Spatial sc = (Spatial)o;
					removeAllControllers(sc);
				}
			}
		}
	}
}
