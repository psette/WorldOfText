package game_logic;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class DocumentFilterWrap extends DocumentFilter implements Serializable {
	private static final long serialVersionUID = 4600919500143097900L;
	private Color currColor, currFill;
	private int currSize;
	String currFont;
	private boolean bold, italics, underline;

	public DocumentFilterWrap(Color currColor, Color currFill, int currSize, String currFont, boolean bold,
			boolean italics, boolean underline) {
		this.bold = bold;
		this.italics = italics;
		this.underline = underline;
		this.currFont = currFont;
		this.currSize = currSize;
		this.currColor = currColor;
	}

	@Override
	public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {

		SimpleAttributeSet newAttrs = new SimpleAttributeSet();
		StyleConstants.setForeground(newAttrs, currColor);
		StyleConstants.setBackground(newAttrs, currFill);
		StyleConstants.setFontSize(newAttrs, currSize);
		StyleConstants.setFontFamily(newAttrs, currFont);
		StyleConstants.setBold(newAttrs, bold);
		StyleConstants.setItalic(newAttrs, italics);
		StyleConstants.setUnderline(newAttrs, underline);

		if (fb == null) {
			System.out.println("fb null");
		}
		a = newAttrs;

		super.replace(fb, offs, length, str, a);

	}
}
