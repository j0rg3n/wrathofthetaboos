package net.hokuspokus.wott.utils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Hashtable;

import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class TextureUtil
{
	/** This is set to true if we want to load lowres textures (fast loading) */
	public static final boolean USE_LOWRES_IF_AVAIL = true;

	private static TextureUtil instance;
	
	Hashtable<String, WeakReference<TextureState>>     cached_texture_states = new Hashtable<String, WeakReference<TextureState>>();
	private AlphaState general_alphastate = null;
	private MaterialState general_diffuse_material = null;
	
	
	/**
	 * Will clear our texture-state cache, and the texturemanagers cache.
	 */
	public void clearCache()
	{
		TextureManager.clearCache();
		cached_texture_states.clear();
	}

	/**
	 * This simple method just sets up alpha-blending the way most ppl want it.
	 * 
	 * @param s
	 */
	public void setAlphaBlending(Spatial s)
	{
		if(general_alphastate == null)
		{
	        general_alphastate = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
	        general_alphastate.setBlendEnabled(true);
	        general_alphastate.setSrcFunction(AlphaState.SB_SRC_ALPHA);
	        general_alphastate.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
	        general_alphastate.setTestEnabled(true);
	        general_alphastate.setTestFunction(AlphaState.TF_ALWAYS);
	        general_alphastate.setEnabled(true);
		}
        
		s.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        s.setRenderState(general_alphastate);
	}
	
	public static void clearRenderStateRecursively(Spatial s, int state)
	{
		s.clearRenderState(state);
		if((s.getType() & SceneElement.NODE) != 0)
		{
			Node node = (Node)s;
			for(Object o : node.getChildren())
			{
				Spatial sc = (Spatial)o;
				//System.out.println("Looking at: "+s.getName()+":"+s.getClass().getName());
				clearRenderStateRecursively(sc, state);
			}
		}
	}

	/**
	 * Call this method when you just want to apply a texture to some spatial.
	 * 
	 * @param s
	 * @param tex_filename
	 */
	public void setTexture(Spatial s, String tex_filename)
	{
		setTexture(s, tex_filename, false);
	}
	
	public void setTexture(Spatial s, String tex_filename, boolean flip)
	{
		TextureState ts = getTextureState(tex_filename, flip);
		s.setRenderState(ts);
	}
	
	
	
	public void setMultiplyTexture(Spatial s, String parch_tex, float parch_scale, String overlay_tex, float overlay_scale)
	{
		TextureState ts = createTextureState();
		ts.setEnabled(true);
		
		URL res1 = getResource(parch_tex);
		//Texture t1 = TextureManager.loadTexture(res1, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, ts.getMaxAnisotropic(), false);
		Texture t1 = TextureManager.loadTexture(res1, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
        ts.setTexture(t1, 0);
        
		URL res2 = getResource(overlay_tex);
		//Texture t2 = TextureManager.loadTexture(res2, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, ts.getMaxAnisotropic(), false);
		Texture t2 = TextureManager.loadTexture(res2, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
        ts.setTexture(t2, 1);

        t1.setApply(Texture.AM_COMBINE);
        t1.setCombineFuncRGB(Texture.ACF_REPLACE);
        t1.setCombineSrc0RGB(Texture.ACS_TEXTURE);
        t1.setCombineOp0RGB(Texture.ACO_SRC_COLOR);
        t1.setCombineScaleRGB(1.0f);
        
        t1.setWrap(Texture.WM_WRAP_S_WRAP_T);
        t1.setScale(new Vector3f(parch_scale, parch_scale, parch_scale));

        t2.setApply(Texture.AM_COMBINE);
        t2.setCombineFuncRGB(Texture.ACF_MODULATE);
        t2.setCombineSrc0RGB(Texture.ACS_TEXTURE);
        t2.setCombineOp0RGB(Texture.ACO_SRC_COLOR);
        t2.setCombineSrc1RGB(Texture.ACS_PREVIOUS);
        t2.setCombineOp1RGB(Texture.ACO_SRC_COLOR);
        t2.setCombineScaleRGB(1.0f);

        t2.setWrap(Texture.WM_WRAP_S_WRAP_T);
        //t2.setScale(new Vector3f(overlay_scale, overlay_scale, overlay_scale));
        
		
        //s.setTextureCombineMode(TextureState.REPLACE);
        
		s.setRenderState(ts);
		s.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	}

	
	public URL getResource(String res)
	{
		URL url = TextureUtil.class.getResource(res);
		if(url == null)
		{
			url = TextureUtil.class.getClassLoader().getResource(res);
		}
		return url;
	}

	/**
	 * This method will return a new texture-state with the given texture as the only one.
	 * It returns a cached version if it has one, and null if the file does not exist.
	 * 
	 * @param tex_filename
	 * @param flip 
	 * @return
	 */
	public TextureState getTextureState(String tex_filename)
	{
		return getTextureState(tex_filename, false);
	}
	
	public TextureState getTextureState(String tex_filename, boolean flip)
	{
		WeakReference<TextureState> ts_ref = cached_texture_states.get(tex_filename);
		TextureState ts = null;
		if(ts_ref == null || (ts = ts_ref.get()) != null)
		{
			URL res = getResource(tex_filename);
			if(res != null)
			{
				ts = createTextureState();
				Texture tex = TextureManager.loadTexture(res, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, ts.getMaxAnisotropic(), flip);
				tex.setWrap(Texture.WM_WRAP_S_WRAP_T);
				ts.setTexture(tex);
				
				cached_texture_states.put(tex_filename, new WeakReference<TextureState>(ts));
			}
			else
			{
				throw new RuntimeException("Could not locate: "+tex_filename);
			}
		}
		else
		{
			//System.out.println("Using Cached TextureState: "+ts);
		}
		return ts;
	}
	
	/**
	 * Simple wrapper for creating the render-states.
	 * @return
	 */
	private TextureState createTextureState()
	{
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setCorrection(TextureState.CM_PERSPECTIVE);
		ts.setEnabled(true);
		return ts;
	}
	
	/**
	 * This method simply creates a texture from an Image.
	 * @param image
	 * @param anislevel
	 * @return
	 */
	public static Texture createTexture(Image image, float anislevel)
	{
        Texture texture = new Texture();
        texture.setAnisoLevel(anislevel);
        texture.setFilter(Texture.FM_LINEAR);
        texture.setImage(image);
        texture.setMipmapState(Texture.MM_LINEAR_LINEAR);
        texture.setWrap(Texture.WM_WRAP_S_WRAP_T);
        //texture.setMipmapState(Texture.MM_LINEAR);
        return texture;
	}
	

	public void setDiffuseMaterial(Spatial s)
	{
		if(general_diffuse_material == null)
		{
			general_diffuse_material = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
			general_diffuse_material.setEnabled(true);
			general_diffuse_material.setColorMaterial(MaterialState.CM_DIFFUSE);
		}
		s.setRenderState(general_diffuse_material);
	}

	public static TextureUtil getInstance()
	{
		if(instance == null)
			instance = new TextureUtil();
		return instance;
	}

	public void disableZBuffer(Spatial foo, boolean enabled, boolean writable) {
		ZBufferState zState = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zState.setEnabled(enabled);
		zState.setWritable(writable);
		foo.setRenderState(zState);
	}

	public static Quad getFullscreenQuad(String name, String path) {
		int sWidth = DisplaySystem.getDisplaySystem().getWidth();
		int sHeight = DisplaySystem.getDisplaySystem().getHeight();
		
		Quad foo = new Quad(name, sWidth, sHeight);
		foo.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		foo.setLightCombineMode(LightState.OFF);
		foo.setLocalTranslation(sWidth / 2.0f, sHeight / 2.0f, 0.0f);
		foo.setLocalRotation(new Quaternion(new float[]{ FastMath.PI, 0, 0 }));
		getInstance().disableZBuffer(foo, false, false);
		getInstance().setTexture(foo, path);
		
		return foo;
	}

	public static Quad getQuad(String name, String path, int tWidth, int tHeight) {
		Quad foo = new Quad(name, tWidth, tHeight);
		foo.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		foo.setLightCombineMode(LightState.OFF);
		foo.setLocalRotation(new Quaternion(new float[]{ FastMath.PI, 0, 0 }));
		getInstance().disableZBuffer(foo, false, false);
		getInstance().setTexture(foo, path);
		
		return foo;
	}

	public static Quad getTransparentQuad(String name, String path, int tWidth, int tHeight) {
		Quad foo = TextureUtil.getQuad(name, path, tWidth, tHeight);
		TextureUtil.getInstance().setAlphaBlending(foo);
		foo.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		return foo;
	}

	/*
	public TextureState createTextureState(String img1, String img2, CombineMethod method)
	{
		Image img = ImageUtils.combine(img1, img2, method);
		
		TextureState ts = createTextureState();
		Texture t = TextureUtil.createTexture(img, ts.getMaxAnisotropic());
		ts.setTexture(t);
		
		return ts;
	}

	public TextureState createTextureState(Image img1, Image img2)
	{
		return createTextureState(img1, img2, CombineMethod.NORMAL_LAYER);
	}
	
	public TextureState createTextureState(Image img1, String img2)
	{
		return createTextureState(img1, img2, CombineMethod.NORMAL_LAYER);
	}
	
	public TextureState createTextureState(String img1, String img2)
	{
		return createTextureState(img1, img2, CombineMethod.NORMAL_LAYER);
	}
	
	public TextureState createTextureState(String img1, String img2, String img3, String img4)
	{
		TextureState ts = createTextureState();
		Texture t = null;
		Image res1 = ImageUtils.combine(img1, img2, ImageUtils.CombineMethod.NORMAL_LAYER);
		
		if (img3 == null)
			t = TextureUtil.createTexture(res1, ts.getMaxAnisotropic());
		else
		{
			Image res2 = ImageUtils.combine(res1, img3, ImageUtils.CombineMethod.NORMAL_LAYER);
			if (img4 == null)
				t = TextureUtil.createTexture(res2, ts.getMaxAnisotropic());
			else
			{
				Image res3 = ImageUtils.combine(res2, img4, ImageUtils.CombineMethod.NORMAL_LAYER);
				t = TextureUtil.createTexture(res3, ts.getMaxAnisotropic());
			}
		}
		ts.setTexture(t);
		
		return ts;
	}
	*/
	
	
	/**
	 * This method will free the Texture Target and remove the cached texture.
	 * 
	 * @param frame
	 * @param bottom
	 * @param top
	 * @param pattern
	 * @param texture
	 * @param icon
	public void deleteFlagTexture(FlagTextureDesc desc)
	{
		// TODO: implement (its kinda like the TextureManager of jMonkeyEngine)
	}
	 */


}
