package net.hokuspokus.wott.common;

import java.util.LinkedList;
import java.util.List;
import net.hokuspokus.wott.client.PukInputHandler;
import net.hokuspokus.wott.client.WrathOfTaboo;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.common.Player.PlayerColor;
import net.hokuspokus.wott.utils.NodeUtils;
import net.hokuspokus.wott.utils.TextureUtil;
import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.RenderState;

public class PlayingMode extends GameMode {

	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	TurnTimer timer;
	private PukInputHandler real_input;
	Quad background;
	boolean choosingTaboo;
	
	TabooBar tabooBar;
	
	private static final int MANCOUNT = 15;
	private static final int WOMANCOUNT = 15;
	
	public PlayingMode(WrathOfTaboo game) {
		
		super(game);
		
		background = TextureUtil.getFullscreenQuad("bg", 
				"/ressources/2d gfx/background.jpg");
		
		game.getBgRootNode().attachChild(background);
		
		tabooBar = new TabooBar();
		game.getBgRootNode().attachChild(tabooBar.getRootNode());
		
		boardNode = new Node();
		selectorNode = new Node();
		
		rootNode.attachChild(boardNode);
		rootNode.attachChild(selectorNode);
		
		createNewBoard();
		
		createSurroundings();
		
		createNewSelector();

		timer = new TurnTimer();
        rootNode.attachChild(timer.getRootNode());

		// Make sure the camera starts in position
        real_input = new PukInputHandler(this);

		timer.reset(board.getRoundTime());
		//choosingTaboo = false;
	}
	
	private void createSurroundings()
	{
		Node pyramid = (Node)NodeUtils.loadNode("ressources/3d gfx/test.jme");
		pyramid.setLocalScale(new Vector3f(board.getWidth()/2, board.getHeight()/2, board.getWidth()/2));
		pyramid.getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
		//TextureUtil.getInstance().setMultiplyTexture(pyramid, "ressources//2d gfx/dirt.jpg", 10, "ressources//2d gfx/dirt_overlay.jpg", 1f);
		TextureUtil.getInstance().setTexture(pyramid, "/ressources/3d gfx/GRYDIRT2.JPG");
		pyramid.setLocalTranslation(new Vector3f(0f,6.2f,0f));
		rootNode.attachChild(pyramid);
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
		p1 = new Player(PlayerColor.RED, 
				"/ressources/2d gfx/god_a_big.png",
				"/ressources/2d gfx/god_a_small.png");
		p2 = new Player(PlayerColor.BLUE,
				"/ressources/2d gfx/god_b_big.png",
				"/ressources/2d gfx/god_b_small.png");
		
		game.getBgRootNode().attachChild(p1.bigIcon);
		game.getBgRootNode().attachChild(p2.bigIcon);
		game.getBgRootNode().attachChild(p1.smallIcon);
		game.getBgRootNode().attachChild(p2.smallIcon);
		
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

		float t = (System.currentTimeMillis() % 1000) * 2.0f / 1000.0f * FastMath.PI;

		p1.smallIcon.setLocalTranslation(game.getDisplay().getWidth() * 0.25f, 
				game.getDisplay().getHeight() * (0.9f + FastMath.sin(t) * .01f), 0);
		p2.smallIcon.setLocalTranslation(game.getDisplay().getWidth() * 0.75f, 
				game.getDisplay().getHeight() * (0.9f + FastMath.cos(t) * .01f), 0);
        
		board.markViolators(selector.getCurrent());

        if (!board.isDone()) {
	        if ( timer.isTimeOut()) {
	        	
	        	selector.next();
	        	timer.reset(board.getRoundTime());
	        	
	        	board.killViolators();
	        }
        } else {
        	
        }
        

		// Upate game-logic
		board.update();

		// Draw taboo selector
		selector.update();
		tabooBar.update();
		
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
