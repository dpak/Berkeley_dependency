/**
 * 
 */
package ling;

import java.io.*;
import java.util.*;

/**
 * 
 * 
 * 
 *
 */
	 public class PennTreeReader implements Iterator<Tree<String>> {
		    public static String ROOT_LABEL = "ROOT";

		    PushbackReader in;
		    Tree<String> nextTree;

		    public boolean hasNext() {
		      return (nextTree != null);
		    }

		    public Tree<String> next() {
		      if (!hasNext()) throw new NoSuchElementException();
		      Tree<String> tree = nextTree;
//		      System.out.println(nextTree);
//		      System.out.println("in next");
		      nextTree = readRootTree();
		      return tree;
		    }

		    private Tree<String> readRootTree() {
		      try {
		        readWhiteSpace();
		        if (!isLeftParen(peek())) return null;
		        return readTree(true);
		      } catch (IOException e) {
		      	e.printStackTrace();
		        throw new RuntimeException("Error reading tree.");
		      }
		    }

		    private Tree<String> readTree(boolean isRoot) throws IOException {
		      readLeftParen();
		      String label = readLabel();
		      if (label.length() == 0 && isRoot) label = ROOT_LABEL;
		      List<Tree<String>> children = readChildren();
		      readRightParen();
		      return new Tree<String>(label, children);
		    }

		    private String readLabel() throws IOException {
		      readWhiteSpace();
		      return readText();
		    }

		    private String readText() throws IOException {
		      StringBuilder sb = new StringBuilder();
		      int ch = in.read();
		      while (!isWhiteSpace(ch) && !isLeftParen(ch) && !isRightParen(ch)) {
		        sb.append((char) ch);
		        ch = in.read();
		      }
		      in.unread(ch);
//		      System.out.println("Read text: ["+sb+"]");
		      return sb.toString().intern();
		    }

		    private List<Tree<String>> readChildren() throws IOException {
		      readWhiteSpace();
		      if (!isLeftParen(peek()))
		        return Collections.singletonList(readLeaf());
		      return readChildList();
		    }

		    private int peek() throws IOException {
		      int ch = in.read();
		      in.unread(ch);
		      return ch;
		    }

		    private Tree<String> readLeaf() throws IOException {
		      String label = readText();
		      return new Tree<String>(label);
		    }

		    private List<Tree<String>> readChildList() throws IOException {
		      List<Tree<String>> children = new ArrayList<Tree<String>>();
		      readWhiteSpace();
		      while (!isRightParen(peek())) {
		        children.add(readTree(false));
		        readWhiteSpace();
		      }
		      return children;
		    }

		    private void readLeftParen() throws IOException {
//		      System.out.println("Read left.");
		      readWhiteSpace();
		      int ch = in.read();
		      if (!isLeftParen(ch)) throw new RuntimeException("Format error reading tree. (leftParen)");
		    }

		    private void readRightParen() throws IOException {
//		      System.out.println("Read right.");
		      readWhiteSpace();
		      int ch = in.read();
		      
		      if (!isRightParen(ch)) {
		      	System.out.println((char)ch);
		      	throw new RuntimeException("Format error reading tree. (rightParen)");
		      }
		    }

		    private void readWhiteSpace() throws IOException {
		      int ch = in.read();
		      while (isWhiteSpace(ch)) {
		        ch = in.read();
		      }
//		      System.out.println(ch);
		      in.unread(ch);
		    }

		    private boolean isWhiteSpace(int ch) {
		      return (ch == ' ' || ch == '\t' || ch == '\f' || ch == '\r' || ch == '\n');
		    }

		    private boolean isLeftParen(int ch) {
		      return ch == '(';
		    }

		    private boolean isRightParen(int ch) {
		      return ch == ')';
		    }

		    public void remove() {
		      throw new UnsupportedOperationException();
		    }
		    
		    public PennTreeReader(Reader in) {
		      this.in = new PushbackReader(in);
		      nextTree = readRootTree();
		      //System.out.println(nextTree);
		    }
		  }
	

