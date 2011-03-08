package fom.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			NIOFSDirectory dir = new NIOFSDirectory(new File("data/geonamesIdx"));
			IndexSearcher is = new IndexSearcher(dir);
			
			BufferedReader bReader = new BufferedReader(new FileReader("data/countries.txt"));
			String line;
			while((line=bReader.readLine())!=null){
				String[] splitArray = line.split("\t");
				if(splitArray.length>5){						
					String nationName = splitArray[4];
					String capitalName = splitArray[5];
				
					System.out.println("Searching lat-lon of the capital of " + nationName + ": " + capitalName);
					if(!capitalName.equalsIgnoreCase("")){
						try {
							TopDocs td = is.search(new QueryParser(Version.LUCENE_30, "names", new StandardAnalyzer(Version.LUCENE_30)).parse(capitalName), 1);
							for(ScoreDoc doc : td.scoreDocs){
								System.out.println("\t" + is.doc(doc.doc).get("names") + "->(" + is.doc(doc.doc).get("lat") + ", " + is.doc(doc.doc).get("lon") + ")");
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
