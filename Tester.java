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
import scorer.TreeEvaluator;
import ling.PennTreeReader;

/**
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 */
public class Tester {

	public static void main(String[] args) throws FileNotFoundException {
		
		TreeEvaluator _tEval = new TreeEvaluator();
		
		File _gold=new File("22.gold");
	    Reader _goldIn=new  FileReader(_gold);
	   
	    File _eval=new File("22p.txt");
	    Reader _evalIn=new  FileReader(_eval);
	   
	    PennTreeReader _goldReader = new PennTreeReader(_goldIn);
	    PennTreeReader _evalReader = new PennTreeReader(_evalIn);
	  
	    double _sum=0; int _nos=0;
	    
	    ArrayList<Double> _scores = new ArrayList<Double>();
	    
	//    while(_goldReader.hasNext() && _evalReader.hasNext()) {
	
	    	Double _pval = new Double(_tEval.EvaluateTree(_goldReader.next(),_evalReader.next()));
	    	_scores.add(_pval);
	    	_sum=_sum+_pval.doubleValue();
	    	_nos++;
	    	System.out.println(_nos +" : "+ _pval.doubleValue());
	  //  }
	}
	
	public static void splitLabels(Tree<String> _inputTree) {
		
		String _label=_inputTree.getLabel();
		String[] _lbls=_label.split("=");
		
		if(_lbls[1].equalsIgnoreCase("S")) {
			_inputTree.left=null;
			_inputTree.label="S";
			_inputTree.right=null;
		}
		
		else if(_lbls[1].equalsIgnoreCase("L")) {
			_inputTree.left=null;
			_inputTree.label=_lbls[1];
			_inputTree.right=_lbls[2];
		}
		
		else if(_lbls[1].equalsIgnoreCase("R")) {
			_inputTree.left=_lbls[0];
			_inputTree.label=_lbls[1];
			_inputTree.right=null;
		}
		else if(_lbls[1].equalsIgnoreCase("X")) {
			_inputTree.left=_lbls[0];
			_inputTree.label="X";
			if(_lbls.length<3)
			_inputTree.right=null;
			else
			_inputTree.right=_lbls[2];
		}
		else if(_lbls[1]!=null) {
			_inputTree.left=null;
			_inputTree.label=_lbls[1];
			_inputTree.right=_lbls[2];
		}

		if(_inputTree.isLeaf())
			return;
		else
			for(Tree<String> _child : _inputTree.getChildren())
				splitLabels(_child);
		
	}
}
