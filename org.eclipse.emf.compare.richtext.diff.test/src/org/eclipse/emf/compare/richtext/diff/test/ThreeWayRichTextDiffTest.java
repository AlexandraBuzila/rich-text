/**
 * 
 */

package org.eclipse.emf.compare.richtext.diff.test;

import java.io.IOException;

import org.eclipse.emf.compare.richtext.diff.ThreeWayRichTextDiff;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexandra Buzila
 * @author Florian Zoubek
 *
 */
public class ThreeWayRichTextDiffTest {
	RichTextMergeInputData inputData = new RichTextMergeInputData();
	
	/* 
	 * Paragraph testcases
	 * ===================
	 */
	
	/* Testcase P1.1 - Changes in different paragraphs with two additions  */
	
	/**
	 * Testcase P1.1 - tests conflict state
	 */
	@Test
	public void testConflict_changeInDifferentParagraphs_twoAdditions() throws IOException {
		String origin = inputData.getChangeDifferentParagraphOrigin();
		String right = inputData.getChangeDifferentParagraphRight();
		String left = inputData.getChangeDifferentParagraphLeft();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}

	/**
	 * Testcase P1.1 - tests merge result
	 */
	@Test
	public void testMerge_changeInDifferentParagraphs_twoAdditions() throws IOException {
		String origin = inputData.getChangeDifferentParagraphOrigin();
		String right = inputData.getChangeDifferentParagraphRight();
		String left = inputData.getChangeDifferentParagraphLeft();
		String result = inputData.getChangeDifferentParagraphResult();
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertTrue(result.equals(merged));
	}
	
	/* Testcase P1.2 - Changes in different paragraphs with two deletions  */
	
	/**
	 * Testcase P1.2 - tests conflict state
	 */
	@Test
	public void testConflict_changeInDifferentParagraphs_twoDeletions() throws IOException {
		String origin = inputData.getChangeDifferentParagraphDeletionsOrigin();
		String right = inputData.getChangeDifferentParagraphDeletionsRight();
		String left = inputData.getChangeDifferentParagraphDeletionsLeft();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}

	/**
	 * Testcase P1.2 - tests merge result
	 */
	@Test
	public void testMerge_changeInDifferentParagraphs_twoDeletions() throws IOException {
		String origin = inputData.getChangeDifferentParagraphDeletionsOrigin();
		String right = inputData.getChangeDifferentParagraphDeletionsRight();
		String left = inputData.getChangeDifferentParagraphDeletionsLeft();
		String result = inputData.getChangeDifferentParagraphDeletionsResult();
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertTrue(result.equals(merged));
	}
	
	/* Testcase P1.3 - Changes in different paragraphs with one addition (left) and deletion (right)  */
	
	/**
	 * Testcase P1.3 - tests conflict state
	 */
	@Test
	public void testConflict_changeInDifferentParagraphs_additionDeletion() throws IOException {
		String origin = inputData.getChangeDifferentParagraphAdditionDeletionOrigin();
		String right = inputData.getChangeDifferentParagraphAdditionDeletionRight();
		String left = inputData.getChangeDifferentParagraphAdditionDeletionLeft();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}

	/**
	 * Testcase P1.3 - tests merge result
	 */
	@Test
	public void testMerge_changeInDifferentParagraphs_additionDeletion() throws IOException {
		String origin = inputData.getChangeDifferentParagraphAdditionDeletionOrigin();
		String right = inputData.getChangeDifferentParagraphAdditionDeletionRight();
		String left = inputData.getChangeDifferentParagraphAdditionDeletionLeft();
		String result = inputData.getChangeDifferentParagraphAdditionDeletionResult();
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, merged);
	}

	/* Testcase P2.1 - text additions in same paragraph */
	
	/**
	 * Testcase P2.1 - tests conflict state
	 */
	@Test
	public void testConflict_changeSameParagraph() throws IOException {
		String origin = inputData.getChangeSameParagraphOrigin();
		String left = inputData.getChangeSameParagraphLeft();
		String right = inputData.getChangeSameParagraphRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}

	/* Testcase P3.1 - additional paragraphs at different locations  */
	
	/**
	 * Testcase P3.1 - tests conflict state
	 */
	@Test
	public void testConflict_insertParagraphDifferentLocation() throws IOException {
		String origin = inputData.getInsertParagraphDifferentLocationOrigin();
		String left = inputData.getInsertParagraphDifferentLocationLeft();
		String right = inputData.getInsertParagraphDifferentLocationRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase P3.1 - tests merge result
	 */
	@Test
	public void testMerge_insertParagraphDifferentLocation() throws IOException {
		String origin = inputData.getInsertParagraphDifferentLocationOrigin();
		String left = inputData.getInsertParagraphDifferentLocationLeft();
		String right = inputData.getInsertParagraphDifferentLocationRight();
		String result = inputData.getInsertParagraphDifferentLocationResult();
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, merged);
	}
	
	/* Testcase P3.2 - Insert paragraphs at same location */
	
	/**
	 * Testcase P3.2 - tests conflict state
	 */
	@Test
	public void testConflict_insertParagraphSameLocation() throws IOException {
		String origin = inputData.getInsertParagraphSameLocationOrigin();
		String left = inputData.getInsertParagraphSameLocationLeft();
		String right = inputData.getInsertParagraphSameLocationRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}
	
	/* Testcase P3.3 - Insert same paragraph in both versions (Pseudoconflict)  */
	
	/**
	 * Testcase P3.3 - tests conflict state
	 */
	@Test
	public void testConflict_insertParagraphSameLocation_Pseudoconflict() throws IOException {
		String origin = inputData.getInsertParagraphSameLocationPseudoconflictOrigin();
		String left = inputData.getInsertParagraphSameLocationPseudoconflictLeft();
		String right = inputData.getInsertParagraphSameLocationPseudoconflictRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase P3.3 - tests merge result
	 */
	@Test
	public void testMerge_insertParagraphSameLocation_Pseudoconflict() throws IOException {
		String origin = inputData.getInsertParagraphSameLocationPseudoconflictOrigin();
		String left = inputData.getInsertParagraphSameLocationPseudoconflictLeft();
		String right = inputData.getInsertParagraphSameLocationPseudoconflictRight();
		String result = inputData.getInsertParagraphSameLocationPseudoconflictResult();
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, merged);
	}
	
	/* Testcase P3.4 - Left: paragraph deleted - Right: text additions in deleted paragraph */
	
	/**
	 * Testcase P3.4 - tests conflict state
	 */
	@Test
	public void testConflict_changeInDeletedParagraph_additions() throws IOException {
		String origin = inputData.getAdditionsInDeletedParagraphOrigin();
		String left = inputData.getAdditionsInDeletedParagraphLeft();
		String right = inputData.getAdditionsInDeletedParagraphRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}
	
	/* Testcase P3.5 - Left: paragraph deleted - Right: text deletions in deleted paragraph */
	
	/**
	 * Testcase P3.5 - tests conflict state
	 */
	@Test
	public void testConflict_changeInDeletedParagraph_deletions() throws IOException {
		String origin = inputData.getDeletionsInDeletedParagraphOrigin();
		String left = inputData.getDeletionsInDeletedParagraphLeft();
		String right = inputData.getDeletionsInDeletedParagraphRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}


	/* Testcase P4.1 - modification in moved paragraph  */
	
	/**
	 * Testcase P4.1 - tests conflict state
	 */
	@Test
	public void testConflict_changeMovedParagraph() throws IOException {
		String origin = inputData.getChangeMovedParagraphOrigin();
		String left = inputData.getChangeMovedParagraphLeft();
		String right = inputData.getChangeMovedParagraphRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}

	/* 
	 * Table testcases
	 * =============== 
	 */
	
	/* Testcase T1.1 - Additions in different paragraphs within a table cell */

	/**
	 * Testcase T1.1 - tests conflict state
	 */
	@Test
	public void testConflict_changeInDifferentParagraphs_sameTableCell() throws IOException {
		String origin = inputData.getChangeInTableSameCellOrigin();
		String left = inputData.getChangeInTableSameCellLeft();
		String right = inputData.getChangeInTableSameCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase T1.1 - tests merge result
	 */
	@Test
	public void testMerge_changeInDifferentParagraphs_sameTableCell() throws IOException {
		String origin = inputData.getChangeInTableSameCellOrigin();
		String left = inputData.getChangeInTableSameCellLeft();
		String right = inputData.getChangeInTableSameCellRight();
		String result = inputData.getChangeInTableSameCellResult();
		
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, merged);
	}

	/* Testcase T1.2 - Deletions in different paragraphs within a table cell */
	
	/**
	 * Testcase T1.2 - tests conflict state
	 */
	@Test
	public void testConflict_deletionsInDifferentParagraphs_sameTableCell() throws IOException {
		String origin = inputData.getDeletionsInTableSameCellOrigin();
		String left = inputData.getDeletionsInTableSameCellLeft();
		String right = inputData.getDeletionsInTableSameCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase T1.2 - tests merge result
	 */
	@Test
	public void testMerge_deletionsInDifferentParagraphs_sameTableCell() throws IOException {
		String origin = inputData.getDeletionsInTableSameCellOrigin();
		String left = inputData.getDeletionsInTableSameCellLeft();
		String right = inputData.getDeletionsInTableSameCellRight();
		String result = inputData.getDeletionsInTableSameCellResult();
		
		result = result.replaceAll("\n", "").replaceAll("\r", "");
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, merged);
	}

	/* Testcase T3.1 - Insert column (left), remove column (right) */
	
	/**
	 * Testcase T3.1 - tests conflict state
	 */
	@Test
	public void testConflict_addRemoveColumn() throws IOException {
		String origin = inputData.getAddRemoveTableColumnOrigin();
		String left = inputData.getAddRemoveTableColumnLeft();
		String right = inputData.getAddRemoveTableColumnRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}

	/* Testcase T3.2 - Insert row (right), remove row (left) */

	/**
	 * Testcase T3.2 - tests conflict state
	 */
	@Test
	public void testConflict_addRemoveRow() throws IOException {
		String origin = inputData.getAddRemoveTableRowOrigin();
		String left = inputData.getAddRemoveTableRowLeft();
		String right = inputData.getAddRemoveTableRowRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}

	/* Testcase T3.3 - Insert column (left), remove row (right) */
	
	/**
	 * Testcase T3.3 - tests conflict state
	 */
	@Test
	public void testConflict_addColumnRemoveRow() throws IOException {
		String origin = inputData.getAddColumnRemoveTableRowOrigin();
		String left = inputData.getAddColumnRemoveTableRowLeft();
		String right = inputData.getAddColumnRemoveTableRowRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}

}
