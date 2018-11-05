package com.outofprintmagazine.nlp.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class DrupalHandler {

	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		Statement statement = null;
	    ResultSet resultSet = null;
	    
	    String sql = "select author.field_author_value, node.title, email.field_authoremail_value, from_unixtime(node.created) as created, body.body_value " + 
	    		"from Drupal7.node node " + 
	    		"inner join Drupal7.field_data_body body on node.nid = body.entity_id " + 
	    		"inner join Drupal7.field_data_field_author author on node.nid = author.entity_id " + 
	    		"inner join Drupal7.field_data_field_authoremail email on node.nid = email.entity_id ";
	    Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        // Setup the connection with the DB
        connection = DriverManager.getConnection("jdbc:mysql://drupal.cydsp2udvq7b.us-east-1.rds.amazonaws.com/Drupal7?user=Admin&password=Administrator");

        // Statements allow to issue SQL queries to the database
        statement = connection.createStatement();
        // Result set get the result of the SQL query
        resultSet = statement.executeQuery(sql);
        
        //30 September 2013 14:54
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMMM yyyy HH:mm");
        while(resultSet.next()) {
        	String author = resultSet.getString("field_author_value");
            String title = resultSet.getString("title");
            String email = resultSet.getString("field_authoremail_value");
            java.sql.Date date = resultSet.getDate("created");
            String body = resultSet.getString("body_value");
            String id = java.util.UUID.randomUUID().toString();
            File submissionFile = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Submissions\\" + id + ".txt");
		      // creates the file
			submissionFile.createNewFile();
			PrintWriter fout = new PrintWriter(submissionFile); 
			fout.println("From: " + author + "<" + email + ">");
			fout.println("Date: " + fmt.format(date));
			fout.println("Subject: " + title);
			fout.println("To: outofprintmagazine@gmail.com");
			fout.println();
			fout.println(body);
			fout.flush();
		    fout.close();
        }
        resultSet.close();
        statement.close();
        connection.close();
       
	}

}
