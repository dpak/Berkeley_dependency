/**
 * 
 */
package ling;

/**
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 * 
 * Represent dependency grammar constituents..
 *
 */
public class Constituent {
	
  public String label,left,right;
  
  public Constituent(String label, String left, String right) {
	  this.label=label;
	  this.left=left;
	  this.right=right;
  }
  
  public String getLabel() {
	  return this.label;
  }
  
  public String getLeft() {
	  return this.left;
  }  
  
  public String getRight() {
	  return this.right;
  }
 }
