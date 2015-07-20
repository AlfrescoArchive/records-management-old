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

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Advanced search page.
 * 
 * @author Roy Wetherall
 */
@Component
public class AdvancedSearchPage extends SharePage
{
    /** base url */
    private static final String URL = "/page/advsearch";
    
    /** keywords text input */
    @FindBy(css="input[id$='default-search-text']")
    private TextInput keywordsTextInput;
    
    /** search button */
    @FindBy(css="button[id$='default-search-button-1-button']")
    private Button searchButton;
    
    /** search result page */
    @Autowired
    private SearchResultsPage searchResultsPage;

    /**
     * @see org.alfresco.po.share.page.SharePage#getPageURL(java.lang.String[])
     */
    @Override
    protected String getPageURL(String... context)
    {
        // TODO check for site context and adjust URL accordingly        
        return URL;
    }

    /**
     * set keywords
     */
    public AdvancedSearchPage setKeywords(String keywords)
    {
        Utils.clearAndType(keywordsTextInput, keywords);
        return this;
    }
    
    /**
     * Click on search button
     */
    public SearchResultsPage clickOnSearch()
    {
        searchButton.click();
        return searchResultsPage.render();
    }
    
}
