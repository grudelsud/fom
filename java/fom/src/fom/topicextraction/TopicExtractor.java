package fom.topicextraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.MalletLogger;

import fom.model.Post;
import fom.utils.StringOperations;

public class TopicExtractor {
	
	public static List<List<String>> extractTopics(List<Post> posts){
		
		ArrayList<Pipe> pipelist = new ArrayList<Pipe>();
		pipelist.add(new CharSequence2TokenSequence());
		pipelist.add(new TokenSequenceLowercase());
		pipelist.add(new TokenSequenceRemoveStopwords());
		pipelist.add(new TokenSequence2FeatureSequence());
		
		InstanceList instances = new InstanceList(new SerialPipes(pipelist));
		
		List<Instance> tmpInstanceList = new ArrayList<Instance>();
		
		for(Post post : posts){
			String sanitizedPost = post.getContent();
			sanitizedPost = StringOperations.removeURLfromString(sanitizedPost);
			sanitizedPost = StringOperations.removeStopwords(sanitizedPost);
			if(!sanitizedPost.trim().equalsIgnoreCase("")){
				Instance inst = new Instance(sanitizedPost, null, post, post.getContent());
				tmpInstanceList.add(inst);
			}
		}
		
		instances.addThruPipe(tmpInstanceList.iterator());		
		
		MalletLogger.getLogger(ParallelTopicModel.class.getName()).setLevel(Level.OFF);
		
		int numberOfTopics = 3;
		ParallelTopicModel lda = new ParallelTopicModel(numberOfTopics);
		
		List<List<String>> topics = new ArrayList<List<String>>();

		lda.addInstances(instances);
		try {
			lda.estimate();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e){
		}		

		Object[][] topWords = lda.getTopWords(5);
		int limit = posts.size()<numberOfTopics?posts.size():numberOfTopics;
		for(int topicCount=0; topicCount<limit && topicCount<topWords.length; topicCount++){
			List<String> topic = new ArrayList<String>();
			for(Object word : topWords[topicCount]){
				topic.add(word.toString());
			}
			topics.add(topic);
		}
		
		return topics;
	}
}
