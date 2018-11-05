package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.PosScorerImpl;
import com.outofprintmagazine.nlp.scores.NgramScore;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import io.phrasefinder.Corpus;
import io.phrasefinder.Phrase;
import io.phrasefinder.Phrase.Token;
import io.phrasefinder.PhraseFinder;
import io.phrasefinder.SearchOptions;
import io.phrasefinder.SearchResult;
import io.phrasefinder.SearchResult.Status;

public class NGrams extends PosScorerImpl implements DocumentCategoricalScorer {
		
    SearchOptions options;
	
	public NGrams(Ta ta) throws IOException {
		this(ta, Arrays.asList("NNP", "NNPS", "CC", "CD", "FW", "LS", "UH", "SYM"));
	}
	
	public NGrams(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}
	

	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<Score> retval = new ArrayList<Score>();
		HashMap<CoreLabel,ArrayList<NgramScore>> tokenNgramScores = new HashMap<CoreLabel,ArrayList<NgramScore>>();
		SearchOptions wildcardOptions = new SearchOptions();
		wildcardOptions.setMaxResults(100);
		SearchOptions ngramOptions = new SearchOptions();
		ngramOptions.setMaxResults(10);
		
		for (CoreSentence sentence : document.sentences()) {
			System.out.println(sentence);
			List<CoreLabel> tokens = sentence.tokens();
			
			//for each token in sentence
			for (int token_idx = 0;token_idx < tokens.size(); token_idx++) {
				//extract 2-gram, 3-gram, 4-gram, 5-gram
				if (scoreToken(tokens.get(token_idx)) != null) {
					continue;
				}

				for (int ngram_idx=0;ngram_idx<5&&token_idx+ngram_idx<tokens.size();ngram_idx++) {
					//exclude proper nouns? NNP NNPS
					//break on conjunctions? CC
					//exclude non-wordy CD FW LS UH SYM
					//break on clauses? IN
					String token = scoreToken(tokens.get(token_idx+ngram_idx));
					if (token != null) {
						break;
					}
					//construct query string from n-1-gram
				    StringBuffer query = new StringBuffer();
				    for (int i=token_idx;i<ngram_idx+token_idx;i++) {
				    	query.append(tokens.get(i).originalText());
				    	query.append(" ");
				    }


			    	//add wildcard for nth-gram
				    //System.out.println("wildcard: " + query.toString().trim()+" "+tokens.get(ngram_idx+token_idx).originalText());
				    SearchResult wildcardResult = null;
				    if (query.toString().trim().length()==0) {
				    	wildcardResult = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, tokens.get(ngram_idx+token_idx).originalText(), wildcardOptions);
				    }
				    else {
				    	wildcardResult = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query.toString().trim() + " ?", wildcardOptions);
				    }
			    	if (wildcardResult.getStatus() == Status.OK) {
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

			    		for (Phrase phrase : wildcardResult.getPhrases()) {
				    		//aggregate the first 100 wildcard matches
			    			totalMatchCount+=phrase.getMatchCount();
			    			totalVolumeCount+=phrase.getVolumeCount();
			    			if (totalFirstYear>phrase.getFirstYear()) {
			    				totalFirstYear = phrase.getFirstYear();
			    			}
			    			if (totalLastYear<phrase.getLastYear()) {
			    				totalLastYear = phrase.getLastYear();
			    			}

				    		//if we get the nth-ngram in the wildcard search, aggregate those entries
			    			Token[] phraseTokens = phrase.getTokens();
			    			if (ngram_idx < phraseTokens.length) {
			    				if (tokens.get(ngram_idx+token_idx).originalText().equalsIgnoreCase(phraseTokens[ngram_idx].getText())) {
			    					phraseScore+=phrase.getScore();
					    			matchCount+=phrase.getMatchCount();
					    			volumeCount+=phrase.getVolumeCount();
					    			if (firstYear>phrase.getFirstYear()) {
					    				firstYear = phrase.getFirstYear();
					    			}
					    			if (lastYear<phrase.getLastYear()) {
					    				lastYear = phrase.getLastYear();
					    			}
			    				}
		    				}
			    		}
			    		//if there were records in the wildcard search, but we didn't find the ngram in the first 100, search for the full n-gram and calculate the phraseScore
			    		if (totalMatchCount > 0 && matchCount == 0) {
			    			//System.out.println("ngram: " + query.toString().trim()+" "+tokens.get(ngram_idx+token_idx).originalText());
				    		SearchResult result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query.toString().trim()+" "+tokens.get(ngram_idx+token_idx).originalText(), ngramOptions);
						    if (result.getStatus() == Status.OK) {
					    		for (Phrase phrase : result.getPhrases()) {
						    		matchCount+=phrase.getMatchCount();
						    		volumeCount+=phrase.getVolumeCount();
						    		if (firstYear>phrase.getFirstYear()) {
						    			firstYear = phrase.getFirstYear();
						    		}
						    		if (lastYear<phrase.getLastYear()) {
						    			lastYear = phrase.getLastYear();
						    		}
					    		}
						    	if (totalMatchCount>0) {
						    		phraseScore = ((long)matchCount/totalMatchCount);
						    	}
				    		}
			    		}
			    		//turn these into scores
			    		ArrayList<NgramScore> scoreList = tokenNgramScores.get(tokens.get(token_idx+ngram_idx));
			    		if (scoreList == null) {
			    			scoreList = new ArrayList<NgramScore>();
			    		}
			    		NgramScore tmpScore = new NgramScore();
			    		tmpScore.setName(ngram_idx + "-gram");
			    		tmpScore.setTotalVolumeCount(totalVolumeCount);
			    		tmpScore.setTotalMatchCount(totalMatchCount);
			    		tmpScore.setTotalFirstYear(totalFirstYear);
			    		tmpScore.setTotalLastYear(totalLastYear);
			    		tmpScore.setPhraseScore(phraseScore);
			    		tmpScore.setVolumeCount(volumeCount);
			    		tmpScore.setMatchCount(matchCount);
			    		tmpScore.setFirstYear(firstYear);
			    		tmpScore.setLastYear(lastYear);
			    		scoreList.add(tmpScore);
			    		//System.out.println(tmpScore);
			    		//System.out.println();
			    		tokenNgramScores.put(tokens.get(token_idx+ngram_idx), scoreList);	
			    	}
				}
			}
		}

		//Aggregate ngram where firstYear > 1999
		HashMap<String,Score> millenial = new HashMap<String,Score>();
		HashMap<String,Score> dated = new HashMap<String,Score>();
		HashMap<String,Score> archaic = new HashMap<String,Score>();	
		HashMap<String,Score> novel = new HashMap<String,Score>();
		for (int i=0;i<5;i++) {
			millenial.put(i +"-gram", new Score("millenial-" + (i+1) + "-gram", 0));
			dated.put(i +"-gram", new Score("dated-" + (i+1) + "-gram", 0));
			archaic.put(i +"-gram", new Score("archaic-" + (i+1) + "-gram", 0));
			novel.put(i +"-gram", new Score("novel-" + (i+1) + "-gram", 0));
		}

		for (Entry<CoreLabel, ArrayList<NgramScore>> entry : tokenNgramScores.entrySet()) {
			for (NgramScore t : entry.getValue()) {
				if (t.getMatchCount() == 0) {
					Score novelScore = novel.get(t.getName());
					novelScore.setScore(novelScore.getScore()+1);
					novel.put(t.getName(), novelScore);
				}
				else if (t.getFirstYear() > 1999) {
					Score millenialScore = millenial.get(t.getName());
					millenialScore.setScore(millenialScore.getScore()+1);
					millenial.put(t.getName(), millenialScore);
				}
				else if (t.getLastYear() < 1900) {
					Score archaicScore = archaic.get(t.getName());
					archaicScore.setScore(archaicScore.getScore()+1);
					archaic.put(t.getName(), archaicScore);
				}
				else if (t.getLastYear() < 1950) {
					Score datedScore = dated.get(t.getName());
					datedScore.setScore(datedScore.getScore()+1);
					dated.put(t.getName(), datedScore);
				}
			}
		}
		retval.addAll(novel.values());
		retval.addAll(millenial.values());
		retval.addAll(archaic.values());
		retval.addAll(dated.values());
		
		return retval;
	}
}
