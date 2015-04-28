/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff;

import org.outerj.daisy.diff.html.modification.ModificationType;

/** A three way rich text difference. */
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

	/**
	 * @return <code>true</code> if the left and right side contain conflicting
	 *         text changes.
	 */
	public boolean isConflicting() {
		if (rightDiff == null || leftDiff == null) {
			return false;
		}
		if (rightDiff.getModification().getType() == ModificationType.CONFLICT
				|| leftDiff.getModification().getType() == ModificationType.CONFLICT) {
			return true;
		}
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
