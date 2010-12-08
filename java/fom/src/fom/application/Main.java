package fom.application;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.Option;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import fom.resultlogging.ResultLogger;
import fom.resultlogging.logengines.CSVLogger;
import fom.resultlogging.logengines.ConsoleLogger;
import fom.resultlogging.logengines.FolderLogger;
import fom.resultlogging.logengines.RPCRemoteLogger;

public class Main {
	
    private static void printUsage() {
        System.err.println("Usage:\n" +
        					"fom [--expEng {wikiminer,...}] [--twitter] [--teamlife]\n" +
        					"[--query queryString] [--since YYYY-MM-DD] [--until YYYY-MM-DD]\n" +
        					"[--nearLat latitude] [--nearLon longitude] [--radius radius]\n" +
        					"[--timeGran {hour, day, week}] [--geoGran {poi, neighborhood, city}]\n" +
        					"[--consoleLog] [--csvLog] [--folderLog] [--rpcLog]");
    }

	public static void main(String[] args){
		//Define command line options:
		
		CmdLineParser parser = new CmdLineParser();
		Option expEngineNameOpt = parser.addStringOption("expEng");
		Option twitterSrcOpt = parser.addBooleanOption("twitter");
		Option teamlifeSrcOpt = parser.addBooleanOption("teamlife");
		
		Option queryStringOpt = parser.addStringOption("query");
		Option startTimeOpt = parser.addStringOption("since");
		Option endTimeOpt = parser.addStringOption("until");
		Option nearLatOpt = parser.addDoubleOption("nearLat");
		Option nearLonOpt = parser.addDoubleOption("nearLon");
		Option radiusOpt = parser.addIntegerOption("radius");
		Option timeGranOpt = parser.addStringOption("timeGran");
		Option geoGranOpt = parser.addStringOption("geoGran");
		
		Option consoleLogOpt = parser.addBooleanOption("consoleLog");
		Option csvLogOpt = parser.addBooleanOption("csvLog");
		Option folderLogOpt = parser.addBooleanOption("folderLog");
		Option rpcLogOpt = parser.addBooleanOption("rpcLog");
		
		try {
			parser.parse(args);
		} catch (IllegalOptionValueException e) {
			printUsage();
			System.exit(-1);
		} catch (UnknownOptionException e) {
			printUsage();
			System.exit(-1);
		}
		
		//Parse options:
		
		//Expansion Engine
		String expEngineName = (String)parser.getOptionValue(expEngineNameOpt, "wikiminer");
		if(!expEngineName.equalsIgnoreCase("wikiminer")){
			printUsage();
			System.exit(-1);
		}
		
		//Sources
		List<String> sourceNames = new ArrayList<String>();
		if(parser.getOptionValue(twitterSrcOpt)!=null){
			sourceNames.add("twitter");
		}
		if(parser.getOptionValue(teamlifeSrcOpt)!=null){
			sourceNames.add("teamlife");
		}
		
		//Loggers
		ResultLogger logger = new ResultLogger();
		if(parser.getOptionValue(consoleLogOpt)!=null){
			logger.addLogEngine(new ConsoleLogger());			
		}
		if(parser.getOptionValue(csvLogOpt)!=null){
			logger.addLogEngine(new CSVLogger());			
		}
		if(parser.getOptionValue(folderLogOpt)!=null){
			logger.addLogEngine(new FolderLogger());			
		}
		if(parser.getOptionValue(rpcLogOpt)!=null){
			logger.addLogEngine(new RPCRemoteLogger());			
		}
		
		//UserId (Fixed ATM)
		long userId = 1; //TODO: handle multiple users
		
		//Query
		String queryString = (String)parser.getOptionValue(queryStringOpt, "");		
		
		//Since and Until
		DateTimeFormatter dateParser = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		DateTime startTime = new DateTime().minusDays(0);
		String startTimeOptString = (String)parser.getOptionValue(startTimeOpt);
		if(startTimeOptString!=null){
			startTime = dateParser.parseDateTime(startTimeOptString);
		}
		
		DateTime endTime = new DateTime().minusDays(0);
		String endTimeOptString = (String)parser.getOptionValue(endTimeOpt);
		if(endTimeOptString!=null){
			endTime = dateParser.parseDateTime(endTimeOptString);
		}
		
		//Near location
		double nearLat = (Double)parser.getOptionValue(nearLatOpt, new Double(0));
		double nearLon = (Double)parser.getOptionValue(nearLonOpt, new Double(0));
		int radius = (Integer)parser.getOptionValue(radiusOpt, 0);
		
		//Granularities
		String timeGranularity = (String)parser.getOptionValue(timeGranOpt, "day");
		if(!(timeGranularity.equalsIgnoreCase("hour") || timeGranularity.equalsIgnoreCase("day") || timeGranularity.equalsIgnoreCase("week"))){
			printUsage();
			System.exit(-1);
		}
		String geoGranularity = (String)parser.getOptionValue(geoGranOpt, "city");
		if(!(geoGranularity.equalsIgnoreCase("poi") || geoGranularity.equalsIgnoreCase("neighborhood") || geoGranularity.equalsIgnoreCase("city"))){
			printUsage();
			System.exit(-1);
		}
		
		QueryHandler qHandler = new QueryHandler(expEngineName, sourceNames, logger);
		qHandler.executeQuery(userId, queryString, startTime, endTime, timeGranularity, geoGranularity, nearLat, nearLon, radius);
	}
}
