/**
 * 
 */
package scorer;

import java.util.ArrayList;
import java.util.List;

import ling.*;

/**
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 * 
 * Program to evaluate the edge accuracy of two Split head encoded
 * trees in PTB style format converted from a dependency tree..
 *
 */
public class TreeEvaluator {
	
	public double EvaluateTree(Tree<String> _goldTree, Tree<String> _parsedTree) {
		/*
		 * return -1 if terminal yield is different.. 
		 * return -2 if the number of rules differ in the trees..
		 * signifies error
		 * 
		 */
		int _goldtreeSize=_goldTree.getYield().size();
		int _parsedtreeSize=_parsedTree.getYield().size();
		if(_goldtreeSize!=_parsedtreeSize) {
			return -1;
		}
		List<Rule> _goldRules = new ArrayList<Rule>();
		List<Rule> _obsRules = new ArrayList<Rule>();
		splitLabels(_goldTree);
		splitLabels(_parsedTree);
		makeRules(_goldTree,_goldRules);
		makeRules(_parsedTree,_obsRules);
		for(Rule r : _goldRules ) {
			String _ruleString=r.leftProduction.left+"*"+r.leftProduction.label+"*"+r.leftProduction.right;
			_ruleString = _ruleString + " -> ";
		for(Constituent c : r.rightProduction)
			  _ruleString=_ruleString+c.left+ "*"+c.label+ "*"+c.right+ "*"+" ";
			
			r.RuleString=_ruleString;
			System.out.println(r.RuleString);
		}
		for(Rule r : _obsRules ) {
			String _ruleString=r.leftProduction.left+"*"+r.leftProduction.label+"*"+r.leftProduction.right;
			_ruleString = _ruleString + " -> ";
			for(Constituent c : r.rightProduction)
			  _ruleString=_ruleString+c.left+ "*"+c.label+ "*"+c.right+ "*"+" ";
			r.RuleString=_ruleString;
			System.out.println("2"+r.RuleString);
		}

		/*
		 * return -2 if the number of rules differ for the gold tree
		 * and the tree to be evaluated.
		 * 
		 */
		
		int _numRules=_goldRules.size(),_obRules = _obsRules.size();
		System.out.println("The Total number of rules is.."+_numRules);
		if (_numRules!=_obRules)
			return -2;
		for(Rule _gRule : _goldRules)
			for(Rule _obRule : _obsRules)
				if(_obRule.RuleString.equalsIgnoreCase(_gRule.RuleString)) 
					System.out.println("fired! for ." + _obRules--); 
		double retval = _numRules - _obRules;
		return retval/_numRules;
	}
	public void makeRules(Tree<String> _inputTree, List<Rule> observedRules) {
		Constituent _leftConst = new Constituent(_inputTree.getLabel(),_inputTree.getLeft(),_inputTree.getRight());
		List<Constituent> _rightConst = new ArrayList<Constituent>();
		boolean isUnaryRule=false;
		for(Tree<String> _childTree: _inputTree.getChildren()) {
			Constituent _curConstituent=new Constituent(_childTree.getLabel(),_childTree.getLeft(),_childTree.getRight());
			_rightConst.add(_curConstituent);
		}
		if(_rightConst.size()==1)
			isUnaryRule=true;
		else if(_rightConst.size()==0)
			return;
		Rule _rule = new Rule(_leftConst,_rightConst,isUnaryRule);
		observedRules.add(_rule);
		if(_inputTree.isPreTerminal())
			return;
		else
			for(Tree<String> _childTree : _inputTree.getChildren())
			   makeRules(_childTree, observedRules);
	}
	public static void splitLabels(Tree<String> _inputTree) {
		String _label=_inputTree.getLabel();
		String[] _lbls=_label.split("=");
		
		if(_lbls.length==0) return;
		
		if(_lbls[1].equals("S")) {
			
			_inputTree.left=null;
			_inputTree.label="S";
			_inputTree.right=null;
		}
		else if(_lbls[1].equals("L")) {
			_inputTree.left=null;
			_inputTree.label=_lbls[1];
			System.out.println(_lbls[1]);
			_inputTree.right=_lbls[2];
		}
		else if(_lbls[1].equals("R")) {
			_inputTree.left=_lbls[0];
			_inputTree.label=_lbls[1];
			_inputTree.right=null;
		}
		else if(_lbls[1].equals("X")) {
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
			if(!(_lbls.length <3))
			_inputTree.right=_lbls[2];
		}
		if(_inputTree.isLeaf())
			return;
		else
			for(Tree<String> _child : _inputTree.getChildren())
				splitLabels(_child);
	}
}