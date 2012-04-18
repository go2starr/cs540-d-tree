import java.util.ArrayList;
import java.util.List;

class Feature {
	// List of possible values
	List <String> values = new ArrayList <String> ();
	String featureName;
	
	// C'tor
	public Feature (String featureName, List <String> vals) {
		this.featureName = featureName;
		this.values = vals;
	}
	
	// List of possible values
	public List <String> featureVals () {
		return values;
	}
	
	// Error checking
	public boolean isValid (String val) {
		return values.contains(val);
	}
}
