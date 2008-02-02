package net.hokuspokus.wott.common;

import java.util.LinkedList;
import java.util.List;

import net.hokuspokus.wott.client.PukInputHandler;
import net.hokuspokus.wott.client.WrathOfTaboo;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.common.Player.PlayerColor;
import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.app.BaseGame;
import com.jme.app.SimpleGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Quad;

public class PlayingMode extends GameMode {

	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	TurnTimer timer;
	private PukInputHandler real_input;
	
	private static final int MANCOUNT = 15;
	private static final int WOMANCOUNT = 15;
	
	public PlayingMode(WrathOfTaboo game) {
		
		super(game);
		
		boardNode = new Node();
		selectorNode = new Node();
		
		//rootNode.setLocalRotation(new Quaternion(new float[]{ (float)Math.PI * 1.0f / 4.0f , 0, 0 }));
		
		rootNode.attachChild(boardNode);
		rootNode.attachChild(selectorNode);
		
		createNewBoard();
		
		createNewSelector();

		timer = new TurnTimer();
        rootNode.attachChild(timer.getRootNode());

		// Make sure the camera starts in position
        real_input = new PukInputHandler(this);

		timer.reset(board.getRoundTime());
	}
	
	public InputHandler initInput() {
		return real_input;	
	}

	public Player getPlayer(int index) {
		return index == 0 ? p1 : p2;
	}
	
	public void initCameraPosition(Camera cam)
	{		
		cam.setDirection(cam.getDirection().set(0, -1, -1).normalizeLocal());
		cam.setLocation(cam.getLocation().set(board.getWidth()/2, board.getHeight()*1.0f, board.getHeight()*1.5f));
		cam.setLeft(cam.getLeft().set(-1, 0, 0));
		cam.setUp(cam.getUp().set(0, 1, -1).normalizeLocal());
	}

	private void createNewSelector() {
		
		selector = new TabooSelector();
		selectorNode = selector.getRootNode();
		
		selectorNode.setLocalTranslation(0, (game.getDisplay().getHeight() + selector.getHeight()) / 2.0f, 0);
		
		selectorNode.updateGeometricState( 0.0f, true );
        selectorNode.updateRenderState();
        
        rootNode.attachChild(selectorNode);
	}

	private void createNewBoard()
	{
		board = new Board(10, 10, game);
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
	
	public Node getBoardNode() {
		return boardNode;
	}

	public void update() {
        board.markViolators(selector.getCurrent());

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                WrathOfTaboo.NEXT_TABOO_BINDING, false ) || timer.isTimeOut()) {
        	selector.next();
        	timer.reset(board.getRoundTime());
        	
        	board.killViolators();
        }

		// Upate game-logic
		board.update();

		// Draw taboo selector
		selector.update();
		
		// Update timer
		timer.update();
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public boolean isDone() {
		return board.isDone();
	}
}
