package net.hokuspokus.wott.common;

import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;

public class HighscoreDisplay {

	Node rank, name, score;
	Node rootNode;
	DisplaySystem disp;
	
	public HighscoreDisplay(DisplaySystem disp) {
		this.disp = disp;

		rootNode = new Node();

		setHighscore(0, new Highscore("", 0));
	}
	
	public void setHighscore(int index, Highscore h) {
		
		rootNode.detachAllChildren();
		
		rank = TextureUtil.createShadowText("rank", index + ". ");
		rootNode.attachChild(rank);
		name = TextureUtil.createShadowText("name", h.name);
		rootNode.attachChild(name);
		score = TextureUtil.createShadowText("score", "" + h.score);
		rootNode.attachChild(score);

		rank.setLocalScale(1.5f);
		name.setLocalScale(2.0f);
		score.setLocalScale(2.0f);
		
		rank.setLocalTranslation(0.4f * disp.getWidth() - ((Text)rank.getChild(0)).getWidth() * 1.5f, 0, 0);
		name.setLocalTranslation(0.4f * disp.getWidth(), 0, 0);
		score.setLocalTranslation(0.6f * disp.getWidth() - ((Text)score.getChild(0)).getWidth() * 2.0f, 0, 0);
	}
	
	public void update() {
	}

	public Node getRootNode() {
		return rootNode;
	}
	
	
}
