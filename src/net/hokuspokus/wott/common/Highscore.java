package net.hokuspokus.wott.common;

public class Highscore implements Comparable<Highscore> {

	public String name;
	public int score;
	
	
	
	public Highscore(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}



	@Override
	public int compareTo(Highscore o) {
		return o.score - score;
	}
	
}
