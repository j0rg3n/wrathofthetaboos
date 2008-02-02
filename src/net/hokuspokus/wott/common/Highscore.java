package net.hokuspokus.wott.common;

public class Highscore implements Comparable<Highscore> {

	public String name;
	public int score;
	
	
	
	public Highscore(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public int compareTo(Highscore arg0)
	{
		return arg0.score - score;
	}
	
}
