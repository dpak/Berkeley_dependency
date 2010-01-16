/**
 * 
 */
package scorer;

import java.util.ArrayList;
import java.util.List;

import ling.*;

/**
 * @author Deepak Santhanam <deepak@cs.brown.edu>
 * @author Stu Black <dsblack@cs.brown.edu>
 * 
 * Program to evaluate the edge accuracy of two Split head encoded
 * trees in PTB style format converted from a dependency tree..
 *
 */
public class TreeEvaluator {
    public static class MisparseException extends Exception {
        public MisparseException() {
            super();
        }
        public MisparseException(String message) {
            super(message);
        }
        public MisparseException(Throwable reason) {
            super(reason);
        }
        public MisparseException(String message, Throwable reason) {
            super(message, reason);
        }
    }

    public static class EvaluationResult {
        protected int arcs;
        protected int correctArcs;
        protected boolean misparse;

        public EvaluationResult(int arcs, int correctArcs) {
            this(arcs, correctArcs, false);
        }

        public EvaluationResult(int arcs, int correctArcs, boolean misparse) {
            this.arcs = arcs;
            this.correctArcs = correctArcs;
            this.misparse = misparse;
        }

        public int getArcs() { return arcs; }
        public void setArcs(int arcs) { this.arcs = arcs; }

        public int getCorrectArcs() { return correctArcs; }
        public void setCorrectArcs(int correctArcs) { this.correctArcs = correctArcs; }

        public boolean isMisparse() { return misparse; }
        public void setMisparse(boolean misparse) { this.misparse = misparse; }

        public boolean isCompletelyCorrect() { return !misparse && (arcs == correctArcs); }
    }

	public static EvaluationResult evaluateTree(Tree<String> _goldTree, Tree<String> _parsedTree) {
		int _goldtreeSize=_goldTree.getYield().size();
		int _parsedtreeSize=_parsedTree.getYield().size();
		if(_goldtreeSize!=_parsedtreeSize) {
                    return new EvaluationResult(_goldtreeSize, 0, true);
		}

                try {
                    int[] goldParents = getTreeParents(_goldTree);
                    int[] testParents = getTreeParents(_parsedTree);

                    int correctArcs = 0;
                    for (int i = 0; i < goldParents.length; i++) {
                        if (goldParents[i] == testParents[i]) {
                            correctArcs++;
                        }
                    }

                    return new EvaluationResult(goldParents.length, correctArcs, false);
                } catch (MisparseException me) {
                    return new EvaluationResult(_goldtreeSize, 0, true);
                }
        }


    protected static int[] getTreeParents(Tree<String> tree) throws MisparseException {
        Tree<IndexedNode> indexedTree = indexifyTree(tree);
        int[] parents = new int[tree.getYield().size()];
        for (java.util.Iterator<Tree<IndexedNode>> i = indexedTree.iterator(); i.hasNext(); ) {
            Tree<IndexedNode> t = i.next(); 
            List<Tree<IndexedNode>> children = t.getChildren();

            IndexedNode x_child, L_child, R_child;
            switch(t.getLabel().type) {
            case 'L':
                if (children.size() == 2) {
                    x_child = children.get(0).getLabel();
                    L_child = children.get(1).getLabel();
                    if (parents[x_child.index] != 0) throw new MisparseException("already filled in parent");
                    parents[x_child.index] = L_child.index;
                }
                break;
            case 'R':
                if (children.size() == 2) {
                    x_child = children.get(1).getLabel();
                    R_child = children.get(0).getLabel();
                    if (parents[x_child.index] != 0) throw new MisparseException("already filled in parent");
                    parents[x_child.index] = R_child.index;
                }
                break;
            case 'S':
                x_child = children.get(0).getLabel();
                if (parents[x_child.index] != 0) throw new MisparseException("already filled in parent");
                parents[x_child.index] = -1;
                break;
            default:
                break;
            }
        }

        return parents;
    }

    protected static class IndexedNode {
        String label;
        int index;
        char type;

        IndexedNode(String label, int index, char type) {
            this.label = label;
            this.index = index;
            this.type = type;
        }
    }

    protected static Tree<IndexedNode> indexifyTree(Tree<String> tree) throws MisparseException {
        return indexifyTree(tree, new int[]{0});
    }

    protected static Tree<IndexedNode> indexifyTree(Tree<String> tree, int[] currentIndex) throws MisparseException {
        String[] label = tree.getLabel().split("_");

        java.util.List<Tree<IndexedNode>> newChildren = new java.util.ArrayList<Tree<IndexedNode>>();
        for (Tree<String> child : tree.getChildren()) {
            newChildren.add(indexifyTree(child, currentIndex));
        }

        int index;
        char type = label[1].charAt(0);
        switch(type) {
        case 'l':
        case 'r':
            if (tree.isLeaf()) index = currentIndex[0]++;
            else index = newChildren.get(0).getLabel().index;
            break;
        case 'S':
            index = -1;
            break;
        case 'L':
            if (newChildren.size() == 1) {
                index = newChildren.get(0).getLabel().index;
            } else {
                index = newChildren.get(1).getLabel().index;
            }
            break;
        case 'R':
            index = newChildren.get(0).getLabel().index;
            break;
        case 'X':
            index = newChildren.get(0).getLabel().index;
            break;
        default:
            throw new MisparseException("Unrecognized label " + tree.getLabel());
        }

        return new Tree<IndexedNode>(new IndexedNode(tree.getLabel(), index, type),
                                     newChildren);
    }
}
