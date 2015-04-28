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
package org.eclipse.emf.compare.richtext.diff.internal;

import org.outerj.daisy.diff.html.dom.Node;

/**
 * Interface for {@link Node} objects that need to compare instances of other nodes with themselves.
 * 
 * @author Alexandra Buzila
 */
public interface RTNodeMatcher {

	/**
	 * @return <code>true</code> if the node is equal to the current object.
	 */
	boolean isSameNode(Node node);
}
