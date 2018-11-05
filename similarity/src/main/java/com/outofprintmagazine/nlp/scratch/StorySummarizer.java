package com.outofprintmagazine.nlp.scratch;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

public class StorySummarizer {

	public static void main(String[] args) throws ClassCastException, ClassNotFoundException, IOException {
	    String serializedClassifier = "c:/users/rsada/eclipse-workspace/NLP/src/main/resources/stanford-ner-2018-02-27/classifiers/english.all.3class.distsim.crf.ser.gz";
	    AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
	    String[] example = {
	    		"Good afternoon Rajat Raina, how are you today? \nI go to school at Stanford University, which is located in Palo Alto, California, US.\n" 
	    };
	    
	    
		ArrayList<String> people = new ArrayList<String>();
		ArrayList<String> places = new ArrayList<String>();
		String previousAnnotation = "";
		String combinedNer = "";
		String fileContents = IOUtils.slurpFile("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Story.txt");
		List<List<CoreLabel>> out = classifier.classify(fileContents);
		for (List<CoreLabel> sentence : out) {
	        for (CoreLabel cl : sentence) {
				String currentAnnotation = cl.get(CoreAnnotations.AnswerAnnotation.class);
				if (!currentAnnotation.equals(previousAnnotation)) {
					if (!combinedNer.equals("")) {
						if (previousAnnotation.equals("PERSON")) {
							people.add(combinedNer);
						}
						else if (previousAnnotation.equals("LOCATION")) {
							places.add(combinedNer);
						}
					}
					combinedNer = "";
				}
				if (!currentAnnotation.equals("O")) {
					combinedNer += cl.originalText() + " ";
				}
				previousAnnotation = currentAnnotation;
			}
		}
		if (!combinedNer.equals("")) {
			if (previousAnnotation.equals("PERSON")) {
				people.add(combinedNer);
			}
			else if (previousAnnotation.equals("LOCATION")) {
				places.add(combinedNer);
			}
		}

		places.sort(null);
		ListIterator<String> iter = null;
		iter = places.listIterator(places.size());
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
		
		System.out.println("Places");
		HashMap<String, Integer> placeMap = new HashMap<String, Integer>();
		for (String place : places) {
			Integer existingPlace = placeMap.get(place);
			if (existingPlace == null) {
				placeMap.put(place, new Integer(1));
			}
			else {
				placeMap.put(place, new Integer(existingPlace.intValue()+1));
			}
		}
        List<Entry<String, Integer>> list = new ArrayList<>(placeMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        // Generate an iterator. Start just after the last element.
        ListIterator<Entry<String, Integer>> li = list.listIterator(list.size());

        // Iterate in reverse.
        for (int i=0;li.hasPrevious()&&i<3;i++) {
          System.out.println(li.previous().getKey());
        }
        
		people.sort(null);
		iter = people.listIterator(people.size());
		prev = "";
		while (iter.hasPrevious()) {
			String cur = iter.previous();
			if (prev.contains(cur)) {
				iter.set(prev);
			}
			else {
				prev = cur;
			}		
		}
		
		System.out.println("People");
		HashMap<String, Integer> personMap = new HashMap<String, Integer>();
		for (String person : people) {
			Integer existingPerson = personMap.get(person);
			if (existingPerson == null) {
				personMap.put(person, new Integer(1));
			}
			else {
				personMap.put(person, new Integer(existingPerson.intValue()+1));
			}
		}
        list = new ArrayList<>(personMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        li = list.listIterator(list.size());

        // Iterate in reverse.
        for (int i=0;li.hasPrevious()&&i<3;i++) {
          System.out.println(li.previous().getKey());
        }

	}

}
