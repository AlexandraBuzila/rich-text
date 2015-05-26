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
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase P2.1 - tests merge result
	 */
	@Test
	public void testMerge_changeSameParagraph() throws IOException {
		String origin = inputData.getChangeSameParagraphOrigin();
		String left = inputData.getChangeSameParagraphLeft();
		String right = inputData.getChangeSameParagraphRight();
		String result = inputData.getChangeSameParagraphResult();
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase P3.2 - tests merge result
	 */
	@Test
	public void testMerge_insertParagraphSameLocation() throws IOException {
		String origin = inputData.getInsertParagraphSameLocationOrigin();
		String left = inputData.getInsertParagraphSameLocationLeft();
		String right = inputData.getInsertParagraphSameLocationRight();
		String result = inputData.getInsertParagraphSameLocationResult();
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase P3.4 - tests merge result
	 */
	@Test
	public void testMerge_changeInDeletedParagraph_additions() throws IOException {
		String origin = inputData.getAdditionsInDeletedParagraphOrigin();
		String left = inputData.getAdditionsInDeletedParagraphLeft();
		String right = inputData.getAdditionsInDeletedParagraphRight();
		String result = inputData.getAdditionsInDeletedParagraphResult();
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase P3.5 - tests merge result
	 */
	@Test
	public void testMerge_changeInDeletedParagraph_deletions() throws IOException {
		String origin = inputData.getDeletionsInDeletedParagraphOrigin();
		String left = inputData.getDeletionsInDeletedParagraphLeft();
		String right = inputData.getDeletionsInDeletedParagraphRight();
		String result = inputData.getDeletionsInDeletedParagraphResult();
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase P4.1 - tests merge result
	 */
	@Test
	public void testMerge_changeMovedParagraph() throws IOException {
		String origin = inputData.getChangeMovedParagraphOrigin();
		String left = inputData.getChangeMovedParagraphLeft();
		String right = inputData.getChangeMovedParagraphRight();
		String result = inputData.getChangeMovedParagraphResult();
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}

	/* Testcase T1.3 - Addition (left) & deletion (right) in different paragraphs within a table cell */
	
	/**
	 * Testcase T1.3 - tests conflict state
	 */
	@Test
	public void testConflict_additiondeletionInDifferentParagraphs_sameTableCell() throws IOException {
		String origin = inputData.getAdditionDeletionInTableSameCellOrigin();
		String left = inputData.getAdditionDeletionInTableSameCellLeft();
		String right = inputData.getAdditionDeletionInTableSameCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase T1.3 - tests merge result
	 */
	@Test
	public void testMerge_additiondeletionInDifferentParagraphs_sameTableCell() throws IOException {
		String origin = inputData.getAdditionDeletionInTableSameCellOrigin();
		String left = inputData.getAdditionDeletionInTableSameCellLeft();
		String right = inputData.getAdditionDeletionInTableSameCellRight();
		String result = inputData.getAdditionDeletionInTableSameCellResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}
	
	/* Testcase T2.1 - Insertion of new paragraphs at different locations within a table cell */
	
	/**
	 * Testcase T2.1 - tests conflict state
	 */
	@Test
	public void testConflict_insertParagraphAtDifferentLocation_sameTableCell() throws IOException {
		String origin = inputData.getInsertParagraphAtDifferentLocationInSameTableCellOrigin();
		String left = inputData.getInsertParagraphAtDifferentLocationInSameTableCellLeft();
		String right = inputData.getInsertParagraphAtDifferentLocationInSameTableCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase T2.1 - tests merge result
	 */
	@Test
	public void testMerge_insertParagraphAtDifferentLocation_sameTableCell() throws IOException {
		String origin = inputData.getInsertParagraphAtDifferentLocationInSameTableCellOrigin();
		String left = inputData.getInsertParagraphAtDifferentLocationInSameTableCellLeft();
		String right = inputData.getInsertParagraphAtDifferentLocationInSameTableCellRight();
		String result = inputData.getInsertParagraphAtDifferentLocationInSameTableCellResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}

	/* Testcase T2.2 - Insertion of new paragraphs at the same location within a table cell */
	
	/**
	 * Testcase T2.2 - tests conflict state
	 */
	@Test
	public void testConflict_insertParagraphAtSameLocation_sameTableCell() throws IOException {
		String origin = inputData.getInsertParagraphAtSameLocationInSameTableCellOrigin();
		String left = inputData.getInsertParagraphAtSameLocationInSameTableCellLeft();
		String right = inputData.getInsertParagraphAtSameLocationInSameTableCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}
	
	/**
	 * Testcase T2.2 - tests merge result
	 */
	@Test
	public void testMerge_insertParagraphAtSameLocation_sameTableCell() throws IOException {
		String origin = inputData.getInsertParagraphAtSameLocationInSameTableCellOrigin();
		String left = inputData.getInsertParagraphAtSameLocationInSameTableCellLeft();
		String right = inputData.getInsertParagraphAtSameLocationInSameTableCellRight();
		String result = inputData.getInsertParagraphAtSameLocationInSameTableCellResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}

	/* Testcase T2.3 - Insertion of new paragraphs and same content (Pseudoconflict) at the same location within a table cell */
	
	/**
	 * Testcase T2.3 - tests conflict state
	 */
	@Test
	public void testConflict_insertParagraphAtSameLocation_sameTableCell_pseudoConflict() throws IOException {
		String origin = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin();
		String left = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft();
		String right = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertFalse(diff.isConflicting());
	}
	
	/**
	 * Testcase T2.3 - tests merge result
	 */
	@Test
	public void testMerge_insertParagraphAtSameLocation_sameTableCell_pseudoConflict() throws IOException {
		String origin = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin();
		String left = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft();
		String right = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight();
		String result = inputData.getInsertParagraphAtSameLocationInSameTableCellPseudoconflictResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}
	
	/* Testcase T2.4 - Text addition in deleted paragraph */

	/**
	 * Testcase T2.4 - tests conflict state
	 */
	@Test
	public void testConflict_additionInDeletedParagraph_sameTableCell() throws IOException {
		String origin = inputData.getAdditionsInDeletedParagraphInSameTableCellOrigin();
		String left = inputData.getAdditionsInDeletedParagraphInSameTableCellLeft();
		String right = inputData.getAdditionsInDeletedParagraphInSameTableCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}
	

	/**
	 * Testcase T2.4 - tests merge result
	 */
	@Test
	public void testMerge_additionInDeletedParagraph_sameTableCell() throws IOException {
		String origin = inputData.getAdditionsInDeletedParagraphInSameTableCellOrigin();
		String left = inputData.getAdditionsInDeletedParagraphInSameTableCellLeft();
		String right = inputData.getAdditionsInDeletedParagraphInSameTableCellRight();
		String result = inputData.getAdditionsInDeletedParagraphInSameTableCellResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}

	/* Testcase T2.5 - Text addition in deleted paragraph */
	
	/**
	 * Testcase T2.5 - tests conflict state
	 */
	@Test
	public void testConflict_deletionInDeletedParagraph_sameTableCell() throws IOException {
		String origin = inputData.getDeletionsInDeletedParagraphInSameTableCellOrigin();
		String left = inputData.getDeletionsInDeletedParagraphInSameTableCellLeft();
		String right = inputData.getDeletionsInDeletedParagraphInSameTableCellRight();

		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		Assert.assertTrue(diff.isConflicting());
	}
	
	/**
	 * Testcase T2.5 - tests merge result
	 */
	@Test
	public void testMerge_deletionInDeletedParagraph_sameTableCell() throws IOException {
		String origin = inputData.getDeletionsInDeletedParagraphInSameTableCellOrigin();
		String left = inputData.getDeletionsInDeletedParagraphInSameTableCellLeft();
		String right = inputData.getDeletionsInDeletedParagraphInSameTableCellRight();
		String result = inputData.getDeletionsInDeletedParagraphInSameTableCellResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase T3.1 - tests merge result
	 */
	@Test
	public void testMerge_addRemoveColumn() throws IOException {
		String origin = inputData.getAddRemoveTableColumnOrigin();
		String left = inputData.getAddRemoveTableColumnLeft();
		String right = inputData.getAddRemoveTableColumnRight();
		String result = inputData.getAddRemoveTableColumnResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase T3.2 - tests merge result
	 */
	@Test
	public void testMerge_addRemoveRow() throws IOException {
		String origin = inputData.getAddRemoveTableRowOrigin();
		String left = inputData.getAddRemoveTableRowLeft();
		String right = inputData.getAddRemoveTableRowRight();
		String result = inputData.getAddRemoveTableRowResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
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
	
	/**
	 * Testcase T3.3 - tests merge result
	 */
	@Test
	public void testMerge_addColumnRemoveRow() throws IOException {
		String origin = inputData.getAddColumnRemoveTableRowOrigin();
		String left = inputData.getAddColumnRemoveTableRowLeft();
		String right = inputData.getAddColumnRemoveTableRowRight();
		String result = inputData.getAddColumnRemoveTableRowResult();
		
		result = normalizeHtml(result);
		ThreeWayRichTextDiff diff = new ThreeWayRichTextDiff(origin, left, right);
		String merged = diff.getMerged();
		Assert.assertEquals(result, normalizeHtml(merged));
	}
	
	/**
	 * normalizes html code using the following operations:
	 * 
	 * <ul>
	 * <li>removes whitespaces and tabs before and after tags</li>
	 * <li>removes line breaks</li>
	 * <li>adds line breaks between tags and between content and tags</li>
	 * <li>transform tag names and attribute names to lower case</li>
	 * </ul>
	 * 
	 * @param html
	 *            the html code to normalize
	 * @return the normalized html code
	 */
	private String normalizeHtml(String html){
		StringBuilder cleanedHtml = new StringBuilder(html.length());
		
		boolean inTag = false;
		boolean isTagPredecessor = false;
		boolean inAttribute = false;
		
		for(int currentCharIndex = 0; currentCharIndex < html.length(); currentCharIndex++){
			char currentChar = html.charAt(currentCharIndex);
			
			if(currentChar == '<'){
				// tag opened
				inTag = true;
				// remove whitespaces and tabs before a tag
				cleanedHtml = removeTrailingChars(cleanedHtml, new char[]{' ','\t'});
				// add line break before the tag if necessary
				if(cleanedHtml.length() > 0 && cleanedHtml.charAt(cleanedHtml.length()-1) != '\n'){
					cleanedHtml.append('\n');
				}					
			}else if(currentChar == '>'){
				// tag closed
				inTag = false;
				inAttribute = false; // just in case the html code is malformed
				isTagPredecessor = true;
			}else if (currentChar == '/' && html.charAt(currentCharIndex+1) == '>' ){
				// void tag
				cleanedHtml = removeTrailingChars(cleanedHtml, new char[]{' ','\t'});
			}else if(currentChar == '"' && inTag){
				// attribute value started/ended
				inAttribute = !inAttribute;
			}
			
			if(currentChar != '\n' && currentChar != '\r'){
				if(inTag && !inAttribute){
					// transform uppercase characters which are not attribute values in tags to lower case 
					cleanedHtml.append(Character.toLowerCase(currentChar));
				}else if(isTagPredecessor){
					// ignore whitespaces and tabs after the tag
					if(currentChar != ' ' && currentChar != '\t'){
						cleanedHtml.append(currentChar);
						if(currentChar != '>'){
							isTagPredecessor = false;
						}
					}
				}else{
					cleanedHtml.append(currentChar);
				}
				
				if(currentChar == '>'){
					// add line break after tag
					cleanedHtml.append('\n');
				}
			}
		}
		
		return cleanedHtml.toString();
	}
	
	/**
	 * removes all trailing characters contained in the given character array
	 * from the given {@link StringBuilder} instance until a character is found
	 * which is not present in given character array.
	 * 
	 * @param builder
	 *            the {@link StringBuilder} to modify
	 * @param chars
	 *            the characters to delete
	 * @return the given (and modified) {@link StringBuilder} instance
	 */
	private static StringBuilder removeTrailingChars(StringBuilder builder, char[] chars){
		
		for(int endIndex = builder.length()-1; endIndex >= 0; endIndex--){
			char predecessor = builder.charAt(endIndex);
			for(char character : chars){
				if(predecessor == character ){
					builder.deleteCharAt(endIndex);
					break;
				}
			}

			// the length must be bigger than endIndex if no character has been deleted 
			if(builder.length() > endIndex){
				break;
			}
		}
		return builder;
	}

}
