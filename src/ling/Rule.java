/**
 * 
 */
package ling;

import java.util.List;


/**
 *
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 *
 * Represent dependency rules..
 * 
 */
public class Rule {
	
    public Constituent leftProduction;
    public List<Constituent> rightProduction;
    public boolean isUnary;
    public String RuleString;
    
    public Rule(Constituent _left, List<Constituent> _right, Boolean _isUnary) {
    	this.leftProduction=_left;
    	this.rightProduction=_right;
    	this.isUnary=_isUnary;
    }
    
    public Constituent getLeft() {
    	return this.leftProduction;
    }
    
    public List<Constituent> getRight() {
    	return this.rightProduction;
    }
}
