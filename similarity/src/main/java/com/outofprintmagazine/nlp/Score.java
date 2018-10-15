package com.outofprintmagazine.nlp;

public class Score {
	
	public Score() {
		super();
	}
	
	public Score(String name, Double score) {
		this.name = name;
		this.score = score.doubleValue();
	}
	
	public Score(String name, double score) {
		this.name = name;
		this.score = score;
	}

	private String name;
	private double score;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	public String toString() {
		return String.format("name: %s, score: %s", name, score);
	}
	
}
