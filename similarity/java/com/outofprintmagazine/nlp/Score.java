package com.outofprintmagazine.nlp;

public class Score {
	
	public Score() {
		super();
	}
	
	public Score(String name, Integer score) {
		this.name = name;
		this.score = score.intValue();
	}
	
	public Score(String name, int score) {
		this.name = name;
		this.score = score;
	}

	private String name;
	private int score;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	public String toString() {
		return String.format("name: %s, score: %s", name, score);
	}
	
}
