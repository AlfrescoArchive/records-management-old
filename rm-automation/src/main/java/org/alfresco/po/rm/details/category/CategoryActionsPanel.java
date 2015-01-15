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
package org.alfresco.po.rm.details.category;

import org.alfresco.po.rm.browse.fileplan.CategoryActions;
import org.alfresco.po.share.panel.ActionPanel;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Category Actions Panel
 * @author Tatiana Kalinovskaya
 */
@Component
public class CategoryActionsPanel extends ActionPanel
        implements CategoryActions
{
    @FindBy(css="div.folder-actions h2")
    private WebElement clickableTitle;

    /**
     * @see org.alfresco.po.share.panel.Panel#getClickableTitle()
     */
    @Override
    protected WebElement getClickableTitle()
    {
        return clickableTitle;
    }
}

