/**
 * 
 */
package org.eclipse.emf.compare.richtext.diff;

import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.html.modification.Modification;

/**
 * @author Alexandra Buzila
 *
 */
public class RichTextDiff {
	
	private Modification modification;
	public Modification getModification() {
		return modification;
	}

	public TextNode getChild() {
		return child;
	}

	private TextNode child;

	public RichTextDiff(TextNode child, Modification mod) {
		this.modification = mod;
		this.child = child;
	}

}
