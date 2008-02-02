package net.hokuspokus.wott.client;

import java.net.URL;
import net.hokuspokus.wott.common.Board;
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
import net.hokuspokus.wott.common.TabooDisplay;
import net.hokuspokus.wott.common.TabooSelector;

import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.Mouse;
import com.jme.input.MouseInput;
import com.jme.input.controls.controller.CameraController;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;

public class WrathOfTaboo extends SimpleGame
{
	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	private InputHandler old_fps_input;
	private PukInputHandler real_input;
	private static WrathOfTaboo singleton;
	
	public static void main(String[] args)
	{
		singleton = new WrathOfTaboo();
		singleton.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG, (URL)null);
		singleton.start();
	}
	
	@Override
	protected void simpleInitGame()
	{
		//rootNode = new Node();
		boardNode = new Node();
		selectorNode = new Node();
		
		//rootNode.setLocalRotation(new Quaternion(new float[]{ (float)Math.PI * 1.0f / 4.0f , 0, 0 }));
		
		rootNode.attachChild(boardNode);
		rootNode.attachChild(selectorNode);
		createNewBoard();
		
		createNewSelector();
		
		// Kill the first-person input
		old_fps_input = input;
		input = real_input = new PukInputHandler(this);
		//Mouse
		
		// Make sure the camera starts in position
		initCameraPosition();
		
        KeyBindingManager.getKeyBindingManager().set( "toggle_input_handler", KeyInput.KEY_F12 );		
	}
	
	
	private void initCameraPosition()
	{
		cam.setDirection(cam.getDirection().set(0, -1, -1).normalizeLocal());
		cam.setLocation(cam.getLocation().set(board.getWidth()/2, board.getHeight()*1.0f, board.getHeight()*1.5f));
		cam.setLeft(cam.getLeft().set(-1, 0, 0));
		cam.setUp(cam.getUp().set(0, 1, -1).normalizeLocal());
	}

	private void createNewSelector() {
		
		selector = new TabooSelector();
		
		for (TabooDisplay d : selector.getDisplays()) {
			selectorNode.attachChild(d.getGeometry());
		}
		
	}

	private void createNewBoard()
	{
		board = new Board(20, 20);
		p1 = new Player(new ColorRGBA(1,0,0,1));
		p2 = new Player(new ColorRGBA(0,0,1,1));
		
		boardNode.detachAllChildren();
		for(Person person : p1.getPopulation())
		{
			boardNode.attachChild(person.getGeometry());
			board.addPiece(person);
		}
		
		for(Person person : p2.getPopulation())
		{
			boardNode.attachChild(person.getGeometry());
			board.addPiece(person);
		}
		
		for(int y = 0; y < board.getHeight(); y++)
		{
			for(int x = 0; x < board.getHeight(); x++)
			{
				boardNode.attachChild(board.getTile(x, y));
			}
		}
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
            	initCameraPosition();
            	input = real_input;
            }
            else
            {
            	MouseInput.get().setCursorVisible(false);
            	input = old_fps_input;
            }
        }
		
		// Upate game-logic
		board.update();
	}

	public static WrathOfTaboo getInstance()
	{
		return singleton;
	}

	public Camera getCamera()
	{
		return cam;
	}

	public Node getBoardNode()
	{
		return boardNode;
	}
}