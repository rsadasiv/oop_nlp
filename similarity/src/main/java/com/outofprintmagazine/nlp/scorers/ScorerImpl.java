package com.outofprintmagazine.nlp.scorers;

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
	
	public HashMap<String, Integer> consolidateMentions(ArrayList<String> rawScores) {
		//Consolidate mentions
		HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
		for (String rawScore : rawScores) {
			Integer existingScore = scoreMap.get(rawScore);
			if (existingScore == null) {
				scoreMap.put(rawScore, new Integer(1));
			}
			else {
				scoreMap.put(rawScore, new Integer(existingScore.intValue()+1));
			}
		}
		return scoreMap;
	}
	
	public HashMap<String, Integer> normalizeScores(HashMap<String, Integer> scoreMap, CoreDocument document) {
		//TODO switch to BigDecimal? int is too small (int)1/5000*100 == 0 
		//for (String key : scoreMap.keySet()) {
			//System.err.println(key + " : " + scoreMap.get(key).intValue() + " " + document.tokens().size());
			//scoreMap.put(key, new Integer((int)(scoreMap.get(key).intValue()/document.tokens().size()*100)));
		//}
		return scoreMap;
	}
	
	public List<Entry<String, Integer>> sortByScore(HashMap<String, Integer> scoreMap) {
		//Sort by score
	    List<Entry<String, Integer>> list = new ArrayList<>(scoreMap.entrySet());
	    list.sort(Entry.comparingByValue());
	    return list;
	}
	
	public List<Score> sortDescending(HashMap<String, Integer> scoreMap) {
	    //Return in descending order
		ArrayList<Score> retval = new ArrayList<Score>();
		List<Entry<String, Integer>> list = sortByScore(scoreMap);
	    ListIterator<Entry<String, Integer>> li = list.listIterator(list.size());
	    while (li.hasPrevious()) {
	    	Entry<String, Integer> entry = li.previous();
	    	Score tmp = new Score(entry.getKey(), entry.getValue());
	    	retval.add(tmp);
	    }
	    return retval;
	}
	
	public List<Score> sortAscending(HashMap<String, Integer> scoreMap) {
	    //Return in descending order
		ArrayList<Score> retval = new ArrayList<Score>();
		List<Entry<String, Integer>> list = sortByScore(scoreMap);
	    ListIterator<Entry<String, Integer>> li = list.listIterator();
	    while (li.hasNext()) {
	    	Entry<String, Integer> entry = li.next();
	    	Score tmp = new Score(entry.getKey(), entry.getValue());
	    	retval.add(tmp);
	    }
	    return retval;
	}
	
	public List<Score> rawScoresToScoreList(ArrayList<String> rawScores, CoreDocument document) {
		return(sortDescending(normalizeScores(consolidateMentions(rawScores),document)));
	}
	
}
