package fom.langidentification;

public interface LanguageIdentifier {
	public enum Language {
		albanian,
		danish,
		dutch,
		english,
		finnish,
		french,
		german,
		hungarian,
		italian,
		norwegian,
		polish,
		slovakian,
		slovenian,
		spanish,
		swedish,
		unknown;
	}
	
	public Language identifyLanguageOf(String text);
}
