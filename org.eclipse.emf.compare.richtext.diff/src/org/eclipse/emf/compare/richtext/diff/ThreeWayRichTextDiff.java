/**
 * 
 */
package org.eclipse.emf.compare.richtext.diff;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.SeparatingNode;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.dom.WhiteSpaceNode;
import org.outerj.daisy.diff.html.modification.ModificationType;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Alexandra Buzila
 * @author Philip Langer <planger@eclipsesource.com>
 *
 */
public class ThreeWayRichTextDiff {

	/**
	 * The conflict state of a three-way difference may either be unknown,
	 * conflicting, or free of conflicts.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private enum ConflictState {
		/** The conflict state is unknown yet. */
		UNKNOWN,
		/** The conflict state is conflicting. */
		CONFLICTING,
		/** The conflict state is free of conflicts. */
		NOT_CONFLICTING;
	}

	/** Specifies whether {@link #left} or {@link #right} has been unset. */
	private final boolean isLeftOrRightUnset;

	/** The computed three-way line differences. */
	private final List<RichTextThreeWayDiff> threeWayDifferences;

	/** The conflict state. */
	private ConflictState conflictState = ConflictState.UNKNOWN;

	/** The cached merged tree. */
	private TextNodeComparator merged;

	private final List<String> STRUCTURE_NODES = Arrays.asList("body", "p", "td");

	private ArrayList<RichTextDiff> leftDiffs;

	private ArrayList<RichTextDiff> rightDiffs;

	private TextNodeComparator leftComparator;

	public TextNodeComparator getLeftComparator() {
		return leftComparator;
	}

	private TextNodeComparator rightComparator;

	public TextNodeComparator getRightComparator() {
		return rightComparator;
	}

	/**
	 */
	public ThreeWayRichTextDiff(String origin, String left, String right) {
		this.isLeftOrRightUnset = origin != null && (left == null || right == null);
		this.threeWayDifferences = computeThreeWayDiffs(origin, left, right);
	}

	/**
	 */
	public String getMerged() {
		// if (merged == null) {
		merged = computeMerged();
		// }
		String output = getOutput(merged.getBodyNode());
		return output;
	}

	private String getOutput(TagNode node) {
		try {
			SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
			TransformerHandler result = tf.newTransformerHandler();
			Writer writer = new StringWriter();
			StreamResult streamResult = new StreamResult(writer);
			result.setResult(streamResult);

			result.startDocument();
			StringOutputGenerator outputGen = new StringOutputGenerator(result);
			outputGen.generateOutput(node);
			result.endDocument();
			String output = writer.toString();

			return output.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 */
	public boolean isConflicting() {
		// if (ConflictState.UNKNOWN.equals(conflictState)) {
		conflictState = computeConflictState();
		// }
		return ConflictState.CONFLICTING.equals(conflictState);
	}

	private List<RichTextThreeWayDiff> computeThreeWayDiffs(String origin, String left, String right) {
		RichTextDiffer leftDiffer = new RichTextDiffer();
		leftDiffs = leftDiffer.getDiffs(origin, left);
		leftComparator = leftDiffer.getComparator();

		RichTextDiffer rightDiffer = new RichTextDiffer();
		rightDiffs = rightDiffer.getDiffs(origin, right);
		rightComparator = rightDiffer.getComparator();

		final ArrayList<RichTextThreeWayDiff> threeWayDiffs = new ArrayList<RichTextThreeWayDiff>();
		// TODO - check the length of the lists and iterate over the smaller one
		@SuppressWarnings("unchecked")
		ArrayList<RichTextDiff> leftDiffsCpy = (ArrayList<RichTextDiff>) leftDiffs.clone();
		for (RichTextDiff diff : rightDiffs) {
			RichTextDiff oppositeDiff = findOppositeDiff(diff.getChild(), leftDiffsCpy);
			if (oppositeDiff != null) {
				leftDiffsCpy.remove(oppositeDiff);
			}
			threeWayDiffs.add(new RichTextThreeWayDiff(oppositeDiff, diff));
		}
		for (RichTextDiff diff : leftDiffsCpy) {
			threeWayDiffs.add(new RichTextThreeWayDiff(diff, null));
		}
		return threeWayDiffs;
	}

	private RichTextDiff findOppositeDiff(TextNode child, ArrayList<RichTextDiff> leftDiffs) {
		for (RichTextDiff diff : leftDiffs) {
			if (diff.getChild() == null)
				continue;
			if (diff.getChild().isSameText(child)) {
				return diff;
			}
		}
		return null;
	}

	/**
	 * Specifies whether {@code diff} is a deletion.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a deletion, <code>false</code>
	 *         otherwise.
	 */
	private boolean isDelete(RichTextDiff diff) {
		return diff != null && ModificationType.REMOVED.equals(diff.getModification().getType());
	}

	/**
	 * Specifies whether {@code diff} is a insertion.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a insertion, <code>false</code>
	 *         otherwise.
	 */
	private boolean isInsert(RichTextDiff diff) {
		return diff != null && ModificationType.ADDED.equals(diff.getModification().getType());
	}

	/**
	 * Computes the conflict state based on the conflict state of all
	 * {@link #threeWayDifferences}.
	 * 
	 * @return The computed conflict state.
	 */
	private ConflictState computeConflictState() {
		for (RichTextThreeWayDiff threeWayDiff : threeWayDifferences) {
			// conflict comes from changes to the same text node
			if (threeWayDiff.isConflicting()) {
				return ConflictState.CONFLICTING;
			}
		}
		return hasConflictingStructuralChanges();
	}

	/***/
	private ConflictState hasConflictingStructuralChanges() {
		for (RichTextDiff diff : leftDiffs) {
			TagNode firstParent = findParent(diff.getChild());
			if (!firstParent.getQName().equals("body")) {
				for (RichTextDiff diff2 : rightDiffs) {
					TagNode secondParent = findParent(diff2.getChild());
					if (isSameTag(firstParent, secondParent)) {
						if (firstParent.getNbChildren() == secondParent.getNbChildren()) {
							boolean equal = true;
							int i = 0;
							while (equal && i < firstParent.getNbChildren()) {
								Node firstChild = firstParent.getChild(i);
								Node secondChild = secondParent.getChild(i);
								if (!(firstChild instanceof SeparatingNode) && !(secondChild instanceof SeparatingNode)) {
									if (firstChild instanceof TextNode)
										equal = ((TextNode) firstChild).isSameText(secondChild);
									else
										equal = firstChild.equals(secondChild);
								}
								i++;
							}
							if (equal)// pseudoconflict
								continue;
						}

						return ConflictState.CONFLICTING;
					}
				}
			}
		}
		return ConflictState.NOT_CONFLICTING;
	}

	/**
	 * return true if the tag nodes are located in the same position in the tree
	 * and have the same children
	 */
	public static boolean isSameTag(TagNode rightParent, TagNode leftParent) {

		if (rightParent == leftParent)
			return true;

		if (!rightParent.getQName().equals(leftParent.getQName()))
			return false;

		// useful??
		double matchRatio = rightParent.getMatchRatio(leftParent);
		if (matchRatio > 0.5)
			return false;
		if (matchRatio == 0)
			return true;

		// the roots are always equal (<body> node)
		if (rightParent.getParent() == null) {
			return (leftParent.getParent() == null);
		}
		// left is root, but right wasn't
		if (leftParent.getParent() == null)
			return false;

		int rightIndexInParent = rightParent.getParent().getIndexOf(rightParent);
		int leftIndexInParent = leftParent.getParent().getIndexOf(leftParent);

		if (rightIndexInParent == leftIndexInParent) {

			ArrayList<Node> leftChildren = cloneChildren(leftParent);
			ArrayList<Node> rightChildren = cloneChildren(rightParent);

			removeNewChildren(leftChildren);
			removeNewChildren(rightChildren);
			if (leftChildren.size() != rightChildren.size())
				return false;
			else
				for (int i = 0; i < leftChildren.size(); i++) {
					Node leftNode = leftChildren.get(i);
					Node rightNode = rightChildren.get(i);
					if (!leftNode.getClass().equals(rightNode.getClass()))
						return false;
					if (leftNode instanceof TextNode) {
						String leftText = ((TextNode) leftNode).getText();
						String rightText = ((TextNode) rightNode).getText();
						if (!leftText.equals(rightText))
							return false;
					} else {
						String leftTag = ((TagNode) leftNode).getQName();
						String rightTag = ((TagNode) rightNode).getQName();
						if (!leftTag.equals(rightTag))
							return false;
					}

				}
			return true;

			/* from http://dl.acm.org/citation.cfm?doid=1030397.1030399 */
			// findCommonNodes(leftParent, rightParent);
			// double commonLeaves = 0;
			// ArrayList<TextNode> rightLeaves = new ArrayList<TextNode>();
			// getTextNodes(rightParent, rightLeaves);
			// ArrayList<TextNode> leftLeaves = new ArrayList<TextNode>();
			// getTextNodes(leftParent, leftLeaves);
			// for (TextNode node : rightLeaves) {
			// if (leftLeaves.contains(node))
			// commonLeaves++;
			// }
			// return (commonLeaves / Math.max(rightLeaves.size(),
			// leftLeaves.size())) > 0.5;
		}
		if (leftParent.getParent().getNbChildren() > leftIndexInParent) {
			Node tempNodeAtSamePosition = leftParent.getParent().getChild(leftIndexInParent);
			if (rightParent.isSimilarTag(tempNodeAtSamePosition)) {
				return false;
			}
		}
		return true;
	}

	private static void removeNewChildren(ArrayList<Node> children) {
		ListIterator<Node> listIterator = children.listIterator();

		while (listIterator.hasNext()) {
			Node child = listIterator.next();
			if (child instanceof TextNode) {
				if (((TextNode) child).getModification().getType() == ModificationType.ADDED) {
					listIterator.remove();
				}
				// FIXME - whitespaces are removed for now, because DaisyDiff
				// currently does not correctly handle them (multiple
				// whitespaces are ignored while parsing, whitespace
				// modifications are not correctly assigned - the whitespace
				// before a currently inserted node is not marked as new if the
				// word is preceded by existing text)
				else if (child instanceof WhiteSpaceNode) {
					listIterator.remove();
				}
			} else if (allNewChildren((TagNode) child)) {
				listIterator.remove();
			}
		}
	}

	private static boolean allNewChildren(TagNode node) {
		ArrayList<TextNode> textNodes = new ArrayList<TextNode>();
		getTextNodes(node, textNodes);
		if (textNodes.isEmpty())
			return false;
		for (TextNode textNode : textNodes) {
			if (textNode.getModification().getType() != ModificationType.ADDED)
				return false;
		}
		return true;
	}

	private static ArrayList<Node> cloneChildren(TagNode parent) {
		ArrayList<Node> children = new ArrayList<Node>();
		for (Node child : parent) {
			children.add(child);
		}
		return children;
	}

	private static ArrayList<TextNode> getTextNodes(TagNode parent, ArrayList<TextNode> textNodes) {
		for (int i = 0; i < parent.getNbChildren(); i++) {
			if (parent.getChild(i) instanceof TagNode)
				getTextNodes((TagNode) parent.getChild(i), textNodes);
			else
				textNodes.add((TextNode) parent.getChild(i));
		}
		return null;
	}

	/**
	 * @return the first parent of the node, that is a structure node (table
	 *         cell, paragraph, body node)
	 */
	private TagNode findParent(Node child) {
		if (isStructureNode(child.getParent()))
			return child.getParent();
		return findParent(child.getParent());
	}

	private boolean isStructureNode(TagNode parent) {
		return STRUCTURE_NODES.contains(parent.getQName());
	}

	/**
	 * Computes the merge result based on the {@link #threeWayDifferences}.
	 * 
	 * @return The result of merging all {@link #threeWayDifferences}.
	 */
	private TextNodeComparator computeMerged() {
		for (RichTextThreeWayDiff threeWayDiff : threeWayDifferences) {
			// TODO The code currently merges rtl. Add logic for ltr merge.
			RichTextDiff rightDiff = threeWayDiff.getRightDiff();
			RichTextDiff leftDiff = threeWayDiff.getLeftDiff();
			if (rightDiff == null) {
				continue;
			}
			TagNode root = leftComparator.getBodyNode();
			TextNode rightClone = (TextNode) rightDiff.getChild().copyTree();
			TagNode rightParent = rightDiff.getChild().getParent();
			TagNode leftParent = findTagNode(root, rightParent);
			if (rightDiff.getModification().getType() == ModificationType.ADDED) {
				int index = findInsertionIndex(leftParent, rightDiff.getChild());
				addNode(leftParent, rightClone, index);
			} else if (rightDiff.getModification().getType() == ModificationType.REMOVED) {
				removeNode(leftParent, rightClone);
			} else if (rightDiff.getModification().getType() == ModificationType.CHANGED) {
				changeNode(leftParent, rightClone);
			}
		}
		return leftComparator;
	}

	private TagNode findTagNode(TagNode root, TagNode rightParent) {
		for (Node node : root) {
			if (node instanceof TagNode) {
				if (isSameTag((TagNode) node, rightParent))
					return ((TagNode) node);
			}
		}
		return null;
	}

	private int findInsertionIndex(TagNode leftParent, TextNode child) {
		int indexOf = child.getParent().getIndexOf(child);
		return indexOf;
	}

	private void changeNode(TagNode leftParent, Node rightClone) {
		// TODO Auto-generated method stub

	}

	private void removeNode(TagNode parent, Node node) {
		Node toRemove;
		if (node instanceof TagNode)
			toRemove = findTagNode(parent, (TagNode) node);
		toRemove = findTextNode(parent, (TextNode) node);
		TagNode directParent = toRemove.getParent();
		toRemove.setParent(null);
		List<Node> children = getChildren(directParent);
		children.remove(toRemove);
	}

	@SuppressWarnings("unchecked")
	private List<Node> getChildren(TagNode directParent) {
		try {
			Field field = TagNode.class.getDeclaredField("children");
			field.setAccessible(true);
			return (List<Node>) field.get(directParent);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Node findTextNode(TagNode parent, TextNode searchNode) {
		for (Node node : parent) {
			if (node instanceof TagNode)
				findTextNode((TagNode) node, searchNode);
			if (((TextNode) node).getText().equals(searchNode.getText())) {
				//FIXME how do we handle repeating words? check position of node in the parent's children list
				return node;
			}
		}
		return null;
	}

	private void addNode(TagNode parent, TextNode child, int index) {
		child.setParent(parent);
		int nbChildren = parent.getNbChildren();
		if (index > nbChildren)
			parent.addChild(nbChildren, child);
		else
			parent.addChild(index, child);

	}

}
