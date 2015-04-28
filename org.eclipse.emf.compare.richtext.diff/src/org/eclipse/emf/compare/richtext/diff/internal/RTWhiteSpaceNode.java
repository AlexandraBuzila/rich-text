/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Florian Zoubek - initial implementation
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff.internal;

import java.util.ArrayList;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.WhiteSpaceNode;

/**
 *
 */
public class RTWhiteSpaceNode extends WhiteSpaceNode implements RTNode{

	public RTWhiteSpaceNode(TagNode parent, String s) {
		super(parent, s);
	}
	
	public RTWhiteSpaceNode(TagNode parent, String s, Node like) {
		super(parent, s, like);
	}

	@Override
	public boolean isSameNode(Node node) {
		if (this == node) {
			return true;
		}

		if (!(node instanceof RTWhiteSpaceNode)) {
			return false;
		}

		RTWhiteSpaceNode otherWhiteSpaceNode = (RTWhiteSpaceNode) node;

		RTNode parent = (RTNode) getParent();
		RTNode otherParent = (RTNode) otherWhiteSpaceNode.getParent();
		
		if (!parent.isSameNode(otherWhiteSpaceNode.getParent()))
			return false;

		int indexOfThis = parent.getListOfChildrenWithoutInsertions().indexOf(this);
		int indexOfOther = otherParent.getListOfChildrenWithoutInsertions().indexOf(otherWhiteSpaceNode);

		return (indexOfThis == indexOfOther);
	}

	@Override
	public ArrayList<Node> getListOfChildrenWithoutInsertions() {
		// Whitespace nodes don't have any children
		return new ArrayList<Node>();
	}

}
