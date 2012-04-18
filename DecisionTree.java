import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *  DecisionTree - creates a decision tree given sample input / output pairs
 * 
 * 	Given a set of @examples, @parentExamples, and @features to classify by, this
 *  class recursively creates a binary decision tree using the ID3 algorithm.
 *  
 *  After being built, the tree can classify an example using @classify(), and
 *  can be printed recursively using @print().
 *  
 * 		
 * @author Mike Starr (mstarr@wisc.edu / starr)
 *
 */
public class DecisionTree {
	Set <Example> parentExamples = new HashSet<Example> ();
	Set <Example> examples = new HashSet<Example> ();
	List <Feature> features = new ArrayList<Feature> ();
	Map <String, DecisionTree> children = new HashMap <String, DecisionTree> ();
	Feature splitFeature = null;
	String classification = "";
	

	/**
	 * DecisionTree - public and private constructors
	 * @param examples - examples to build tree from
	 * @param parentExamples - parent examples for base cases in recursion
	 * @param list - list of features to examine
	 */
	private DecisionTree(Set<Example> examples, Set<Example> parentExamples,
			List<Feature> list) {
		this.examples = examples;
		this.parentExamples = parentExamples;
		this.features = list;
	}
	
	public DecisionTree (Set <Example> examples, List<Feature> list) {
		this (examples, examples, list);
	}
	
	
	/**
	 * print - public access print method to recursively print the decision tree.
	 */
	// Print
	public void print() {
		print("");
	}
	// Recursive print
	private void print(String prefix) {
		if (splitFeature != null) {  // Still traveling down tree
			System.out.printf ("%s ( %s )\n", prefix, splitFeature.featureName);
			System.out.printf ("%s    | \\\n", prefix);
			
			// Iterate over values
			Iterator <String> it = children.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (it.hasNext()) {  // more to come
					System.out.printf("%s    |  =%s\n", prefix, key);
					children.get(key).print (prefix + "    | ");
				}
				else {  // last one
					System.out.printf("%s     \\\n", prefix);
					System.out.printf("%s       =%s\n", prefix, key);
					children.get (key).print (prefix + "      ");
				}
			}
		} else {  // leaf node
			System.out.printf("%s  **%s**\n", prefix, classification);
		}
	}

	/**
	 * classify - return a best-guess classification for example @e
	 * @param e - Example to classify
	 * @return Classification
	 */
	public String classify (Example e) {
		if (classification.equals("")) { // need to check children
			String sVal = e.getFeatureVal(splitFeature);
			return children.get(sVal).classify(e);
		}
		return classification;
	}

	/**
	 * ID3 - the decision tree learning algorithm.  Recursively builds the decision tree.
	 */
	public void ID3 () {
		// Example with these features never seen
		if (examples.isEmpty()) {
			this.classification = plurality (parentExamples);
		}
		// All examples the same classification
		else if (singleClassification (examples)) {
			for (Example e: examples) {
				classification = e.getClassification();
				break;
			}
		}
		// Varying amount of examples with different class
		else if (features.isEmpty()) {
			this.classification = plurality (examples);
		} else {
			// Pull the feature that best divides the data
			splitFeature = mostImportantFeature (features, examples);
			
			// For each possible value for this feature
			for (String fVal: splitFeature.featureVals()) {
				// Create a set of examples matching the value
				Set <Example> subExamples = new HashSet <Example> ();
				for (Example e: examples) {
					if (e.getFeatureVal(splitFeature).equals(fVal)) { // matches
						subExamples.add(e);
					}
				}
				// Remove splitFeature from features
				List <Feature> subFeatures = new ArrayList <Feature> (features);
				subFeatures.remove(splitFeature);
				
				// Create a subtree with these values
				DecisionTree subTree = new DecisionTree (subExamples, examples, subFeatures);
				
				// Recursively learn
				subTree.ID3();
				
				// Add to subtrees
				children.put(fVal, subTree);
			}	
		}
	}
	
	
	/**
	 * singleClassification - returns true if all examples have the same class
	 */
	private boolean singleClassification(Set<Example> examples) {
		String s = null;
		for (Example e: examples) {
			if (s == null) {
				s = e.getClassification();
			} else {
				if (!e.getClassification().equals(s)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * booleanEntropy - returns the boolean entropy given the probability of a positive example, @p
	 * @return
	 */
	private double booleanEntropy (double p) {
		if (p == 0 || p == 1) return 0;
		return -(p * Math.log(p) + (1-p) * Math.log(1-p));
	}

	/**
	 * mostImportantFeature - returns the feature with the least remaining entropy
	 * if examples were to be divided by this feature.
	 * @return
	 */
	private Feature mostImportantFeature(List<Feature> features2,
			Set<Example> examples) {
		// We want the feature with the least amount of remaining entropy
		float minEntropy = 9999999f;  // A big number
		Feature splitFeature = null;
				
		// For each feature
		for (Feature f: features2) {
			float entropy = 0;
			int total = examples.size();
			
			// For each value of this feature
			for (String fVal : f.featureVals()) {
				// Count examples with matching feature value
				int p = 0;
				int count = 0;
				for (Example e: examples) {
					if (e.getFeatureVal(f).equals(fVal)) {
						count++;
						if (e.getClassification().equals(e.getClassifications().get(0)))
							p++;
					}
				}
				if (count != 0)
					entropy += ((double) count) / total * booleanEntropy ((double)p / count);
			}
			if (entropy < minEntropy) {
				minEntropy = entropy;
				splitFeature = f;
			}
		}
		return splitFeature;		
	}

	/**
	 * plurality - returns the majority classification of a set
	 */
	private String plurality (Set <Example> s) {
		Map <String, Integer> totals = new HashMap <String, Integer> ();
		for (Example e: s) {
			String c = e.getClassification();
			if (totals.get(c) == null) {
				totals.put(c, 0);
			}
			totals.put(c, totals.get(c) + 1);
		}
		int max = 0;
		String ret = "";
		for (String c: totals.keySet()) {
			if (totals.get(c) > max) {
				ret = c;
				max = totals.get (c);
			}
		}
		return ret;
	}
}

