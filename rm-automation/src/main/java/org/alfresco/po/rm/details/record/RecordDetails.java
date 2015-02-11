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
package org.alfresco.po.rm.details.record;

import java.util.List;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.rm.details.DisposableItemDetailsPage;
import org.alfresco.po.share.details.document.PropertyPanel;
import org.alfresco.po.share.details.document.SharePanel;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * @author Roy Wetherall
 */
@Component
public class RecordDetails extends DisposableItemDetailsPage
{
    @Autowired
    @RenderableChild
    private ActionsPanel recordActionsPanel;

    @Autowired
    @RenderableChild
    private SharePanel sharePanel;

    @Autowired
    @RenderableChild
    private PropertyPanel propertyPanel;

    @Autowired
    @RenderableChild
    private ReferencePanel referencePanel;

    @FindBy(css = ".onDownloadDocumentClick a")
    private List<Button> downloadButton;

    public ActionsPanel getRecordActionsPanel()
    {
        return recordActionsPanel;
    }

    public boolean isDownloadButtonPresent()
    {
        return downloadButton.size() != 0 && downloadButton.get(0).isDisplayed();
    }
}
