import java.io.IOException;


/**
 * BuildAndTestDecisionTree - driver class to, well, build and test a decision tree.
 * 
 * @author starr
 *
 */
class BuildAndTestDecisionTree {
    
	/**
	 * main - parses input data @args[0] to build a decision tree, and
	 * tests the test data, @args[1].
	 * 
	 * @param args
	 */
    public static void main (String[] args)
    {
    	if (args.length != 2) {
    		System.out.println ("Usage:\n  java BuildAndTestDecisionTree </path/to/trainset> </path/to/testset>");
    	} else {

    		// Read in filenames
    		String trainset = args[0];
    		String testset  = args[1];

    		// Parse training set
    		DecisionTreeParser p = null;
    		try {
    			p = new DecisionTreeParser (trainset);
    			p.parseExamples();
    		} catch (IOException e) {
    			System.out.println ("Unable to open file " + trainset);
    			System.exit (1);
    		}
    		
    		// Create decision tree
    		DecisionTree dTree = new DecisionTree (p.getExamples (), p.getFeatures ());
    		dTree.ID3 ();
    		
    		// Parse test data
    		DecisionTreeParser t = null;
    		try {
    			t = new DecisionTreeParser (testset);  
    			t.setFeatures (p.getFeatures ());  // MUST reuse the data structures produced by training set, this just consumes input
    			t.parseExamples();
    			
    			// Let's check the accuracy
    			int count = 0, correct = 0;
    			// System.out.println ("\n\n====  Failed Examples  ====");
    			for (Example e: t.getExamples()) {
    				count ++;
    				String classification = dTree.classify (e);
    				if (classification.equals(e.getClassification())) {
    					correct ++;
    				} else {
    					System.out.println (e.getLabel());
    				}
    			}
			//				if (correct - count == 0) System.out.print ("          -None-  ");
    			//System.out.println ("\n===========================");
    			//System.out.println ("Parsed with " + (double)correct/count*100 + "% accuracy");
			System.out.println ((double)correct/count*100);
    			//System.out.println ("===========================");
    			//System.out.println ("\n\nTree used: ");
    			dTree.print();
    		} catch (IOException e) {
    			System.out.println ("Unable to open file " + testset);
    			System.exit (1);
    		}
    	}
    }
}
