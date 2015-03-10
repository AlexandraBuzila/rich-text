/**
 * 
 */
package org.eclipse.emf.compare.richtext.diff;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.Modification;
import org.outerj.daisy.diff.html.modification.ModificationType;
import org.outerj.daisy.diff.output.DiffOutput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Wrapper class for the HTMLDiffer from DaisyDiff
 * 
 * @author Alexandra Buzila
 *
 */
public class RichTextDiffer {

	private TextNodeComparator rightComparator;
	private TextNodeComparator leftComparator;

	public ArrayList<RichTextDiff> getDiffs(String baseContent, String newContent) {
		Locale locale = Locale.getDefault();
		HtmlCleaner cleaner = new HtmlCleaner();

		InputSource oldSource = new InputSource(new StringReader(baseContent));
		InputSource newSource = new InputSource(new StringReader(newContent));

		DomTreeBuilder oldHandler = new DomTreeBuilder();
		try {
			cleaner.cleanAndParse(oldSource, oldHandler);
			TextNodeComparator leftComparator = new TextNodeComparator(oldHandler, locale);
			DomTreeBuilder newHandler = new DomTreeBuilder();
			cleaner.cleanAndParse(newSource, newHandler);
			rightComparator = new TextNodeComparator(newHandler, locale);
			HTMLDiffer differ = new HTMLDiffer(new DummyOutput());
			differ.diff(leftComparator, rightComparator);
			return getDiffs(rightComparator);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public LinkedList<ThreeWayRichTextDiff> getDiffs(String baseContent, String rightContent, String leftContent) {
		Locale locale = Locale.getDefault();
		HtmlCleaner cleaner = new HtmlCleaner();

		InputSource baseSource = new InputSource(new StringReader(baseContent));
		InputSource rightSource = new InputSource(new StringReader(rightContent));
		InputSource leftSource = new InputSource(new StringReader(leftContent));

		try {
			DomTreeBuilder baseHandler = new DomTreeBuilder();
			cleaner.cleanAndParse(baseSource, baseHandler);
			TextNodeComparator baseComparator = new TextNodeComparator(baseHandler, locale);
			
			DomTreeBuilder rightHandler = new DomTreeBuilder();
			cleaner.cleanAndParse(rightSource, rightHandler);
			rightComparator = new TextNodeComparator(rightHandler, locale);
			
			DomTreeBuilder leftHandler = new DomTreeBuilder();
			cleaner.cleanAndParse(leftSource, leftHandler);
			leftComparator = new TextNodeComparator(leftHandler, locale);
			
			HTMLDiffer differ = new HTMLDiffer(new DummyOutput());
			differ.diff(baseComparator, leftComparator, rightComparator );
			return getDiffs(baseComparator, rightComparator, leftComparator);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private LinkedList<ThreeWayRichTextDiff> getDiffs(TextNodeComparator baseComparator2, TextNodeComparator rightComparator2, TextNodeComparator leftComparator2) {
		System.out.println("ORIGIN:");
		printDifferences(baseComparator2.getBodyNode());
		System.out.println("RIGHT:");
		printDifferences(rightComparator2.getBodyNode());
		System.out.println("LEFT:");
		printDifferences(leftComparator2.getBodyNode());
		
		return null;
	}

	private void printDifferences(TagNode bodyNode) {
		for (Node node:bodyNode){
			if (!(node instanceof TextNode))
			{
				printDifferences((TagNode) node);
				continue;
			}
			TextNode textNode = (TextNode) node;
			Modification modification = textNode.getModification();
			if (modification.getType() != ModificationType.NONE){
				String changes = modification.getType()==ModificationType.CHANGED?" changes:"+modification.getChanges():"";
				System.out.println(textNode.getText()+" "+modification.getType()+changes);
			}
		}
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
			if (child instanceof TextNode) {
				TextNode textChild = (TextNode) child;
				Modification mod = textChild.getModification();
				if (mod.getType() != ModificationType.NONE) {
					diffs.add(new RichTextDiff(textChild, mod));
				}
			}
		}
		return diffs;
	}
	
	public TextNodeComparator getComparator(){
		return rightComparator;
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
