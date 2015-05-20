/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Florian Zoubek - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.richtext.diff.internal;

import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;

/**
 * Represents an empty text within an empty {@link TagNode}. This class exists
 * only to make empty tag nodes visible to daisyDiff, as its difference
 * computation is based on {@link TextNode}s.
 */
public class RTEmptyTextNode extends RTTextNode {

	public RTEmptyTextNode(TagNode parent) {
		super(parent, "");
	}

}
