package fom.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;


import fom.model.Post;
import fom.model.Query;
import fom.model.TimeCluster;

public class TimeClustering {
	private List<Post> posts;
	private List<TimeCluster> clusters;
	private Query originatingQuery;
	private String granularity;
	
	public TimeClustering(Query originatingQuery, List<Post> posts, String granularity){
		this.originatingQuery = originatingQuery;
		clusters = new ArrayList<TimeCluster>();
		this.posts = posts;
		this.granularity = granularity;
	}
	
	public List<TimeCluster> performClustering(){
		List<Post> sortedByDatePosts = new ArrayList<Post>();
		sortedByDatePosts.addAll(posts);
		
		Collections.sort(sortedByDatePosts, new Comparator<Post>() {

			@Override
			public int compare(Post o1, Post o2) {
				return o1.getCreated().compareTo(o2.getCreated());
			}
		});
		
		List<DateTime> roundedDates = new ArrayList<DateTime>();

		for(Post post: sortedByDatePosts){		
			if(granularity.equalsIgnoreCase("day")){
				roundedDates.add(new DateTime(GregorianChronology.getInstance().dayOfYear().roundCeiling(post.getCreated().getMillis())));
			}else if(granularity.equalsIgnoreCase("week")){
				roundedDates.add(new DateTime(GregorianChronology.getInstance().weekyear().roundCeiling(post.getCreated().getMillis())));				
			}else if(granularity.equalsIgnoreCase("hour")){
				roundedDates.add(new DateTime(GregorianChronology.getInstance().hourOfDay().roundCeiling(post.getCreated().getMillis())));
			}
		}
		
		TimeCluster currentCluster = new TimeCluster(originatingQuery);
		clusters.add(currentCluster);
		for(int i=0; i<roundedDates.size(); i++){
			if(i==0){
				currentCluster.addPost(sortedByDatePosts.get(i));
			}else {
				if(roundedDates.get(i).equals(roundedDates.get(i-1))){
					currentCluster.addPost(sortedByDatePosts.get(i));
				}else{
					currentCluster = new TimeCluster(originatingQuery);
					clusters.add(currentCluster);
					currentCluster.addPost(sortedByDatePosts.get(i));
				}
			}
		}
		return clusters;
	}
	
	
}
