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

import javax.xml.transform.sax.TransformerHandler;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.output.DiffOutput;
import org.xml.sax.SAXException;

public class StringOutputGenerator implements DiffOutput {

	private TransformerHandler handler;

	public StringOutputGenerator(TransformerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void generateOutput(TagNode node) throws SAXException {
		if (!node.getQName().equalsIgnoreCase("body")) {
			handler.startElement("", node.getQName(), node.getQName(), node.getAttributes());
		}
		for (Node child : node) {
			if (child instanceof TagNode) {
				generateOutput(((TagNode)child));
			} else if (child instanceof TextNode) {
				TextNode textChild = (TextNode)child;
				char[] chars = textChild.getText().toCharArray();
				handler.characters(chars, 0, chars.length);
			}
		}
		if (!node.getQName().equalsIgnoreCase("body")) {
			handler.endElement("", node.getQName(), node.getQName());
		}
	}
}
