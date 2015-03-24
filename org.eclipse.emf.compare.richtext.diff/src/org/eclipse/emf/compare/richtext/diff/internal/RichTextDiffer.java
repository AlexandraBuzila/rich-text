/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *     Florian Zoubek - bugfixes
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff.internal;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.emf.compare.richtext.diff.RichTextDiff;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
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
			comparator = new TextNodeComparator(newHandler, locale);
			HTMLDiffer differ = new HTMLDiffer(new DummyOutput());
			differ.diff(originComparator, comparator);
			postProcess(originComparator, comparator);
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
	private void postProcess(TextNodeComparator originComparator, TextNodeComparator comparator) {
	}

	private ArrayList<RichTextDiff> getDiffs(TextNodeComparator comparator) {
		ArrayList<RichTextDiff> diffs = new ArrayList<RichTextDiff>();
		getDiffs(diffs, comparator.getBodyNode());
		return diffs;
	}

	private ArrayList<RichTextDiff> getDiffs(ArrayList<RichTextDiff> diffs, TagNode node) {
		for (Node child : node) {
			if (child instanceof TagNode) {
				getDiffs(diffs, (TagNode) child);
			} else
			/* TextNodes are leaves */
			if (child instanceof RTTextNode) {
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
