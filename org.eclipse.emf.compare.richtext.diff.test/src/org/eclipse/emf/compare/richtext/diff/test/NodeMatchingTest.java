package org.eclipse.emf.compare.richtext.diff.test;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.emf.compare.richtext.diff.ThreeWayRichTextDiff;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.BodyNode;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NodeMatchingTest {
	static RichTextMergeInputData inputData = new RichTextMergeInputData();
	private static BodyNode originRoot;
	private static BodyNode leftRoot;
	private static BodyNode rightRoot;

@BeforeClass
public static void before() throws IOException, SAXException{
	String origin = inputData.getChangeDifferentParagraphOrigin();
	String right = inputData.getChangeDifferentParagraphRight();
	String left = inputData.getChangeDifferentParagraphLeft();

	DomTreeBuilder originTree = getDomTree(origin);
	// DomTreeBuilder leftTree = getDomTree(left);
	// DomTreeBuilder rightTree = getDomTree(right);
	ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
	TextNodeComparator rightComparator = diff.getRightComparator();
	TextNodeComparator leftComparator = diff.getLeftComparator();

	originRoot = originTree.getBodyNode();
	leftRoot = leftComparator.getBodyNode();
	rightRoot = rightComparator.getBodyNode();
}
	
	@Test
	public void testMatchingNodes_roots() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag(originRoot, originRoot));
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag(originRoot, leftRoot));
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag(originRoot, rightRoot));
	}
	
	@Test
	public void testMatchingNodes1() throws IOException, SAXException {
		Assert.assertFalse(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(0),
				(TagNode) originRoot.getChild(1)));
	}
	@Test
	public void testMatchingNodes2() throws IOException, SAXException {
		Assert.assertFalse(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(0),
				(TagNode) leftRoot.getChild(1)));
	}
	@Test
	public void testMatchingNodes3() throws IOException, SAXException {
		Assert.assertFalse(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(0),
				(TagNode) rightRoot.getChild(1)));
	}
	@Test
	public void testMatchingNodes4() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(0),
				(TagNode) originRoot.getChild(0)));
	}
	@Test
	public void testMatchingNodes5() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(0),
				(TagNode) leftRoot.getChild(0)));
	}
	@Test
	public void testMatchingNodes6() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(0),
				(TagNode) rightRoot.getChild(0)));
	}
	@Test
	public void testMatchingNodes7() throws IOException, SAXException {
		Assert.assertFalse(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(1),
				(TagNode) leftRoot.getChild(0)));
	}
	@Test
	public void testMatchingNodes8() throws IOException, SAXException {
		Assert.assertFalse(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(1),
				(TagNode) rightRoot.getChild(0)));
	}
	@Test
	public void testMatchingNodes9() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(1),
				(TagNode) originRoot.getChild(1)));
	}
	@Test
	public void testMatchingNodes10() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(1),
				(TagNode) leftRoot.getChild(1)));
	}
	@Test
	public void testMatchingNodes11() throws IOException, SAXException {
		Assert.assertTrue(ThreeWayRichTextDiff.isSameTag((TagNode) originRoot.getChild(1),
				(TagNode) rightRoot.getChild(1)));
	}
	
	private static DomTreeBuilder getDomTree(String sourceContent) throws IOException, SAXException {
		InputSource source = new InputSource(new StringReader(sourceContent));
		HtmlCleaner cleaner = new HtmlCleaner();
		DomTreeBuilder handler = new DomTreeBuilder();
		cleaner.cleanAndParse(source, handler);
		return handler;
	}

}
