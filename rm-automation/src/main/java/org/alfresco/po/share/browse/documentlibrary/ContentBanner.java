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
package org.alfresco.po.share.browse.documentlibrary;

import org.openqa.selenium.By;

/**
 * Banners that can appear over content in the document library.
 *
 * @author tpage
 * @since 3.0
 */
public enum ContentBanner
{
    /** A banner giving the classification of some content. */
    CLASSIFICATION(".classified-banner");

    private String cssSelector;

    private ContentBanner(String cssSelector)
    {
        this.cssSelector = cssSelector;
    }

    /**
     * Construct a selector for the banner type.
     *
     * @return A CSS selector matching the banner.
     */
    public By getSelector()
    {
        return By.cssSelector(cssSelector);
    }
}
