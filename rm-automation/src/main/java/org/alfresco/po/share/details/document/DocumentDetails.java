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

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.DetailsPage;
import org.alfresco.po.share.site.CollaborationSiteNavigation;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author David Webster
 * @since 2.4.a
 */
@Component
public class DocumentDetails extends DetailsPage<CollaborationSiteNavigation>
{
    @Autowired
    @RenderableChild
    private DocumentActionsPanel documentActionsPanel;
    @Autowired
    @RenderableChild
    private SocialActions socialActions;
    @Autowired
    @RenderableChild
    private VersionHistory versionHistory;
    @Autowired
    private DocumentLibrary documentLibrary;
    

    public DocumentActionsPanel getDocumentActionsPanel()
    {
        return documentActionsPanel;
    }

    public SocialActions getSocialActions()
    {
        return socialActions;
    }

    public VersionHistory getVersionHistory()
    {
        return versionHistory;
    }
    
    public boolean isPreviewAvailable()
    {
        try 
        {
            Utils.getWebDriver().findElement(By.cssSelector("div[id*='document-details'] .message"));
        } 
            catch (NoSuchElementException e)
        {
            return true;
        }
        return false;
    }       
    
    public DocumentLibrary navigateUpToDocumentsBrowseView()
    {
        Utils.waitForFind(By.cssSelector(".node-info .folder-link a[href $='documentlibrary']")).click();  
        Utils.waitFor(ExpectedConditions.visibilityOfElementLocated(By.id("alfresco-documentlibrary")));
        return documentLibrary.render();
    }        
    
    
}
