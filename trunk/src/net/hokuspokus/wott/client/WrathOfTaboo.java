package net.hokuspokus.wott.client;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import sun.rmi.transport.LiveRef;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.hokuspokus.wott.common.Board;
import net.hokuspokus.wott.common.GameMode;
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
import net.hokuspokus.wott.common.PlayingMode;
import net.hokuspokus.wott.common.TabooDisplay;
import net.hokuspokus.wott.common.TabooSelector;
import net.hokuspokus.wott.common.TurnTimer;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.common.Player.PlayerColor;

import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Quaternion;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class WrathOfTaboo extends SimpleGame
{
	public static final String NEXT_TABOO_BINDING = "next_taboo";

	private InputHandler old_fps_input;
	private static WrathOfTaboo singleton;
	
	GameMode currentMode;
	
	public static void main(String[] args)
	{
		singleton = new WrathOfTaboo();
		//singleton.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG, (URL)null);
		singleton.start();
	}
	
	@Override
	protected void simpleInitGame()
	{
		currentMode = new PlayingMode(rootNode);
		
        // Kill the first-person input
		old_fps_input = input;
		input = currentMode.initInput(); 
		//Mouse
        currentMode.initCameraPosition(cam);
		
        KeyBindingManager.getKeyBindingManager().set( "toggle_input_handler", KeyInput.KEY_F12 );		
        KeyBindingManager.getKeyBindingManager().set( NEXT_TABOO_BINDING, KeyInput.KEY_F11 );		
	}

	public DisplaySystem getdisplay()
	{
		return display;
	}
	
	@Override
	protected void simpleRender()
	{
		super.simpleRender();
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
            }
            else
            {
            	MouseInput.get().setCursorVisible(false);
            	input = old_fps_input;
            }
        }

        currentMode.update();
	}

	public static WrathOfTaboo getInstance()
	{
		return singleton;
	}
}