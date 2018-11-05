package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISenseKey;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public abstract class PosLexicalGroupScorerImpl extends ScorerImpl {

	private List<String> tags = new ArrayList<String>();
	private String dictionaryFileName = "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_50k.txt";
	private String dictionaryCommonFileName = "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_100.txt";
	
	public PosLexicalGroupScorerImpl() {
		super();
	}
	

	public PosLexicalGroupScorerImpl(Ta ta) {
		this();
		setTa(ta);
	}
	
	public PosLexicalGroupScorerImpl(Ta ta, List<String> tags) throws IOException {
		this(ta);
		setTags(tags);
	}
	

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String scoreToken(CoreLabel token) {
		String score = null;
		if (getTags().contains(token.tag())) {
			try {
				IIndexWord idxWord = getTa().getWordnet().getIndexWord(token.lemma(), tagToPOS(token));
				if (idxWord != null && idxWord.getWordIDs().size() > 0) {
					IWordID wordID = idxWord.getWordIDs().get(0);
					IWord word = getTa().getWordnet().getWord(wordID);
					score = word.getSynset().getLexicalFile().getName();
				}
			}
			catch (Exception e) {
				System.err.println(token.toString());
				e.printStackTrace();
			}
		}
		return score;
	}
	
	public String scoreToken(CoreLabel token, CoreSentence sentence) {
		String score = null;
		if (getTags().contains(token.tag())) {
			try {
				IIndexWord idxWord = getTa().getWordnet().getIndexWord(token.lemma(), tagToPOS(token));
				if (idxWord != null && idxWord.getWordIDs().size() > 0) {
					ISenseKey senseKey = simplifiedLesk(token, sentence);
					for (IWordID wordID : idxWord.getWordIDs()) {
						IWord word = getTa().getWordnet().getWord(wordID);
						if (word.getSenseKey().equals(senseKey)) {
							score = word.getSynset().getLexicalFile().getName();
							break;
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println(token.toString());
				e.printStackTrace();
			}
		}
		return score;
	}
	
	/*
	 * function SIMPLIFIED LESK(word,sentence) returns best sense of word
	best-sense <- most frequent sense for word
	max-overlap <- 0
	context <- set of words in sentence
	for each sense in senses of word do
	signature <- set of words in the gloss and examples of sense
	overlap <- COMPUTEOVERLAP (signature,context)
	if overlap > max-overlap then
	max-overlap <- overlap
	best-sense <- sense
	end return (best-sense)		
	 */
	
	public ISenseKey simplifiedLesk(CoreLabel target, CoreSentence sentence) throws IOException {
		ISenseKey retval = null;
		POS pos = tagToPOS(target);
		if (pos == null) {
			return retval;
		}
		IIndexWord idxWord = getTa().getWordnet().getIndexWord(target.lemma(), tagToPOS(target));	
		if (idxWord != null) {
			IWord word0 = getTa().getWordnet().getWord(idxWord.getWordIDs().get(0));
			if (idxWord.getWordIDs().size() == 1) {
				return(word0.getSenseKey());
			}
			IWord word1 = getTa().getWordnet().getWord(idxWord.getWordIDs().get(1));
			if (getTa().getWordnet().getSenseEntry(word1.getSenseKey()).getTagCount() < (getTa().getWordnet().getSenseEntry(word0.getSenseKey()).getTagCount()/3)) {
				return(word0.getSenseKey());
			}
			ArrayList<String> sentenceGlosses = new ArrayList<String>();
			for (CoreLabel token : sentence.tokens()) {
				if (!token.lemma().equals(target.lemma())) {
					POS tokenPOS = tagToPOS(token);
					if (tokenPOS != null) {
						IIndexWord idxWordSentence = getTa().getWordnet().getIndexWord(token.lemma(), tagToPOS(token));
						if (idxWordSentence != null) {
							for (IWordID wordSentenceID : idxWordSentence.getWordIDs()) {
								IWord wordSentence = getTa().getWordnet().getWord(wordSentenceID);
								String gloss = wordSentence.getSynset().getGloss();
								gloss = gloss.toLowerCase();
								gloss = gloss.replace(";", "");
								gloss = gloss.replace("\"", "");
								gloss = gloss.replace(",", "");
								ArrayList<String> glossWords = new ArrayList<String>();
								glossWords.addAll(Arrays.asList(gloss.split("\\s+")));
								glossWords.add(wordSentence.getLemma());
								ISynset synset = wordSentence.getSynset();
						        for (IWord w : synset.getWords()) {
						            glossWords.add(w.getLemma());
						        }
								Iterator<String> glossWordsIter = glossWords.iterator();
								while (glossWordsIter.hasNext()) {
									String glossWord = glossWordsIter.next();
									for (String commonWord : getTa().getDictionary(dictionaryCommonFileName).keySet()) {
										if (glossWord.equals(commonWord)) {
											glossWordsIter.remove();
											break;
										}
									}
								}
								sentenceGlosses.addAll(glossWords);
								//throw in the words from all matching sentences in semcor
								//throw in the words from all matching sentences in MASC
							}
						}
					}
				}
			}

			Set<String> hs = new HashSet<>();
			hs.addAll(sentenceGlosses);
			sentenceGlosses.clear();
			sentenceGlosses.addAll(hs);
//			System.err.println("sentenceGlosses: " + sentenceGlosses);
			double maxOverlap = 0;
			for (IWordID wordID : idxWord.getWordIDs()) {
				IWord word = getTa().getWordnet().getWord(wordID);
				ArrayList<String> targetGlosses = new ArrayList<String>();
				if (word != null) {
					ArrayList<String> glossWords = new ArrayList<String>();
					if (word.getSynset() != null) {
						String gloss = word.getSynset().getGloss();
						gloss = gloss.toLowerCase();
						gloss = gloss.replace(";", "");
						gloss = gloss.replace(",", "");
						gloss = gloss.replace("\"", "");
						glossWords.addAll(Arrays.asList(gloss.split("\\s+")));
						Iterator<String> glossWordsIter = glossWords.iterator();
						while (glossWordsIter.hasNext()) {
							String glossWord = glossWordsIter.next();
							for (String commonWord : getTa().getDictionary(dictionaryCommonFileName).keySet()) {
								if (glossWord.equals(commonWord)) {
									glossWordsIter.remove();
								}
							}
						}
					}
					glossWords.add(word.getLemma());
					ISynset synset = word.getSynset();
			        for (IWord w : synset.getWords()) {
			            glossWords.add(w.getLemma());
			        }						
			        Set<String> hsx = new HashSet<>();
					hsx.addAll(glossWords);
					glossWords.clear();
					glossWords.addAll(hsx);
					targetGlosses.addAll(glossWords);
				}
				double overlap = 0.0;
//				System.err.println(word.getSenseKey());
//				System.err.println("tagCount: " + getTa().getWordnet().getSenseEntry(word.getSenseKey()).getTagCount());
//				System.err.println("targetGlosses: " + targetGlosses);


				for (String x : targetGlosses) {
					if (sentenceGlosses.contains(x)) {
						//String wordFrequency = getTa().getDictionary(dictionaryFileName).get(x);
						//double numerator = 100;
						//if (wordFrequency == null) {
							overlap++;
						//}
						//else {
						//	overlap+=(double) numerator/new Integer(wordFrequency).doubleValue();
						//}
					}
				}
//				System.err.println("maxOverlap: " + maxOverlap);
//				System.err.println("retval: " + retval);
//				System.err.println("overlap: " + overlap);
//				System.err.println("------------------------------------");
				if (overlap > maxOverlap) {
					maxOverlap = overlap;
					retval = word.getSenseKey();
				}	
			}
		}
//		System.err.println("lesk: " + target.lemma());
//		System.err.println("final senseKey: " + retval.toString());
		return retval;
	}
	
	protected POS tagToPOS(CoreLabel token) {
		POS wnPos = null;
		if (token.tag().startsWith("N")) {
			wnPos = POS.NOUN;
		}
		else if (token.tag().startsWith("V")) {
			wnPos = POS.VERB;
		}
		else if (token.tag().startsWith("J")) {
			wnPos = POS.ADJECTIVE;
		}
		else if (token.tag().startsWith("R")) {
			wnPos = POS.ADVERB;
		}
		return wnPos;
	}

}
