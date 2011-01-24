package fom.tests;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexReader;
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
			NIOFSDirectory dir = new NIOFSDirectory(new File("data/linkIdx"));
			IndexReader ir = IndexReader.open(dir);
			IndexSearcher is = new IndexSearcher(ir);
			TopDocs td = is.search(new QueryParser(Version.LUCENE_30, "content", new SimpleAnalyzer()).parse("twitter"), 10);
			for(ScoreDoc doc : td.scoreDocs){
				System.out.println(doc.doc + " - " + doc.score);
				System.out.println(ir.document(doc.doc).get("content"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
