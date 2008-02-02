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
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
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
import com.jme.scene.Text;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class WrathOfTaboo extends SimpleGame
{
	private static final String NEXT_TABOO_BINDING = "next_taboo";
	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	private InputHandler old_fps_input;
	private PukInputHandler real_input;
	private static WrathOfTaboo singleton;
	
	TurnTimer timer;
	
	private static final int MANCOUNT = 5;
	private static final int WOMANCOUNT = 5;
	
	static
	{
		try
		{
			// I this fails, you must add "WrathOfTheTaboo" to you classpath (in the run... dialog)
			LogManager.getLogManager().readConfiguration(WrathOfTaboo.class.getResourceAsStream("/logging.properties"));
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
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
		
		timer = new TurnTimer();
        rootNode.attachChild(timer.getRootNode());
		
		// Make sure the camera starts in position
		initCameraPosition();
		
        KeyBindingManager.getKeyBindingManager().set( "toggle_input_handler", KeyInput.KEY_F12 );		
        KeyBindingManager.getKeyBindingManager().set( NEXT_TABOO_BINDING, KeyInput.KEY_F11 );		
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
		selectorNode = selector.getRootNode();
		
		selectorNode.updateGeometricState( 0.0f, true );
        selectorNode.updateRenderState();
        
        rootNode.attachChild(selectorNode);
	}

	private void createNewBoard()
	{
		board = new Board(10, 10);
		p1 = new Player(PlayerColor.RED);
		p2 = new Player(PlayerColor.BLUE);
		
		boardNode.detachAllChildren();
		
		for (Player p : new Player[]{ p1, p2 }) {
			
			List<Person> people = new LinkedList<Person>();
			
			for (int i = 0; i < MANCOUNT; ++i) {
				people.add(new Person(p, PersonType.MAN));
			}

			for (int i = 0; i < WOMANCOUNT; ++i) {
				people.add(new Person(p, PersonType.WOMAN));
			}	

			for(Person person : people)
			{
				boardNode.attachChild(person.getGeometry());
				
				if (Board.SHOW_CELL_MEMBERSHIP) 
					boardNode.attachChild(person.getCellPointerGeometry());
				
				board.addPiece(person);
			}
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
        
        board.markViolators(selector.getCurrent());

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                NEXT_TABOO_BINDING, false ) || timer.isTimeOut()) {
        	selector.next();
        	timer.reset(15);
        	
        	board.killViolators();
        }

		// Upate game-logic
		board.update();

		// Draw taboo selector
		selector.update();
		
		// Update timer
		timer.update();
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