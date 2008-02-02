package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import net.hokuspokus.wott.client.WrathOfTaboo;

import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Text;

public class HighscoreMode extends GameMode {

	private Text heading;
	
	public HighscoreMode(WrathOfTaboo game) {
		super(game);
		
		heading = SuperDuperAssistants.createText("Intro heading", 
			"You are more evil than the other gods.");
		heading.setLocalTranslation(
				(game.getDisplay().getWidth() - heading.getWidth()) / 2, 
				game.getDisplay().getHeight() - heading.getHeight(), 0);
		rootNode.attachChild(heading);
	}
	
	@Override
	public void initCameraPosition(Camera cam) {
		
	}

	@Override
	public InputHandler initInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		
	}

}
