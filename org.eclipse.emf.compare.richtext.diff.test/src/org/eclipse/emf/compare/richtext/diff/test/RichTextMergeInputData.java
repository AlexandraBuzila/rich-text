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

	public String getChangeInTableSameCellOrigin() throws IOException {
		return readFile("data/table/changeinsamecell/origin.html");
	}

	public String getChangeInTableSameCellLeft() throws IOException {
		return readFile("data/table/changeinsamecell/left.html");
	}

	public String getChangeInTableSameCellRight() throws IOException {
		return readFile("data/table/changeinsamecell/right.html");
	}

	public String getAddRemoveTableColumnOrigin() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/origin.html");
	}

	public String getAddRemoveTableColumnLeft() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/left.html");
	}

	public String getAddRemoveTableColumnRight() throws IOException {
		return readFile("data/table/addcolumnremovecolumn/right.html");
	}

	public String getAddRemoveTableRowOrigin() throws IOException {
		return readFile("data/table/addrowremoverow/origin.html");
	}

	public String getAddRemoveTableRowLeft() throws IOException {
		return readFile("data/table/addrowremoverow/left.html");
	}

	public String getAddRemoveTableRowRight() throws IOException {
		return readFile("data/table/addrowremoverow/right.html");
	}

	public String getAddColumnRemoveTableRowOrigin() throws IOException {
		return readFile("data/table/addcolumnremoverow/origin.html");
	}

	public String getAddColumnRemoveTableRowLeft() throws IOException {
		return readFile("data/table/addcolumnremoverow/left.html");
	}

	public String getAddColumnRemoveTableRowRight() throws IOException {
		return readFile("data/table/addcolumnremoverow/right.html");
	}

	public String getChangeSameParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changesameparagraph/origin.html");
	}

	public String getChangeSameParagraphLeft() throws IOException {
		return readFile("data/paragraph/changesameparagraph/left.html");
	}

	public String getChangeSameParagraphRight() throws IOException {
		return readFile("data/paragraph/changesameparagraph/right.html");
	}
	
	/* Left: paragraph deleted - Right: text additions in deleted paragraph */
	
	/**
	 * Origin version, contains:
	 * 
	 * <ul><li>2 paragraphs with text</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphLeft()
	 * @see #getAdditionsInDeletedParagraphRight()
	 */
	public String getAdditionsInDeletedParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/origin.html");
	}
	
	/**
	 * Left version, changes:
	 * 
	 * <ul><li>Second paragraph deleted</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphOrigin()
	 * @see #getAdditionsInDeletedParagraphRight()
	 */
	public String getAdditionsInDeletedParagraphLeft() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/left.html");
	}
	
	/**
	 * Right version, changes:
	 * 
	 * <ul><li>Text deletion in second paragraph</li></ul>
	 * 
	 * @see #getAdditionsInDeletedParagraphOrigin()
	 * @see #getAdditionsInDeletedParagraphLeft()
	 */
	public String getAdditionsInDeletedParagraphRight() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/addition/right.html");
	}
	
	/* Left: paragraph deleted - Right: text deletions in deleted paragraph */
	
	/**
	 * Origin version, contains:
	 * 
	 * <ul><li>2 paragraphs with text</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphLeft()
	 * @see #getDeletionsInDeletedParagraphRight()
	 */
	public String getDeletionsInDeletedParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/origin.html");
	}

	/**
	 * Left version, changes:
	 * 
	 * <ul><li>Second paragraph deleted</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphOrigin()
	 * @see #getDeletionsInDeletedParagraphRight()
	 */
	public String getDeletionsInDeletedParagraphLeft() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/left.html");
	}
	
	/**
	 * Right version, changes:
	 * 
	 * <ul><li>Text additions to second paragraph</li></ul>
	 * 
	 * @see #getDeletionsInDeletedParagraphOrigin()
	 * @see #getDeletionsInDeletedParagraphLeft()
	 */
	public String getDeletionsInDeletedParagraphRight() throws IOException {
		return readFile("data/paragraph/changedeletedparagraph/deletion/right.html");
	}
	

	public String getInsertParagraphSameLocationOrigin() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/origin.html");
	}

	public String getInsertParagraphSameLocationLeft() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/left.html");
	}

	public String getInsertParagraphSameLocationRight() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/right.html");
	}

	public String getInsertParagraphSameLocationPseudoconflictOrigin() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/origin.html");
	}

	public String getInsertParagraphSameLocationPseudoconflictLeft() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/left.html");
	}

	public String getInsertParagraphSameLocationPseudoconflictRight() throws IOException {
		return readFile("data/paragraph/insertparagraphssamelocation/pseudoconflict/right.html");
	}

	public String getInsertParagraphDifferentLocationOrigin() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/origin.html");
	}

	public String getInsertParagraphDifferentLocationLeft() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/left.html");
	}

	public String getInsertParagraphDifferentLocationRight() throws IOException {
		return readFile("data/paragraph/insertparagraphsdifferentlocation/right.html");
	}

	public String getChangeDifferentParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/origin.html");
	}

	public String getChangeDifferentParagraphLeft() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/left.html");
	}

	public String getChangeDifferentParagraphRight() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/right.html");
	}

	public String getChangeDifferentParagraphResult() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/result.html");
	}

	public String getChangeMovedParagraphOrigin() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/origin.html");
	}

	public String getChangeMovedParagraphLeft() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/left.html");
	}

	public String getChangeMovedParagraphRight() throws IOException {
		return readFile("data/paragraph/changemovedparagraph/right.html");
	}

	/* Changes in different paragraphs with one addition (left) and deletion (right)  */

	/**
	 * Origin version, contains:
	 * 
	 * <ul><li>2 paragraphs with text</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionLeft()
	 * @see #getChangeDifferentParagraphAdditionDeletionRight()
	 */
	public String getChangeDifferentParagraphAdditionDeletionOrigin() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/origin.html");
	}

	/**
	 * Right version, changes:
	 * 
	 * <ul><li>Text added to first paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionOrigin()
	 * @see #getChangeDifferentParagraphAdditionDeletionLeft()
	 */
	public String getChangeDifferentParagraphAdditionDeletionRight() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/right.html");
	}

	/**
	 * Left version, changes:
	 * 
	 * <ul><li>Text deleted from first paragraph</li></ul>
	 * 
	 * @see #getChangeDifferentParagraphAdditionDeletionOrigin()
	 * @see #getChangeDifferentParagraphAdditionDeletionRight()
	 */
	public String getChangeDifferentParagraphAdditionDeletionLeft() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/left.html");
	}

	/**
	 * expected merged version of
	 * {@link #getChangeDifferentParagraphAdditionDeletionLeft()},
	 * {@link #getChangeDifferentParagraphAdditionDeletionRight()} and
	 * {@link #getChangeDifferentParagraphAdditionDeletionOrigin()}
	 * 
	 */
	public String getChangeDifferentParagraphAdditionDeletionResult() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/additiondeletion/result.html");
	}
	
	public String getChangeDifferentParagraphDeletionsOrigin() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/origin.html");
	}

	public String getChangeDifferentParagraphDeletionsRight() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/right.html");
	}

	public String getChangeDifferentParagraphDeletionsLeft() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/left.html");
	}

	public String getChangeDifferentParagraphDeletionsResult() throws IOException {
		return readFile("data/paragraph/changedifferentparagraph/deletions/result.html");
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