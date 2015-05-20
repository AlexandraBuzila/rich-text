/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *     Florian Zoubek - bugfixes, tag modification detection 
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff.internal;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.emf.compare.richtext.diff.RichTextDiff;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.ancestor.AncestorComparator;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.SeparatingNode;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.dom.WhiteSpaceNode;
import org.outerj.daisy.diff.html.modification.Modification;
import org.outerj.daisy.diff.html.modification.ModificationType;
import org.outerj.daisy.diff.output.DiffOutput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Wrapper class for the HTMLDiffer from DaisyDiff
 */
public class RichTextDiffer {

	private TextNodeComparator comparator;

	public ArrayList<RichTextDiff> getDiffs(String baseContent, String newContent) {
		Locale locale = Locale.getDefault();
		HtmlCleaner cleaner = new HtmlCleaner();

		InputSource oldSource = new InputSource(new StringReader(baseContent));
		InputSource newSource = new InputSource(new StringReader(newContent));

		RTDomTreeBuilder oldHandler = new RTDomTreeBuilder();
		try {
			cleaner.cleanAndParse(oldSource, oldHandler);
			TextNodeComparator originComparator = new TextNodeComparator(oldHandler, locale);
			RTDomTreeBuilder newHandler = new RTDomTreeBuilder();
			cleaner.cleanAndParse(newSource, newHandler);
			comparator = new TextNodeComparator(newHandler.getDomTreeCopy(), locale);
			HTMLDiffer differ = new HTMLDiffer(new DummyOutput());
			differ.diff(originComparator, comparator);
			postProcess(originComparator, comparator, new TextNodeComparator(newHandler, locale)); // We use a new TextNodeComparator here because HTMLDiffer modifies the DomTree of the right comparator 
			return getDiffs(comparator);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** Adds modifications (insert, delete) to RTTagNodes.
	 * @param comparator */
	private void postProcess(TextNodeComparator leftComparator,
			TextNodeComparator annotatedComparator,
			TextNodeComparator rightComparator) {
		// FIXME move to more useful position
		leftComparator.expandWhiteSpace();
		rightComparator.expandWhiteSpace();
		
		postProcessNode(leftComparator.getBodyNode(), annotatedComparator.getBodyNode(), rightComparator.getBodyNode());
		
	}
	
	/**
	 * post process the direct children of the given nodes - this method assumes
	 * that each unmodified tag has a modification type of NONE assigned. The
	 * basic idea of this algorithm is to walk through all three trees in
	 * parallel from the leaves to the root, and find the modification type
	 * for all tags using the modification information produced by daisydiff.
	 * 
	 * @param leftNode
	 * @param annotatedNode
	 * @param rightNode
	 * @return the common modification type of all children,
	 *         {@link ModificationType#NONE} if the they have no common
	 *         modification type or null if there are no children at all
	 */
	private ModificationType postProcessNode(TagNode leftNode, TagNode annotatedNode, TagNode rightNode){
		Iterator<Node> leftChildrenIt = leftNode.iterator();
		Iterator<Node> annotatedChildrenIt = annotatedNode.iterator();
		Iterator<Node> rightChildrenIt = rightNode.iterator();
		ParalellLevelTraversalState traversalState = new ParalellLevelTraversalState(leftChildrenIt, annotatedChildrenIt, rightChildrenIt);

		Node currentLeftChild = null;
		Node currentAnnotatedChild = null;
		Node currentRightChild = null;
		
		boolean deleteNextAnnotatedNode = false;
		List<Node> nodesToDelete = new ArrayList<Node>();
		
		while(!traversalState.isFinished()){
			
			currentAnnotatedChild = traversalState.getCurrentAnnotatedNode();
			
			if(deleteNextAnnotatedNode){
				// DaisyDiff treats empty tags with deleted content as deleted, and therefore creates a copy in the annotated 
				// dom tree if an empty tag remains in the new version - so we have to delete it from the tree afterwards and
				// ignore it during traversal 
				nodesToDelete.add(currentAnnotatedChild);
				
				traversalState.moveOnAnnotated();
				currentAnnotatedChild = traversalState.getCurrentAnnotatedNode();
				
				if(traversalState.isFinished()){
					break;
				}
			}
			
			currentLeftChild = traversalState.getCurrentLeftNode();
			
			if(currentLeftChild == null){
				// all remaining child nodes must have been added in the right version, so
				// all remaining child nodes must be present in the right version
				assert(rightChildrenIt.hasNext());
				
				traversalState.skipOrMarkRemainingNodeAs(ModificationType.ADDED);
				break;
			}
			
			currentRightChild = traversalState.getCurrentRightNode();
			
			if(currentRightChild == null){
				// all remaining child nodes must have been removed in the right version, so
				// all remaining child nodes must be present in the left version
				assert(leftChildrenIt.hasNext());
				traversalState.skipOrMarkRemainingNodeAs(ModificationType.REMOVED);
				break;
			}
			if (currentAnnotatedChild instanceof RTTagNode) {
				RTTagNode annotatedChildRT = (RTTagNode) currentAnnotatedChild;
				if (currentLeftChild instanceof RTTagNode) {
					RTTagNode leftChildRT = (RTTagNode) currentLeftChild;
					if (currentRightChild instanceof RTTagNode) {
						// The nodes in all 3 versions are tags, now check the modifications of all children, and afterwards decide 
						// which modification type applies to these nodes
						RTTagNode rightChildRT = (RTTagNode)currentRightChild;
						ModificationType childModificationType = postProcessNode(leftChildRT, annotatedChildRT, rightChildRT);
						
						traversalState.updateCommonModificationType(childModificationType);

						if (childModificationType == null) {
							// Tag has no children, compare equality only
							if( areNodeTreesEqual(annotatedChildRT, leftChildRT) ){
								if( areNodeTreesEqual(annotatedChildRT, rightChildRT) ){
									// Tag has not been changed
									traversalState.skipAlreadyMarkedNode();
									// we don't want to propagate a possible CHANGED state through the whole tree, so we just manually make sure 
									// that the common modification type will be NONE
									traversalState.updateCommonModificationType(ModificationType.NONE);
									
								} else {
									// present in left but not in right version - Tag has been deleted									
									traversalState.markCurrentTagAndMoveOn(ModificationType.REMOVED);
								}
							} else {
								
								// not present in left but present in right version - Tag has been added
								assert(annotatedChildRT.isSameTag(rightChildRT));								
								traversalState.markCurrentTagAndMoveOn(ModificationType.ADDED);
							}
						} else if (childModificationType == ModificationType.ADDED) {
							
							// All children have been added, so the tag has either been added or stayed the same
							if (annotatedChildRT.isSameTag(leftChildRT)
									&& (leftChildRT.getChildren().isEmpty() || (leftChildRT
											.getChildren().size() == 1 && leftChildRT
											.getChildren().get(0) instanceof RTEmptyTextNode))) {
								// if the same tag exists in the left version it must not have any children, as all children have been added 
								// Tag has stayed the same
								traversalState.skipAlreadyMarkedNode();
								// we don't want to propagate a possible CHANGED state through the whole tree, so we just manually make sure 
								// that the common modification type will be NONE
								traversalState.updateCommonModificationType(ModificationType.NONE);
							} else {
								// Tag has been added
								traversalState.markCurrentTagAndMoveOn(ModificationType.ADDED);
							}
						} else if (childModificationType == ModificationType.REMOVED) {
							// All children have been changed, so the tag has either been removed or stayed the same
							if (annotatedChildRT.isSameTag(rightChildRT)
									&& (rightChildRT.getChildren().isEmpty() || (rightChildRT
											.getChildren().size() == 1 && rightChildRT
											.getChildren().get(0) instanceof RTEmptyTextNode))) {
								// if the same tag exists in the right version it must not have any children, as all children have been removed 
								// Tag has stayed the same
								traversalState.skipAlreadyMarkedNode();
								// we don't want to propagate a possible CHANGED state through the whole tree, so we just manually make sure 
								// that the common modification type will be NONE
								traversalState.updateCommonModificationType(ModificationType.NONE);
								
								// daisydiff treats empty tags with deleted content as deleted, and therefore creates a copy in the annotated 
								// dom tree - so we have to delete it somehow
								deleteNextAnnotatedNode = true;
								
							} else {
								// Tag has been removed								
								traversalState.markCurrentTagAndMoveOn(ModificationType.REMOVED);
							}
						} else if (childModificationType == ModificationType.CHANGED) {
							// At least one of the tags in the parent tree has been changed
							handleParentTreeChange(leftChildRT, annotatedChildRT);
							
							// if all children have the modification type CHANGED, the Modification type 
							// of the tag can only be either CHANGED or NONE - we don't want to propagate 
							// the CHANGED state through the whole tree, so we just set it to NONE
							traversalState.updateCommonModificationType(ModificationType.NONE);
							traversalState.skipAlreadyMarkedNode();
						} else {
							// The tag cannot be added or removed if not all of
							// its children have been added, removed or changed
							// - so nothing has changed
							
							// nothing to update here, as this node might be already marked due to an 
							// parent tree change - if not NONE is already the default value 
							traversalState.skipAlreadyMarkedNode();
						}
					} else {
						// Tag has been removed
						traversalState.markCurrentTagAndMoveOn(ModificationType.REMOVED);
					}
				} else {
					// Tag has been added
					traversalState.markCurrentTagAndMoveOn(ModificationType.ADDED);
				}
			} else if (currentAnnotatedChild instanceof TextNode) {
				// Text nodes already have a modification type, so we just need to consider its type for the common modification type
				traversalState.skipAlreadyMarkedNode();
			}
		}
		
		// delete duplicated nodes created by daisydiff 
		
		for (Node node : nodesToDelete) {
			if (annotatedNode instanceof RTTagNode) {
				List<Node> children = ((RTTagNode)annotatedNode).getChildren();
				children.remove(node);
			}
		}
		
		return traversalState.getCommonModificationType();
	}
	
	/**
	 * mark all tags (inclusive root node) within the node tree described by the
	 * given root node with the given modification type.
	 * 
	 * @param node
	 *            the root node of the node tree
	 * @param modificationType
	 */
	private static void markNodeTreeAsModified(Node node, ModificationType modificationType){
		if(node instanceof RTTagNode){
			((RTTagNode) node).setModification(new Modification(modificationType, modificationType));
		}// leave TextNodes as is, because DaisyDiff already marked them
		if(node instanceof TagNode){
			for(Node child : (TagNode)node){
				markNodeTreeAsModified(child, modificationType);
			}
		}
	}
	
	/**
	 * tests if two node trees are equal by checking their qualified name,
	 * children (and their order) and text.
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
					if(child1 instanceof RTTextNode && child2 instanceof RTTextNode){
						if(!((RTTextNode)child1).isSameText(child2)){
							return false;
						}
					}else if(child1 instanceof RTTagNode && child2 instanceof RTTagNode){
						if(!areNodeTreesEqual((RTTagNode)child1, (RTTagNode)child2)){
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
	 * computes and sets modifications of the type
	 * {@link ModificationType#CHANGED} to changed nodes in the list of parent
	 * tags ("parent tree") of the given tag node of the annotated version by daisydiff.
	 * 
	 * @param leftNode the tag in the origin version
	 * @param annotatedNode the tag in the annotated version
	 */
	private void handleParentTreeChange(RTTagNode leftNode, RTTagNode annotatedNode) {

		// recreate the mapping used by daisydiff and mark the differences as changed

		// parents to compare
		List<TagNode> leftParents = leftNode.getParentTree();
		leftParents.add(leftNode);
		List<TagNode> annotatedParents = annotatedNode.getParentTree();
		annotatedParents.add(annotatedNode);

		AncestorComparator leftComparator = new AncestorComparator(leftParents);
		AncestorComparator annotatedComparator = new AncestorComparator(
				annotatedParents);

		RangeDifference[] differences = RangeDifferencer.findDifferences(
				leftComparator, annotatedComparator);

		// mark all changes in parent tags 
		for (RangeDifference diff : differences) {
			// we only need consider the right side as the left side tags are not present in the annotated dom tree
			if (diff.rightLength() > 0) {
				for (int i = diff.rightStart(); i < diff.rightEnd(); i++) {
					TagNode changedTag = annotatedParents.get(i);
					if (changedTag instanceof RTTagNode) {
						((RTTagNode) changedTag)
								.setModification(new Modification(
										ModificationType.CHANGED,
										ModificationType.CHANGED));
					}
				}
			}
		}
		
	}
	
	/**
	 * A traversal helper object which supports parallel traversal of a single
	 * level in DOM trees that reflect the left, right and annotated versions of
	 * a changed DOM tree. It provides basic methods ({@code moveOn}-prefixed
	 * methods) to move on in the level as well as methods to mark and skip the
	 * current node in the annotated version. Additionally it also allows
	 * tracking of a single common {@link ModificationType} shared by all marked
	 * or skipped nodes in the level.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ParalellLevelTraversalState {
		
		private Iterator<Node> leftIt;
		private Iterator<Node> annotatedIt;
		private Iterator<Node> rightIt;
		
		private Node currentLeftNode;
		private Node currentAnnotatedNode;
		private Node currentRightNode;
		
		private ModificationType commonModificationType;
		
		/**
		 * 
		 * @param leftIt the iterator used to iterate over the nodes of a level in the left version  
		 * @param annotatedIt the iterator used to iterate over the nodes of a level in the annotated version
		 * @param rightIt the iterator used to iterate over the nodes of a level in the right version
		 */
		public ParalellLevelTraversalState(Iterator<Node> leftIt,
				Iterator<Node> annotatedIt, Iterator<Node> rightIt) {
			super();
			this.leftIt = leftIt;
			this.annotatedIt = annotatedIt;
			this.rightIt = rightIt;
			moveOnLeft();
			moveOnAnnotated();
			moveOnRight();
		}

		/**
		 * moves a single step forward in the left level version
		 */
		public void moveOnLeft(){
			currentLeftNode = getNextNode(leftIt);
		}
		
		/**
		 * moves a single step forward in the left level version
		 */
		public void moveOnAnnotated(){
			currentAnnotatedNode = getNextNode(annotatedIt);
		}
		
		/**
		 * moves a single step forward in the left level version
		 */
		public void moveOnRight(){
			currentRightNode = getNextNode(rightIt);
		}
		
		/**
		 * moves a single step in different level versions based on the
		 * {@link ModificationType}. First, in all cases a single step is taken
		 * in the annotated level version. If the {@link ModificationType} is
		 * {@link ModificationType#ADDED} a step is also taken in the right
		 * level version. In contrast, a step is taken in the left version if
		 * the {@link ModificationType} is {@link ModificationType#REMOVED}. For
		 * the {@link ModificationType}s {@link ModificationType#CHANGED} and
		 * {@link ModificationType#NONE}, a step is taken in the left and the
		 * right level versions.
		 * 
		 * @param modificationType
		 */
		public void moveOn(ModificationType modificationType){
			moveOnAnnotated();
			switch (modificationType) {
			case ADDED:
				moveOnRight();
				break;
			case REMOVED:
				moveOnLeft();
				break;
			case CHANGED:
			case NONE:
				moveOnLeft();
				moveOnRight();
				break;
			default:
				break;
			}
		}
		
		/**
		 * 
		 * @return the current node of the left level version, null if the left
		 *         level version does not contain any more nodes.
		 */
		public Node getCurrentLeftNode(){
			return currentLeftNode;
		}

		/**
		 * 
		 * @return the current node of the annotated level version, null if the
		 *         annotated level version does not contain any more nodes.
		 */
		public Node getCurrentAnnotatedNode() {
			return currentAnnotatedNode;
		}

		/**
		 * 
		 * @return the current node of the right level version, null if the
		 *         right level version does not contain any more nodes.
		 */
		public Node getCurrentRightNode() {
			return currentRightNode;
		}
		
		/**
		 * 
		 * @return the common {@link ModificationType} shared by all marked or
		 *         skipped nodes in this level, {@link ModificationType#NONE} if
		 *         no common {@link ModificationType} exists (or if all nodes
		 *         have {@link ModificationType#NONE}), or {@code null} if no
		 *         nodes have been traversed.
		 */
		public ModificationType getCommonModificationType() {
			return commonModificationType;
		}
		
		/**
		 * 
		 * @return true if the traversal is complete, that is if all nodes in
		 *         the annotated level version have been traversed
		 */
		public boolean isFinished(){
			return currentAnnotatedNode == null;
		}
		
		/**
		 * update the common {@link ModificationType} based on the current nodes
		 * {@link Modification} and moves on. Does nothing if the node does not
		 * provide a {@link Modification} instance.
		 */
		public void skipAlreadyMarkedNode(){
			Modification mod = null;
			if(currentAnnotatedNode instanceof RTNode){
				mod = ((RTNode)currentAnnotatedNode).getModification();
			}else if(currentAnnotatedNode instanceof TextNode){
				mod = ((TextNode)currentAnnotatedNode).getModification();
			}
			if(mod != null){
				ModificationType modificationType = mod.getType();
				updateCommonModificationType(modificationType);
				moveOn(modificationType);
			}
		}
		
		/**
		 * marks the current annotated node with the given modification type,
		 * updates the common modification type and moves on in the level based
		 * on the given {@link ModificationType}. Currently, only
		 * {@link RTTagNode}s can be marked with a modification type. If the
		 * node is not a tag node nothing will be changed.
		 * 
		 * @param modificationType
		 *            the {@link ModificationType} to mark the current node
		 *            with.
		 */
		public void markCurrentTagAndMoveOn(ModificationType modificationType){
			if(currentAnnotatedNode instanceof RTTagNode){
				markCurrentTagAs(modificationType);
				moveOn(modificationType);
			}
		}
		
		/**
		 * marks the current annotated node with the given modification type and
		 * updates the common modification type. Currently, only
		 * {@link RTTagNode}s can be marked with a modification type, other
		 * nodes and the common modification state will remain unchanged.
		 * 
		 * @param modificationType
		 *            the {@link ModificationType} to mark the current node
		 *            with.
		 */
		public void markCurrentTagAs(ModificationType modificationType){
			
			if(currentAnnotatedNode instanceof RTTagNode){
				RTTagNode rtTagNode = (RTTagNode)currentAnnotatedNode;
				rtTagNode.setModification(new Modification(modificationType, modificationType));
				updateCommonModificationType(modificationType);
			}
		}
		
		/**
		 * marks all remaining Tag nodes in the annotated level version with the
		 * given {@link ModificationType} and skips all other nodes while
		 * recording the common {@link ModificationType}.
		 * 
		 * @param modificationType
		 */
		public void skipOrMarkRemainingNodeAs(ModificationType modificationType){
			while(currentAnnotatedNode != null){
				if(currentAnnotatedNode instanceof RTTagNode){
					markCurrentTagAs(modificationType);
					markNodeTreeAsModified(getCurrentAnnotatedNode(), modificationType);
					moveOn(modificationType);
				}else{
					skipAlreadyMarkedNode();
				}
			}
		}
		
		/**
		 * return the next node that needs to be post processed. In this case all
		 * nodes except {@link SeparatingNode}s need to be processed.
		 * 
		 * @param iterator
		 * @return the next node that needs to be post processed, or null if no node
		 *         has been found that needs to processed
		 */
		private Node getNextNode(Iterator<Node> iterator){
			boolean nodeFound = false;
			Node node = null;
			while(iterator.hasNext() && !nodeFound){
				node = iterator.next();
				nodeFound = !(node instanceof SeparatingNode);
			}
			if(nodeFound)
				return node;
			else
				return null;
		}
		
		/**
		 * updates the tracked common {@link ModificationType} shared by all
		 * skipped or marked nodes. The common {@link ModificationType} by the
		 * first {@link ModificationType} that is set trough this method, all
		 * remaining calls will either set it to {@link ModificationType#NONE}
		 * if the new one does not match the old one, or leave the value as
		 * it is (if the new one is the same as the old one).
		 * 
		 * @param modifcationType
		 *            the new {@link ModificationType}
		 */
		public void updateCommonModificationType(ModificationType modificationType){
			if(commonModificationType == null){
				commonModificationType = modificationType;
			}else{
				if(commonModificationType != modificationType){
					commonModificationType = ModificationType.NONE;
				}
			}
		}
		
	}

	private ArrayList<RichTextDiff> getDiffs(TextNodeComparator comparator) {
		ArrayList<RichTextDiff> diffs = new ArrayList<RichTextDiff>();
		getDiffs(diffs, comparator.getBodyNode());
		return diffs;
	}

	private ArrayList<RichTextDiff> getDiffs(ArrayList<RichTextDiff> diffs, TagNode node) {
		
		if(node instanceof RTTagNode){
			RTTagNode tagNode = (RTTagNode) node;
			Modification mod = tagNode.getModification();
			if (mod.getType() != ModificationType.NONE) {
				diffs.add(new RichTextDiff(tagNode, mod));
			}
		}
		
		for (Node child : node) {
			if (child instanceof TagNode) {
				getDiffs(diffs, (TagNode) child);
			} else
			/* TextNodes are leaves */
			if (child instanceof RTTextNode && !(child instanceof RTEmptyTextNode)) {
				RTTextNode textChild = (RTTextNode) child;
				Modification mod = textChild.getModification();
				if (mod.getType() != ModificationType.NONE) {
					diffs.add(new RichTextDiff(textChild, mod));
				}
			}
			
			if(child instanceof WhiteSpaceNode){
				WhiteSpaceNode whitespace = (WhiteSpaceNode) child;
				Modification mod = whitespace.getModification();
				if (mod.getType() != ModificationType.NONE) {
					diffs.add(new RichTextDiff(whitespace, mod));
				}
			}
		}
		return diffs;
	}

	public TextNodeComparator getComparator() {
		return comparator;
	}

	/**
	 * Dummy diff output object needed for creating a new {@link HTMLDiffer}
	 * instance. The generateOutput method does nothing, since we are not
	 * interested in outputting the diffs at the moment.
	 */
	private class DummyOutput implements DiffOutput {

		@Override
		public void generateOutput(TagNode node) throws SAXException {
			// do nothing
		}

	}

}
