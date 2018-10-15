package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;

import edu.stanford.nlp.pipeline.CoreDocument;

public abstract class ScorerImpl {

	private Ta ta;
	private String analysisName = null;
	
	public ScorerImpl() {
		super();
	}
	
	public ScorerImpl(Ta ta) {
		this();
		setTa(ta);
	}
	
	public Ta getTa() {
		return ta;
	}

	public void setTa(Ta ta) {
		this.ta = ta;
	}
	
	public String getAnalysisName() {
		return analysisName;
	}

	public void setAnalysisName(String analysisName) {
		this.analysisName = analysisName;
	}
	
	public ArrayList<String> consolidateSubstrings(ArrayList<String> rawScores) {
		//Consolidate substrings
		rawScores.sort(null);
		ListIterator<String> iter = rawScores.listIterator(rawScores.size());
		String prev = "";
		while (iter.hasPrevious()) {
			String cur = iter.previous();
			if (prev.contains(cur)) {
				iter.set(prev);
			}
			else {
				prev = cur;
			}		
		}
		return rawScores;
	}
	
	public HashMap<String, Double> consolidateMentions(ArrayList<String> rawScores) {
		//Consolidate mentions
		HashMap<String, Double> scoreMap = new HashMap<String, Double>();
		for (String rawScore : rawScores) {
			Double existingScore = scoreMap.get(rawScore);
			if (existingScore == null) {
				scoreMap.put(rawScore, new Double(1));
			}
			else {
				scoreMap.put(rawScore, new Double(existingScore.doubleValue()+1));
			}
		}
		return scoreMap;
	}
	
	public HashMap<String, Double> normalizeScores(HashMap<String, Double> scoreMap, CoreDocument document) {
		for (String key : scoreMap.keySet()) {
			scoreMap.put(key, new Double((scoreMap.get(key).doubleValue()/document.sentences().size())));
		}
		return scoreMap;
	}
	
	public List<Entry<String, Double>> sortByScore(HashMap<String, Double> scoreMap) {
		//Sort by score
	    List<Entry<String, Double>> list = new ArrayList<>(scoreMap.entrySet());
	    list.sort(Entry.comparingByValue());
	    return list;
	}
	
	public List<Score> sortDescending(HashMap<String, Double> scoreMap) {
	    //Return in descending order
		ArrayList<Score> retval = new ArrayList<Score>();
		List<Entry<String, Double>> list = sortByScore(scoreMap);
	    ListIterator<Entry<String, Double>> li = list.listIterator(list.size());
	    while (li.hasPrevious()) {
	    	Entry<String, Double> entry = li.previous();
	    	Score tmp = new Score(entry.getKey(), entry.getValue());
	    	retval.add(tmp);
	    }
	    return retval;
	}
	
	public List<Score> sortAscending(HashMap<String, Double> scoreMap) {
	    //Return in descending order
		ArrayList<Score> retval = new ArrayList<Score>();
		List<Entry<String, Double>> list = sortByScore(scoreMap);
	    ListIterator<Entry<String, Double>> li = list.listIterator();
	    while (li.hasNext()) {
	    	Entry<String, Double> entry = li.next();
	    	Score tmp = new Score(entry.getKey(), entry.getValue());
	    	retval.add(tmp);
	    }
	    return retval;
	}
	
	public List<Score> rawScoresToScoreList(ArrayList<String> rawScores, CoreDocument document) {
		return(sortDescending(normalizeScores(consolidateMentions(rawScores),document)));
	}
	
	public Score scoreDocumentScalar(List<Score> scores) throws IOException {
		return scoreListToScalar(scores, new ArrayList<String>());
	}
	
	protected Score scoreListToScalar(List<Score> scores, List<String> scoreNames) {
		double total = 0;
		for (Score score : scores) {
			if (scoreNames.size() == 0 || scoreNames.contains(score.getName())) {
				total += score.getScore();
			}
		}
		return new Score(getAnalysisName(), (total/(double)scores.size()));
	}
	
	public List<Score> scoreDocumentRanked(List<Score> allScores) throws IOException {
		int topCount = (int) Math.sqrt(allScores.size());
		if (topCount < 15) topCount = 15;
		ArrayList<Score> rankedScores = new ArrayList<Score>();
		for (int i=0;i<topCount&&i<allScores.size();i++) {
			rankedScores.add(allScores.get(i));
		}
		return rankedScores;
	}
	
}
