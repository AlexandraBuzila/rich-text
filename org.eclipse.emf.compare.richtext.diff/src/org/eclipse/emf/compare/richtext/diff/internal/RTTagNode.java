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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.Modification;
import org.outerj.daisy.diff.html.modification.ModificationType;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class RTTagNode extends TagNode implements RTNode {

	Modification modification;

	/** the list of children nodes retrieved from the superclass */
	private List<Node> _children;

	private ArrayList<Node> childrenNoInsertions;

	public RTTagNode(TagNode parent, String qName, Attributes attributesarg) {
		super(parent, qName, attributesarg);
		this._children = getChildrenPrivate();
		modification = new Modification(ModificationType.NONE, ModificationType.NONE);
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

		ArrayList<Node> ourNeighbors = ((RTNode) getParent()).getListOfChildrenWithoutInsertions();
		ArrayList<Node> otherNeighbors = ((RTNode) tagNode.getParent()).getListOfChildrenWithoutInsertions();

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
	
	// We have to override this to satisfy the condition that all RTTextNode
	// only have RTNodes as parent, as the original implementation creates
	// TagNodes instead of RTTagNodes
	@Override
	public boolean splitUntill(TagNode parent, Node split, boolean includeLeft) {
        boolean splitOccured = false;
        if (parent != this) {
            TagNode part1 = new RTTagNode(null, getQName(), getAttributes());
            TagNode part2 = new RTTagNode(null, getQName(), getAttributes());
            part1.setParent(getParent());
            part2.setParent(getParent());

            // FIXME maybe we should find a better solution to access the children
            int i = 0;
            while (i < _children.size() && _children.get(i) != split) {
                _children.get(i).setParent(part1);
                part1.addChild(_children.get(i));
                i++;
            }
            if (i < _children.size()) {//means we've found "split" node
                if (includeLeft) {
                    _children.get(i).setParent(part1);
                    part1.addChild(_children.get(i));
                } else {
                    _children.get(i).setParent(part2);
                    part2.addChild(_children.get(i));
                }
                i++;
            }
            while (i < _children.size()) {
                _children.get(i).setParent(part2);
                part2.addChild(_children.get(i));
                i++;
            }
            if (part1.getNbChildren() > 0) {
				getParent().addChild(getParent().getIndexOf(this), part1);
			}

            if (part2.getNbChildren() > 0) {
				getParent().addChild(getParent().getIndexOf(this), part2);
			}

            if (part1.getNbChildren() > 0 && part2.getNbChildren() > 0) {
                splitOccured = true;
            }
            
            //since split isn't meant for no-children tags,
            //we won't have a case where we removed this and did not
            //substitute it with anything
            detachFromParent();

            if (includeLeft) {
				getParent().splitUntill(parent, part1, includeLeft);
			} else {
				getParent().splitUntill(parent, part2, includeLeft);
			}
        }
        return splitOccured;

    }
	
	/**
	 * detaches this node from its parent node
	 */
	private void detachFromParent(){
        // FIXME this is a quick & dirty hack, we really should find a better solution
        Method removeMethod;
		try {
			removeMethod = TagNode.class.getDeclaredMethod("removeChild", Node.class );
	        removeMethod.setAccessible(true);
	        removeMethod.invoke(getParent(), this);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// We have to override this to satisfy the condition that all RTTextNode
	// only have RTNodes as parent, as the original implementation creates
	// TagNodes instead of RTTagNodes
	@Override
    public Node copyTree() {
        TagNode newThis = new RTTagNode(null, getQName(), new AttributesImpl(
                getAttributes()));
        newThis.setWhiteBefore(isWhiteBefore());
        newThis.setWhiteAfter(isWhiteAfter());
        for (Node child : this) {
            Node newChild = child.copyTree();
            newChild.setParent(newThis);
            newThis.addChild(newChild);
        }
        return newThis;
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
