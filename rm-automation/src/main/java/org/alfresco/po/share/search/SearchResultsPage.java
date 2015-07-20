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

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Faceted search results page.
 * 
 * @author Roy Wetherall
 * @since 3.0.a
 */
@Component
public class SearchResultsPage extends SharePage
{    
    /** bean name, used to create search results */
    private static final String BEAN_NAME = "searchResult";
    
    /** search result row selector */
    private static final By SEARCH_RESULT_ROW_SELECTOR = By.cssSelector("tr"); 
    
    /** search results */
    @FindBy(css = "#FCTSRCH_SEARCH_RESULTS_LIST")
    private WebElement searchResults;
    
    /** application context */
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * @see org.alfresco.po.share.page.SharePage#getPageURL(java.lang.String[])
     */
    @Override
    protected String getPageURL(String... context)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * get the search results
     */
    public List<SearchResult> getSearchResults()
    {
        List<WebElement> rows = searchResults.findElements(SEARCH_RESULT_ROW_SELECTOR);
        List<SearchResult> results = new ArrayList<SearchResult>(rows.size());
        for (WebElement row : rows)
        {
            // crate new search result
            SearchResult searchResult = (SearchResult)applicationContext.getBean(BEAN_NAME);
            searchResult.setSearchResultRow(row);
            
            // add search result
            results.add(searchResult);
        }
        
        return results;
    }    
}
