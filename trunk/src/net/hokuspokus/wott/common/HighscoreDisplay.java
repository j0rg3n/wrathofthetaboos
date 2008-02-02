package net.hokuspokus.wott.common;

import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;

public class HighscoreDisplay {

	Text rank, name, score;
	Node rootNode;
	DisplaySystem disp;
	
	public HighscoreDisplay(DisplaySystem disp) {
		this.disp = disp;

		rootNode = new Node();
		
		rank = SuperDuperAssistants.createText("rank", "rank");
		rootNode.attachChild(rank);
		name = SuperDuperAssistants.createText("name", "name");
		rootNode.attachChild(name);
		score = SuperDuperAssistants.createText("score", "score");
		rootNode.attachChild(score);
	}
	
	public void setHighscore(int index, Highscore h) {
		rank.setLocalTranslation(0.4f * disp.getWidth() - rank.getWidth(), 0, 0);
		rank.print(index + ". ");
		name.setLocalTranslation(0.4f * disp.getWidth(), 0, 0);
		name.print(h.name);
		score.setLocalTranslation(0.6f * disp.getWidth() - score.getWidth(), 0, 0);
		score.print("" + h.score);
	}
	
	public void update() {
	}

	public Node getRootNode() {
		return rootNode;
	}
	
	
}
