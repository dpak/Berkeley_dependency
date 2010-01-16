/**
 * 
 */
package ling;

/**
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 *
 * Represent Dependency between two words..
 *
 */
public class Dependency {

	/*
	 * I'm using an integer to represent left or right dependencies
	 * 
	 * 1- left dependency
	 * 2- right dependency
	 * 
	 */
	
	public int type = -1;
	public String left,right;
	
	public Dependency(int _type, String _left, String _right) {
		this.left=_left;
		this.right=_right;
		this.type=_type;
	}
	
	
	
}
