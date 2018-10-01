package com.outofprintmagazine.nlp.renderers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;

import org.w3c.dom.Element;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.collections.Collection;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.test.TestInterface;
import com.flickr4java.flickr.util.IOUtilities;

public class FlickrHandler {

	public static void main(String[] args) throws FlickrException, FileNotFoundException, IOException {
		FlickrHandler me = new FlickrHandler();
		Properties api_props = new Properties();
		api_props.load(new FileReader("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\src\\main\\resources\\flickr_api_key.txt"));
		Flickr flickr = new Flickr(api_props.getProperty("apiKey"), api_props.getProperty("secret"), new REST());
		Properties token_props = new Properties();
		token_props.load(new FileReader("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\src\\main\\resources\\flickr_credentials.txt"));
		RequestContext requestContext = RequestContext.getRequestContext();
        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(token_props.getProperty("Token"));
        auth.setTokenSecret(token_props.getProperty("Secret"));
        requestContext.setAuth(auth);
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
		
	    SearchParameters searchParameters = new SearchParameters();
	    String[] tags = {"India"};
	    searchParameters.setTags(tags);
	    searchParameters.setSort(SearchParameters.INTERESTINGNESS_DESC);

	    PhotoList<Photo> list = flickr.getPhotosInterface().search(searchParameters, 10, 1);
	    
	    for (Photo p : list) {
	        try {
	            Photo nfo = flickr.getPhotosInterface().getInfo(p.getId(), null);
	            if (nfo.getOriginalSecret().isEmpty()) {
	                System.out.println(p.getTitle() + "\t" + p.getLargeUrl());
	            } else {
	                p.setOriginalSecret(nfo.getOriginalSecret());
	                System.out.println(p.getTitle() + "\t" + p.getOriginalUrl());
	            }
	        } 
	        catch (FlickrException e) {
	            e.printStackTrace();
	        }
	    	System.out.println("-----------------------------------");
		}
	}

}
