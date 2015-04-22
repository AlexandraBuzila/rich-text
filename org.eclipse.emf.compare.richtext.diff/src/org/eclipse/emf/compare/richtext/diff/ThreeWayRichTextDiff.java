/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Alexandra Buzila
 *     Florian Zoubek
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.emf.compare.richtext.diff.internal.RTNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTTagNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTTextNode;
import org.eclipse.emf.compare.richtext.diff.internal.RichTextDiffer;
import org.eclipse.emf.compare.richtext.diff.internal.StringOutputGenerator;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.ModificationType;
import org.xml.sax.SAXException;

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
		if (merged == null) {
			merged = computeMerged();
		}
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
		if (ConflictState.UNKNOWN.equals(conflictState)) {
			conflictState = computeConflictState();
		}
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

	private RichTextDiff findOppositeDiff(Node node, ArrayList<RichTextDiff> searchList) {
		for (RichTextDiff diff : searchList) {
			if (diff.getChild() == null) {
				continue;
			}
			if (!(diff.getChild() instanceof RTNode)) {
				// FIXME should we fail in this case?
				continue;
			}
			RTNode child = (RTNode) diff.getChild();
			if (child.isSameNode(node))
				return diff;
		}
		return null;
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
	// TODO update once "requires/requiredBy" and "equivalence" relationships
	// for diffs are implemented
	private ConflictState hasConflictingStructuralChanges() {
		for (RichTextDiff diff : leftDiffs) {
			TagNode firstParent = findParent(diff.getChild());
			if (!firstParent.getQName().equals("body") && firstParent instanceof RTTagNode) {
				for (RichTextDiff diff2 : rightDiffs) {
					TagNode secondParent = findParent(diff2.getChild());
					if (secondParent instanceof RTTagNode && ((RTTagNode)firstParent).isSameNode((RTTagNode)secondParent)) {
						
						if(areNodeTreesEqual((RTTagNode)firstParent, (RTTagNode)secondParent)){
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
	 * tests if two node trees are equal by checking their qualified name,
	 * modification type, children (and their order) and text.
	 * 
	 * @param treeRoot1
	 *            the root {@link TagNode} of the first tree
	 * @param treeRoot2
	 *            the root {@link TagNode} of the second tree
	 * @return true if the trees are equal in the sense of equal qualified names
	 *         and equal children for {@link RTTagNode}s, as well as equal text
	 *         for {@link RTTextNode}s, false otherwise.
	 */
	private static boolean areNodeTreesEqual(RTTagNode treeRoot1, RTTagNode treeRoot2){
		
		if(treeRoot1.isSameTag(treeRoot2)){
			
			if(treeRoot1.getChildren().size() == treeRoot2.getChildren().size()){
				Iterator<Node> tree1ChildIt = treeRoot1.iterator();
				Iterator<Node> tree2ChildIt = treeRoot2.iterator();
				while(tree1ChildIt.hasNext()){
					Node child1 = tree1ChildIt.next();
					Node child2 = tree2ChildIt.next();
					if(child1 instanceof TextNode && child2 instanceof TextNode){
						if(!((TextNode)child1).isSameText(child2) || ((TextNode)child1).getModification().getType() != ((TextNode)child2).getModification().getType() ){
							return false;
						}
					}else if(child1 instanceof RTTagNode && child2 instanceof RTTagNode){
						if(!areNodeTreesEqual((RTTagNode)child1, (RTTagNode)child2)){
							return false;
						}
					}else{
						if( !child1.equals(child2)){
							return false;
						}
					}
				}
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @return the first parent of the node, that is a structure node (table
	 *         cell, paragraph, body node)
	 */
	private TagNode findParent(Node child) {
		if (isStructureNode(child.getParent())) {
			return child.getParent();
		}
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
		/*
		 * The list of nodes that need to be removed. The nodes that need to be
		 * removed will remain as placeholders in their parent's tree, until all
		 * the diffs are merged, enabling us to match nodes based on their index
		 * in the children's list. The node removal operation will be performed
		 * at the end of the merge operation.
		 */
		List<Node> nodesToRemove = new ArrayList<Node>();
		for (RichTextThreeWayDiff threeWayDiff : threeWayDifferences) {

			// TODO - handle conflicts

			// TODO The code currently merges rtl. Add logic for ltr merge.
			RichTextDiff rightDiff = threeWayDiff.getRightDiff();
			RichTextDiff leftDiff = threeWayDiff.getLeftDiff();

			if (rightDiff == null) {
				continue;
			}
			TagNode root = leftComparator.getBodyNode();
			TagNode rightParent = rightDiff.getChild().getParent();
			TagNode leftParent = (TagNode) findNode(root, rightParent);
			if (!(leftParent instanceof RTNode)) {
				// XXX - return?
				continue;
			}

			switch (rightDiff.getModification().getType()) {
			case ADDED: {
				addNode((RTNode) leftParent, rightDiff.getChild());
				break;
			}
			case REMOVED: {
				Node node = findNode(leftParent, rightDiff.getChild());
				if (node != null) {
					nodesToRemove.add(node);
				}
				break;
			}
			case CHANGED: {
				changeNode(leftParent, rightDiff.getChild());
				break;
			}
			default:
				break;
			}
		}
		removeNodes(leftComparator.getBodyNode(), nodesToRemove);
		return leftComparator;
	}

	private void removeNodes(TagNode root, List<Node> nodesToRemove) {

		getDeletedNodes(root, nodesToRemove);

		for (Node node : nodesToRemove) {
			deleteNodeFromParentList(node);
		}

	}

	private void getDeletedNodes(TagNode root, List<Node> nodesToRemove) {
		for (Node node : root) {
			if (node instanceof TagNode) {
				getDeletedNodes(((TagNode) node), nodesToRemove);
			} else {
				TextNode textNode = (TextNode) node;
				if (textNode.getModification().getType() == ModificationType.REMOVED) {
					nodesToRemove.add(node);
				}
			}
		}
	}

	private void deleteNodeFromParentList(Node node) {
		if (node == null) {
			return;
		}
		TagNode directParent = node.getParent();
		if (directParent == null) {
			return;
		}
		node.setParent(null);
		List<Node> children = getChildren(directParent);
		children.remove(node);
	}

	/**
	 * @param searchNode
	 *            the {@link TagNode} that needs to be found
	 * @param root
	 *            the root of the tree in which the search will be performed
	 * @return the match of the {@link TagNode} {@code searchNode} in the tree
	 *         with the given root
	 */
	private Node findNode(Iterable<Node> root, Node searchNode) {
		for (Node node : root) {
			if (node instanceof RTNode && ((RTNode) node).isSameNode(searchNode)) {
				return node;
			}
		}
		return null;
	}

	private void changeNode(TagNode leftParent, Node rightClone) {
		// TODO Auto-generated method stub

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

	private void addNode(RTNode parent, Node child) {
		if (!(parent instanceof TagNode) || !(child.getParent() instanceof RTNode))
			return;

		TagNode tagParent = (TagNode) parent;
		int index = child.getParent().getIndexOf(child);

		Node clone = child.copyTree();
		clone.setParent(tagParent);

		/*
		 * the parent in which we will insert the node can't have modified
		 * children, otherwise a conflict would have been raised
		 */
		int nbChildren = tagParent.getNbChildren();
		if (index > nbChildren) {
			tagParent.addChild(nbChildren, clone);
		} else {
			tagParent.addChild(index, clone);
		}

	}

}
