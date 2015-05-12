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

import org.outerj.daisy.diff.html.dom.BodyNode;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.Modification;
import org.outerj.daisy.diff.html.modification.ModificationType;

public class RTBodyNode extends BodyNode implements RTNode {

	private Modification modification;
	/** the list of children nodes retrieved from the superclass */
	private List<Node> _children;
	private ArrayList<Node> childrenNoInsertions;

	public RTBodyNode() {
		super();
		this._children = getChildrenPrivate();
		// body nodes can't be modified
		modification = new Modification(ModificationType.NONE, ModificationType.NONE);
	}

	@Override
	public Node copyTree() {
		RTBodyNode newThis = new RTBodyNode();
		for (Node child : this) {
			Node newChild = child.copyTree();
			newChild.setParent(newThis);
			newThis.addChild(newChild);
		}
		return newThis;
	}

	@Override
	public boolean isSameNode(Node node) {
		if (this == node) {
			return true;
		}

		if (!(node instanceof BodyNode)) {
			return false;
		}
		// body nodes are always equal
		return true;
	}

	@Override
	public Modification getModification() {
		return modification;
	}

	@Override
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