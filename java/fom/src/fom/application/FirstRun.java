package fom.application;

import java.io.File;

import org.wikipedia.miner.model.WikipediaDatabase;

import fom.properties.PropertyHandler;

public class FirstRun {

	public static void main(String[] args) {
		System.out.println("Creating post index Folder...");
		File postsFolderIdx = new File("data/postIdx");
		postsFolderIdx.mkdirs();
		System.out.println("Creating link index Folder...");
		File linksFolderIdx = new File("data/linkIdx");
		linksFolderIdx.mkdirs();
		System.out.println("Loading wikipedia miner DB");
		WikipediaDatabase wikiDB;
		try {
			wikiDB = new WikipediaDatabase(	PropertyHandler.getStringProperty("WikiminerDBServer"), 
											PropertyHandler.getStringProperty("WikiminerDBName"), 
											PropertyHandler.getStringProperty("WikiminerDBUser"), 
											PropertyHandler.getStringProperty("WikiminerDBPass"));
			wikiDB.loadData(new File("data/wikiminer"), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
