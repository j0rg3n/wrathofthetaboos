package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Board;

import com.jme.app.SimpleGame;
import com.jme.scene.Node;

public class WrathOfTaboo extends SimpleGame
{
	Board board;
	
	public static void main(String[] args)
	{
		new WrathOfTaboo().start();
	}
	
	@Override
	protected void simpleInitGame()
	{
		rootNode = new Node();
		
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