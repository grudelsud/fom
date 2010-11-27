package fom.tests;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class TopicExtraction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String a = "blah blah";
		String b = "blah boh";
		String c = "boh blah";
		
		List<String> strings = new ArrayList<String>();
		strings.add(a);
		strings.add(b);
		strings.add(c);
		
		ArrayList<Pipe> pipelist = new ArrayList<Pipe>();
		pipelist.add(new CharSequence2TokenSequence());
		pipelist.add(new TokenSequenceLowercase());
		pipelist.add(new TokenSequenceRemoveStopwords());
		pipelist.add(new TokenSequence2FeatureSequence());
		
		InstanceList instances = new InstanceList(new SerialPipes(pipelist));
		
		for(String string : strings){
			Instance inst = new Instance(string, null, null, string);
			instances.addThruPipe(inst);
		}
		
		ParallelTopicModel lda = new ParallelTopicModel(2);
		lda.addInstances(instances);
		try {
			lda.estimate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	lda.displayTopWords(2, true);
		lda.printDocumentTopics(new PrintWriter(System.out));
	}

}
