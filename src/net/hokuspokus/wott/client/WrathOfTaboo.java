package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Board;
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
import net.hokuspokus.wott.common.TabooDisplay;
import net.hokuspokus.wott.common.TabooSelector;

import com.jme.app.SimpleGame;
import com.jme.input.InputHandler;
import com.jme.input.Mouse;
import com.jme.input.controls.controller.CameraController;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class WrathOfTaboo extends SimpleGame
{
	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	private InputHandler old_fps_input;
	
	public static void main(String[] args)
	{
		new WrathOfTaboo().start();
	}
	
	@Override
	protected void simpleInitGame()
	{
		rootNode = new Node();
		boardNode = new Node();
		selectorNode = new Node();
		
		//rootNode.setLocalRotation(new Quaternion(new float[]{ (float)Math.PI * 1.0f / 4.0f , 0, 0 }));
		
		rootNode.attachChild(boardNode);
		rootNode.attachChild(selectorNode);
		createNewBoard();
		createNewSelector();
		
		// Kill the first-person input
		old_fps_input = input;
		//input = new InputHandler();
		//Mouse
	}
	
	
	private void createNewSelector() {
		
		selector = new TabooSelector();
		
		for (TabooDisplay d : selector.getDisplays()) {
			selectorNode.attachChild(d.getGeometry());
		}
		
	}

	private void createNewBoard()
	{
		board = new Board(15, 15);
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
	
	@Override
	protected void simpleRender()
	{
		super.simpleRender();
	}
	
	@Override
	protected void simpleUpdate()
	{
		super.simpleUpdate();
		
		board.update();
		
		
		// HACKISH
		for(Person person : p1.getPopulation())
		{
			//person.getGeomtry().setLocalTranslation(person.getP, y, z)
		}
		
		for(Person person : p2.getPopulation())
		{
			//boardNode.attachChild(person.getGeomtry());
		}
	
		
		// 
		System.out.println("dir:"+cam.getDirection());
		cam.setDirection(cam.getDirection().set(0, -1, -1).normalizeLocal());
		System.out.println("loc:"+cam.getLocation());
		cam.setLocation(cam.getLocation().set(board.getWidth()/2, board.getHeight()*1.0f, board.getHeight()*1.5f));
		System.out.println("lef:"+cam.getLeft());
		cam.setLeft(cam.getLeft().set(-1, 0, 0));
		System.out.println("up: "+cam.getUp());
		cam.setUp(cam.getUp().set(0, 1, -1).normalizeLocal());
		
	}
}