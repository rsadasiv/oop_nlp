package com.outofprintmagazine.nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.mit.jverbnet.data.FrameType;
import edu.mit.jverbnet.data.IFrame;
import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.IWordnetKey;
import edu.mit.jverbnet.index.IVerbIndex;
import edu.mit.jverbnet.index.VerbIndex;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.stanford.nlp.paragraphs.ParagraphAnnotator;

public class Ta {

	private StanfordCoreNLP pipeline;
	private IDictionary wordnet = null;
	private HashMap<String, ArrayList<String>> verbnet = new HashMap<String, ArrayList<String>>();
	private HashMap<String, HashMap<String, String>> dictionaries = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, List<String>> lists = new HashMap<String, List<String>>();
	
	public static Properties getDefaultProps() {
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
	    // adding our own annotator property
	    props.put("customAnnotatorClass.paragraphs",
	            "com.outofprintmagazine.nlp.annotators.ParagraphAnnotator");
	    props.put("customAnnotatorClass.gender",
	            "com.outofprintmagazine.nlp.annotators.GenderAnnotator");	    
	    props.put("paragraphs.paragraphBreak", "two");

	    // configure pipeline
	//    edu.stanford.nlp.paragraphs.ParagraphAnnotator x = new edu.stanford.nlp.paragraphs.ParagraphAnnotator(props, verbose)
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment,paragraphs,gender");
		props.setProperty("ner.applyFineGrained", "false");
		
		return props;
	}
	
	public StanfordCoreNLP getPipeline() {
		return pipeline;
	}


	public IDictionary getWordnet() {
		return wordnet;
	}
	
	public HashMap<String, ArrayList<String>> getVerbnet() throws IOException {
		if (verbnet.size() == 0) {
			initVerbnet();
		}
		return verbnet;
	}
	
	private void initVerbnet() throws IOException {
		String pathToVerbnet = "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\new_vn\\";
		URL url = new URL("file", null, pathToVerbnet);
		IVerbIndex index = new VerbIndex(url);
		index.open();
		Iterator<IVerbClass> verbClassIter = index.iteratorRoots();
		while (verbClassIter.hasNext()) {
			IVerbClass verb = verbClassIter.next();
			for (IMember member : verb.getMembers()) {
				for (IWordnetKey senseKey : member.getWordnetTypes().keySet()) {
					ArrayList<String> senseKeyList = verbnet.get(senseKey.toString());
					if (senseKeyList == null) {
						senseKeyList = new ArrayList<String>();
					}
					senseKeyList.add(verb.getID());
					//System.err.println("adding: " + senseKey.toString() + " " + senseKeyList.size());
					verbnet.put(senseKey.toString(), senseKeyList);
				}
			}
			
		}	
	}


	public HashMap<String, HashMap<String, String>> getDictionaries() {
		return dictionaries;
	}

	public HashMap<String, String> getDictionary(String name) throws IOException {
		HashMap<String, String> retval = dictionaries.get(name);
		if (retval == null) {
			setDictionary(name);
			retval = dictionaries.get(name);
		}
		return retval;
	}
	
	public void setDictionary(String name, HashMap<String, String> dictionary) {
		dictionaries.put(name, dictionary);
	}
	
	public void setDictionary(String fileName) throws IOException {
		HashMap<String, String> dict = new HashMap<String, String>();
		//FileReader fileReader = new FileReader("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_uncommon.txt");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			String[] name_value = line.split(" ");
			dict.put(name_value[0], name_value[1]);
		}
		bufferedReader.close();
		setDictionary(fileName, dict);
	}
	
	public HashMap<String, List<String>> getLists() {
		return lists;
	}

	public List<String> getList(String name) {
		return lists.get(name);
	}
	
	public void setList(String name, List<String> list) {
		lists.put(name, list);
	}
	
	public void setList(String fileName) throws IOException {
		setList(fileName, Files.readAllLines(new File(fileName).toPath(), StandardCharsets.UTF_8));
	}


	public Ta() throws IOException {
		this(Ta.getDefaultProps());
	}
	
	public Ta(Properties props) throws IOException {
		super();
		pipeline = new StanfordCoreNLP(props);
		wordnet = new Dictionary(new URL("file", null, "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\wn3.1.dict\\dict"));
		wordnet.open();
	}
	
	public CoreDocument annotate(String text) {
		// create a document object
		CoreDocument document = new CoreDocument(text);
		// annnotate the document
		pipeline.annotate(document);
		return document;
	}
}
