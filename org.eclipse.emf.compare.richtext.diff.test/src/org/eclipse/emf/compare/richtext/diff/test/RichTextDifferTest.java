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
package org.eclipse.emf.compare.richtext.diff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.compare.richtext.diff.RichTextDiff;
import org.eclipse.emf.compare.richtext.diff.internal.RTNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTTagNode;
import org.eclipse.emf.compare.richtext.diff.internal.RTTextNode;
import org.eclipse.emf.compare.richtext.diff.internal.RichTextDiffer;
import org.junit.Test;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.ModificationType;

/**
 * @author Florian Zoubek
 *
 */
public class RichTextDifferTest {
	
	/* 
	 * Simple text testcases
	 * =====================
	 */
	
	/**
	 * Testcase TXT 1.1 - tests simple text addition  
	 */
	@Test
	public void testDiff_textAddition() {

		String origin = "<p>This is line</p>";
		String newVersion = "<p>This is line 1</p>";

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		// only 1 diff expected, but text node property "whiteBefore" must be
		// set to "true"
		assertEquals(1, diffs.size());
		for (RichTextDiff diff : diffs) {
			RTNode changedNode = (RTNode) diff.getChild();
			assertTrue(changedNode instanceof RTTextNode);
			assertTrue(((RTTextNode) changedNode).isWhiteBefore());
			String text = ((RTTextNode) changedNode).getText();
			assertTrue(text.equals(" ") || text.equals("1"));
			assertEquals(ModificationType.ADDED, diff.getModification()
					.getType());
		}
	}
	
	/**
	 * Testcase TXT 1.2 - tests simple text deletion  
	 */
	@Test
	public void testDiff_textDeletion() {

		String origin = "<p>This is line 1</p>";
		String newVersion = "<p>This is line</p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("1", ModificationType.REMOVED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertEquals(2, diffs.size());
		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TXT 2.1 - tests text deletion resulting in an empty tag  
	 */
	@Test
	public void testDiff_textDeletion_emptyTag() {

		String origin = "<p>Text</p>";
		String newVersion = "<p></p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedText("Text", ModificationType.REMOVED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertEquals(1, diffs.size());
		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TXT 2.2 - tests text deletion resulting in an empty tag within another tag
	 */
	@Test
	public void testDiff_textDeletion_emptyTag_complex() {

		String origin = "<i>Text 1</i><p><b>Deletion</b></p><pre>Text 3</pre>";
		String newVersion = "<i>Text 1</i><p><b></b></p><pre>Text 3</pre>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedText("Deletion", ModificationType.REMOVED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertEquals(1, diffs.size());
		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TXT 2.3 - tests text addition in an empty tag
	 */
	@Test
	public void testDiff_textAddition_emptyTag() {

		String origin = "<p></p>";
		String newVersion = "<p>Text</p>";

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertEquals(1, diffs.size());
		RichTextDiff diff = diffs.get(0);
		new ExpectedModifiedText("Text", ModificationType.ADDED).assertDiffEqual(diff);
	}
	
	/* 
	 * Tag testcases
	 * =============
	 */
	
	/**
	 * Testcase TAG 1.1 - tests block tag name change
	 */
	@Test
	public void testDiff_blockTagChanged() {

		String origin = "<div>Text</div>";
		String newVersion = "<p>Text</p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.CHANGED),
						new ExpectedModifiedText("Text", ModificationType.CHANGED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}

	/**
	 * Testcase TAG 1.2 - tests complete change of a block tag hierarchy
	 */
	@Test
	public void testDiff_blockTagHierarchyChanged_all() {

		String origin = "<div><p>Text 1</p></div>";
		String newVersion = "<blockquote><h1>Text 1</h1></blockquote>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("blockquote", ModificationType.CHANGED),
						new ExpectedModifiedTag("h1", ModificationType.CHANGED),
						new ExpectedModifiedText("Text", ModificationType.CHANGED),
						new ExpectedModifiedText(" ", ModificationType.CHANGED),
						new ExpectedModifiedText("1", ModificationType.CHANGED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TAG 1.3 - tests partial change of a block tag hierarchy
	 */
	@Test
	public void testDiff_blockTagHierarchyChanged_partial() {

		String origin = "<div><p>Text 1</p></div>";
		String newVersion = "<blockquote><p>Text 1</p></blockquote>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("blockquote", ModificationType.CHANGED),
						new ExpectedModifiedText("Text", ModificationType.CHANGED),
						new ExpectedModifiedText(" ", ModificationType.CHANGED),
						new ExpectedModifiedText("1", ModificationType.CHANGED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TAG 1.4 - tests inline tag name change
	 */
	@Test
	public void testDiff_inlineTagChanged() {

		String origin = "<p>This is <b>line 1</b></p>";
		String newVersion = "<p>This is <i>line 1</i></p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("i", ModificationType.CHANGED),
						new ExpectedModifiedText("line", ModificationType.CHANGED),
						new ExpectedModifiedText(" ", ModificationType.CHANGED),
						new ExpectedModifiedText("1", ModificationType.CHANGED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}

	/**
	 * Testcase TAG 2.1 - tests tag addition
	 */
	@Test
	public void testDiff_blockTagAdded() {

		String origin = "<p>This is line 1</p>";
		String newVersion = "<p>This is line 1</p><p>This is line 2</p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.ADDED),
						new ExpectedModifiedText("This", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("is", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("line", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("2", ModificationType.ADDED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}

	/**
	 * Testcase TAG 2.2 - tests tag addition within other tag
	 */
	@Test
	public void testDiff_blockTagAdded_nested() {

		String origin = "<div><p>This is line 1</p></div>";
		String newVersion = "<div><p>This is line 1</p><p>This is line 2</p></div>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.ADDED),
						new ExpectedModifiedText("This", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("is", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("line", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("2", ModificationType.ADDED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}

	/**
	 * Testcase TAG 3.1 - tests tag deletion
	 */
	@Test
	public void testDiff_blockTagRemoved() {

		String origin = "<p>This is line 1</p><p>This is line 2</p>";
		String newVersion = "<p>This is line 1</p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.REMOVED),
						new ExpectedModifiedText("This", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("is", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("line", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("2", ModificationType.REMOVED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}

	/**
	 * Testcase TAG 3.2 - tests tag deletion within other tag
	 */
	@Test
	public void testDiff_blockTagRemoved_nested() {

		String origin = "<div><p>This is line 1</p><p>This is line 2</p></div>";
		String newVersion = "<div><p>This is line 1</p></div>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.REMOVED),
						new ExpectedModifiedText("This", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("is", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("line", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("2", ModificationType.REMOVED) 
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}

	/**
	 * asserts that the given difference list is equal to the given list of expected
	 * differences, regardless of the order of the elements in both lists.
	 * 
	 * @param diffs
	 * @param exepectedDiffs
	 */
	private static void assertDiffListEqual(List<RichTextDiff> diffs,
			List<ExpectedDiff> expectedDiffs) {
		
		List<ExpectedDiff> expectedDiffsCopy = new ArrayList<ExpectedDiff>(expectedDiffs);
		
		for (RichTextDiff diff : diffs) {
			ExpectedDiff expectedDiff = null;
			for(ExpectedDiff exDiff : expectedDiffsCopy){
				if(exDiff.isExpected(diff)){
					expectedDiff = exDiff;
				}
			}
			if(expectedDiff == null){
				fail("Unexpected diff found: " + toString(diff));
			}else{
				expectedDiffsCopy.remove(expectedDiff);
			}
			
		}
		// check if all expected diffs have been found
		assertTrue("Not all expected diffs found - Remaining expected diffs: "
				+ expectedDiffsCopy.toString(), expectedDiffsCopy.isEmpty());
	}
	
	private static String toString(RichTextDiff diff){
		return "RichTextDiff["
				+"child = " + diff.getChild().toString() + " , "
				+ "modType = " + diff.getModification().getType()
				+ " ]";
	}

	/**
	 * Base interface for expected difference entries which are used to validate the result of difference lists 
	 * 
	 * @author Florian Zoubek
	 *
	 */
	interface ExpectedDiff {
		
		/**
		 * @param diff the difference to check
		 * @return true if the given difference matches the expected values 
		 */
		public boolean isExpected(RichTextDiff diff);

		/**
		 * asserts that the given difference matches the expected values
		 * @param diff the difference to check
		 */
		public void assertDiffEqual(RichTextDiff diff);

		/**
		 * asserts that the given difference does not match the expected values
		 * @param diff the difference to check
		 */
		public void assertDiffNotEquals(RichTextDiff diff);
	}

	/**
	 * Represents an expected difference entry containing a {@link TextNode}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	class ExpectedModifiedText implements ExpectedDiff {
		public String text;
		public ModificationType modType;

		public ExpectedModifiedText(String text, ModificationType modType) {
			this.text = text;
			this.modType = modType;
		}
		
		@Override
		public boolean isExpected(RichTextDiff diff) {
			Node node = diff.getChild();
			if (node instanceof TextNode) {
				TextNode textNode = ((TextNode) node);
				return textNode.getText().equals(text);
			}
			return false;
		}

		@Override
		public void assertDiffEqual(RichTextDiff diff) {
			Node node = diff.getChild();
			assertTrue(node instanceof TextNode);
			TextNode textNode = ((TextNode) node);
			assertEquals(text, textNode.getText());

		}

		@Override
		public void assertDiffNotEquals(RichTextDiff diff) {
			Node node = diff.getChild();
			assertFalse(node instanceof TextNode);
			TextNode textNode = ((TextNode) node);
			assertNotEquals(text, textNode.getText());
		}

		@Override
		public String toString() {
			return "ExpectedModifiedText [text=" + text + ", modType="
					+ modType + "]";
		}
	}

	/**
	 * Represents an expected difference entry containing a {@link RTTagNode}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	class ExpectedModifiedTag implements ExpectedDiff {
		public String qName;
		public ModificationType modType;

		public ExpectedModifiedTag(String qName, ModificationType modType) {
			this.qName = qName;
			this.modType = modType;
		}

		@Override
		public boolean isExpected(RichTextDiff diff) {
			Node node = diff.getChild();
			if (node instanceof RTTagNode) {
				RTTagNode rtNode = ((RTTagNode) node);
				return rtNode.getQName().equals(qName)
						&& rtNode.getModification().getType().equals(modType);
			}
			return false;
		}

		@Override
		public void assertDiffEqual(RichTextDiff diff) {
			Node node = diff.getChild();
			assertTrue(node instanceof RTTagNode);
			RTTagNode rtNode = ((RTTagNode) node);
			assertEquals(qName, rtNode.getQName());
			assertEquals(modType, rtNode.getModification().getType());
		}

		@Override
		public void assertDiffNotEquals(RichTextDiff diff) {
			Node node = diff.getChild();
			assertFalse(node instanceof RTTagNode);
			RTTagNode rtNode = ((RTTagNode) node);
			assertNotEquals(qName, rtNode.getQName());
			assertNotEquals(modType, rtNode.getModification().getType());
		}

		@Override
		public String toString() {
			return "ExpectedModifiedTag [qName=" + qName + ", modType="
					+ modType + "]";
		}
	}
}
