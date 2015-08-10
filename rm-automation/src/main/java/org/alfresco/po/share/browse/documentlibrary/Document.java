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
package org.alfresco.po.share.browse.documentlibrary;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.dialog.DeleteConfirmationDialog;
import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.rm.dialog.classification.EditClassifiedContentDialog;
import org.alfresco.po.share.browse.ListItem;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
import org.alfresco.po.share.details.document.DocumentDetails;
import org.alfresco.po.share.details.document.SocialActions;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Document list item
 *
 * @author Roy Wetherall
 */
@Scope("prototype")
@Component
public class Document extends ListItem implements DocumentActions
{
    @Autowired
    private DocumentDetails documentDetails;

    @Autowired
    private DeleteConfirmationDialog deleteConfirmationDialog;

    @Autowired
    @RenderableChild
    private SocialActions socialActions;

    @Autowired
    private DocumentLibrary documentLibrary;

    @Autowired
    private ClassifyContentDialog classifyContentDialog;

    @Autowired
    private EditClassifiedContentDialog editClassifiedContentDialog;

    /**
     * Click on declare as record action
     */
    public DocumentLibrary clickOnDeclareAsRecord()
    {
        return (DocumentLibrary)clickOnAction(ACTION_DECLARE_RECORD);
    }

    /**
     * Click on record details link
     */
    @Override
    public DocumentDetails clickOnLink()
    {
        return super.clickOnLink(documentDetails);
    }

    /**
     * Click on delete action
     */
    public DeleteConfirmationDialog clickOnDelete()
    {
        return clickOnAction(DELETE, deleteConfirmationDialog);
    }

    /**
     * Click on edit offline action
     */
    public DocumentLibrary clickOnEditOffline()
    {
        return clickOnAction(EDIT_OFFLINE, documentLibrary);
    }

    /**
     * Click on cancel edit action
     */
    public DocumentLibrary clickOnCancelEdit()
    {
        return clickOnAction(CANCEL_EDIT, documentLibrary);
    }

    /**
     * Return the social actions for a Document
     */
    public boolean isShareDocumentAvailable()
    {
        return Utils.elementExists(this.getRow(), By.cssSelector("a.quickshare-action"));
    }

    /**
     * Click on classify content
     */
    public ClassifyContentDialog clickOnClassify()
    {
        return clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);
    }

    /** Click on edit classification. */
    public EditClassifiedContentDialog clickOnEditClassification()
    {
        return clickOnAction(DocumentActionsPanel.EDIT_CLASSIFICATION, editClassifiedContentDialog);
    }
}
