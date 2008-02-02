package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import net.hokuspokus.wott.client.WrathOfTaboo;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Text;

public class IntroMode extends GameMode {

	private Text heading, blink;
	private HOF hof;
	private List<HighscoreDisplay> lines = new ArrayList<HighscoreDisplay>();

	public IntroMode(WrathOfTaboo game) {
		super(game);
		
		heading = SuperDuperAssistants.createText("Intro heading", 
			"W R A T H  O F  T H E  T A B O O S");
		heading.setLocalTranslation(
				(game.getDisplay().getWidth() - heading.getWidth()) / 2, 
				(game.getDisplay().getHeight() - heading.getHeight()) / 2, 
				0);
		rootNode.attachChild(heading);
		
		blink = SuperDuperAssistants.createText("Insert coin", 
			"(insert coin)");
		rootNode.attachChild(blink);

	
		hof = new HOF();
		
		rootNode.attachChild(heading);

		for (Highscore highscore : hof.getHighscores()) {
			HighscoreDisplay highscoreDisplay = new HighscoreDisplay(game.getDisplay());
			highscoreDisplay.setHighscore(highscore);
			
			Node highscoreNode = highscoreDisplay.getRootNode();

			lines.add(highscoreDisplay);
			rootNode.attachChild(highscoreNode);
		}
	}
	
	@Override
	public void initCameraPosition(Camera cam) {
		// TODO Auto-generated method stub
	}

	@Override
	public InputHandler initInput() {
		return null;
	}

	@Override
	public void update() {
		int blinkState = (int)(System.currentTimeMillis() / 500) & 1;
		blink.setLocalTranslation(
				(game.getDisplay().getWidth() - blink.getWidth()) / 2 + 
				blinkState * game.getDisplay().getWidth(), 
				(game.getDisplay().getHeight() - blink.getHeight()) / 2 - 2 * blink.getHeight(), 
				0);
	
		float t = (System.currentTimeMillis() % 2000) * FastMath.PI / 1000.0f;
		
		int i = 0;
		for (HighscoreDisplay highscoreDisplay : lines) {
			Node highscoreNode = highscoreDisplay.getRootNode();

			float y = game.getDisplay().getHeight() - ((i + 3) * heading.getHeight());
			highscoreNode.setLocalTranslation(FastMath.sin(t + FastMath.PI * i / 7.0f) * 
					heading.getHeight(), y, 0);
			++i;
		}
	}

	@Override
	public boolean isDone() {
    	return KeyBindingManager.getKeyBindingManager().isValidCommand(
                WrathOfTaboo.START_GAME_BINDING, false );        	
	}	
}
