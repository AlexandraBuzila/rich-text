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
package org.outerj.daisy.diff.helper;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.compare.rangedifferencer.RangeDifference;

/**
 * @author Alexandra Buzila
 *
 */
public class RangeDifferenceHelper {

	public static RangeDifference getRangeDifference(int change, int i, int rangeCount, int j, int rangeCount2) {
		try {
			Constructor<RangeDifference> constructor = RangeDifference.class.getDeclaredConstructor(int.class,
					int.class, int.class, int.class, int.class);
			constructor.setAccessible(true);
			RangeDifference diff = constructor.newInstance(change, i, rangeCount, j, rangeCount2);
			return diff;
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public static RangeDifference getRangeDifference(int kind, int rightStart, int rightLength, int leftStart,
			int leftLength, int ancestorStart, int ancestorLength) {
		try {
			Constructor<RangeDifference> constructor = RangeDifference.class.getDeclaredConstructor(int.class,
					int.class, int.class, int.class, int.class, int.class, int.class);
			constructor.setAccessible(true);
			RangeDifference diff = constructor.newInstance(kind, rightStart, rightLength, leftStart, leftLength,
					ancestorStart, ancestorLength);
			return diff;
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

}
