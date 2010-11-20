package fom.termextraction;

import java.util.List;

public interface TermExtractor {
	public List<String> extractKeywords(String plaintext);
}
