import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DecisionTreeParser - parses input data to produce @examples, @features, and @categories.
 * 
 * @author starr
 *
 */
public class DecisionTreeParser {
	
	Set <Example> examples = new HashSet<Example> ();
	List <Feature> features = new ArrayList<Feature> ();
	List <String> categories = new ArrayList <String> ();
	StreamTokenizer fileTokens = null;
	
	// C'tor
	public DecisionTreeParser (String filename) throws IOException {
		FileReader inFile = new FileReader (filename);
		
		// Create tokenizer
		fileTokens = new StreamTokenizer (inFile);
		fileTokens.lowerCaseMode(true);  // lowercase only
		fileTokens.commentChar('/');     // comment char
		
		// Read in names of categories
		String firstVal = readNextWord (fileTokens);
		String secondVal = readNextWord (fileTokens);
		categories = new ArrayList<String> ();
		categories.add (firstVal);
		categories.add (secondVal);
		
		// Build a vector of all features
		int numOfFeatures = readNextInteger (fileTokens);
		for (int i = 0; i < numOfFeatures; i++) {
			String featureName = readNextWord (fileTokens);
			List <String> featureVals = new ArrayList<String> ();
			// Only two possible values each
			featureVals.add(readNextWord (fileTokens));
			featureVals.add(readNextWord (fileTokens));
			features.add (new Feature (featureName, featureVals));
		}
	}
	
	public void parseExamples () {
		// Build examples
		int numExamples = readNextInteger (fileTokens);
		for (int i = 0; i < numExamples; i++) {
			Example example = new Example (features, categories);
			example.setLabel (readNextWord (fileTokens));  // skip training number
			String classification = readNextWord (fileTokens);
			if (example.setClassification(classification)) {
				System.out.println ("Bad classification value: " + classification);
				System.out.println ("Quitting...");
				System.exit (1); // Error out
			}
			for (int j = 0; j < features.size (); j++) {
				String featureValue = readNextWord (fileTokens);
				if (example.setFeature(featureValue, j)) {
					System.out.println ("Bad feature value: " + featureValue);
					System.out.println ("Quitting...");
					System.exit(1);  // Error out
				}
			}
			examples.add(example);
		}
	}
	
	public void printExamples () {
		for (Example e: examples) {
			System.out.println (e.toString());
		}
	}


	// Tokenizer utilities
	private int readNextInteger(StreamTokenizer st) {try
    {
	      switch (st.nextToken())
	      {          
	        case StreamTokenizer.TT_NUMBER:
	          return (int)st.nval;

	        default:
	          System.out.println("Expecting an integer in readNextInteger().");
	          return -1;
	      }
	    }
	    catch (IOException ioe)
	    {
	      System.out.println("I/O Exception? " + ioe);
	      return -1;
	    }
	  }

	private String readNextWord(StreamTokenizer st) {
		 try
		    {
		      switch (st.nextToken())
		      {
		        case StreamTokenizer.TT_WORD:
		          return st.sval;

		        default:
		          System.out.println("Expecting a string in readNextWord().");
		          return null;
		      }
		    }
		    catch (IOException ioe)
		    {
		      System.out.println("I/O Exception? " + ioe);
		      return null;
		    }
	}
	
	// Getters & Setters
	public List<Feature> getFeatures() {
		return features;
	}
	public Set<Example> getExamples() {
		return examples;
	}
	public void setFeatures(List<Feature> features) {
		this.features = features;		
	}
}
