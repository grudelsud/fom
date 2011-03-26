package fom.langidentification;

import org.knallgrau.utils.textcat.TextCategorizer;

public class TextcatLangIdentifier implements LanguageIdentifier {

	TextCategorizer categorizer;
	
	public TextcatLangIdentifier(){
		categorizer = new TextCategorizer();
	}
	
	@Override
	public Language identifyLanguageOf(String text) {
		return Enum.valueOf(Language.class, categorizer.categorize(text));		
	}

}
