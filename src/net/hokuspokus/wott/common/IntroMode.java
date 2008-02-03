package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.hokuspokus.wott.client.WrathOfTaboo;
import net.hokuspokus.wott.utils.SpriteQuad;
import net.hokuspokus.wott.utils.TextureUtil;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

public class IntroMode extends GameMode {

	private static final int MAX_HIGHSCORE_LINES = 5;
	private Text heading, blink;
	private List<HighscoreDisplay> lines = new ArrayList<HighscoreDisplay>();

	public IntroMode(WrathOfTaboo game) {
		super(game);
		
		SpriteQuad background = TextureUtil.getFullscreenQuad("foo", "ressources/2d gfx/splashscreen.jpg");
		background.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight(), 0);
		rootNode.attachChild(background);
		
		blink = TextureUtil.createText("Insert coin", "(insert coin)");
		rootNode.attachChild(blink);

		Iterator<Highscore> hi = game.getHighscore().getHighscores().iterator();
		for (int i = 0; (i < MAX_HIGHSCORE_LINES) && hi.hasNext(); ++i) {
			Highscore highscore = hi.next();

			HighscoreDisplay highscoreDisplay = new HighscoreDisplay(game.getDisplay());
			highscoreDisplay.setHighscore(i + 1, highscore);
			
			Node highscoreNode = highscoreDisplay.getRootNode();

			lines.add(highscoreDisplay);
			rootNode.attachChild(highscoreNode);
		}
	}

	@Override
	public void initCameraPosition(Camera cam) {
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
				game.getDisplay().getHeight() * 0.45f - blink.getHeight() - 2 * blink.getHeight(), 
				0);
	
		float t = (System.currentTimeMillis() % 2000) * FastMath.PI / 1000.0f;
		
		int i = 0;
		for (HighscoreDisplay highscoreDisplay : lines) {
			Node highscoreNode = highscoreDisplay.getRootNode();

			float y = game.getDisplay().getHeight() * 0.32f - ((i + 2) * blink.getHeight() * 2);
			highscoreNode.setLocalTranslation(FastMath.sin(t + FastMath.PI * i / 23.0f) *
					(FastMath.sin(t + FastMath.PI * i / 9.0f) * .5f + .5f) * 2 *
					blink.getHeight(), y, 0);
			++i;
		}
	}

	@Override
	public boolean isDone() {
    	return KeyBindingManager.getKeyBindingManager().isValidCommand(
                WrathOfTaboo.START_GAME_BINDING, false );        	
	}	
}
