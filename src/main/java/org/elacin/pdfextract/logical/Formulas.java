/*
 * Copyright 2010 Øyvind Berg (elacin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elacin.pdfextract.logical;

import org.elacin.pdfextract.StyledText;

import java.util.Collection;

import static org.elacin.pdfextract.style.Style.GRAPHIC_CHARACTER;
import static org.elacin.pdfextract.style.Style.GRAPHIC_MATH_BAR;

/**
 * Created by IntelliJ IDEA. User: elacin Date: 29.11.10 Time: 01.30 To change this template use
 * File | Settings | File Templates.
 */
public class Formulas {
// -------------------------- PUBLIC STATIC METHODS --------------------------

public static boolean textContainsMath(StyledText text){
	if (text.getStyle().isMathFont()){
		return true;
	}

	for (int i = 0; i < text.getText().length(); i++){
		if (Character.getType(text.getText().codePointAt(i)) == Character.MATH_SYMBOL){
			return true;
		}
	}
	return false;
}

public static boolean textSeemsToBeFormula(Collection<? extends StyledText> texts) {
	if (texts.size() < 4) {
		return false;
	}

	int looksLikeMath = 0;
	int wordCount = 0;

	int containedGraphics = 0;
	for (StyledText word : texts) {
		if (word.getStyle().equals(GRAPHIC_MATH_BAR) || word.getStyle().equals(GRAPHIC_CHARACTER)) {
			containedGraphics++;
			continue;
		}

		wordCount += word.getText().length();

		/* first check whether the whole word seems to be formatted in a math font */
		if (word.getStyle().isMathFont()) {
			looksLikeMath += 3 * word.getText().length();
			continue;
		}

		for (int i = 0; i < word.getText().length(); i++) {
			final char c = word.getText().charAt(i);
			if (Character.getType(c) == (int) Character.MATH_SYMBOL) {
				looksLikeMath += 5;
			} else if (Character.isDigit(c)) {
				looksLikeMath += 2;
			}
		}
	}

	looksLikeMath += containedGraphics * looksLikeMath * 0.1f;

	return looksLikeMath > wordCount;
}
}