package fom.tests;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.ClustererFactory;
import fom.clustering.algorithms.kmedoids.metrics.TFIDFSimilarity;

public class Clustering {

	public static void main(String[] args) {
		
		String a = "Violent crime in New York City has decreased in the last fifteen years, " +
				"and the murder rate in 2007 was at its lowest level since at least 1963, when " +
				"reliable statistics were first kept and the city had half a million fewer residents." +
				"[1][2] Crime rates spiked in the 1980s and early 1990s as the crack epidemic hit " +
				"the city. During the 1990s the New York City Police Department (NYPD) adopted " +
				"CompStat, broken windows policing and other strategies in a major effort to reduce " +
				"crime. The city's dramatic drop in crime has been attributed by criminologists to " +
				"these policing tactics, the end of the crack epidemic and demographic changes.[3][4] " +
				"There is evidence that the data may have been manipulated to create the sense of a " +
				"more secure atmosphere.[5]";
		
		String b = "Organized crime has long been associated with New York City, beginning " +
				"with the Forty Thieves and the Roach Guards in the Five Points in the 1820s. " +
				"The 20th century saw a rise in the Mafia dominated by the Five Families. Gangs " +
				"including the Black Spades and Supreme Team also grew in the late 20th century.[6] " +
				"Numerous major riots have occurred in New York City since the mid 19th century," +
				" including the Draft Riots in 1863, the Stonewall riots, multiple riots at Tompkins " +
				"Square Park, and in Harlem.[7] The serial killings by the 'Son of Sam', which began " +
				"on July 29, 1976, terrorized the city for the next year.[8]";
		
		String c = "Organized crime has long been associated with New York City, beginning " +
				"with the Forty Thieves and the Roach Guards in the Five Points in the 1820s. " +
				"In 1835, the New York Herald was established by James Gordon Bennett, Sr., who " +
				"helped revolutionize journalism by covering stories that appeal to the masses " +
				"including crime reporting. When Helen Jewett was murdered on April 10, 1836, " +
				"Bennett did innovative on-the-scene investigation and reporting and helped bring " +
				"the story to national attention.[6] The murder of Mary Rogers in 1841 was also " +
				"heavily covered by the press, which also put the spotlight on the ineptitude and " +
				"corruption in the city's watchmen system of law enforcement.[6] At the time, " +
				"New York City's population of 320,000 was served by an archaic force, consisting " +
				"of one night watch, one hundred city marshals, thirty-one constables, fifty-one " +
				"police officers.[9] Peter Cooper, at request of the Common Council, drew up a " +
				"proposal to create a police force of 1,200 officers. The state legislature approved " +
				"the proposal which authorized creation of a police force on May 7, 1844, along with " +
				"abolition of the nightwatch system.[9] Under Mayor William Havemeyer, the police " +
				"force was reorganized and officially established on May 13, 1845 as the New York " +
				"City Police Department (NYPD), with the city divided into three districts, with " +
				"courts, magistrates, and clerks, and station houses set-up.[9]";
		
		String d = "Computer science or computing science (sometimes abbreviated CS) is the study " +
				"of the theoretical foundations of information and computation, and of practical " +
				"techniques for their implementation and application in computer systems.[1][2][3][4] " +
				"It is frequently described as the systematic study of algorithmic processes that " +
				"create, describe, and transform information. Computer science has many sub-fields; " +
				"some, such as computer graphics, emphasize the computation of specific results," +
				" while others, such as computational complexity theory, study the properties of " +
				"computational problems. Still others focus on the challenges in implementing " +
				"computations. For example, programming language theory studies approaches to " +
				"describe computations, while computer programming applies specific programming " +
				"languages to solve specific computational problems, and human-computer interaction " +
				"focuses on the challenges in making computers and computations useful, usable, and " +
				"universally accessible to people.";
		
		String e = "The general public sometimes confuses computer science with careers that " +
				"deal with computers (such as information technology), or think that it relates " +
				"to their own experience of computers, which typically involves activities such " +
				"as gaming, web-browsing, and word-processing. However, the focus of computer science " +
				"is more on understanding the properties of the programs used to implement software " +
				"such as games and web-browsers, and using that understanding to create new programs " +
				"or improve existing ones.[5]";
		
		String f = "Although many initially believed it was impossible that computers themselves " +
				"could actually be a scientific field of study, in the late fifties it gradually " +
				"became accepted among the greater academic population.[14] It is the now well-known " +
				"IBM brand that formed part of the computer science revolution during this time. IBM " +
				"(short for International Business Machines) released the IBM 704 and later the IBM " +
				"709 computers, which were widely used during the exploration period of such devices." +
				"Still, working with the IBM [computer] was frustrating...if you had misplaced as much" +
				" as one letter in one instruction, the program would crash, and you would have to start" +
				" the whole process over again.[14] During the late 1950s, the computer science " +
				"discipline was very much in its developmental stages, and such issues were " +
				"commonplace. Time has seen significant improvements in the usability and " +
				"effectiveness of computer science technology. Modern society has seen a significant " +
				"shift from computers being used solely by experts or professionals to a more widespread" +
				" user base. Initially, computers were quite costly, and for their most-effective use, " +
				"some degree of human aid was needed, in part by professional computer operators. " +
				"However, as computers became widespread and far more affordable, less human assistance " +
				"was needed, although residues of the original assistance still remained.";
		
		String[] documents = {a,b,c,d,e,f};
		
		Clusterer<String> kmedoidsClusterer = new ClustererFactory<String>().kMedoidsClusterer(documents, 2, new TFIDFSimilarity(), 100);
	
		for(int i : kmedoidsClusterer.getClusterIndexes()){
			System.out.println(i);
		}
	}

}
