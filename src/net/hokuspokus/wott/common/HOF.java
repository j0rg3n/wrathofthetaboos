package net.hokuspokus.wott.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HOF implements Serializable {
	
	private static final long serialVersionUID = -3297910793456162831L;
	
	public List<Highscore> highscores = new ArrayList<Highscore>();
	
	public HOF() {
		
		addHighscore("FOO", 23);
		addHighscore("BAR", 1000000);
		addHighscore("ZOO", 42);
		addHighscore("FOO", 23);
		addHighscore("BAR", 1000000);
		addHighscore("ZOO", 42);
		addHighscore("FOO", 23);
		addHighscore("BAR", 1000000);
		addHighscore("ZOO", 42);
		addHighscore("FOO", 23);
		addHighscore("BAR", 1000000);
		addHighscore("ZOO", 42);
		
	}

	public Collection<Highscore> getHighscores() {
		return highscores;
	}
	
	public void addHighscore(String name, int score) {
		highscores.add(new Highscore(name, score));
		Collections.sort(highscores);
	}
	
}
