package net.hokuspokus.wott.common;

import java.io.Serializable;

public class Highscore implements Comparable<Highscore>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5242222860443241372L;
	
	public String name;
	public int score;
	
	
	public Highscore(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public int compareTo(Highscore o) {
		return o.score - score;
	}	
}
