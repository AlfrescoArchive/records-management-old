/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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

import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * The social actions available at the top of the document details page and underneath each document on the document
 * library page.
 *
 * @author tpage
 * @since 3.0
 */
@Component
public class SocialActions extends Renderable
{
    //@FindBy(css = ".node-social a[title='Share document']")
    @FindBy(css = ".node-social a.quickshare-action")
    private WebElement shareDocumentLink;

    /** Share the document. */
    public SocialActions clickShareDocument()
    {
        shareDocumentLink.click();
        return this;
    }
}
