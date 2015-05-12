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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.emf.compare.richtext.diff.internal.RTBodyNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTTagNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTTextNode;
import org.eclipse.emf.compare.richtext.diff.internal.RichTextDiffer;
import org.eclipse.emf.compare.richtext.diff.internal.StringOutputGenerator;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.Modification;
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
			Node node = diff.getChild();
			TagNode firstParent = findParent(diff.getChild());
			if (firstParent instanceof RTTagNode || firstParent instanceof RTBodyNode) {
				for (RichTextDiff diff2 : rightDiffs) {
					TagNode secondParent = findParent(diff2.getChild());
					if (secondParent instanceof RTTagNode && ((RTTagNode)firstParent).isSameNode((RTTagNode)secondParent)) {
						
						if(areNodeTreesEqual((RTTagNode)firstParent, (RTTagNode)secondParent)){
							continue;
						}

						return ConflictState.CONFLICTING;
					} else if (node == firstParent
							&& secondParent == diff2.getChild()
							&& diff.getModification().getType() == ModificationType.ADDED
							&& diff2.getModification().getType() == ModificationType.ADDED) {
						// although the tags are not equal, they might be added
						// on the same location within their parent, which is
						// also a conflict
						
						// first retrieve the parents
						TagNode realLeftParent = node.getParent();
						TagNode realRightParent = diff2.getChild().getParent();
						if (realLeftParent instanceof RTTagNode
								&& realRightParent instanceof RTTagNode) {
							RTTagNode leftRTParent = (RTTagNode) realLeftParent;
							RTTagNode rightRTParent = (RTTagNode) realRightParent;
							// if the parents are not the same there is no need
							// to check the insertion index
							if (leftRTParent.isSameNode(rightRTParent)
									&& countPreceedingSiblingsWithoutInsertions(node) == countPreceedingSiblingsWithoutInsertions(diff2
											.getChild())) {
								return ConflictState.CONFLICTING;
								
							}
						}else if (realLeftParent instanceof RTBodyNode
								&& realRightParent instanceof RTBodyNode
								&& countPreceedingSiblingsWithoutInsertions(node) == countPreceedingSiblingsWithoutInsertions(diff2
										.getChild())) {
							// body nodes are always equal, so we do not need to check the equality
							return ConflictState.CONFLICTING;
						}
					}
				}
			}
			// search for conflicting structural changes inside tables such
			// as adding/removing columns in one version and adding/removing
			// rows in the other
			if (node instanceof TagNode && ((TagNode) node)
							.getQName().equals("td")) {
				
				// td has changed -> check if this is part of a column change
				// and if that's the case, also check that no row in the table
				// has been changed in the right version
				TagNode leftTable = findParentTag(node, "table");
				if (leftTable instanceof RTTagNode
						&& hasTableColumnChanged(leftTable)) {
					
					// table columnn has been changed -> check for row changes
					// in the same table of the right version
					for (RichTextDiff rightDiff : rightDiffs) {
						
						Node rightNode = rightDiff.getChild();
						if(rightNode instanceof TagNode && ((TagNode) rightNode).getQName().equals("tr")){
							
							TagNode rightTable = findParentTag(rightNode, "table");
							if (rightTable instanceof RTTagNode && ((RTTagNode)leftTable).isSameNode((RTTagNode)rightTable)) {
								return ConflictState.CONFLICTING;
							}
						}
						
					}
				}
				// FIXME how should we handle malformed HTML (table rows or columns without parent table) in this case?
			}else if(node instanceof TagNode && ((TagNode) node)
					.getQName().equals("tr")){
				
				// table row has changed -> check that no table column in the
				// parent table has been changed
				TagNode leftTable = findParentTag(node, "table");
				if (leftTable instanceof RTTagNode){
					
					for (RichTextDiff rightDiff : rightDiffs) {
						
						Node rightNode = rightDiff.getChild();
						if(rightNode instanceof TagNode && ((TagNode) rightNode).getQName().equals("td")){
							
							TagNode rightTable = findParentTag(rightNode, "table");
							if (rightTable instanceof RTTagNode
									&& ((RTTagNode) leftTable)
											.isSameNode((RTTagNode) rightTable)
									&& hasTableColumnChanged(rightTable)) {
								return ConflictState.CONFLICTING;
							}
							
						}
						
					}
					
				}
				// FIXME how should we handle malformed HTML (table rows or columns without parent table) in this case?
			}
		}
		return ConflictState.NOT_CONFLICTING;
	}
	
	/**
	 * counts the number of preceeding siblings that have not been added.
	 * 
	 * @param node
	 *            the node whose siblings should be counted
	 * @return the number of preceeding siblings that have not been added.
	 */
	private int countPreceedingSiblingsWithoutInsertions( Node node ){
		int counter = 0;
		
		TagNode parent = node.getParent();
		
		if(parent != null){
			for(Node sibling : parent){
				if(sibling == node){
					break;
				}
				ModificationType type = ModificationType.NONE;
				if(sibling instanceof RTTagNode){
					type = ((RTTagNode) sibling).getModification().getType();
				}else if(sibling instanceof TextNode){
					type = ((TextNode) sibling).getModification().getType();
				}
				
				if(type != ModificationType.ADDED){
					counter++;
				}
			}
		}
		
		return counter;
	}
	
	/**
	 * checks if at least one column in the given table tag has been changed
	 * (all td tags in a column have a modification type !=
	 * {@link ModificationType#NONE}).
	 * 
	 * @param parentTable
	 *            the table to check
	 * @return true if at least one column has been changed, false otherwise
	 */
	private boolean hasTableColumnChanged(TagNode parentTable) {
		
		int numRows = 0;
		int colIndex = 0;
		
		Map<Integer, Integer> changedCellsInCol = new HashMap<Integer, Integer>();
		
		Stack<TagNode> iterationStack = new Stack<TagNode>();
		iterationStack.push(parentTable);
		
		// iterate over the DOM-tree and count the number of rows and changed
		// cells in each row
		while(!iterationStack.isEmpty()){
			TagNode node = iterationStack.pop();
			String qName = node.getQName();
			
			if(qName.equals("tr")){
				
				numRows++;
				colIndex = 0;
				
			}else if(qName.equals("td")){
				
				if (node instanceof RTTagNode
						&& !((RTTagNode) node).getModification().getType()
								.equals(ModificationType.NONE)) {
					
					if(changedCellsInCol.containsKey(colIndex)){
						
						int prevCount = changedCellsInCol.get(colIndex);
						prevCount++;
						changedCellsInCol.put(colIndex, prevCount);
						
					}else{
						changedCellsInCol.put(colIndex, 1);
					}
					
				}
				colIndex++;
				
			}
			
			if (!qName.equals("td")) {
				// we don't iterate deeper than the first table cell, as we
				// don't want to count rows and cells inside other table cells
				for(Node childNode : node){
					
					if(childNode instanceof TagNode){
						iterationStack.push((TagNode)childNode);
					}
					
				}
			}
		}
		
		for(Entry<Integer, Integer> column : changedCellsInCol.entrySet()){
			
			if(column.getValue() == numRows){
				return true;
			}
			
		}
		
		return false;
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
	 * @return the first parent of the node or the node itself, that is a
	 *         structure node (table cell, paragraph, body node)
	 */
	private TagNode findParent(Node child) {
		if(child instanceof TagNode && isStructureNode((TagNode) child)){
			return (TagNode)child;
		}
		return findParent(child.getParent());
	}
	
	/**
	 * 
	 * @param node the node to find the parent tag for
	 * @param qName the qualified name of the parent tag to search for
	 * @return the parent table tag or null if it does not exist
	 */
	private TagNode findParentTag(Node node, String qName){
		// FIXME move this into RTNode?
		TagNode parent = node.getParent();
		while(!parent.getQName().equals("body")){
			if(parent.getQName().equals(qName)){
				return parent;
			}
			parent = parent.getParent();
		}
		return null;
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
		/*
		 * The list of added nodes that have been merged during an addNode()
		 * call and so do not need to be handled any more.
		 */
		List<Node> addedNodes = new ArrayList<Node>();
		for (RichTextThreeWayDiff threeWayDiff : threeWayDifferences) {
			
			// TODO - handle conflicts

			// TODO The code currently merges rtl. Add logic for ltr merge.
			RichTextDiff rightDiff = threeWayDiff.getRightDiff();
			RichTextDiff leftDiff = threeWayDiff.getLeftDiff();

			if (rightDiff == null || addedNodes.contains(rightDiff.getChild())) {
				continue;
			}
			TagNode root = leftComparator.getBodyNode();
			TagNode rightParent = rightDiff.getChild().getParent();
			TagNode leftParent = (TagNode) findNode(root, rightParent);
			if (!(leftParent instanceof RTNode)) {
				// XXX - return?
				continue;
			}
			
			if(leftDiff != null && leftDiff.getModification().getType() == ModificationType.ADDED){
				continue;
			}

			switch (rightDiff.getModification().getType()) {
			case ADDED: {
				addNode((RTNode) leftParent, rightDiff.getChild(), addedNodes);
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
	private Node findNode(Node root, Node searchNode) {
		if(root instanceof RTNode && ((RTNode) root).isSameNode(searchNode)){
			return root;
		}
		if(root instanceof TagNode){
			for (Node childNode : (TagNode)root) {
				Node node = findNode(childNode, searchNode);
				if(node != null){
					return node;
				}
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

	/**
	 * adds a node of one version to the given new parent. This method requires
	 * that any preceding sibling with the {@link ModificationType#ADDED} of the
	 * given node must have been added to the new parent before adding the given
	 * node.
	 * 
	 * @param newParent
	 * @param child
	 * @param resolvedNodes
	 */
	private void addNode(RTNode newParent, Node child, List<Node> resolvedNodes) {
		if (!(newParent instanceof TagNode) || !(child.getParent() instanceof RTNode))
			return;

		TagNode newParentTag = (TagNode) newParent;
		TagNode oldParent = child.getParent();
		// search for the first preceding sibling that is not an addition
		RTNode sibling = getFirstPrecedingSiblingByModificationType(
				child, ModificationType.CHANGED, ModificationType.NONE,
				ModificationType.REMOVED);

		int index = 0;
		
		if(sibling != null){
			// find the corresponding node to the sibling in the parent
			List<Node> oldSiblingsWithoutInsertions = null;
			List<Node> newSiblingsWithoutInsertions = null;
			List<Node> newSiblings = null;
			
			if(oldParent instanceof RTTagNode){
				oldSiblingsWithoutInsertions = ((RTTagNode) oldParent).getListOfChildrenWithoutInsertions();
			}else if(oldParent instanceof RTBodyNode){
				oldSiblingsWithoutInsertions = ((RTBodyNode) oldParent).getListOfChildrenWithoutInsertions();
			}
			
			if(newParent instanceof RTTagNode){
				newSiblings = ((RTTagNode) newParent).getChildren();
				newSiblingsWithoutInsertions = newParent.getListOfChildrenWithoutInsertions();
			}else if(newParent instanceof RTBodyNode){
				newSiblings = ((RTBodyNode) newParent).getChildren();
				newSiblingsWithoutInsertions = newParent.getListOfChildrenWithoutInsertions();
			}
			
			if(oldSiblingsWithoutInsertions != null && newSiblingsWithoutInsertions != null && newSiblings != null){
				Node node = newSiblingsWithoutInsertions.get(oldSiblingsWithoutInsertions.indexOf(sibling));
				/*
				 * the insertion index is the index of the sibling in the new
				 * parent plus the number of added nodes between the sibling and
				 * the node to add, plus one. This is necessary because we need
				 * to keep the order of the added elements the same. However the
				 * current approach requires that added preceding sibling must
				 * have been merged before the node can be inserted.
				 */
				index = newSiblings.indexOf(node)
						+ (countNodesBetween((Node) sibling, child,
								ModificationType.ADDED)) + 1;
			}else{
				// We cannot determine the insertion index in this case
				return;
			}
		}

		Node clone = child.copyTree();
		clone.setParent(newParentTag);
		
		/**
		 * we copy the complete tree, so we also merge child nodes - we must
		 * ignore them later so that the merge is only performed once. Therefore
		 * we have to remember all added child nodes.
		 */
		collectNodesByModificationType(child, resolvedNodes, ModificationType.ADDED);

		int nbChildren = newParentTag.getNbChildren();
		if (index > nbChildren) {
			newParentTag.addChild(nbChildren, clone);
		} else {
			newParentTag.addChild(index, clone);
		}

	}
	
	/**
	 * Counts the number of nodes with the given {@link ModificationType}s
	 * between the given nodes (exclusive).
	 * 
	 * @param firstNode
	 * @param secondNode
	 * @param modificationTypes
	 * @return the number of node with the given {@link ModificationType}s
	 *         between the given nodes
	 */
	private int countNodesBetween(Node firstNode, Node secondNode, ModificationType... modificationTypes ){
		int counter = 0;
		boolean inBetween = false;
		TagNode parent = firstNode.getParent();
		for(Node child : parent){
			if(child == firstNode){
				inBetween = true;
			}else if(child == secondNode){
				break;
			}else if(inBetween){
				if(isModified(child, modificationTypes)){
					counter++;
				}
			}
		}
		return counter;
	}
	
	/**
	 * collects all nodes in the given node hierarchy with the given
	 * {@link ModificationType}s in the given list.
	 * 
	 * @param node
	 *            the root node of the node hierarchy
	 * @param resolvedNodes
	 *            the list used to store the nodes.
	 * @param modificationTypes
	 *            the modification types of the nodes to collect
	 */
	private void collectNodesByModificationType(Node node, List<Node> resolvedNodes, ModificationType... modificationTypes){
		if(isModified(node, modificationTypes)){
			resolvedNodes.add(node);
		}
		if(node instanceof TagNode){
			for(Node child : (TagNode) node){
				collectNodesByModificationType(child, resolvedNodes, modificationTypes);
			}
		}
	}
	
	/**
	 * 
	 * @param node
	 *            the node to check
	 * @param modificationTypes
	 *            the modification types to check
	 * @return true if the given node has a {@link Modification} with an
	 *         {@link ModificationType} equal to one of the given
	 *         {@link ModificationType}s, false otherwise
	 */
	private boolean isModified(Node node, ModificationType... modificationTypes){
		ModificationType type = null;
		if(node instanceof RTNode){
			type = ((RTNode) node).getModification().getType();
		}else if(node instanceof TextNode){
			type = ((TextNode) node).getModification().getType();
		}
		if(type != null){
			for(ModificationType modType : modificationTypes){
				if(modType == type){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * finds the first preceding sibling which has a {@link Modification} with
	 * an {@link ModificationType} equal to one of the given
	 * {@link ModificationType}s.
	 * 
	 * @param node
	 *            the node used to determine the sibling
	 * @param modificationTypes
	 *            the {@link ModificationType}s to check
	 * @return the first preceding sibling with one of the given
	 *         {@link ModificationType}s or null, if none exists.
	 */
	private RTNode getFirstPrecedingSiblingByModificationType(Node node, ModificationType... modificationTypes){
		RTNode sibling = null;
		TagNode parent = node.getParent();
		for(Node child : parent){
			if(child == node){
				break;
			}
			if(child instanceof RTNode){
				for(ModificationType modificationType : modificationTypes){
					if(((RTNode) child).getModification().getType() == modificationType){
						sibling = (RTNode)child;
					}
				}
			}
		}
		return sibling;
	}

}
