package net.hokuspokus.wott.common;

import net.hokuspokus.wott.client.WrathOfTaboo;
import net.hokuspokus.wott.utils.SpriteQuad;
import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;

public class TurnTimer {

	private static final int TIMER_SIZE_W = 512;
	private static final int TIMER_SIZE_H = 32;
	Node timerRoot;
	Text timerText;
	long mark;
	long startMark;
	private SpriteQuad timerBackground;
	
	public TurnTimer(WrathOfTaboo game) {
		timerRoot = new Node("timerNode");
		timerRoot.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		timerRoot.setLightCombineMode(LightState.OFF);
		timerRoot.setCullMode( SceneElement.CULL_NEVER );
		timerRoot.setLocalTranslation((game.getDisplay().getWidth()-TIMER_SIZE_W)/2, game.getDisplay().getHeight()-200, 0);
		
		// Add some text
		timerText = Text.createDefaultTextLabel( " Timer label" );
		timerText.setTextureCombineMode( TextureState.REPLACE );
		timerRoot.attachChild(timerText);
		
		// Add the real thing
		timerRoot.attachChild(timerBackground = TextureUtil.getQuad("TimerFrame", "/ressources/2d gfx/TimerBar_progress.png", TIMER_SIZE_W, TIMER_SIZE_H));
		timerRoot.attachChild(TextureUtil.getTransparentQuad("TimerFrame", "/ressources/2d gfx/TimerBar_border.png", TIMER_SIZE_W, TIMER_SIZE_H));
		
		reset(0);
	}
	
	public void reset(int seconds) {
		startMark = System.currentTimeMillis();
		mark = System.currentTimeMillis() + seconds * 1000;
		timerBackground.getLocalScale().set(1, 1, 1);
		timerBackground.updateWorldVectors();
	}
	
	public void update() {
		long left = getTimeLeft();

		StringBuffer buf = new StringBuffer();
		
		if (!isTimeOut()) {
			buf.append(left);
		} else {
			buf.append("Time out.");
		}
		timerText.print(buf);
		
		// Update progressbar
		long diff = (mark - startMark);
		float scale_x = (mark - System.currentTimeMillis()) / (float)diff;
		timerBackground.getLocalScale().set(scale_x, 1, 1);
	}

	public boolean isTimeOut() {
		return getTimeLeft() <= 0;
	}

	public long getTimeLeft() {
		return mark - System.currentTimeMillis();
	}

	public Spatial getRootNode() {
		return timerRoot;
	}

	
}
