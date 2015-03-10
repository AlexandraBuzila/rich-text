/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.outerj.daisy.diff.rangedifferencer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.internal.CompareMessages;
import org.eclipse.compare.internal.core.LCS;
import org.eclipse.compare.rangedifferencer.IRangeComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.outerj.daisy.diff.helper.RangeDifferenceHelper;

@SuppressWarnings("restriction")
public class CustomRangeComparatorLCS extends LCS {

	private static double TOO_LONG = 0;
	private static double POW_LIMIT = 0;
	private final IRangeComparator comparator1, comparator2;
	private int[][] lcs;

	public static RangeDifference[] findDifferences(IProgressMonitor pm, IRangeComparator left, IRangeComparator right,
			double powLimit, double tooLong) {
		
		CustomRangeComparatorLCS lcs = new CustomRangeComparatorLCS(left, right);
		lcs.setPowLimit(powLimit);
		lcs.setTooLong(tooLong);
		
		SubMonitor monitor = SubMonitor.convert(pm, "", 100);
		try {
			lcs.longestCommonSubsequence(monitor.newChild(95));
			return lcs.getDifferences(monitor.newChild(5));
		} finally {
			if (pm != null)
				pm.done();
		}
	}

	public CustomRangeComparatorLCS(IRangeComparator comparator1, IRangeComparator comparator2) {
		this.comparator1 = comparator1;
		this.comparator2 = comparator2;
	}

	protected int getLength1() {
		return comparator1.getRangeCount();
	}

	protected int getLength2() {
		return comparator2.getRangeCount();
	}

	protected void initializeLcs(int lcsLength) {
		lcs = new int[2][lcsLength];
	}

	protected boolean isRangeEqual(int i1, int i2) {
		return comparator1.rangesEqual(i1, comparator2, i2);
	}

	protected void setLcs(int sl1, int sl2) {
		// Add one to the values so that 0 can mean that the slot is empty
		lcs[0][sl1] = sl1 + 1;
		lcs[1][sl1] = sl2 + 1;
	}

	/**
	 * Myers' algorithm for longest common subsequence. O((M + N)D) worst case
	 * time, O(M + N + D^2) expected time, O(M + N) space
	 * (http://citeseer.ist.psu.edu/myers86ond.html)
	 * 
	 * Note: Beyond implementing the algorithm as described in the paper I have
	 * added diagonal range compression which helps when finding the LCS of a
	 * very long and a very short sequence, also bound the running time to (N +
	 * M)^1.5 when both sequences are very long.
	 * 
	 * After this method is called, the longest common subsequence is available
	 * by calling getResult() where result[0] is composed of entries from l1 and
	 * result[1] is composed of entries from l2
	 * 
	 * @param subMonitor
	 */
	@Override
	public void longestCommonSubsequence(SubMonitor subMonitor) {
		int length1 = getLength1();
		int length2 = getLength2();
		if (length1 == 0 || length2 == 0) {
			setLength(0);
			return;
		}

		int max_diff = (length1 + length2 + 1) / 2;
		setMaxDifferences(max_diff); // ceil((N+M)/2)
		if ((double) length1 * (double) length2 > getTooLong()) {
			// limit complexity to D^POW_LIMIT for long sequences
			setMaxDifferences((int) Math.pow(max_diff, getPowLimit() - 1.0));
		}

		initializeLcs(length1);

		subMonitor.beginTask(null, length1);

		/*
		 * The common prefixes and suffixes are always part of some LCS, include
		 * them now to reduce our search space
		 */
		int forwardBound;
		int max = Math.min(length1, length2);
		for (forwardBound = 0; forwardBound < max && isRangeEqual(forwardBound, forwardBound); forwardBound++) {
			setLcs(forwardBound, forwardBound);
			worked(subMonitor, 1);
		}

		int backBoundL1 = length1 - 1;
		int backBoundL2 = length2 - 1;

		while (backBoundL1 >= forwardBound && backBoundL2 >= forwardBound && isRangeEqual(backBoundL1, backBoundL2)) {
			setLcs(backBoundL1, backBoundL2);
			backBoundL1--;
			backBoundL2--;
			worked(subMonitor, 1);
		}

		setLength(forwardBound
				+ length1
				- backBoundL1
				- 1
				+ getLcsRec(forwardBound, backBoundL1, forwardBound, backBoundL2, new int[2][length1 + length2 + 1],
						new int[3], subMonitor));

	}

	private int getLcsRec(int forwardBound, int backBoundL1, int forwardBound2, int backBoundL2, int[][] is, int[] is2,
			SubMonitor subMonitor) {
		try {
			Method declaredMethod = getClass().getSuperclass().getDeclaredMethod("lcs_rec", int.class, int.class,
					int.class, int.class, int[][].class, int[].class, SubMonitor.class);
			declaredMethod.setAccessible(true);
			int result = (Integer) declaredMethod.invoke(this, forwardBound, backBoundL1, forwardBound2, backBoundL2,
					is, is2, subMonitor);
			return result;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private void setMaxDifferences(int max_differences) {
		setPrivateFieldInSuperclass("max_differences", max_differences);
	}

	private void setLength(int length) {
		setPrivateFieldInSuperclass("length", length);
	}

	private void setPrivateFieldInSuperclass(String fieldName, Object value) {
		try {
			Field field = getClass().getSuperclass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set((LCS)this, value);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RangeDifference[] getDifferences(SubMonitor subMonitor) {
		try {
			List differences = new ArrayList();
			int length = getLength();
			if (length == 0) {
				differences.add(RangeDifferenceHelper.getRangeDifference(RangeDifference.CHANGE, 0, comparator2.getRangeCount(), 0,
						comparator1.getRangeCount()));
			} else {
				subMonitor.beginTask(null, length);
				int index1, index2;
				index1 = index2 = 0;
				int l1, l2;
				int s1 = -1;
				int s2 = -1;
				while (index1 < lcs[0].length && index2 < lcs[1].length) {
					// Move both LCS lists to the next occupied slot
					while ((l1 = lcs[0][index1]) == 0) {
						index1++;
						if (index1 >= lcs[0].length)
							break;
					}
					if (index1 >= lcs[0].length)
						break;
					while ((l2 = lcs[1][index2]) == 0) {
						index2++;
						if (index2 >= lcs[1].length)
							break;
					}
					if (index2 >= lcs[1].length)
						break;
					// Convert the entry to an array index (see setLcs(int,
					// int))
					int end1 = l1 - 1;
					int end2 = l2 - 1;
					if (s1 == -1 && (end1 != 0 || end2 != 0)) {
						// There is a diff at the beginning
						// TODO: We need to conform that this is the proper
						// order
						differences.add(RangeDifferenceHelper.getRangeDifference(RangeDifference.CHANGE, 0, end2, 0, end1));
					} else if (end1 != s1 + 1 || end2 != s2 + 1) {
						// A diff was found on one of the sides
						int leftStart = s1 + 1;
						int leftLength = end1 - leftStart;
						int rightStart = s2 + 1;
						int rightLength = end2 - rightStart;
						// TODO: We need to conform that this is the proper
						// order
						differences.add(RangeDifferenceHelper.getRangeDifference(RangeDifference.CHANGE, rightStart, rightLength, leftStart,
								leftLength));
					}
					s1 = end1;
					s2 = end2;
					index1++;
					index2++;
					worked(subMonitor, 1);
				}
				if (s1 != -1 && (s1 + 1 < comparator1.getRangeCount() || s2 + 1 < comparator2.getRangeCount())) {
					// TODO: we need to find the proper way of representing an
					// append
					int leftStart = s1 < comparator1.getRangeCount() ? s1 + 1 : s1;
					int rightStart = s2 < comparator2.getRangeCount() ? s2 + 1 : s2;
					// TODO: We need to conform that this is the proper order
					differences.add(RangeDifferenceHelper.getRangeDifference(RangeDifference.CHANGE, rightStart, comparator2.getRangeCount()
							- (s2 + 1), leftStart, comparator1.getRangeCount() - (s1 + 1)));
				}

			}
			return (RangeDifference[]) differences.toArray(new RangeDifference[differences.size()]);
		} finally {
			subMonitor.done();
		}
	}

	private void worked(SubMonitor subMonitor, int work) {
		if (subMonitor.isCanceled())
			throw new OperationCanceledException();
		subMonitor.worked(work);
	}

	public static double getTooLong() {
		return TOO_LONG;
	}

	public static double getPowLimit() {
		return POW_LIMIT;
	}

	public void setTooLong(double tooLong) {
		TOO_LONG = tooLong;
	}

	public void setPowLimit(double powLimit) {
		POW_LIMIT = powLimit;
	}

}
