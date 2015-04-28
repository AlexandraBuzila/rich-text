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

import java.util.ArrayList;

import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.modification.Modification;

public interface RTNode extends RTNodeMatcher{
	
	public Modification getModification();
	
	public ArrayList<Node> getListOfChildrenWithoutInsertions();

}
