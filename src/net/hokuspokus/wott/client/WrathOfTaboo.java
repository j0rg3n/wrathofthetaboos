package net.hokuspokus.wott.client;

import java.io.IOException;
import java.util.logging.LogManager;

import net.hokuspokus.wott.common.GameMode;
import net.hokuspokus.wott.common.IntroMode;
import net.hokuspokus.wott.common.PlayingMode;
import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public class WrathOfTaboo extends SimpleGame
{
	public static final String NEXT_TABOO_BINDING = "switch_taboo_now";
	Node environmentNode;
	public static final String START_GAME_BINDING = "next_game_mode";

	private InputHandler old_fps_input;
	private static WrathOfTaboo singleton;
	
	GameMode currentMode;
	SoundCenter soundCenter;
	
	static {
		try
		{
			LogManager.getLogManager().readConfiguration(WrathOfTaboo.class.getClassLoader().getResourceAsStream("logging.properties"));
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		singleton = new WrathOfTaboo();
		//singleton.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG, (URL)null);
		singleton.start();
	}
	
	@Override
	protected void simpleInitGame()
	{
		soundCenter = new SoundCenter(cam);
    	MouseInput.get().setCursorVisible(false);

    	setMode(new IntroMode(this));
        //setMode(new HighscoreMode(this));
        //setMode(new PlayingMode(this));
    	
        KeyBindingManager.getKeyBindingManager().set( "toggle_input_handler", KeyInput.KEY_F12 );
        KeyBindingManager.getKeyBindingManager().set( NEXT_TABOO_BINDING, KeyInput.KEY_F11 );
        KeyBindingManager.getKeyBindingManager().set( START_GAME_BINDING, KeyInput.KEY_SPACE );
	}

	private void setMode(GameMode newMode) {

		rootNode.updateRenderState();
		
		currentMode = newMode;
		
		// Kill the first-person input
		old_fps_input = input;
		InputHandler newInput = currentMode.initInput();
		if (newInput != null) {
			input = newInput;  
		}
		//Mouse
        currentMode.initCameraPosition(cam);

        //initBackgroundAndEnvironment();
	}

	private void initBackgroundAndEnvironment()
	{
		/*
		if(environmentNode != null)
		{
			environmentNode.detachAllChildren();
		}
		else
		{
			environmentNode = new Node("Environment");
			rootNode.attachChild(environmentNode);
		}
		// Create a bottom plane (with earth texture)
		Quad bottomPlane = new Quad("Bottom plane", 100, 100);
		bottomPlane.copyTextureCoords(0, 0, 1);
		TextureUtil.getInstance().setMultiplyTexture(bottomPlane, "/2d gfx/dirt.jpg", 10, "/2d gfx/dirt_overlay.jpg", 1f);
		//setTexture(bottomPlane, "2d gfx/dirt.jpg", new Vector3f());
		bottomPlane.getLocalRotation().fromAngles(FastMath.HALF_PI, 0, 0);
		bottomPlane.getLocalTranslation().set(0, -0.03f, 0);
		environmentNode.attachChild(bottomPlane);
		
		// Set up sides for the game
		Box leftSide = new Box("left border", new Vector3f(-0.2f, 0, 0), new Vector3f(0, 1, board.getHeight()));
		environmentNode.attachChild(leftSide);
		Box rightSide = new Box("right border", new Vector3f(board.getWidth(), 0, 0), new Vector3f(board.getWidth()+0.2f, 1, board.getHeight()));
		environmentNode.attachChild(rightSide);
		Box topSide = new Box("top border", new Vector3f(0, 0, -0.2f), new Vector3f(board.getWidth(), 1, 0));
		environmentNode.attachChild(topSide);
		Box bottomSide = new Box("bottom border", new Vector3f(0, 0, board.getHeight()), new Vector3f(board.getWidth(), 1, board.getHeight()+0.2f));
		environmentNode.attachChild(bottomSide);
		
		// Ensure that all is up2date
		environmentNode.updateRenderState();
		*/
	}

	@Override
	protected void simpleRender()
	{
		super.simpleRender();
	}
	
	public Node getBgRootNode() {
		return bgRootNode;
	}
	
	@Override
	protected void simpleUpdate()
	{
		super.simpleUpdate();
	
		// Change input
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_input_handler", false ) ) {
            if(input instanceof FirstPersonHandler)
            {
            	currentMode.initCameraPosition(cam);
            	input = currentMode.initInput();
            	initBackgroundAndEnvironment();
            }
            else
            {
            	MouseInput.get().setCursorVisible(false);
            	input = old_fps_input;
            }
        }

        if ( currentMode.isDone() ) {
        	
        	rootNode.detachAllChildren();
        	bgRootNode.detachAllChildren();
        	
        	if (currentMode instanceof IntroMode) {
        		setMode(new PlayingMode(this));
        	} else if (currentMode instanceof PlayingMode) {
        		setMode(new IntroMode(this));
        	}
        }
        
        // we must have sound
        soundCenter.updateSounds(cam);
        
        
        // Switch game mode on death.
        currentMode.update();
	}

	public static WrathOfTaboo getInstance()
	{
		return singleton;
	}
	
	public DisplaySystem getDisplay() {
		return display;
	}

	public Node getRootNode() {
		
		return rootNode;
	}

	public Camera getCamera()
	{
		return cam;
	}
	
	public SoundCenter getSoundCenter()
	{
		return soundCenter;
	}
}