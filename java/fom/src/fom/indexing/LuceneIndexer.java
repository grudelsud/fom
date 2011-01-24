package fom.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import fom.model.Link;
import fom.model.Post;

public class LuceneIndexer implements Runnable {

	private Post[] posts;
	
	public LuceneIndexer(Post[] posts){
		this.posts = posts;
	}
	
	@Override
	public void run() {
		IndexWriter postIndexWriter = null;
		IndexWriter linkIndexWriter = null;
		try {
			NIOFSDirectory postIndexDir = new NIOFSDirectory(new File("data/postIdx"));
			NIOFSDirectory linkIndexDir = new NIOFSDirectory(new File("data/linkIdx"));
			postIndexWriter = new IndexWriter(postIndexDir, new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
			linkIndexWriter = new IndexWriter(linkIndexDir, new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Post post : posts){
			Document postDocument = new Document();
			postDocument.add(new Field("id", Long.toString(post.getId()), Field.Store.YES, Field.Index.NO));
			postDocument.add(new Field("content", post.getContent(), Field.Store.YES, Field.Index.ANALYZED));
			postDocument.add(new Field("date", DateTools.timeToString(post.getCreated().getMillis(), DateTools.Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED));
			postDocument.add(new Field("lat",Double.toString(post.getLat()), Field.Store.YES, Field.Index.NOT_ANALYZED));
			postDocument.add(new Field("lon", Double.toString(post.getLon()), Field.Store.YES, Field.Index.NOT_ANALYZED));
			try {
				postIndexWriter.addDocument(postDocument);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Link link : post.getLinks()){
				Document linkDocument = new Document();
				linkDocument.add(new Field("id", Long.toString(link.getId()), Field.Store.YES, Field.Index.NO));
				linkDocument.add(new Field("content", link.getContent(), Field.Store.YES, Field.Index.ANALYZED));
				try {
					linkIndexWriter.addDocument(linkDocument);
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			linkIndexWriter.optimize();
			linkIndexWriter.close();
			postIndexWriter.optimize();
			postIndexWriter.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
