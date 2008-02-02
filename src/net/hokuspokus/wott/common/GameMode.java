package net.hokuspokus.wott.common;

import com.jme.input.InputHandler;
import com.jme.renderer.Camera;
import com.jme.scene.Node;

public abstract class GameMode {

	protected Node rootNode;
	
	public GameMode() {
		rootNode = new Node();
	}
	
	

	public Node getRootNode() {
		return rootNode;
	}



	public abstract void update();
	public abstract void initCameraPosition(Camera cam);
	public abstract InputHandler initInput();
	
}
