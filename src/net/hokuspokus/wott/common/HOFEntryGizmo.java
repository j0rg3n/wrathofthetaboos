package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.scene.Node;
import com.jme.scene.Text;

public class HOFEntryGizmo extends Node {

	private static final float SCALE = 10.0f;

	public String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÆÖØÅ¤"; 
	
	List<Node> letters = new ArrayList<Node>();
	Node marker;
	
	String text;
	float maxLetterWidth, letterHeight;
	int activeLetterIndex;
	
	public HOFEntryGizmo() {

		// Scan letters and get sizes.
		for (char c : LETTERS.toCharArray()) {
			Text letter = TextureUtil.createText("letter" + c, 
					Character.toString(c));
			
			letterHeight = letter.getHeight();
			if (maxLetterWidth < letter.getWidth()) {
				maxLetterWidth = letter.getWidth();
			}
		}
		
		letterHeight *= SCALE;
		maxLetterWidth *= SCALE;		
		
		marker = TextureUtil.createShadowText("marker", "^");
		marker.setLocalScale(SCALE);
		attachChild(marker);
		
		setText("");
	}
	
	public float getWidth() {
		return maxLetterWidth * text.length();
	}
	
	public float getHeight() {
		return letterHeight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		
		if ((this.text == null) || !this.text.equals(text)) {
			this.text = text;

			// Remove existing text
			for (Node letter : letters) {
				letter.removeFromParent();
			}
			letters.clear();
			
			// Create new text
			for (int i = 0; i < text.length(); ++i) {
				letters.add(null);
				createLetterAt(i, text.charAt(activeLetterIndex));
			}
		}
		
		setActiveLetter(0);
	}

	private void createLetterAt(int i, char c) {
		Node letter = TextureUtil.createShadowText("letter" + i, Character.toString(c));
		letter.setLocalScale(SCALE);
		letter.setLocalTranslation(maxLetterWidth * i, 0, 0);
		
		if (letters.get(i) != null) {
			letters.get(i).removeFromParent();
		}
		letters.set(i, letter);

		attachChild(letter);
	}
	
	public void setActiveLetter(int i) {
		this.activeLetterIndex = i;
		marker.setLocalTranslation(maxLetterWidth * i, -letterHeight, 0);
	}
	
	public void rollActiveLetter(int offset) {
		int currIndex = LETTERS.indexOf(text.charAt(activeLetterIndex));
		currIndex += offset; 
		while (currIndex < 0) {
			currIndex += LETTERS.length();
		}
		while (currIndex >= LETTERS.length()) {
			currIndex -= LETTERS.length();
		}

		createLetterAt(activeLetterIndex, LETTERS.charAt(currIndex));
	}

	public void update() {
		// FIXME WHY do we need to do this???
		updateGeometricState(0, false);
	}
}
