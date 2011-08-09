package fom.topicextraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.MalletLogger;

import fom.langidentification.LanguageIdentifier.Language;
import fom.model.Link;
import fom.model.Post;
import fom.model.Topic;
import fom.utils.StringOperations;

public class TopicExtractor {
	
	public static List<Topic> extractTopics(List<Post> posts, int numberOfTopics, int numberOfWords, boolean disableLangDetection, boolean excludeRelLinksText, Language language){
		
		ArrayList<Pipe> pipelist = new ArrayList<Pipe>();
		pipelist.add( new CharSequence2TokenSequence() );
		pipelist.add( new TokenSequenceLowercase() );
//		pipelist.add( new TokenSequenceRemoveStopwords() );
		pipelist.add( new TokenSequence2FeatureSequence() );
		
		InstanceList instances = new InstanceList(new SerialPipes(pipelist));
		
		List<Instance> tmpInstanceList = new ArrayList<Instance>();
		
		String bigInstance = "";
		
		for(Post post : posts) {
			String sanitizedPost = post.getContent();
			sanitizedPost = StringOperations.removeURLfromString(sanitizedPost);
			sanitizedPost = StringOperations.removeMentions(sanitizedPost);
			sanitizedPost = StringOperations.removeNonLettersFromString(sanitizedPost);
			
			if(!disableLangDetection){
				sanitizedPost = StringOperations.removeStopwords(sanitizedPost, post.getLanguage());				
			}
			
			if(!sanitizedPost.trim().equalsIgnoreCase("")){
				bigInstance += " " + sanitizedPost;
			}
			
			if(!excludeRelLinksText){
				for(Link link : post.getLinks()){
					String sanitizedLink = link.getContent();
					sanitizedLink = StringOperations.removeURLfromString(sanitizedLink);
					sanitizedLink = StringOperations.removeNonLettersFromString(sanitizedLink);
					if(!disableLangDetection){
						sanitizedLink = StringOperations.removeStopwords(sanitizedLink, link.getLanguage());						
					}
					if(!sanitizedLink.trim().equalsIgnoreCase("")){
						bigInstance += " " + sanitizedLink;
					}
				}				
			}
		}
		
		// eccoci qua
		Instance inst = new Instance(bigInstance, null, "data", "data");
		tmpInstanceList.add(inst);

		instances.addThruPipe(tmpInstanceList.iterator());		
		
		MalletLogger.getLogger(ParallelTopicModel.class.getName()).setLevel(Level.OFF);
		
		ParallelTopicModel lda = new ParallelTopicModel(numberOfTopics);
		
		List<Topic> topics = new ArrayList<Topic>();

		lda.addInstances(instances);
		try {
			lda.estimate();				
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e){
			
		}
		
		Object[][] topWords = lda.getTopWords(numberOfWords);
		int limit = posts.size() < numberOfTopics ? posts.size() : numberOfTopics;
		int bigInstanceLength = bigInstance.length();

		for(int i = 0; i < limit && i < topWords.length; i++) {
			Topic topic = new Topic(lda.alpha[i], language);
			int keywordsInTopic = 0;
			for(Object word : topWords[i]) {
				
				String keyword = word.toString();
				
				if( keyword.length() > 2 ) {
					int keywordOccurrences = 0;
					Pattern pattern = Pattern.compile( keyword, Pattern.CASE_INSENSITIVE );
					Matcher matcher = pattern.matcher(bigInstance);
					while( matcher.find() ) {
						keywordOccurrences++;
					}
					keywordsInTopic++;
					Float score = (float)(100.0 * keyword.length() * keywordOccurrences) / (float)bigInstanceLength;
					if( score == 0.0 ) System.err.println( "--- 0 DETECTED ---" );
					topic.addWord(keyword, score);
				}
			}
			if( keywordsInTopic > 0 ) {
				topics.add(topic);
			}
		}
		
		return topics;
	}
}
