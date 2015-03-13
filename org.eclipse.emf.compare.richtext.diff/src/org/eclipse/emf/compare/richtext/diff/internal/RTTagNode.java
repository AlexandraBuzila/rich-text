/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.Modification;
import org.outerj.daisy.diff.html.modification.ModificationType;
import org.xml.sax.Attributes;

public class RTTagNode extends TagNode implements RTNode {

	Modification modification;

	/** the list of children nodes retrieved from the superclass */
	private List<Node> _children;

	private ArrayList<Node> childrenNoInsertions;

	public RTTagNode(TagNode parent, String qName, Attributes attributesarg) {
		super(parent, qName, attributesarg);
		this._children = getChildrenPrivate();
	}

	@Override
	public boolean isSameNode(Node node) {

		if (this == node) {
			return true;
		}

		if (!(node instanceof RTTagNode)) {
			return false;
		}

		RTTagNode tagNode = (RTTagNode) node;

		if (!getQName().equals(tagNode.getQName())) {
			return false;
		}

		// useful??
		double matchRatio = getMatchRatio(tagNode);
		if (matchRatio > 0.5) {
			return false;
		}
		if (matchRatio == 0) {
			return true;
		}

		ArrayList<Node> ourNeighbors = ((RTTagNode) getParent()).getListOfChildrenWithoutInsertions();
		ArrayList<Node> otherNeighbors = ((RTTagNode) tagNode.getParent()).getListOfChildrenWithoutInsertions();

		int thisIndexInParent = ourNeighbors.indexOf(this);
		int otherIndexInParent = otherNeighbors.indexOf(tagNode);

		if (thisIndexInParent != otherIndexInParent || thisIndexInParent == -1) {
			return false;
		}

		// the nodes are at the same index in the children's list, but we
		// also need to make sure that we have the same neighbors
		if (ourNeighbors.size() != otherNeighbors.size()) {
			return false;
		}
		return sameNodes(ourNeighbors, otherNeighbors);
	}

	private boolean sameNodes(ArrayList<Node> leftNodes, ArrayList<Node> rightNodes) {
		for (int i = 0; i < leftNodes.size(); i++) {
			Node leftNode = leftNodes.get(i);
			Node rightNode = rightNodes.get(i);
			if (!leftNode.getClass().equals(rightNode.getClass())) {
				return false;
			}
			if (leftNode instanceof TextNode) {
				String leftText = ((TextNode) leftNode).getText();
				String rightText = ((TextNode) rightNode).getText();
				if (!leftText.equals(rightText)) {
					return false;
				}
			} else {
				String leftTag = ((TagNode) leftNode).getQName();
				String rightTag = ((TagNode) rightNode).getQName();
				if (!leftTag.equals(rightTag)) {
					return false;
				}
			}
		}
		return true;
	}

	// /**
	// * @param child
	// * the {@link Node} to search for
	// * @return the index of the <code>child</code> in the list of the node's
	// * children, without considering the nodes that are marked as
	// * insertions
	// */
	public ArrayList<Node> getListOfChildrenWithoutInsertions() {
		if (childrenNoInsertions == null) {
			childrenNoInsertions = new ArrayList<Node>();
			// if the current node is an insertion, all it's children are
			// insertions as well (we don't support MOVE)
			if (getModification().getType() == ModificationType.ADDED) {
				return childrenNoInsertions;
			}
			for (Node child : this) {
				if (isInsert(child)) {
					continue;
				}
				childrenNoInsertions.add(child);
			}
		}
		return childrenNoInsertions;
	}

	private boolean isInsert(Node node) {
		if (node instanceof RTTagNode) {
			return ((RTTagNode) node).getModification().getType() == ModificationType.ADDED;
		}
		if (node instanceof TextNode) {
			return ((TextNode) node).getModification().getType() == ModificationType.ADDED;
		}
		return false;
	}

	@Override
	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}

	@SuppressWarnings("unchecked")
	private List<Node> getChildrenPrivate() {
		try {
			Field field = TagNode.class.getDeclaredField("children");
			field.setAccessible(true);
			return (List<Node>) field.get(this);
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
	 * @return the _children
	 */
	public List<Node> getChildren() {
		return _children;
	}

}
