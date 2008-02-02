package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Board;
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;

import com.jme.app.SimpleGame;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;

public class WrathOfTaboo extends SimpleGame
{
	Board board;
	Player p1;
	Player p2;
	
	public static void main(String[] args)
	{
		new WrathOfTaboo().start();
	}
	
	@Override
	protected void simpleInitGame()
	{
		rootNode = new Node();
		createNewBoard();
	}
	
	
	private void createNewBoard()
	{
		board = new Board();
		p1 = new Player(new ColorRGBA(1,0,0,1));
		p2 = new Player(new ColorRGBA(0,0,1,1));
		
		for(Person person : p1.getPopulation())
		{
			rootNode.attachChild(person.getGeomtry());
		}
		
		for(Person person : p2.getPopulation())
		{
			rootNode.attachChild(person.getGeomtry());
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