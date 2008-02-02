package net.hokuspokus.wott.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.jme.animation.AnimationController;
import com.jme.animation.Bone;
import com.jme.animation.BoneAnimation;
import com.jme.app.BaseSimpleGame;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.model.converters.MaxToJme;

public class ConvertColladaToJmeBin extends BaseSimpleGame
{
	private final static Logger logger = Logger.getLogger(ConvertColladaToJmeBin.class.getName());
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		double test1 = 0;
		double test2 = 0;
		test1 /= test2;
		System.out.println("test1:"+test1);
		new ConvertColladaToJmeBin().start();
	}

	@Override
	protected void simpleInitGame()
	{
		convertColladaFiles(new File("ressources"));
		finish();
	}

	private void convertColladaFiles(File file)
	{
		if(file.isDirectory())
		{
			for(File s : file.listFiles())
			{
				convertColladaFiles(s);
			}
		}
		else if (file.isFile())
		{
			if(file.getName().endsWith(".dae") || file.getName().endsWith(".DAE") )
			{
				File jme_file = new File(file.getAbsolutePath().replace(".dae", ".jme").replace(".DAE", ".jme"));
				//if(file.getAbsoluteFile().toString().contains("army_Flying.dae"))
				if(!jme_file.exists() || jme_file.lastModified() < file.lastModified())
				{
					convertColladaFile(file, jme_file);
				}
			}
			else if (file.getName().endsWith(".3DS") || file.getName().endsWith(".3ds"))
			{
				//URL model = Player.class.getClassLoader().getResource("ressources/3d gfx/" + (type == PersonType.WOMAN ? "mini_negerkvinde.3DS" : "Mand2.3DS"));
				File jme_file = new File(file.getAbsolutePath().replace(".3DS", ".jme").replace(".3ds", ".jme"));
				if(!jme_file.exists() || jme_file.lastModified() < file.lastModified())
				{
					convert3DSFile(file, jme_file);
				}

			}
		}
	}

	private void convert3DSFile(File from_file, File jme_file)
	{
		try
		{
			logger.info("Converting "+from_file+" to "+jme_file);
			MaxToJme C1 = new MaxToJme();
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			C1.convert(new BufferedInputStream(new FileInputStream(from_file)), BO);
			Savable node = BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			BinaryExporter be = BinaryExporter.getInstance();
			be.save(node, jme_file);
		}
		catch(IOException e)
		{
			
		}
	}

	private void convertColladaFile(File from_file, File jme_file)
	{
		try
		{
			logger.info("Converting "+from_file+" to "+jme_file);
			ColladaImporter.load(new FileInputStream(from_file), from_file.getName());
			Node node = ColladaImporter.getModel();
			NodeUtils.printNodeStructure(node);
			ArrayList<String> animations = ColladaImporter.getControllerNames();
	        if(animations != null && animations.size() > 0)
	        {
		        logger.info("Number of animations: " + animations.size());
		        for(int i = 0; i < animations.size(); i++)
		        {
		            logger.info("Animation["+i+"]:"+animations.get(i));
		        }
		        
		        Bone skel = (Bone) NodeUtils.getFirstChildSubNamed(node, "", SceneElement.NODE, Bone.class);
		        
		        //Obtain the animation from the file by name
		        BoneAnimation anim1 = ColladaImporter.getAnimationController(animations.get(0));
		        //System.out.println("anim1:"+);
			        
		        //set up a new animation controller with our BoneAnimation
		        AnimationController ac = new AnimationController();
		        ac.addAnimation(anim1);
		        ac.setRepeatType(Controller.RT_WRAP);
		        ac.setActive(true);
		        ac.setActiveAnimation(anim1);
			        
		        if(skel != null)
		        {
			        //assign the animation controller to our skeleton
			        skel.addController(ac);
		        }
		        else
		        {
		        	String nodeId = anim1.getBoneTransforms().get(0).getBoneId();
		        	Spatial s = NodeUtils.getFirstChildSubNamed(node, nodeId, SceneElement.NODE, Spatial.class);
		        	if(s != null)
		        	{
		        		s.addController(ac);
		        	}
		        	else
		        	{
		        		throw new RuntimeException("Could not find: "+nodeId);
		        	}
		        }
	        }
	        NodeUtils.printNodeStructure(node);
	        for(int i = RenderState.RS_ALPHA; i < RenderState.RS_MAX_STATE; i++)
	        {
	        	NodeUtils.clearRenderState(node, i);
	        }
	        NodeUtils.removeBoundingVolumes(node);
			
			BinaryExporter be = BinaryExporter.getInstance();
			be.save(node, jme_file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
