package fom.topicextraction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import cc.mallet.topics.TopicAssignment;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.MalletLogger;

import fom.model.Post;

public class TopicExtractor {
	
	public static void extractTopics(List<Post> posts){
		ArrayList<Pipe> pipelist = new ArrayList<Pipe>();
		pipelist.add(new CharSequence2TokenSequence());
		pipelist.add(new TokenSequenceLowercase());
		pipelist.add(new TokenSequenceRemoveStopwords());
		pipelist.add(new TokenSequence2FeatureSequence());
		
		InstanceList instances = new InstanceList(new SerialPipes(pipelist));
		
		List<Instance> tmpInstanceList = new ArrayList<Instance>();
		
		for(Post post : posts){
			Instance inst = new Instance(post.getContent(), null, post, post.getContent());
			tmpInstanceList.add(inst);
		}
		
		instances.addThruPipe(tmpInstanceList.iterator());
		
		MalletLogger.getLogger(ParallelTopicModel.class.getName()).setLevel(Level.OFF);
		
		ParallelTopicModel lda = new ParallelTopicModel(5);
		lda.addInstances(instances);
		try {
			lda.estimate();
			lda.printDocumentTopics(new PrintWriter( new FileWriter( new File("out.txt"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(TopicAssignment topicAssign : lda.getData()){
			System.out.println(topicAssign.instance.getName());
			System.out.println(topicAssign.instance.getLabeling());
		}
		//	lda.displayTopWords(5, true);
	//	lda.printDocumentTopics(new PrintWriter(System.out));
	}
}
