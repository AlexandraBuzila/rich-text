/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *     Florian Zoubek
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.modification.Modification;

/**
 * A difference for {@link Node}s of a rich-text document.
 */
public class RichTextDiff {

	private Modification modification;

	private RichTextDifferenceState state;

	private Node child;

	public RichTextDifferenceState getState() {
		return state;
	}

	public void setState(RichTextDifferenceState state) {
		this.state = state;
	}

	public Modification getModification() {
		return modification;
	}

	public Node getChild() {
		return child;
	}

	public RichTextDiff(Node child, Modification mod) {
		this.modification = mod;
		this.child = child;
		this.state = RichTextDifferenceState.UNRESOLVED;
	}

	@Override
	public String toString() {
		return "RichTextDiff [modificationType=" + modification.getType() + ", state="
				+ state + ", child=" + child + "]";
	}

}
