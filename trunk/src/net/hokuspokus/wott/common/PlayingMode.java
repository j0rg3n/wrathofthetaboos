package net.hokuspokus.wott.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import net.hokuspokus.wott.client.PukInputHandler;
import net.hokuspokus.wott.client.WrathOfTaboo;
import net.hokuspokus.wott.common.Person.PersonType;
import net.hokuspokus.wott.common.Player.PlayerColor;
import net.hokuspokus.wott.common.TabooSelector.TABOO;
import net.hokuspokus.wott.utils.SpriteQuad;
import net.hokuspokus.wott.utils.NodeUtils;
import net.hokuspokus.wott.utils.TextureUtil;
import com.jme.animation.SpatialTransformer;
import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.CameraNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jme.scene.state.RenderState;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.MaxToJme;

public class PlayingMode extends GameMode {

	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	TurnTimer timer;
	private PukInputHandler real_input;
	SpriteQuad background;
	boolean choosingTaboo;
	
	TabooBar tabooBar;
	HOFEntryGizmo hofEntry;
	
	/**
	 * Set this to indicate that we're done.
	 */
	private boolean highscoreEntered = false;
	
	/**
	 * Set this to indicate that the game is over.
	 */
	private boolean gameOver = false;
	private long switchTime;
	private Player winner;
	
	private static final int MANCOUNT = 10;
	private static final int WOMANCOUNT = 10;
	private static final boolean SHOW_TEXT_ONLY_TABOO_DISPLAY = false;
	
	/**
	 * Force immediate game over
	 */
	private static final boolean FORCE_GAME_OVER = false;
	
	public PlayingMode(WrathOfTaboo game) {
		
		super(game);
		
		background = TextureUtil.getFullscreenQuad("bg", 
				"/ressources/2d gfx/background.jpg");
		background.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight(), 0);
		game.getBgRootNode().attachChild(background);
		
		tabooBar = new TabooBar();
		game.getFgRootNode().attachChild(tabooBar.getRootNode());
		
		boardNode = new Node();
		selectorNode = new Node();
		
		rootNode.attachChild(boardNode);
		rootNode.attachChild(selectorNode);
		
		createNewBoard();
		
		createSurroundings();
		
		createNewSelector();

		hofEntry = new HOFEntryGizmo();
		
		timer = new TurnTimer(game);
        rootNode.attachChild(timer.getRootNode());

		// Make sure the camera starts in position
        real_input = new PukInputHandler(this);

		timer.reset(board.getRoundTime());
		//choosingTaboo = false;
	}
	
	private void createSurroundings()
	{
		Node pyramid = (Node)NodeUtils.loadNode("ressources/3d gfx/pyramid43m.jme");
		
		pyramid.setLocalScale(new Vector3f(board.getWidth()/2, board.getHeight()/2, board.getWidth()/2));
		pyramid.getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
		pyramid.setLocalTranslation(new Vector3f(0f,8.7f,0f));
		
		TextureUtil.getInstance().setTexture(pyramid, "/ressources/3d gfx/GRYDIRT2.JPG");
		
		NodeUtils.makeAllAnimationLoopRecursive(pyramid);
		
		rootNode.attachChild(pyramid);
	}

	public InputHandler initInput() {
		return real_input;	
	}

	public Player getPlayer(int index) {
		return index == 0 ? p1 : p2;
	}
	
	@Override
	public void initCameraPosition(Camera cam)
	{
		
		Vector3f cam_pos = new Vector3f(board.getWidth()/2f, board.getHeight()*1.3f, board.getWidth()*1.1f);
		Quaternion rotation = new Quaternion();
		rotation.fromAngles(FastMath.HALF_PI/2-0.2f, FastMath.PI, 0);
		cam.setFrame(cam_pos, rotation);
		/*
		cam.setDirection(cam.getDirection().set(0, -1, -1).normalizeLocal());
		cam.setLocation(cam.getLocation().set(board.getWidth()/2, board.getHeight()*1.0f, board.getHeight()*1.5f));
		cam.setLeft(cam.getLeft().set(-1, 0, 0));
		cam.setUp(cam.getUp().set(0, 1, -1).normalizeLocal());
		*/
	}

	private void createNewSelector() {
		
		selector = new TabooSelector();
		selectorNode = selector.getRootNode();
		
		selectorNode.setLocalTranslation(0, (game.getDisplay().getHeight() - selector.getHeight()) / 2.0f, 0);
		
		selectorNode.updateGeometricState( 0.0f, true );
        selectorNode.updateRenderState();
        
        if (SHOW_TEXT_ONLY_TABOO_DISPLAY) {
        	rootNode.attachChild(selectorNode);
        }
        selector.nextTaboo(board);
	}

	private void createNewBoard()
	{
		board = new Board(15, 8, game);
		p1 = new Player(PlayerColor.RED, 
				"/ressources/2d gfx/god_a_big.png",
				"/ressources/2d gfx/god_a_small.png");
		p2 = new Player(PlayerColor.GREEN,
				"/ressources/2d gfx/god_b_big.png",
				"/ressources/2d gfx/god_b_small.png");
		
		//game.getFgRootNode().attachChild(p1.bigIcon);
		//game.getFgRootNode().attachChild(p2.bigIcon);
		game.getFgRootNode().attachChild(p1.smallIcon);
		game.getFgRootNode().attachChild(p1.pointText);
		game.getFgRootNode().attachChild(p2.smallIcon);
		game.getFgRootNode().attachChild(p2.pointText);
		
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
				
				// place the person
				float x10th = (board.getWidth()/10f);
				if(person.owner == p1)
				{
					person.pos.x = x10th + FastMath.rand.nextFloat()*x10th*2;
					person.pos.y = board.getHeight()*FastMath.rand.nextFloat();
				}
				else
				{
					person.pos.x = x10th*7 + FastMath.rand.nextFloat()*x10th*2;
					person.pos.y = board.getHeight()*FastMath.rand.nextFloat();
				}
				
				board.addPiece(person);
			}
		}
		
		for(int y = 0; y < board.getHeight(); y++)
		{
			for(int x = 0; x < board.getWidth(); x++)
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

		p1.smallIcon.setLocalTranslation(game.getDisplay().getWidth() * 0.02f, 
				game.getDisplay().getHeight() +
				(.05f + FastMath.cos(t) * .05f) * Player.SMALL_ICON_SIZE, 0);
		p1.pointText.getLocalTranslation().set(p1.smallIcon.getLocalTranslation()).addLocal(0,-Player.SMALL_ICON_SIZE,0);
		p1.pointText.updateWorldVectors();
		p2.smallIcon.setLocalTranslation(game.getDisplay().getWidth() * 0.98f - Player.SMALL_ICON_SIZE, 
				game.getDisplay().getHeight() +
				(.05f + FastMath.cos(t) * .05f) * Player.SMALL_ICON_SIZE, 0);
		p2.pointText.getLocalTranslation().set(p2.smallIcon.getLocalTranslation()).addLocal(0,-Player.SMALL_ICON_SIZE,0);
		p2.pointText.updateWorldVectors();
        
		board.markViolators(selector.getCurrent());

		// Ugly: Detect win happening
		if ((FORCE_GAME_OVER || board.isDone()) && !gameOver) {
			gameOver = true;

			winner = board.getWinner();
			if (winner != null) {
				tabooBar.setWinner(winner.getColor());
				
				// Throw the hof entry gizmo into the mix.
				hofEntry.setText("AAA");
				hofEntry.setLocalTranslation(
						(DisplaySystem.getDisplaySystem().getWidth() - hofEntry.getWidth()) * .5f, 
						(DisplaySystem.getDisplaySystem().getHeight() - hofEntry.getHeight()) * .5f, 0);
				game.getFgRootNode().attachChild(hofEntry);
				switchTime = System.currentTimeMillis()+600000; // 10 minutes to type your name
			} else {
				tabooBar.setNoWinner();
				switchTime = System.currentTimeMillis()+2500; // auto-swith in 2.5 seconds
			}
		}
		
		if (!gameOver) {
		
	        if ( timer.isTimeOut()) {
	        	
	        	p1.points += board.getPoints(p1);
	        	p2.points += board.getPoints(p2);
	        	board.killViolatorsAndIncreaseTurn();
	        	
	        	if(!board.isDone())
	        	{
		        	timer.reset(board.getRoundTime());
		        	selector.nextTaboo(board);
	        	}
	        	
	        	p1.pointText.print(""+p1.points);
	        	p2.pointText.print(""+p2.points);
	        }
	        tabooBar.setActiveTaboo(selector.getCurrent());

	        
			// Upate game-logic
			board.update();

			// Draw taboo selector
			selector.update();
			
			// Update timer
			timer.update();
			
        } else {

        	// nothing for now.
        	hofEntry.update();
        }
		tabooBar.update();
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public boolean isDone() {
		if(highscoreEntered)
			return true;
		return isGameOver() && System.currentTimeMillis() > switchTime;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public HOFEntryGizmo getHOFEntry() {
		return hofEntry;
	}

	public void setEntryDone(boolean b) {
		if (highscoreEntered != b) {
			highscoreEntered = b;
			
			if (b && (board.getWinner() != null)) {
				game.getHighscore().addHighscore(hofEntry.getText(), winner.points);
			} else {
				//game.getHighscore().addHighscore("ALL", 0);
			}
		}
	}
}
