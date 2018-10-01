package com.outofprintmagazine.nlp.annotators;

import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.pipeline.Annotation;

public class GenderAnnotator extends edu.stanford.nlp.pipeline.GenderAnnotator {
	
	
	private static Properties getProperties() {
		Properties props = new Properties();
		//props.setProperty("gender.maleNamesFile", "edu/stanford/nlp/models/gender/male_first_names.txt");
		//props.setProperty("gender.femaleNamesFile", "edu/stanford/nlp/models/gender/female_first_names.txt");
		props.setProperty("gender.maleNamesFile", "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\MaleNames.txt");
		props.setProperty("gender.femaleNamesFile", "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\FemaleNames.txt");		
		return props;
	}
	
	public GenderAnnotator() {
		super("GenderAnnotator", GenderAnnotator.getProperties());
	}

}
