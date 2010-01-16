/**
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ling.*;
import static scorer.TreeEvaluator.evaluateTree;
import static scorer.TreeEvaluator.EvaluationResult;


/**
 * @author Deepak Santhanam <deepak@cs.brown.edu>
 * @author Stu Black <dsblack@cs.brown.edu>
 */
public class Tester {

	public static void main(String[] args) {
            String goldFileName = "22.gold";
            String evalFileName = "22p.txt";
            if (args.length == 2) {
                goldFileName = args[0];
                evalFileName = args[1];
            } else if (args.length != 0) {
                System.err.println("Usage: java Tester [goldfile] [testfile]");
                System.err.println("Default goldfile: " + goldFileName);
                System.err.println("Default testfile: " + evalFileName);
                System.exit(-1);
            }

	    PennTreeReader _goldReader = null;
            try {
                _goldReader = new PennTreeReader(new FileReader(goldFileName));
            } catch (FileNotFoundException fnfe) {
                System.err.println("File " + goldFileName + " does not exist.");
                System.exit(-1);
            }

	    PennTreeReader _evalReader = null;
            try {
                _evalReader = new PennTreeReader(new FileReader(evalFileName));
            } catch (FileNotFoundException fnfe) {
                System.err.println("File " + evalFileName + " does not exist.");
                System.exit(-1);
            }

            int trees = 0;
            int totalArcsCorrect = 0;
            int totalArcs = 0;
            float runningAverage = 0;
            int totalTreesCorrect = 0;
	    
	   while(_goldReader.hasNext() && _evalReader.hasNext()) {
               trees++;
               EvaluationResult result = evaluateTree(_goldReader.next(), _evalReader.next());
               if (result.isMisparse()) {
                   System.out.println(trees + ":\tmisparse");
               } else {
                   if (result.isCompletelyCorrect()) {
                       totalTreesCorrect++;
                   }
                   // System.out.println(trees + ":\t" + result.getCorrectArcs() + "/" + result.getArcs());
                   runningAverage += ((float)result.getCorrectArcs()) / result.getArcs();
                   totalArcsCorrect += result.getCorrectArcs();
               }
               totalArcs += result.getArcs();
           }

           System.out.println("Total trees:\t" + trees);
           System.out.println("UAS (micro):\t" + ((float)totalArcsCorrect) / totalArcs);
           System.out.println("UAS (macro):\t" + runningAverage / trees);
           System.out.println("Exact match:\t" + ((float)totalTreesCorrect) / trees);
	}
}
