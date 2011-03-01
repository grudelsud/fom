package fom.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class GeoDBIndexing {
	
	public static void main(String[] args) {
		System.out.println("Deleting old index if any...");
		File indexDir = new File("data/geonamesIdx");
		indexDir.mkdirs();
		for(File file : indexDir.listFiles()){
			file.delete();
		}
		
		System.out.println("Reading and indexing data/geonames5000.txt");
		try {
			NIOFSDirectory geoIndexDir = new NIOFSDirectory(indexDir);
			IndexWriter index = new IndexWriter(geoIndexDir, new StandardAnalyzer(Version.LUCENE_30), MaxFieldLength.UNLIMITED);
		
			BufferedReader bReader = new BufferedReader(new FileReader("data/cities5000.txt"));
			String line;
			while((line=bReader.readLine())!=null){
				String[] splitArray = line.split("\t");
				String firstName = splitArray[1];
				String altName = splitArray[2];
				String[] foreignNames = splitArray[3].split(",");
				
				String finalNames = firstName + " " + altName;
				for(String str : foreignNames){
					finalNames = finalNames.concat(" " + str);
				}
				
				Document doc = new Document();
				doc.add(new Field("names", finalNames, Store.YES, Index.ANALYZED));
				doc.add(new Field("lat", splitArray[4], Store.YES, Index.NOT_ANALYZED));
				doc.add(new Field("lon", splitArray[5], Store.YES, Index.NOT_ANALYZED));
				index.addDocument(doc);
			}
			bReader.close();
			
			System.out.println("Constructing countries index...");
			IndexSearcher is = new IndexSearcher(geoIndexDir);
			QueryParser qp = new QueryParser(Version.LUCENE_30, "names", new StandardAnalyzer(Version.LUCENE_30));
			bReader = new BufferedReader(new FileReader("data/countries.txt"));
			while((line=bReader.readLine())!=null){
				String[] splitArray = line.split("\t");
				if(splitArray.length>5){						
					String nationName = splitArray[4];
					String capitalName = splitArray[5];
				
					System.out.println("Searching lat-lon of the capital of " + nationName + ": " + capitalName);
					if(!capitalName.equalsIgnoreCase("")){
						try {
							TopDocs td = is.search(qp.parse(capitalName), 1);
							if(td.scoreDocs.length>0){
								Document doc = new Document();
								doc.add(new Field("names", nationName, Store.YES, Index.ANALYZED));
								doc.add(new Field("lat", is.doc(td.scoreDocs[0].doc).get("lat"), Store.YES, Index.NOT_ANALYZED));
								doc.add(new Field("lon", is.doc(td.scoreDocs[0].doc).get("lon"), Store.YES, Index.NOT_ANALYZED));
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
				}
			}
			bReader.close();
			System.out.println("Optimizing the index file");
			index.optimize();
			index.close();
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
