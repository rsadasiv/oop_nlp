package com.outofprintmagazine.nlp.scores;

public class NgramScore extends Score{
	
	//wildcard aggregates
	long totalMatchCount = 0;
	long totalVolumeCount = 0;
	int totalFirstYear = 2018;
	int totalLastYear = 0;
	
	//ngram aggregates
	double phraseScore = 0;
	long matchCount = 0;
	long volumeCount = 0;
	long firstYear = 2018;
	long lastYear = 0;
	
	public NgramScore() {
		super();
	}

	public long getTotalMatchCount() {
		return totalMatchCount;
	}

	public void setTotalMatchCount(long totalMatchCount) {
		this.totalMatchCount = totalMatchCount;
	}

	public long getTotalVolumeCount() {
		return totalVolumeCount;
	}

	public void setTotalVolumeCount(long totalVolumeCount) {
		this.totalVolumeCount = totalVolumeCount;
	}

	public int getTotalFirstYear() {
		return totalFirstYear;
	}

	public void setTotalFirstYear(int totalFirstYear) {
		this.totalFirstYear = totalFirstYear;
	}

	public int getTotalLastYear() {
		return totalLastYear;
	}

	public void setTotalLastYear(int totalLastYear) {
		this.totalLastYear = totalLastYear;
	}

	public double getPhraseScore() {
		return phraseScore;
	}

	public void setPhraseScore(double phraseScore) {
		this.phraseScore = phraseScore;
	}

	public long getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(long matchCount) {
		this.matchCount = matchCount;
	}

	public long getVolumeCount() {
		return volumeCount;
	}

	public void setVolumeCount(long volumeCount) {
		this.volumeCount = volumeCount;
	}

	public long getFirstYear() {
		return firstYear;
	}

	public void setFirstYear(long firstYear) {
		this.firstYear = firstYear;
	}

	public long getLastYear() {
		return lastYear;
	}

	public void setLastYear(long lastYear) {
		this.lastYear = lastYear;
	}
	
	public String toString() {
		return String.format("name: %s - phraseScore: %s - matchCount: %s", getName(), getPhraseScore(), getMatchCount());
	}
}
