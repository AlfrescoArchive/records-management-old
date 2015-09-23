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
package org.alfresco.po.share.search;

import java.util.List;

import org.alfresco.po.share.browse.ListItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search result object, representing a row in the search results.
 *
 * @author Roy Wetherall
 * @since 2.4.a
 */
@Scope("prototype")
@Component
public class SearchResult extends ListItem
{
    /** name selector */
    private static final By NAME_SELECTOR = By.cssSelector(".nameAndTitleCell [class*='PropertyLink'] span.value");

    /** classified label selector */
    private static final By CLASSIFIED_LABEL_SELECTOR = By.cssSelector(".classification-label");

    /** row web element */
    private WebElement row;

    /**
     * set the search result row
     */
    public void setSearchResultRow(WebElement row)
    {
        this.row = row;
    }

    public WebElement getSearchResultRow()
    {
        return row;
    }

    /**
     * get the name of the file or folder on this row of the search results
     */
    public String getName()
    {
        WebElement webElement = row.findElement(NAME_SELECTOR);
        return webElement.getText();
    }

    /**
     * Get the classified label for a result. Returns empty string if no label.
     */
    public String getClassifiedLabel()
    {
        String result = "";
        List<WebElement> webElements = row.findElements(CLASSIFIED_LABEL_SELECTOR);
        if (webElements.size() > 0)
        {
            result = webElements.get(0).getText();
        }

        return result;
    }

}
