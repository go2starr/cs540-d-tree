import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * The example class is given a list of valid features and classifications.
 * 
 * Once these legal values are set, specific features may be given values, and
 * a classification may be set.  The methods enforce legal values via return values.
 * 
 * @author starr
 *
 */
class Example {
	
	// Label of example
	private String label;
	
	// Actual Values
	private Map<Feature, String> featureVals = new HashMap<Feature,String> ();		// Actual Values
    private String classification;													// Actual Classification
    
    // Legal values
	private List <Feature> features = new ArrayList<Feature> (); 		// Legal features
	private List <String> classifications = new ArrayList <String> (); 	// Legal classification

	// C'tor
    public Example (List <Feature> features, List <String> classifications) {
    	this.features = features;
    	this.classifications = classifications;
    }
    
    
    // Returns true on error
    public boolean setFeature (Feature feat, String featureVal) {
    	// Check legal value
    	if (!feat.isValid(featureVal))
    		return true;
    	
    	// Check legal feature
    	if (!featureVals.containsKey(feat))
    		return true;
    	
    	// Set feature
    	featureVals.put(feat, featureVal);
    	
    	return false;
    }
    
    // Returns true on error
    public boolean setFeature (String featureVal, int index) {
    	// Check legal value
    	if (!features.get(index).isValid(featureVal)) return true;
    	
    	// Get feature
    	Feature feat = features.get (index);
    	
    	// Put val
    	featureVals.put(feat, featureVal);
    	return false;
    }
    
   // Returns true on error
   public boolean setFeature (String featureVal) {
	   for (Feature f: features) {
		   if (f.isValid(featureVal)) { // found a valid feature
			   featureVals.put(f, featureVal);
			   return false;
		   }
	   }
	   return true;
   }
   
    // Returns true on error
    public boolean setClassification (String classification) {
    	if (!classifications.contains(classification))
    		return true;
    	this.classification = classification;
		return false;
    }
    
    // Printing (debug only)
    public String toString () {
    	String ret = "Features = ";
    	for (Feature f: featureVals.keySet()) {
    		ret += f.featureName + "=" + featureVals.get(f) + " ";
    	}
    	ret += " Classification: " + classification;
    	return ret;
    }

    
    // Getters & Setters
    public String getFeatureVal (Feature f) {
    	return featureVals.get(f);
    }
	public String getClassification() {
		return classification;
	}
	public String getLabel () { 
		return label; 
		}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getClassifications() {
		return classifications;
	}
   }



