/*
 * Copyright 2010 �yvind Berg (elacin@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.elacin.extract.tree;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: elacin
 * Date: Mar 24, 2010
 * Time: 12:17:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentNode extends AbstractParentNode<PageNode, DocumentNode> {
    private PDDocument pdf;

    public final List<TextNode> textNodes = new ArrayList<TextNode>();

    public DocumentNode(final PDDocument pdf) {
        this.pdf = pdf;
    }


    public PDDocument getPdf() {
        return pdf;
    }

    @Override
    public boolean addTextNode(final TextNode node) {
        for (PageNode pageNode : getChildren()) {
            if (pageNode.addTextNode(node)) return true;
        }

        final PageNode newPage = new PageNode(node.getPageNum());
        addChild(newPage);
        newPage.addTextNode(node);
        return false;
    }

    /**
     * Returns a Comparator which will compare pagenumbers of the pages
     *
     * @return
     */
    @Override
    public Comparator<PageNode> getChildComparator() {
        return new Comparator<PageNode>() {
            public int compare(final PageNode o1, final PageNode o2) {
                if (o1.getPage().getPageNumber() < o2.getPage().getPageNumber()) return -1;
                else if (o1.getPage().getPageNumber() > o2.getPage().getPageNumber()) return 1;

                return 0;
            }
        };
    }

    @Override
    public void combineChildren() {
        for (PageNode pageNode : getChildren()) {
            pageNode.combineChildren();
        }
    }
}
