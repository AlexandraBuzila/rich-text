/**
 * 
 */
package org.eclipse.emf.compare.richtext.diff;

import javax.xml.transform.sax.TransformerHandler;

import org.outerj.daisy.diff.html.dom.ImageNode;
import org.outerj.daisy.diff.html.dom.Node;
import org.outerj.daisy.diff.html.dom.TagNode;
import org.outerj.daisy.diff.html.dom.TextNode;
import org.outerj.daisy.diff.output.DiffOutput;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Alexandra Buzila
 *
 */
public class StringOutputGenerator implements DiffOutput {

	private TransformerHandler handler;

	public StringOutputGenerator(TransformerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void generateOutput(TagNode node) throws SAXException {
		if (!node.getQName().equalsIgnoreCase("img") && !node.getQName().equalsIgnoreCase("body")) {
			handler.startElement("", node.getQName(), node.getQName(), node.getAttributes());
		}
		for (Node child : node) {
			if (child instanceof TagNode) {
				generateOutput(((TagNode) child));
			} else if (child instanceof TextNode) {
				TextNode textChild = (TextNode) child;
				char[] chars = textChild.getText().toCharArray();

				if (textChild instanceof ImageNode) {
					writeImage((ImageNode) textChild);
				} else {
					handler.characters(chars, 0, chars.length);
				}
			}
		}
		if (!node.getQName().equalsIgnoreCase("img") && !node.getQName().equalsIgnoreCase("body"))
			handler.endElement("", node.getQName(), node.getQName());
	}
	
    private void writeImage(ImageNode imgNode) throws SAXException {
        AttributesImpl attrs = imgNode.getAttributes();
        handler.startElement("", "img", "img", attrs);
        handler.endElement("", "img", "img");
    }
}
