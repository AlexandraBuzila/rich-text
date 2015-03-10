package org.eclipse.emf.compare.richtext.diff;

import org.outerj.daisy.diff.html.modification.ModificationType;

public class RichTextThreeWayDiff {

	private RichTextDiff leftDiff;

	public RichTextDiff getLeftDiff() {
		return leftDiff;
	}

	public RichTextDiff getRightDiff() {
		return rightDiff;
	}

	private RichTextDiff rightDiff;

	public RichTextThreeWayDiff(RichTextDiff leftDiff, RichTextDiff rightDiff) {
		this.leftDiff = leftDiff;
		this.rightDiff = rightDiff;

	}

	public boolean isConflicting() {
		if (rightDiff == null || leftDiff == null)
			return false;
		if (rightDiff.getModification().getType() == ModificationType.CONFLICT
				|| leftDiff.getModification().getType() == ModificationType.CONFLICT)
			return true;
		if ((rightDiff.getModification().getType() == ModificationType.CHANGED)
				&& (leftDiff.getModification().getType() == ModificationType.CHANGED || leftDiff.getModification()
						.getType() == ModificationType.REMOVED)) {
			return true;
		}
		if ((rightDiff.getModification().getType() == ModificationType.CHANGED || rightDiff.getModification().getType() == ModificationType.REMOVED)
				&& (leftDiff.getModification().getType() == ModificationType.CHANGED)) {
			return true;
		}
		return false;
	}
}
