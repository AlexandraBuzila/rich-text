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

import java.util.ArrayList;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;

/**
 *
 */
public class RTTextNode extends TextNode implements RTNode {

	public RTTextNode(TagNode parent, String s) {
		super(parent, s);
		if (!(parent instanceof RTNode)) {
			//TODO externalize
			throw new IllegalArgumentException("The parent of a RTTextNode must be a RTNode.");
		}
	}

	@Override
	public boolean isSameNode(Node node) {
		if (this == node) {
			return true;
		}

		if (!(node instanceof RTTextNode)) {
			return false;
		}

		RTTextNode textNode = (RTTextNode) node;

		if (!getText().equals(textNode.getText()))
			return false;

		RTNode parent = (RTNode) getParent();
		RTNode otherParent = (RTNode) textNode.getParent();
		
		if (!parent.isSameNode(textNode.getParent()))
			return false;

		int indexOfThis = parent.getListOfChildrenWithoutInsertions().indexOf(this);
		int indexOfOther = otherParent.getListOfChildrenWithoutInsertions().indexOf(textNode);

		return (indexOfThis == indexOfOther);
	}

	@Override
	public ArrayList<Node> getListOfChildrenWithoutInsertions() {
		// Text nodes don't have children
		return new ArrayList<Node>();
	}

	@Override
	public void setParent(TagNode parent) {
		super.setParent(parent);
		if (!(parent instanceof RTNode)) {
			//TODO externalize
			throw new IllegalArgumentException("The parent of a RTTextNode must be a RTNode.");
		}
	}

}
