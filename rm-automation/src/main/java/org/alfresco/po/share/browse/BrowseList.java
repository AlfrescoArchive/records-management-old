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
package org.alfresco.po.share.browse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Retry;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic browse list implementation
 *
 * @author Roy Wetherall
 */
public abstract class BrowseList<F extends BrowseListItemFactory> extends Renderable
{
    /** list item factory */
    @Autowired
    private F listItemFactory;

    /** current page information, wait to be visible */
    @FindBy(css = "div[id$='paginatorBottom'] span[class$='current']")
    private WebElement current;

    /** data list element */
    @FindBy(css = "div[id$='default-documents']")
    private WebElement dataList;

    /** row selector */
    private By rowsSelector = By.cssSelector("div[id$='default-documents'] tbody[class$='data'] tr");

    /** item count */
    private int itemCount = 0;

    /**
     * @see org.alfresco.po.common.renderable.Renderable#render()
     */
    @Override
    public <T extends Renderable> T render()
    {
        T result = super.render();

        // figure out how many items are on the page
        String text = current.getText();
        String[] values = text.split(" ");
        itemCount = Integer.parseInt(values[2]);

        if (itemCount != 0)
        {
            Utils.waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsSelector));
        }

        return result;
    }

    /**
     * get the item map
     */
    private Map<String, ListItem> getItemMap()
    {
        return Utils.retry(new Retry<Map<String, ListItem>>()
        {
            @Override
            public Map<String, ListItem> execute()
            {
                List<WebElement> rows = webDriver.findElements(rowsSelector);

                if (rows.size() == itemCount)
                {
                    // clear the current item map
                    Map<String, ListItem>itemMap = new HashMap<String, ListItem>(rows.size());

                    // build the new item map
                    for (WebElement row : rows)
                    {
                        ListItem item = listItemFactory.getItem(row);
                        itemMap.put(item.getName(), item);
                    }

                    return itemMap;
                }
                else
                {
                    throw new IllegalStateException("Expected " + itemCount + " rows and found " + rows.size());
                }
            }
        }, 5);
    }

    /**
     * Browse list size
     */
    public int size()
    {
        return getItemMap().size();
    }

    /**
     * Indicates whether the item list contains the named item
     *
     * @param name name of the item
     * @return boolean true if contained, false otherwise
     */
    public boolean contains(String name)
    {
        return getItemMap().containsKey(name);
    }

    /**
     * Get list item
     *
     * @param name name of the item
     * @return ListItem list item, null if none
     */
    public ListItem get(String name)
    {
        return getItemMap().get(name);
    }

    /**
     * Get list item of specified type.
     *
     * @param name name of time
     * @param clazz expected type of item
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ListItem> T get(String name, Class<T> clazz)
    {
        ListItem item = getItemMap().get(name);
        if (item != null && !clazz.isInstance(item))
        {
            throw new RuntimeException("Unexpected list item type. Got "+ item.getClass() + " expected " + clazz);
        }
        return (T) item;
    }

    /**
     * Get list item by partial name
     *
     * @param name
     * @return
     */
    public ListItem getByPartialName(String name)
    {
        return getByPartialName(name, ListItem.class);
    }

    /**
     * @param name
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ListItem> T getByPartialName(String name, Class<T> clazz)
    {
        T result = null;

        for (Map.Entry<String, ListItem> entry : getItemMap().entrySet())
        {
            if (entry.getKey().startsWith(name))
            {
                ListItem item = entry.getValue();
                if (!clazz.isInstance(item))
                {
                    throw new RuntimeException("Unexpected list item type. Got "+ item.getClass() + " expected " + clazz);
                }
                result = (T) item;

                break;
            }
        }

        return result;
    }
}
