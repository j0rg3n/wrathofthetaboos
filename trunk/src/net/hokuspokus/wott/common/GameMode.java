package net.hokuspokus.wott.common;

import net.hokuspokus.wott.client.WrathOfTaboo;

import com.jme.app.SimpleGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.renderer.Camera;
import com.jme.scene.Node;

public abstract class GameMode {

	protected Node rootNode;
	protected WrathOfTaboo game;
	
	public GameMode(WrathOfTaboo game) {
		this.game = game;
		this.rootNode = game.getRootNode();
	}
	
	

	public Node getRootNode() {
		return rootNode;
	}



	public abstract void update();
	public abstract void initCameraPosition(Camera cam);
	public abstract InputHandler initInput();
	public abstract boolean isDone();
	
}
