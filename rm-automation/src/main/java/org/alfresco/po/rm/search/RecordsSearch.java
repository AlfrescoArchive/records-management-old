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

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * Records Search page
 *
 * @author Oana Nechiforescu
 */
@Component
public class RecordsSearch extends SharePage 
{
    @Autowired
    private SearchRecordsResults searchRecordsResults;

    @FindBy(css = "button[id*='default-savedsearches-button-button']")
    private Button savedSearches;

    @FindBy(css = "button[id*='default-search-button-button']")
    private Button searchButton;

    @FindBy(css = "li[class='selected']>a[href*='-results-tab']")
    private Link resultsPageEnabled;

    @FindBy(xpath = "//a[contains(text(),'" + SearchConstants.INCOMPLETE_RECORDS + "')]")
    private Button incompleteRecords;

    @FindBy(css = ".options")
    private Button resultsOptions;

    @FindBy(css = "textarea[id*='_default-terms']")
    private WebElement searchQueryTextarea;

    @FindBy(css = "div[id$='default-options']")
    private WebElement optionsContainer;

    @FindBy(css = "input[id*='default-metadata-" + SearchConstants.MODIFIER + "']")
    private CheckBox modifierCheckbox;

    @FindBy(css = "input[id*='default-metadata-" + SearchConstants.HAS_DISPOSITION_SCHEDULE + "']")
    private CheckBox hasDispositionScheduleCheckbox;

    @FindBy(css = "input[id*='default-" + SearchConstants.INCLUDE_INCOMPLETE + "']")
    private CheckBox includeIncompleteCheckbox;

    @FindBy(css = "input[id*='default-" + SearchConstants.SHOW_RECORD_CATEGORIES + "']")
    private CheckBox showRecordCategories;
    
    private final String SEARCH_URL = "/page/site/rm/rmsearch";

    /**
     * selects incomplete records from saved searches 
     */
    public void selectIncompleteRecordsSearch()
    {
        Utils.waitForVisibilityOf(savedSearches);
        savedSearches.click();
        Utils.waitForVisibilityOf(incompleteRecords);
        incompleteRecords.click();
    }
    
    /**
     * check or uncheck an option from Results options
     * @param option
     * @param checked 
     */
    public void checkResultsOption(String option, Boolean checked)
    {
        if (!optionsContainer.isDisplayed())
        {
            resultsOptions.click();
        }
        CheckBox check = getCheckOption(option);
        if (check != null) 
        {
            Utils.waitForVisibilityOf(check);
            if (checked) 
            {
                if (!check.isSelected()) {
                    check.select();
                }
            } 
            else 
            {
                if (check.isSelected()) {
                    check.select();
                }
            }
        }
    }
    
    /**
     * get the CheckBox that displays the given option
     * @param option
     * @return the CheckBox element from the page that is required
     */
    private CheckBox getCheckOption(String option) 
    {
        switch (option) 
        {
            case "Modifier":
                return modifierCheckbox;
            case "Has Disposition Schedule":
                return hasDispositionScheduleCheckbox;
            case "Include Incomplete":
                return includeIncompleteCheckbox;
            case "Show Record Categories":
                return showRecordCategories;
        }
        return null;
    }
    
    /**
     * click on the search button in order to start the search
     */
    public void clickOnSearch() 
    {
        Utils.waitForVisibilityOf(searchButton);
        searchButton.click();
        Utils.waitForVisibilityOf(resultsPageEnabled);
        searchRecordsResults.render();
    }

    @Override
    protected String getPageURL(String... context)
    {
       return SEARCH_URL;
    }
}
