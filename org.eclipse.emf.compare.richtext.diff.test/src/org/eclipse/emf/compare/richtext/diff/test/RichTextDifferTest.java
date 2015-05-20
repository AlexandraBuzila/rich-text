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
	 * No change testcases
	 * ===================
	 */
	
	/**
	 * Testcase NC 1 - tests if the same versions of various nested tags produce
	 * no diffs
	 */
	@Test
	public void testDiff_noChange_variousNestedTags(){

		String origin = "<div><p>This is <i>line</i> 1<br/></p></div>";

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, origin);
		
		assertTrue("Diffs found but none expected - found diffs: " + diffs.toString(), diffs.isEmpty());
	}
	
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
	 * Testcase TAG 2.3 - tests addition of a tag duplicate
	 */
	@Test
	public void testDiff_table_addDuplicate(){
		String origin =     "<table> <tr><td><br/></td></tr> </table>";
		String newVersion = "<table> <tr><td><br/></td></tr> <tr><td><br/></td></tr> </table>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("tr", ModificationType.ADDED),
						new ExpectedModifiedTag("td", ModificationType.ADDED),
						new ExpectedModifiedTag("br", ModificationType.ADDED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TAG 2.4 - tests addition of a tag between to others
	 */
	@Test
	public void testDiff_blockTagAdded_between(){
		String origin =     "<p>Line 1</p><p>Line 2</p>";
		String newVersion = "<p>Line 1</p><p>New paragraph</p><p>Line 2</p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.ADDED),
						new ExpectedModifiedText("New", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("paragraph", ModificationType.ADDED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TAG 2.5 - tests addition of a tag between to others with partial equivalence
	 */
	@Test
	public void testDiff_blockTagAdded_between_partial(){
		String origin =     "<p>Line 1</p><p>Line 2</p>";
		String newVersion = "<p>Line 1</p><div>Line X</div><p>Line 2</p>";
		List<ExpectedDiff> expectedDiffsOption1 = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("div", ModificationType.ADDED),
						new ExpectedModifiedText("Line", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED),
						new ExpectedModifiedText("X", ModificationType.ADDED)
						}));
		List<ExpectedDiff> expectedDiffsOption2 = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("div", ModificationType.CHANGED),
						new ExpectedModifiedText("Line", ModificationType.CHANGED),
						new ExpectedModifiedText(" ", ModificationType.CHANGED),
						new ExpectedModifiedText("X", ModificationType.ADDED),
						new ExpectedModifiedTag("p", ModificationType.ADDED),
						new ExpectedModifiedText("Line", ModificationType.ADDED),
						new ExpectedModifiedText(" ", ModificationType.ADDED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);
		
		assertAnyDiffListEqual(diffs, expectedDiffsOption1, expectedDiffsOption2);
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
	 * Testcase TAG 3.3 - tests deletion of a tag duplicate
	 */
	@Test
	public void testDiff_table_deleteDuplicate(){
		String origin =     "<table> <tr><td><br/></td></tr> <tr><td><br/></td></tr> </table>";
		String newVersion = "<table> <tr><td><br/></td></tr> </table>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("tr", ModificationType.REMOVED),
						new ExpectedModifiedTag("td", ModificationType.REMOVED),
						new ExpectedModifiedTag("br", ModificationType.REMOVED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TAG 3.4 - tests deletion of a block tag between to others
	 */
	@Test
	public void testDiff_blockTagRemoved_between(){
		String origin =     "<p>Line 1</p><p>New paragraph</p><p>Line 2</p>";
		String newVersion = "<p>Line 1</p><p>Line 2</p>";
		List<ExpectedDiff> expectedDiffs = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.REMOVED),
						new ExpectedModifiedText("New", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("paragraph", ModificationType.REMOVED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertDiffListEqual(diffs, expectedDiffs);
	}
	
	/**
	 * Testcase TAG 3.5 - tests deletion of a tag between to others with partial equivalence
	 */
	@Test
	public void testDiff_blockTagRemoved_between_partial(){
		String origin =     "<p>Line 1</p><div>Line X</div><p>Line 2</p>";
		String newVersion = "<p>Line 1</p><p>Line 2</p>";
		List<ExpectedDiff> expectedDiffsOption1 = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("div", ModificationType.REMOVED),
						new ExpectedModifiedText("Line", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("X", ModificationType.REMOVED)
						}));
		List<ExpectedDiff> expectedDiffsOption2 = new ArrayList<ExpectedDiff>(
				Arrays.asList(new ExpectedDiff[] {
						new ExpectedModifiedTag("p", ModificationType.CHANGED),
						new ExpectedModifiedText("Line", ModificationType.CHANGED),
						new ExpectedModifiedText(" ", ModificationType.CHANGED),
						new ExpectedModifiedText("2", ModificationType.CHANGED),
						new ExpectedModifiedTag("p", ModificationType.REMOVED),
						new ExpectedModifiedText("Line", ModificationType.REMOVED),
						new ExpectedModifiedText(" ", ModificationType.REMOVED),
						new ExpectedModifiedText("X", ModificationType.REMOVED)
						}));

		RichTextDiffer differ = new RichTextDiffer();
		ArrayList<RichTextDiff> diffs = differ.getDiffs(origin, newVersion);

		assertAnyDiffListEqual(diffs, expectedDiffsOption1, expectedDiffsOption2);
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
				
		DiffListComparisonResult comparisonResult = compareDiffLists(diffs, expectedDiffs);
		assertTrue("Diffs not equal:\n\n"+comparisonResult.toString(), comparisonResult.isEqual());
	}
	
	/**
	 * asserts that the given difference list is equal to at least one of the
	 * given expected difference lists, regardless of the oder of the elements
	 * in all lists.
	 * 
	 * @param diffs
	 * @param expectedDiffsList
	 */
	private static void assertAnyDiffListEqual(List<RichTextDiff> diffs,
			List<ExpectedDiff>... expectedDiffsList){
		
		boolean isEqual = false;
		StringBuilder comparisonSummary = new StringBuilder();
		
		int diffListIndex = 0;
		
		for(List<ExpectedDiff> expectedDiffs : expectedDiffsList){
			DiffListComparisonResult comparisonResult = compareDiffLists(diffs, expectedDiffs);
			if(comparisonResult.isEqual()){
				isEqual = true;
				break;
			}
			comparisonSummary.append("DiffListComparisonResult #");
			comparisonSummary.append(diffListIndex);
			comparisonSummary.append("\n==========================\n");
			comparisonSummary.append(comparisonResult.toString());
			comparisonSummary.append("\n");
			diffListIndex++;
		}
		
		assertTrue("None of the given expected diff lists have been found:\n"+comparisonSummary, isEqual);
	}
	
	/**
	 * compares a given list of differences with a list of expected diffs.
	 * 
	 * @param diffs
	 * @param expectedDiffs
	 * @return the result of the comparison
	 */
	private static DiffListComparisonResult compareDiffLists(List<RichTextDiff> diffs, List<ExpectedDiff> expectedDiffs){
		DiffListComparisonResult result = new DiffListComparisonResult();
		
		result.remainingExpectedDiffs = new ArrayList<ExpectedDiff>(expectedDiffs);
		
		for (RichTextDiff diff : diffs) {
			ExpectedDiff expectedDiff = null;
			for(ExpectedDiff exDiff : result.remainingExpectedDiffs){
				if(exDiff.isExpected(diff)){
					expectedDiff = exDiff;
				}
			}
			if(expectedDiff == null){
				result.unexpectedDiffs.add(diff);
			}else{
				result.remainingExpectedDiffs.remove(expectedDiff);
			}
			
		}
		
		return result;
	}
	
	/**
	 * Represents a result of a comparison between a list of
	 * {@link RichTextDiff}s and a list of {@link ExpectedDiff}s.
	 *
	 */
	static class DiffListComparisonResult{
		
		List<RichTextDiff> unexpectedDiffs = new ArrayList<RichTextDiff>();
		List<ExpectedDiff> remainingExpectedDiffs = new ArrayList<RichTextDifferTest.ExpectedDiff>();
		
		/**
		 * 
		 * @return true if both lists are equal
		 */
		public boolean isEqual(){
			return unexpectedDiffs.isEmpty() && remainingExpectedDiffs.isEmpty();
		}
		
		/**
		 * a textual summary of the found unexpected differences and not found
		 * expected differences during comparison.
		 */
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("Unexpected diffs:\n");
			for(RichTextDiff diff: unexpectedDiffs){
				sb.append(RichTextDifferTest.toString(diff));
				sb.append("\n");
			}
			sb.append("\nRemaining expected diffs:\n");
			for(ExpectedDiff diff: remainingExpectedDiffs){
				sb.append(diff.toString());
				sb.append("\n");
			}
			return sb.toString();
		}
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
