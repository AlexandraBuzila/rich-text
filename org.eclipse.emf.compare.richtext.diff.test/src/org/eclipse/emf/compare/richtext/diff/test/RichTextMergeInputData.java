package org.eclipse.emf.compare.richtext.diff.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

@SuppressWarnings("nls")
public class RichTextMergeInputData {

	/* 
	 * Paragraph testcases
	 * ===================
	 */

	/* Testcase P1.1 - Changes in different paragraphs with two additions  */
	
	/**
	 * Testcase P1.1 - Origin version
	 * 
	 * @see #getChangeDifferentParagraphOrigin()
	 * @see #getChangeDifferentParagraphLeft()
	 * @see #getChangeDifferentParagraphRight()
	 * @see #getChangeDifferentParagraphResult()
	 */
	public String getChangeDifferentParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/origin.html");
	}

	/**
	 * Testcase P1.1 - Left version, changes:
	 * 
	 * <ul><li>Text additions to first paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphOrigin()
	 * @see #getChangeDifferentParagraphLeft()
	 * @see #getChangeDifferentParagraphRight()
	 * @see #getChangeDifferentParagraphResult()
	 */
	public String getChangeDifferentParagraphLeft() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/left.html");
	}

	/**
	 * Testcase P1.1 - Right version, changes:
	 * 
	 * <ul><li>Text additions to second paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphOrigin()
	 * @see #getChangeDifferentParagraphLeft()
	 * @see #getChangeDifferentParagraphRight()
	 * @see #getChangeDifferentParagraphResult()
	 */
	public String getChangeDifferentParagraphRight() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/right.html");
	}

	/**
	 * Testcase P1.1 - Expected merged version
	 * 
	 * @see #getChangeDifferentParagraphOrigin()
	 * @see #getChangeDifferentParagraphLeft()
	 * @see #getChangeDifferentParagraphRight()
	 * @see #getChangeDifferentParagraphResult()
	 */
	public String getChangeDifferentParagraphResult() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/result.html");
	}
	
	/* Testcase P1.2 - Changes in different paragraphs with two deletions  */
	
	/**
	 * Testcase P1.2 - Origin version
	 * 
	 * @see #getChangeDifferentParagraphDeletionsOrigin()
	 * @see #getChangeDifferentParagraphDeletionsLeft()
	 * @see #getChangeDifferentParagraphDeletionsRight()
	 * @see #getChangeDifferentParagraphDeletionsResult()
	 */
	public String getChangeDifferentParagraphDeletionsOrigin() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/origin.html");
	}
	
	/**
	 * Testcase P1.2 - Right version, changes:
	 * 
	 * <ul><li>Text deletion in second paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphDeletionsOrigin()
	 * @see #getChangeDifferentParagraphDeletionsLeft()
	 * @see #getChangeDifferentParagraphDeletionsRight()
	 * @see #getChangeDifferentParagraphDeletionsResult()
	 */
	public String getChangeDifferentParagraphDeletionsRight() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/right.html");
	}
	
	/**
	 * Testcase P1.2 - Left version, changes:
	 * 
	 * <ul><li>Text deletion in first paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphDeletionsOrigin()
	 * @see #getChangeDifferentParagraphDeletionsLeft()
	 * @see #getChangeDifferentParagraphDeletionsRight()
	 * @see #getChangeDifferentParagraphDeletionsResult()
	 */
	public String getChangeDifferentParagraphDeletionsLeft() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/left.html");
	}

	/**
	 * Testcase P1.2 - Expected merged version
	 * 
	 * @see #getChangeDifferentParagraphDeletionsOrigin()
	 * @see #getChangeDifferentParagraphDeletionsLeft()
	 * @see #getChangeDifferentParagraphDeletionsRight()
	 * @see #getChangeDifferentParagraphDeletionsResult()
	 */
	public String getChangeDifferentParagraphDeletionsResult() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/result.html");
	}
	
	/* Testcase P1.3 - Changes in different paragraphs with one addition (left) and deletion (right)  */

	/**
	 * Testcase P1.3 - Origin version, contains:
	 * 
	 * <ul><li>2 paragraphs with text</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionOrigin()
	 * @see #getChangeDifferentParagraphAdditionDeletionLeft()
	 * @see #getChangeDifferentParagraphAdditionDeletionRight()
	 * @see #getChangeDifferentParagraphAdditionDeletionResult()
	 */
	public String getChangeDifferentParagraphAdditionDeletionOrigin() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/origin.html");
	}

	/**
	 * Testcase P1.3 - Right version, changes:
	 * 
	 * <ul><li>Text added to first paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionOrigin()
	 * @see #getChangeDifferentParagraphAdditionDeletionLeft()
	 * @see #getChangeDifferentParagraphAdditionDeletionRight()
	 * @see #getChangeDifferentParagraphAdditionDeletionResult()
	 */
	public String getChangeDifferentParagraphAdditionDeletionRight() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/right.html");
	}

	/**
	 * Testcase P1.3 - Left version, changes:
	 * 
	 * <ul><li>Text deleted from first paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionOrigin()
	 * @see #getChangeDifferentParagraphAdditionDeletionLeft()
	 * @see #getChangeDifferentParagraphAdditionDeletionRight()
	 * @see #getChangeDifferentParagraphAdditionDeletionResult()
	 */
	public String getChangeDifferentParagraphAdditionDeletionLeft() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/left.html");
	}

	/**
	 * Testcase P1.3 - Expected merged version
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionOrigin()
	 * @see #getChangeDifferentParagraphAdditionDeletionLeft()
	 * @see #getChangeDifferentParagraphAdditionDeletionRight()
	 * @see #getChangeDifferentParagraphAdditionDeletionResult()
	 */
	public String getChangeDifferentParagraphAdditionDeletionResult() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/result.html");
	}
	
	/* Testcase P2.1 - text additions in same paragraph */
	
	/**
	 * Testcase P2.1 - Origin version
	 * 
	 * @see #getChangeSameParagraphOrigin()
	 * @see #getChangeSameParagraphLeft()
	 * @see #getChangeSameParagraphRight()
	 * @see #getChangeSameParagraphResult()
	 */
	public String getChangeSameParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changesameparagraph/origin.html");
	}

	/**
	 * Testcase P2.1 - Left version, changes:
	 * 
	 * <ul><li>Text addition in first paragraph</li></ul>
	 * 
	 * @see #getChangeSameParagraphOrigin()
	 * @see #getChangeSameParagraphLeft()
	 * @see #getChangeSameParagraphRight()
	 * @see #getChangeSameParagraphResult()
	 */
	public String getChangeSameParagraphLeft() throws IOException {
		return readFile("data/paragraph/changesameparagraph/left.html");
	}
	
	/**
	 * Testcase P2.1 - Right version, changes:
	 * 
	 * <ul><li>Text addition in first paragraph</li></ul>
	 * 
	 * @see #getChangeSameParagraphOrigin()
	 * @see #getChangeSameParagraphLeft()
	 * @see #getChangeSameParagraphRight()
	 * @see #getChangeSameParagraphResult()
	 */
	public String getChangeSameParagraphRight() throws IOException {
		return readFile("data/paragraph/changesameparagraph/right.html");
	}
	
	/**
	 * Testcase P2.1 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getChangeSameParagraphOrigin()
	 * @see #getChangeSameParagraphLeft()
	 * @see #getChangeSameParagraphRight()
	 * @see #getChangeSameParagraphResult()
	 */
	public String getChangeSameParagraphResult() throws IOException {
		return readFile("data/paragraph/changesameparagraph/result.html");
	}

	/* Testcase P3.1 - additional paragraphs at different locations  */
	
	/**
	 * Testcase P3.1 - Origin version
	 * 
	 * @see #getInsertParagraphDifferentLocationOrigin()
	 * @see #getInsertParagraphDifferentLocationLeft()
	 * @see #getInsertParagraphDifferentLocationRight()
	 * @see #getInsertParagraphDifferentLocationResult()
	 */
	public String getInsertParagraphDifferentLocationOrigin() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/origin.html");
	}

	/**
	 * Testcase P3.1 - Left version, changes:
	 * 
	 * <ul><li>adds paragraph after first paragraph</li></ul>
	 * 
	 * @see #getInsertParagraphDifferentLocationOrigin()
	 * @see #getInsertParagraphDifferentLocationLeft()
	 * @see #getInsertParagraphDifferentLocationRight()
	 * @see #getInsertParagraphDifferentLocationResult()
	 */
	public String getInsertParagraphDifferentLocationLeft() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/left.html");
	}

	/**
	 * Testcase P3.1 - Right version
	 * 
	 * <ul><li>adds paragraph after last paragraph</li></ul>
	 * 
	 * @see #getInsertParagraphDifferentLocationOrigin()
	 * @see #getInsertParagraphDifferentLocationLeft()
	 * @see #getInsertParagraphDifferentLocationRight()
	 * @see #getInsertParagraphDifferentLocationResult()
	 */
	public String getInsertParagraphDifferentLocationRight() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/right.html");
	}
	
	/**
	 * Testcase P3.1 - Expected merged version
	 * 
	 * @see #getInsertParagraphDifferentLocationOrigin()
	 * @see #getInsertParagraphDifferentLocationLeft()
	 * @see #getInsertParagraphDifferentLocationRight()
	 * @see #getInsertParagraphDifferentLocationResult()
	 */
	public String getInsertParagraphDifferentLocationResult() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/result.html");
	}
	
	/* Testcase P3.2 - Insert paragraphs at same location */

	/**
	 * Testcase P3.2 - Origin version
	 * 
	 * @see #getInsertParagraphSameLocationOrigin()
	 * @see #getInsertParagraphSameLocationLeft()
	 * @see #getInsertParagraphSameLocationRight()
	 * @see #getInsertParagraphSameLocationResult()
	 */
	public String getInsertParagraphSameLocationOrigin() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/origin.html");
	}

	/**
	 * Testcase P3.2 - Left version, changes:
	 * 
	 * <ul><li>adds paragraph after first paragraph</li></ul>
	 * 
	 * @see #getInsertParagraphSameLocationOrigin()
	 * @see #getInsertParagraphSameLocationLeft()
	 * @see #getInsertParagraphSameLocationRight()
	 * @see #getInsertParagraphSameLocationResult()
	 */
	public String getInsertParagraphSameLocationLeft() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/left.html");
	}
	
	/**
	 * Testcase P3.2 - Right version, changes:
	 * 
	 * <ul><li>adds paragraph after first paragraph</li></ul>
	 * 
	 * @see #getInsertParagraphSameLocationOrigin()
	 * @see #getInsertParagraphSameLocationLeft()
	 * @see #getInsertParagraphSameLocationRight()
	 * @see #getInsertParagraphSameLocationResult()
	 */
	public String getInsertParagraphSameLocationRight() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/right.html");
	}
	
	/**
	 * Testcase P3.2 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getInsertParagraphSameLocationOrigin()
	 * @see #getInsertParagraphSameLocationLeft()
	 * @see #getInsertParagraphSameLocationRight()
	 * @see #getInsertParagraphSameLocationResult()
	 */
	public String getInsertParagraphSameLocationResult() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/result.html");
	}

	/* Testcase P3.3 - Insert same paragraph in both versions (Pseudoconflict)  */
	
	/**
	 * Testcase P3.3 - Origin version
	 * 
	 * @see #getInsertParagraphSameLocationPseudoconflictOrigin()
	 * @see #getInsertParagraphSameLocationPseudoconflictLeft()
	 * @see #getInsertParagraphSameLocationPseudoconflictRight()
	 * @see #getInsertParagraphSameLocationPseudoconflictResult()
	 */
	public String getInsertParagraphSameLocationPseudoconflictOrigin() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/origin.html");
	}
	
	/**
	 * Testcase P3.3 - Left version, changes:
	 * 
	 * <ul><li>1 paragraph between the existing ones</li></ul>
	 * 
	 * @see #getInsertParagraphSameLocationPseudoconflictOrigin()
	 * @see #getInsertParagraphSameLocationPseudoconflictLeft()
	 * @see #getInsertParagraphSameLocationPseudoconflictRight()
	 * @see #getInsertParagraphSameLocationPseudoconflictResult()
	 */
	public String getInsertParagraphSameLocationPseudoconflictLeft() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/left.html");
	}
	/**
	 * Testcase P3.3 - Right version, changes:
	 * 
	 * <ul><li>1 paragraph between the existing ones which is the same as in the left version</li></ul>
	 * 
	 * @see #getInsertParagraphSameLocationPseudoconflictOrigin()
	 * @see #getInsertParagraphSameLocationPseudoconflictLeft()
	 * @see #getInsertParagraphSameLocationPseudoconflictRight()
	 * @see #getInsertParagraphSameLocationPseudoconflictResult()
	 */
	public String getInsertParagraphSameLocationPseudoconflictRight() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/right.html");
	}
	
	/**
	 * Testcase P3.3 - Expected merged version
	 * 
	 * @see #getInsertParagraphSameLocationPseudoconflictOrigin()
	 * @see #getInsertParagraphSameLocationPseudoconflictLeft()
	 * @see #getInsertParagraphSameLocationPseudoconflictRight()
	 * @see #getInsertParagraphSameLocationPseudoconflictResult()
	 */
	public String getInsertParagraphSameLocationPseudoconflictResult() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/result.html");
	}
	
	/* Testcase P3.4 - Left: paragraph deleted - Right: text additions in deleted paragraph */
	
	/**
	 * Testcase P3.4 - Origin version, contains:
	 * 
	 * <ul><li>2 paragraphs with text</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphOrigin()
	 * @see #getAdditionsInDeletedParagraphLeft()
	 * @see #getAdditionsInDeletedParagraphRight()
	 * @see #getAdditionsInDeletedParagraphResult()
	 */
	public String getAdditionsInDeletedParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/origin.html");
	}
	
	/**
	 *  Testcase P3.4 - Left version, changes:
	 * 
	 * <ul><li>Second paragraph deleted</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphOrigin()
	 * @see #getAdditionsInDeletedParagraphLeft()
	 * @see #getAdditionsInDeletedParagraphRight()
	 * @see #getAdditionsInDeletedParagraphResult()
	 */
	public String getAdditionsInDeletedParagraphLeft() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/left.html");
	}
	
	/**
	 * Testcase P3.4 - Right version, changes:
	 * 
	 * <ul><li>Text deletion in second paragraph</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphOrigin()
	 * @see #getAdditionsInDeletedParagraphLeft()
	 * @see #getAdditionsInDeletedParagraphRight()
	 * @see #getAdditionsInDeletedParagraphResult()
	 */
	public String getAdditionsInDeletedParagraphRight() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/right.html");
	}
	
	/**
	 * Testcase P3.4 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getAdditionsInDeletedParagraphOrigin()
	 * @see #getAdditionsInDeletedParagraphLeft()
	 * @see #getAdditionsInDeletedParagraphRight()
	 * @see #getAdditionsInDeletedParagraphResult()
	 */
	public String getAdditionsInDeletedParagraphResult() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/result.html");
	}
	
	/* Testcase P3.5 - Left: paragraph deleted - Right: text deletions in deleted paragraph */
	
	/**
	 * Testcase P3.5 - Origin version, contains:
	 * 
	 * <ul><li>2 paragraphs with text</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphOrigin()
	 * @see #getDeletionsInDeletedParagraphLeft()
	 * @see #getDeletionsInDeletedParagraphRight()
	 * @see #getDeletionsInDeletedParagraphResult()
	 */
	public String getDeletionsInDeletedParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/origin.html");
	}

	/**
	 * Testcase P3.5 - Left version, changes:
	 * 
	 * <ul><li>Second paragraph deleted</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphOrigin()
	 * @see #getDeletionsInDeletedParagraphLeft()
	 * @see #getDeletionsInDeletedParagraphRight()
	 * @see #getDeletionsInDeletedParagraphResult()
	 */
	public String getDeletionsInDeletedParagraphLeft() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/left.html");
	}
	
	/**
	 * Testcase P3.5 - Right version, changes:
	 * 
	 * <ul><li>Text additions to second paragraph</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphOrigin()
	 * @see #getDeletionsInDeletedParagraphLeft()
	 * @see #getDeletionsInDeletedParagraphRight()
	 * @see #getDeletionsInDeletedParagraphResult()
	 */
	public String getDeletionsInDeletedParagraphRight() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/right.html");
	}
	
	/**
	 * Testcase P3.5 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getDeletionsInDeletedParagraphOrigin()
	 * @see #getDeletionsInDeletedParagraphLeft()
	 * @see #getDeletionsInDeletedParagraphRight()
	 * @see #getDeletionsInDeletedParagraphResult()
	 */
	public String getDeletionsInDeletedParagraphResult() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/result.html");
	}
	
	/* Testcase P4.1 - modification in moved paragraph  */
	
	/**
	 * Testcase P4.1 - Origin version
	 * 
	 * @see #getChangeMovedParagraphOrigin()
	 * @see #getChangeMovedParagraphLeft()
	 * @see #getChangeMovedParagraphRight()
	 * @see #getChangeMovedParagraphResult()
	 */
	public String getChangeMovedParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/origin.html");
	}

	/**
	 * Testcase P4.1 - Left version, changes:
	 * 
	 * <ul><li>Moves second paragraph at the end</li></ul>
	 * 
	 * @see #getChangeMovedParagraphOrigin()
	 * @see #getChangeMovedParagraphLeft()
	 * @see #getChangeMovedParagraphRight()
	 * @see #getChangeMovedParagraphResult()
	 */
	public String getChangeMovedParagraphLeft() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/left.html");
	}

	/**
	 * Testcase P4.1 - Right version, changes:
	 * 
	 * <ul>
	 * <li>Text modification in second paragraph</li>
	 * <li>Text modification in last paragraph</li>
	 * </ul>
	 * 
	 * @see #getChangeMovedParagraphOrigin()
	 * @see #getChangeMovedParagraphLeft()
	 * @see #getChangeMovedParagraphRight()
	 * @see #getChangeMovedParagraphResult()
	 */
	public String getChangeMovedParagraphRight() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/right.html");
	}
	
	/**
	 * Testcase P4.1 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getChangeMovedParagraphOrigin()
	 * @see #getChangeMovedParagraphLeft()
	 * @see #getChangeMovedParagraphRight()
	 * @see #getChangeMovedParagraphResult()
	 */
	public String getChangeMovedParagraphResult() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/result.html");
	}
	
	/* 
	 * Table testcases
	 * ===============
	 */
	
	/* Testcase T1.1 - Additions in different paragraphs within a table cell */
	
	/**
	 * Testcase T1.1 - Origin version
	 * 
	 * @see #getChangeInTableSameCellOrigin()
	 * @see #getChangeInTableSameCellLeft()
	 * @see #getChangeInTableSameCellRight()
	 * @see #getChangeInTableSameCellResult()
	 */
	public String getChangeInTableSameCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/additions/origin.html");
	}

	/**
	 * Testcase T1.1 - Left version, changes:
	 * 
	 * <ul><li>Text addition in first paragraph in first cell</li></ul>
	 * 
	 * @see #getChangeInTableSameCellOrigin()
	 * @see #getChangeInTableSameCellLeft()
	 * @see #getChangeInTableSameCellRight()
	 * @see #getChangeInTableSameCellResult()
	 */
	public String getChangeInTableSameCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/additions/left.html");
	}

	/**
	 * Testcase T1.1 - Right version, changes:
	 * 
	 * <ul><li>Text addition in second paragraph in first cell</li></ul>
	 * 
	 * @see #getChangeInTableSameCellOrigin()
	 * @see #getChangeInTableSameCellLeft()
	 * @see #getChangeInTableSameCellRight()
	 * @see #getChangeInTableSameCellResult()
	 */
	public String getChangeInTableSameCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/additions/right.html");
	}
	
	/**
	 * Testcase T1.1 - Merged version
	 * 
	 * 
	 * @see #getChangeInTableSameCellOrigin()
	 * @see #getChangeInTableSameCellLeft()
	 * @see #getChangeInTableSameCellRight()
	 * @see #getChangeInTableSameCellResult()
	 */
	public String getChangeInTableSameCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/additions/result.html");
	}
	
	/* Testcase T1.2 - Deletions in different paragraphs within a table cell */
	
	/**
	 * Testcase T1.2 - Origin version
	 * 
	 * @see #getDeletionsInTableSameCellOrigin()
	 * @see #getDeletionsInTableSameCellLeft()
	 * @see #getDeletionsInTableSameCellRight()
	 * @see #getDeletionsInTableSameCellResult()
	 */
	public String getDeletionsInTableSameCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/deletions/origin.html");
	}

	/**
	 * Testcase T1.2 - Left version, changes:
	 * 
	 * <ul><li>Text deletion in first paragraph in first cell</li></ul>
	 * 
	 * @see #getDeletionsInTableSameCellOrigin()
	 * @see #getDeletionsInTableSameCellLeft()
	 * @see #getDeletionsInTableSameCellRight()
	 * @see #getDeletionsInTableSameCellResult()
	 */
	public String getDeletionsInTableSameCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/deletions/left.html");
	}

	/**
	 * Testcase T1.2 - Right version, changes:
	 * 
	 * <ul><li>Text deletion in second paragraph in first cell</li></ul>
	 * 
	 * @see #getDeletionsInTableSameCellOrigin()
	 * @see #getDeletionsInTableSameCellLeft()
	 * @see #getDeletionsInTableSameCellRight()
	 * @see #getDeletionsInTableSameCellResult()
	 */
	public String getDeletionsInTableSameCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/deletions/right.html");
	}
	
	/**
	 * Testcase T1.2 - Merged version
	 * 
	 * @see #getDeletionsInTableSameCellOrigin()
	 * @see #getDeletionsInTableSameCellLeft()
	 * @see #getDeletionsInTableSameCellRight()
	 * @see #getDeletionsInTableSameCellResult()
	 */
	public String getDeletionsInTableSameCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/deletions/result.html");
	}
	
	/* Testcase T1.3 - Addition (left) & deletion (right) in different paragraphs within a table cell */
	
	/**
	 * Testcase T1.3 - Origin version
	 * 
	 * @see #getAdditionDeletionInTableSameCellOrigin()
	 * @see #getAdditionDeletionInTableSameCellLeft()
	 * @see #getAdditionDeletionInTableSameCellRight()
	 * @see #getAdditionDeletionInTableSameCellResult()
	 */
	public String getAdditionDeletionInTableSameCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/additiondeletion/origin.html");
	}

	/**
	 * Testcase T1.3 - Left version, changes:
	 * 
	 * <ul><li>Text addition in first paragraph in first cell</li></ul>
	 * 
	 * @see #getAdditionDeletionInTableSameCellOrigin()
	 * @see #getAdditionDeletionInTableSameCellLeft()
	 * @see #getAdditionDeletionInTableSameCellRight()
	 * @see #getAdditionDeletionInTableSameCellResult()
	 */
	public String getAdditionDeletionInTableSameCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/additiondeletion/left.html");
	}

	/**
	 * Testcase T1.3 - Right version, changes:
	 * 
	 * <ul><li>Text deletion in second paragraph in first cell</li></ul>
	 * 
	 * @see #getAdditionDeletionInTableSameCellOrigin()
	 * @see #getAdditionDeletionInTableSameCellLeft()
	 * @see #getAdditionDeletionInTableSameCellRight()
	 * @see #getAdditionDeletionInTableSameCellResult()
	 */
	public String getAdditionDeletionInTableSameCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/additiondeletion/right.html");
	}
	
	/**
	 * Testcase T1.3 - Merged version
	 * 
	 * @see #getAdditionDeletionInTableSameCellOrigin()
	 * @see #getAdditionDeletionInTableSameCellLeft()
	 * @see #getAdditionDeletionInTableSameCellRight()
	 * @see #getAdditionDeletionInTableSameCellResult()
	 */
	public String getAdditionDeletionInTableSameCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/additiondeletion/result.html");
	}
	
	/* Testcase T2.1 - Insertion of new paragraphs at different locations within a table cell */
	
	/**
	 * Testcase T2.1 - Origin version
	 * 
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtDifferentLocationInSameTableCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/differentlocation/origin.html");
	}

	/**
	 * Testcase T2.1 - Left version, changes:
	 * 
	 * <ul><li>New paragraph after first paragraph</li></ul>
	 * 
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtDifferentLocationInSameTableCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/differentlocation/left.html");
	}

	/**
	 * Testcase T2.1 - Right version, changes:
	 * 
	 * <ul><li>New paragraph after second paragraph</li></ul>
	 * 
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtDifferentLocationInSameTableCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/differentlocation/right.html");
	}
	
	/**
	 * Testcase T2.1 - Merged version
	 * 
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtDifferentLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtDifferentLocationInSameTableCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/differentlocation/result.html");
	}

	/* Testcase T2.2 - Insertion of new paragraphs at the same location within a table cell */
	
	/**
	 * Testcase T2.2 - Origin version
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/samelocation/origin.html");
	}

	/**
	 * Testcase T2.2 - Left version, changes:
	 * 
	 * <ul><li>New paragraph after first paragraph, with different content as the right version</li></ul>
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/samelocation/left.html");
	}

	/**
	 * Testcase T2.2 - Right version, changes:
	 * 
	 * <ul><li>New paragraph after first paragraph, with different content as the left version</li></ul>
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/samelocation/right.html");
	}
	
	/**
	 * Testcase T2.2 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/samelocation/result.html");
	}

	/* Testcase T2.3 - Insertion of new paragraphs and same content (Pseudoconflict) at the same location within a table cell */
	
	/**
	 * Testcase T2.3 - Origin version
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/pseudoconflict/origin.html");
	}

	/**
	 * Testcase T2.3 - Left version, changes:
	 * 
	 * <ul><li>New paragraph after first paragraph, with the same content as the right version</li></ul>
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/pseudoconflict/left.html");
	}

	/**
	 * Testcase T2.3 - Right version, changes:
	 * 
	 * <ul><li>New paragraph after first paragraph, with the same content as the left version</li></ul>
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/pseudoconflict/right.html");
	}
	
	/**
	 * Testcase T2.3 - Merged version
	 * 
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictOrigin()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictLeft()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictRight()
	 * @see #getInsertParagraphAtSameLocationInSameTableCellPseudoconflictResult()
	 */
	public String getInsertParagraphAtSameLocationInSameTableCellPseudoconflictResult() throws IOException {
		return readFile("data/table/changeinsamecell/insertparagraph/pseudoconflict/result.html");
	}
	
	/* Testcase T2.4 - Text addition in deleted paragraph */
	
	/**
	 * Testcase T2.4 - Origin version
	 * 
	 * @see #getAdditionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellRight()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getAdditionsInDeletedParagraphInSameTableCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/addition/origin.html");
	}

	/**
	 * Testcase T2.4 - Left version, changes:
	 * 
	 * <ul><li>Deletes second paragraph</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellRight()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getAdditionsInDeletedParagraphInSameTableCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/addition/left.html");
	}

	/**
	 * Testcase T2.4 - Right version, changes:
	 * 
	 * <ul><li>Text additions in second paragraph</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellRight()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getAdditionsInDeletedParagraphInSameTableCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/addition/right.html");
	}

	/**
	 * Testcase T2.4 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getAdditionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellRight()
	 * @see #getAdditionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getAdditionsInDeletedParagraphInSameTableCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/addition/result.html");
	}
	

	/* Testcase T2.5 - Text addition in deleted paragraph */
	
	/**
	 * Testcase T2.5 - Origin version
	 * 
	 * @see #getDeletionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellRight()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getDeletionsInDeletedParagraphInSameTableCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/deletion/origin.html");
	}

	/**
	 * Testcase T2.5 - Left version, changes:
	 * 
	 * <ul><li>Deletes second paragraph</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellRight()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getDeletionsInDeletedParagraphInSameTableCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/deletion/left.html");
	}

	/**
	 * Testcase T2.5 - Right version, changes:
	 * 
	 * <ul><li>Text deletions in second paragraph</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellRight()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getDeletionsInDeletedParagraphInSameTableCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/deletion/right.html");
	}
	
	/**
	 * Testcase T2.5 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getDeletionsInDeletedParagraphInSameTableCellOrigin()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellLeft()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellRight()
	 * @see #getDeletionsInDeletedParagraphInSameTableCellResult()
	 */
	public String getDeletionsInDeletedParagraphInSameTableCellResult() throws IOException {
		return readFile("data/table/changeinsamecell/changedeletedparagraph/deletion/result.html");
	}
	
	/* Testcase T3.1 - Insert column (left), remove column (right) */
	
	/**
	 * Testcase T3.1 - Origin version
	 * 
	 * @see #getAddRemoveTableColumnOrigin()
	 * @see #getAddRemoveTableColumnLeft()
	 * @see #getAddRemoveTableColumnRight()
	 * @see #getAddRemoveTableColumnResult()
	 */
	public String getAddRemoveTableColumnOrigin() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/origin.html");
	}
	
	/**
	 * Testcase T3.1 - Left version, changes:
	 * 
	 * <ul><li>Adds column</li></ul>
	 * 
	 * @see #getAddRemoveTableColumnOrigin()
	 * @see #getAddRemoveTableColumnLeft()
	 * @see #getAddRemoveTableColumnRight()
	 * @see #getAddRemoveTableColumnResult()
	 */
	public String getAddRemoveTableColumnLeft() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/left.html");
	}
	
	/**
	 * Testcase T3.1 - Right version, changes:
	 * 
	 * <ul><li>Removes column</li></ul>
	 * 
	 * @see #getAddRemoveTableColumnOrigin()
	 * @see #getAddRemoveTableColumnLeft()
	 * @see #getAddRemoveTableColumnRight()
	 * @see #getAddRemoveTableColumnResult()
	 */
	public String getAddRemoveTableColumnRight() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/right.html");
	}
	
	/**
	 * Testcase T3.1 - Merged version:
	 * 
	 * 
	 * @see #getAddRemoveTableColumnOrigin()
	 * @see #getAddRemoveTableColumnLeft()
	 * @see #getAddRemoveTableColumnRight()
	 * @see #getAddRemoveTableColumnResult()
	 */
	public String getAddRemoveTableColumnResult() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/result.html");
	}

	/* Testcase T3.2 - Insert row (right), remove row (left) */
	
	/**
	 * Testcase T3.2 - Origin version
	 * 
	 * @see #getAddRemoveTableRowOrigin()
	 * @see #getAddRemoveTableRowLeft()
	 * @see #getAddRemoveTableRowRight()
	 * @see #getAddRemoveTableRowResult()
	 */
	public String getAddRemoveTableRowOrigin() throws IOException {
		return readFile("data/table/addrowremoverow/origin.html");
	}
	
	/**
	 * Testcase T3.2 - Left version, changes:
	 * 
	 * <ul><li>removes second row</li></ul>
	 * 
	 * @see #getAddRemoveTableRowOrigin()
	 * @see #getAddRemoveTableRowLeft()
	 * @see #getAddRemoveTableRowRight()
	 * @see #getAddRemoveTableRowResult()
	 */
	public String getAddRemoveTableRowLeft() throws IOException {
		return readFile("data/table/addrowremoverow/left.html");
	}
	
	/**
	 * Testcase T3.2 - Right version, changes:
	 * 
	 * <ul><li>adds row at the end</li></ul>
	 * 
	 * @see #getAddRemoveTableRowOrigin()
	 * @see #getAddRemoveTableRowLeft()
	 * @see #getAddRemoveTableRowRight()
	 * @see #getAddRemoveTableRowResult()
	 */
	public String getAddRemoveTableRowRight() throws IOException {
		return readFile("data/table/addrowremoverow/right.html");
	}
	
	/**
	 * Testcase T3.2 - Merged version, changes:
	 * 
	 * <ul><li>adds row at the end</li></ul>
	 * 
	 * @see #getAddRemoveTableRowOrigin()
	 * @see #getAddRemoveTableRowLeft()
	 * @see #getAddRemoveTableRowRight()
	 * @see #getAddRemoveTableRowResult()
	 */
	public String getAddRemoveTableRowResult() throws IOException {
		return readFile("data/table/addrowremoverow/result.html");
	}

	/* Testcase T3.3 - Insert column (left), remove row (right) */
	
	/**
	 * Testcase T3.3 - Origin version
	 * 
	 * @see #getAddColumnRemoveTableRowOrigin()
	 * @see #getAddColumnRemoveTableRowLeft()
	 * @see #getAddColumnRemoveTableRowRight()
	 * @see #getAddColumnRemoveTableRowResult()
	 */
	public String getAddColumnRemoveTableRowOrigin() throws IOException {
		return readFile("data/table/addcolumnremoverow/origin.html");
	}
	
	/**
	 * Testcase T3.3 - Left version, changes:
	 * 
	 * <ul><li>Insert new column after last column</li><ul>
	 * 
	 * @see #getAddColumnRemoveTableRowOrigin()
	 * @see #getAddColumnRemoveTableRowLeft()
	 * @see #getAddColumnRemoveTableRowRight()
	 * @see #getAddColumnRemoveTableRowResult()
	 */
	public String getAddColumnRemoveTableRowLeft() throws IOException {
		return readFile("data/table/addcolumnremoverow/left.html");
	}
	
	/**
	 * Testcase T3.3 - Right version, changes:
	 * 
	 * <ul><li>Removes last row</li><ul> 
	 * 
	 * @see #getAddColumnRemoveTableRowOrigin()
	 * @see #getAddColumnRemoveTableRowLeft()
	 * @see #getAddColumnRemoveTableRowRight()
	 * @see #getAddColumnRemoveTableRowResult()
	 */
	public String getAddColumnRemoveTableRowRight() throws IOException {
		return readFile("data/table/addcolumnremoverow/right.html");
	}
	
	/**
	 * Testcase T3.3 - Merged version, conflict resolved by using right version of conflicting elements
	 * 
	 * @see #getAddColumnRemoveTableRowOrigin()
	 * @see #getAddColumnRemoveTableRowLeft()
	 * @see #getAddColumnRemoveTableRowRight()
	 * @see #getAddColumnRemoveTableRowResult()
	 */
	public String getAddColumnRemoveTableRowResult() throws IOException {
		return readFile("data/table/addcolumnremoverow/result.html");
	}

	
	private String readFile(String filename) throws IOException {
		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.richtext.diff.test");
		URL resolvedURL = FileLocator.resolve(bundle.getEntry(filename));
		
		File file = new File(resolvedURL.getPath());
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

}