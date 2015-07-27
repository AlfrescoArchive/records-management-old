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
package org.alfresco.po.rm.search;

import java.util.ArrayList;
import java.util.List;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * Records Search results page
 *
 * @author Oana Nechiforescu
 */
@Component
public class SearchRecordsResults extends SharePage 
{
    @Autowired
    private RecordsSearch recordsSearchPage;

    @FindBy(css = "a[href*='default-critera-tab']")
    private Link criteriaPage;

    @FindBy(css = "div[id*='_default-results']")
    private WebElement resultsContainer;

    @FindBy(css = "li[class='selected']>a[href*='default-critera-tab']")
    private Link criteriaPageEnabled;
    
    // selector used to retrieve the results names in the search results page
    final static String RESULTS_SELECTOR = "tbody[class='yui-dt-data'] tr";
   
    /**
     * get the search results from the results tab
     *
     * @return the list of search results
     */
    public List<RecordSearchResult> getResults()
    {
        List<RecordSearchResult> recordsResults= new ArrayList<>();
        Utils.waitFor(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(RESULTS_SELECTOR))); 
        List<WebElement> results = webDriver.findElements(By.cssSelector(RESULTS_SELECTOR));
        for (WebElement record : results) 
        {
            RecordSearchResult searchResult = new RecordSearchResult();
            searchResult.setSearchResultRow(record);
            recordsResults.add(searchResult);
        }
        return recordsResults;
    }

    /**
     * checks if the record name given as parameter is displayed in the Name
     * column from the search results
     *
     * @param recordName
     * @return true is the record name is in the search results Name column,
     * false if not
     */
    public boolean recordIsDisplayedInResults(String recordName)
    {
        for (RecordSearchResult searchResult : getResults()) 
        {
            if (searchResult.getName().startsWith(recordName)) 
            {
                return true;
            }
        }
        return false;
    }

    /**
     * navigates to the Criteria tab from the Results tab
     */
    public Renderable navigateToCriteriaTab() 
    {
        criteriaPage.click();
        Utils.waitForVisibilityOf(criteriaPageEnabled);
        return recordsSearchPage.render();
    }
    
    @Override
    protected String getPageURL(String... context) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
