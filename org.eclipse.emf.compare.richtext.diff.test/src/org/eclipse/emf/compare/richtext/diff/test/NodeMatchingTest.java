package org.eclipse.emf.compare.richtext.diff.test;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.emf.compare.richtext.diff.ThreeWayRichTextDiff;
import org.eclipse.emf.compare.richtext.diff.internal.RTBodyNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTDomTreeBuilder;
import org.eclipse.emf.compare.richtext.diff.internal.RTNode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NodeMatchingTest {
	static RichTextMergeInputData inputData = new RichTextMergeInputData();
	private static RTBodyNode originRoot;
	private static RTBodyNode leftRoot;
	private static RTBodyNode rightRoot;

	@BeforeClass
	public static void before() throws IOException, SAXException{
		String origin = inputData.getChangeDifferentParagraphOrigin();
		String right = inputData.getChangeDifferentParagraphRight();
		String left = inputData.getChangeDifferentParagraphLeft();
	
		RTDomTreeBuilder originTree = getDomTree(origin);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		TextNodeComparator rightComparator = diff.getRightComparator();
		TextNodeComparator leftComparator = diff.getLeftComparator();
	
		originRoot = (RTBodyNode)originTree.getBodyNode();
		leftRoot = (RTBodyNode)leftComparator.getBodyNode();
		rightRoot = (RTBodyNode)rightComparator.getBodyNode();
	}
	
	@Test
	public void testMatchingNodes_roots() throws IOException, SAXException {
		Assert.assertTrue(originRoot.isSameNode(originRoot));
		Assert.assertTrue(originRoot.isSameNode(leftRoot));
		Assert.assertTrue(originRoot.isSameNode(rightRoot));
	}
	
	@Test
	public void testMatchingNodes1() throws IOException, SAXException {
		Assert.assertFalse(((RTNode) originRoot.getChild(0))
				.isSameNode(originRoot.getChild(1)));
	}

	@Test
	public void testMatchingNodes2() throws IOException, SAXException {
		Assert.assertFalse(((RTNode) originRoot.getChild(0))
				.isSameNode(leftRoot.getChild(1)));
	}

	@Test
	public void testMatchingNodes3() throws IOException, SAXException {
		Assert.assertFalse(((RTNode) originRoot.getChild(0))
				.isSameNode(rightRoot.getChild(1)));
	}

	@Test
	public void testMatchingNodes4() throws IOException, SAXException {
		Assert.assertTrue(((RTNode) originRoot.getChild(0))
				.isSameNode(originRoot.getChild(0)));
	}

	@Test
	public void testMatchingNodes5() throws IOException, SAXException {
		Assert.assertTrue(((RTNode) originRoot.getChild(0)).isSameNode(leftRoot
				.getChild(0)));
	}

	@Test
	public void testMatchingNodes6() throws IOException, SAXException {
		Assert.assertTrue(((RTNode) originRoot.getChild(0))
				.isSameNode(rightRoot.getChild(0)));
	}

	@Test
	public void testMatchingNodes7() throws IOException, SAXException {
		Assert.assertFalse(((RTNode) originRoot.getChild(1))
				.isSameNode(leftRoot.getChild(0)));
	}

	@Test
	public void testMatchingNodes8() throws IOException, SAXException {
		Assert.assertFalse(((RTNode) originRoot.getChild(1))
				.isSameNode(rightRoot.getChild(0)));
	}

	@Test
	public void testMatchingNodes9() throws IOException, SAXException {
		Assert.assertTrue(((RTNode) originRoot.getChild(1))
				.isSameNode(originRoot.getChild(1)));
	}

	@Test
	public void testMatchingNodes10() throws IOException, SAXException {
		Assert.assertTrue(((RTNode) originRoot.getChild(1)).isSameNode(leftRoot
				.getChild(1)));
	}

	@Test
	public void testMatchingNodes11() throws IOException, SAXException {
		Assert.assertTrue(((RTNode) originRoot.getChild(1))
				.isSameNode(rightRoot.getChild(1)));
	}
	
	private static RTDomTreeBuilder getDomTree(String sourceContent) throws IOException, SAXException {
		InputSource source = new InputSource(new StringReader(sourceContent));
		HtmlCleaner cleaner = new HtmlCleaner();
		RTDomTreeBuilder handler = new RTDomTreeBuilder();
		cleaner.cleanAndParse(source, handler);
		return handler;
	}

}
