package com.outofprintmagazine.nlp.annotators;

import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.pipeline.Annotation;

public class ParagraphAnnotator extends edu.stanford.nlp.paragraphs.ParagraphAnnotator {
	
	
	private static Properties getProperties() {
		Properties props = new Properties();
		props.setProperty("paragraphBreak", "two");
		return props;
	}
	
	public ParagraphAnnotator() {
		super(ParagraphAnnotator.getProperties(), false);
	}

	@Override
	public void unmount() {
		// TODO Auto-generated method stub
		super.unmount();
	}

	@Override
	public void annotate(Annotation annotation) {
		// TODO Auto-generated method stub
		super.annotate(annotation);
	}

	@Override
	public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
		// TODO Auto-generated method stub
		return super.requirementsSatisfied();
	}

	@Override
	public Set<Class<? extends CoreAnnotation>> requires() {
		// TODO Auto-generated method stub
		return super.requires();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	
	
}
