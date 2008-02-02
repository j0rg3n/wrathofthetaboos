package net.hokuspokus.wott.common;

import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.state.TextureState;

public class TurnTimer {

	Text timerText;
	long mark;
	
	public TurnTimer() {
		timerText = Text.createDefaultTextLabel( " Timer label" );
		timerText.setCullMode( SceneElement.CULL_NEVER );
		timerText.setTextureCombineMode( TextureState.REPLACE );
		timerText.setLocalTranslation(0, 750.0f, 0);
		
		reset(0);
	}
	
	public void reset(int seconds) {
		mark = System.currentTimeMillis() + seconds * 1000;
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
	}

	public boolean isTimeOut() {
		return getTimeLeft() <= 0;
	}

	public long getTimeLeft() {
		return mark - System.currentTimeMillis();
	}

	public Text getRootNode() {
		return timerText;
	}

	
}
