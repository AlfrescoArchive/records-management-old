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
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.search.SearchConstants.SavedSearch;
import org.alfresco.po.rm.search.SearchConstants.SearchOption;
import org.alfresco.po.rm.search.SearchConstants.SearchOptionType;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
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

    @Autowired
    private FilePlan filePlan;

    @FindBy(css = "button[id*='default-savedsearches-button-button']")
    private Button savedSearches;

    @FindBy(css = "button[id*='default-search-button-button']")
    private Button searchButton;

    @FindBy(css = "li[class='selected']>a[href*='-results-tab']")
    private Link resultsPageEnabled;

    @FindBy(css = ".options")
    private Button resultsOptions;

    @FindBy(css = "textarea[id$='_default-terms']")
    private WebElement searchQueryTextarea;

    @FindBy(css = "div[id$='default-options']")
    private WebElement optionsContainer;

    private final String SEARCH_URL = "/page/site/rm/rmsearch";

    /** Type into the search box. */
    public RecordsSearch setKeywords(String keywords)
    {
        Utils.clearAndType(searchQueryTextarea, "keywords:" + keywords);
        return this;
    }

    /**
     * selects saved search with the specified label
     * @param savedSearch
     */
    public void selectSavedRecordsSearch(SavedSearch savedSearch)
    {
        Utils.waitForVisibilityOf(savedSearches);
        savedSearches.click();
        Utils.waitForVisibilityOf(By.linkText(savedSearch.getSavedSearchLabel())).click();
    }

    /**
     * check or uncheck an option from Results options
     * @param option the option to check
     * @param type metadata or components section option
     * @param checked
     * @return The records search control.
     */
    public RecordsSearch checkResultsComponentsOption(SearchOption option, SearchOptionType type, boolean checked)
    {
        if (!optionsContainer.isDisplayed())
        {
            resultsOptions.click();
        }
        CheckBox check = getCheckOption(option, type);

        if (checked)
        {
            if (!check.isSelected())
            {
                check.select();
            }
        }
        else
        {
            if (check.isSelected())
            {
                check.select();
            }
        }

        return this;
    }

    /**
     * get the CheckBox that displays the given option
     * @param option
     * @return the CheckBox element from the page that is required
     */
    private CheckBox getCheckOption(SearchOption option, SearchOptionType type)
    {
        String selector = "input[id*='[optionType][optionName]']";
        if (type.equals(SearchOptionType.COMPONENTS))
        {
            selector = selector.replace("[optionType]", SearchOptionType.COMPONENTS.getSelector());
        }
        else if (type.equals(SearchOptionType.METADATA))
        {
            selector = selector.replace("[optionType]", SearchOptionType.METADATA.getSelector());
        }
        selector = selector.replace("[optionName]", option.getOptionSelector());
        return new CheckBox(Utils.waitForVisibilityOf(By.cssSelector(selector)));
    }

    /**
     * click on the search button in order to start the search
     * @return The search record results.
     */
    public SearchRecordsResults clickOnSearch()
    {
        Utils.waitForVisibilityOf(searchButton);
        searchButton.click();
        Utils.waitForVisibilityOf(resultsPageEnabled);
        return searchRecordsResults.render();
    }

    public FilePlan selectSavedSearchFromFilePlan(SavedSearch savedSearch)
    {
        Utils.waitForVisibilityOf(By.cssSelector("a[rel='" + savedSearch.getSavedSearchLabel() + "']")).click();
        Utils.waitForVisibilityOf(By.cssSelector(".message"));
        return filePlan.render();
    }

    @Override
    protected String getPageURL(String... context)
    {
       return SEARCH_URL;
    }
}
