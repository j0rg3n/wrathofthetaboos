package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Board;
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
import net.hokuspokus.wott.common.TabooDisplay;
import net.hokuspokus.wott.common.TabooSelector;

import com.jme.app.SimpleGame;
import com.jme.input.controls.controller.CameraController;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;

public class WrathOfTaboo extends SimpleGame
{
	Board board;
	Player p1;
	Player p2;
	private Node boardNode;
	Node selectorNode;
	private TabooSelector selector;
	
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
		
		rootNode.setLocalRotation(new Quaternion(new float[]{ (float)Math.PI * 1.0f / 4.0f , 0, 0 }));
		
		rootNode.attachChild(boardNode);
		rootNode.attachChild(selectorNode);
		createNewBoard();
		createNewSelector();
	}
	
	
	private void createNewSelector() {
		
		selector = new TabooSelector();
		
		for (TabooDisplay d : selector.getDisplays()) {
			selectorNode.attachChild(d.getGeometry());
		}
		
	}

	private void createNewBoard()
	{
		board = new Board();
		p1 = new Player(new ColorRGBA(1,0,0,1));
		p2 = new Player(new ColorRGBA(0,0,1,1));
		
		for(Person person : p1.getPopulation())
		{
			boardNode.attachChild(person.getGeometry());
		}
		
		for(Person person : p2.getPopulation())
		{
			boardNode.attachChild(person.getGeometry());
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
	}
}