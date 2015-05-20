/*
 * Copyright 2007 Guy Van den Broeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.emf.compare.richtext.diff.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.outerj.daisy.diff.html.dom.BodyNode;
import org.outerj.daisy.diff.html.dom.DomTree;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.outerj.daisy.diff.html.dom.ImageNode;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.SeparatingNode;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.dom.WhiteSpaceNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class based on the {@link DomTreeBuilder} from DaisyDiff. It replaces the
 * {@link TagNode} and {@link TextNode} objects with {@link RTTagNode} and
 * {@link RTTextNode} objects, respectively.
 */
public class RTDomTreeBuilder extends DefaultHandler implements DomTree {

	private List<TextNode> textNodes = new ArrayList<TextNode>(50);

	private BodyNode bodyNode = new RTBodyNode();

	private TagNode currentParent = bodyNode;

	private StringBuilder newWord = new StringBuilder();

	protected boolean documentStarted = false;

	protected boolean documentEnded = false;

	protected boolean bodyStarted = false;

	protected boolean bodyEnded = false;

	private boolean whiteSpaceBeforeThis = false;

	/**
	 * When greater than 0, this indicates that the node being parsed is a
	 * descendant of a pre tag.
	 */
	private int numberOfActivePreTags = 0; // calculating this as required for
											// every node is expensive.

	private Node lastSibling = null;

	@Override
	public BodyNode getBodyNode() {
		return bodyNode;
	}
	
	@Override
	public List<TextNode> getTextNodes() {
		return textNodes;
	}

	@Override
	public void startDocument() throws SAXException {
		if (documentStarted)
			throw new IllegalStateException("This Handler only accepts one document");
		documentStarted = true;
	}

	@Override
	public void endDocument() throws SAXException {
		if (!documentStarted || documentEnded)
			throw new IllegalStateException();
		endWord();
		documentEnded = true;
		documentStarted = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (!documentStarted || documentEnded)
			throw new IllegalStateException();

		if (bodyStarted && !bodyEnded) {
			endWord();

			TagNode newTagNode = new RTTagNode(currentParent, localName, attributes);
			currentParent = newTagNode;
			lastSibling = null;
			if (whiteSpaceBeforeThis && newTagNode.isInline()) {
				newTagNode.setWhiteBefore(true);
			}
			whiteSpaceBeforeThis = false;
			if (newTagNode.isPre()) {
				numberOfActivePreTags++;
			}
			if (isSeparatingTag(newTagNode)) {
				addSeparatorNode();
			}

		} else if (bodyStarted) {
			// Ignoring element after body tag closed
		} else if (localName.equalsIgnoreCase("body")) {
			bodyStarted = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (!documentStarted || documentEnded)
			throw new IllegalStateException();

		if (localName.equalsIgnoreCase("body")) {
			bodyEnded = true;
		} else if (bodyStarted && !bodyEnded) {
			if (localName.equalsIgnoreCase("img")) {
				// Insert a dummy leaf for the image
				ImageNode img = new ImageNode(currentParent, currentParent.getAttributes());
				img.setWhiteBefore(whiteSpaceBeforeThis);
				lastSibling = img;
				textNodes.add(img);
			}
			endWord();
			if (currentParent.isInline()) {
				lastSibling = currentParent;
			} else {
				lastSibling = null;
			}
			if (localName.equalsIgnoreCase("pre")) {
				numberOfActivePreTags--;
			}
			if (isSeparatingTag(currentParent)) {
				addSeparatorNode();
			}
			if (currentParent instanceof RTTagNode
					&& ((RTTagNode) currentParent).getChildren().isEmpty()) {
				RTEmptyTextNode emptyTextNode = new RTEmptyTextNode(currentParent);
				textNodes.add(emptyTextNode);
			}
			currentParent = currentParent.getParent();
			whiteSpaceBeforeThis = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (!documentStarted || documentEnded)
			throw new IllegalStateException();

		for (int i = start; i < start + length; i++) {
			char c = ch[i];
			if (isDelimiter(c)) {
				endWord();
				if (WhiteSpaceNode.isWhiteSpace(c) && numberOfActivePreTags == 0) {
					if (lastSibling != null)
						lastSibling.setWhiteAfter(true);
					whiteSpaceBeforeThis = true;
				} else {
					RTTextNode textNode = new RTTextNode(currentParent, Character.toString(c));
					textNode.setWhiteBefore(whiteSpaceBeforeThis);
					whiteSpaceBeforeThis = false;
					lastSibling = textNode;
					textNodes.add(textNode);

				}
			} else {
				newWord.append(c);
			}

		}
	}

	private void endWord() {
		if (newWord.length() > 0) {
			TextNode node = new RTTextNode(currentParent, newWord.toString());
			node.setWhiteBefore(whiteSpaceBeforeThis);
			whiteSpaceBeforeThis = false;
			lastSibling = node;
			textNodes.add(node);
			newWord.setLength(0);
		}
	}

	/**
	 * Returns <code>true</code> if the given tag separates text nodes from
	 * being successive. I.e. every block starts a new distinct text flow.
	 * 
	 * @param aTagNode
	 * @return
	 */
	private boolean isSeparatingTag(TagNode aTagNode) {
		// treat all block tags as separating
		return aTagNode.isBlockLevel();
	}

	/**
	 * Ensures that a separator is added after the last text node.
	 */
	private void addSeparatorNode() {
		if (textNodes.isEmpty()) {
			return;
		}

		// don't add multiple separators
		if (textNodes.get(textNodes.size() - 1) instanceof SeparatingNode) {
			return;
		}

		textNodes.add(new SeparatingNode(currentParent));
	}

	public static boolean isDelimiter(char c) {
		if (WhiteSpaceNode.isWhiteSpace(c))
			return true;
		switch (c) {
		// Basic Delimiters
		case '/':
		case '.':
		case '!':
		case ',':
		case ';':
		case '?':
		case '=':
		case '\'':
		case '"':
			// Extra Delimiters
		case '[':
		case ']':
		case '{':
		case '}':
		case '(':
		case ')':
		case '&':
		case '|':
		case '\\':
		case '-':
		case '_':
		case '+':
		case '*':
		case ':':
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * 
	 * @return a copy of the DOM tree
	 */
	public DomTree getDomTreeCopy(){
		List<TextNode> copiedTextNodes = new ArrayList<TextNode>(textNodes.size());
		BodyNode copiedBodyNode = (BodyNode)bodyNode.copyTree();
		
		copyTextNodes(copiedBodyNode, copiedTextNodes);
		
		return new SimpleDomTree(copiedTextNodes, copiedBodyNode);
	}
	
	/**
	 * copies all text nodes within the tag tree with the given root node in the given list. 
	 * @param node the root node of the tag tree
	 * @param copiedTextNodes the list used to store the text nodes
	 */
	private void copyTextNodes(TagNode node, List<TextNode> copiedTextNodes){
		Iterator<Node> childIterator = node.iterator();
		while(childIterator.hasNext()){
			Node child = childIterator.next();
			if(child instanceof TextNode){
				copiedTextNodes.add((TextNode) child);
			}else if (child instanceof TagNode){
				copyTextNodes((TagNode) child, copiedTextNodes);
			}
		}
	}
	
	/**
	 * A simple implementation of a {@link DomTree}, which provides no other
	 * functionality than storing the data of a {@link DomTree}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class SimpleDomTree implements DomTree{
		
		private List<TextNode> textNodes;
		
		private BodyNode bodyNode;
		
		public SimpleDomTree(List<TextNode> textNodes, BodyNode bodyNode) {
			super();
			this.textNodes = textNodes;
			this.bodyNode = bodyNode;
		}
		@Override
		public List<TextNode> getTextNodes() {
			return textNodes;
		}
		@Override
		public BodyNode getBodyNode() {
			return bodyNode;
		}
		
	}

}
