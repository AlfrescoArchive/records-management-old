/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package org.alfresco.po.share.details.document;

import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.panel.ActionPanel;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author David Webster
 */
@Component
public class DocumentActionsPanel extends ActionPanel implements DocumentActions
{
    @Autowired private ClassifyContentDialog classifyContentDialog;
    
    @FindBy(css = "div.document-actions h2")
    private WebElement clickableTitle;

    /**
     * @see org.alfresco.po.share.panel.Panel#getClickableTitle()
     */
    @Override
    protected WebElement getClickableTitle()
    {
        return clickableTitle;
    }
    
    /**
     * Click on classify content
     */
    public ClassifyContentDialog clickOnClassify()
    {
        return clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);
    }
}
