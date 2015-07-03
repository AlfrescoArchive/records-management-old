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

import org.alfresco.po.common.annotations.WaitFor;
import org.alfresco.po.common.annotations.WaitForStatus;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic browse list implementation
 *
 * @author Roy Wetherall
 */
public abstract class BrowseList<F extends BrowseListItemFactory> extends Renderable
{
    /** render retry count */
    private static final int RETRY_COUNT = 5;

    /** list item factory */
    @Autowired
    private F listItemFactory;

    /** current page information, wait to be visible */
    @WaitFor(status = WaitForStatus.VISIBLE)
    @FindBy(css = "div[id$='paginatorBottom'] span[class$='current']")
    private WebElement current;

    /** data list element */
    @WaitFor(status = WaitForStatus.VISIBLE)
    @FindBy(css = "div[id$='default-documents']")
    private WebElement dataList;

    /** row selector */
    private By rowsSelector = By.cssSelector("div[id$='default-documents'] tbody[class$='data'] tr");

    /** item map indexed by name */
    private Map<String, ListItem> itemMap;

    /**
     * @see org.alfresco.po.common.renderable.Renderable#render()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Renderable> T render()
    {
        return Utils.retry(() ->
        {
            T result = super.render();

            // figure out how many items are on the page
            String text = current.getText();
            String[] values = text.split(" ");
            int count = Integer.parseInt(values[2]);

            List<WebElement> rows = webDriver.findElements(rowsSelector);

            if (rows.size() == count)
            {
                // clear the current item map
                itemMap = new HashMap<>(rows.size());

                // build the new item map
                for (WebElement row : rows)
                {
                    // check for staleness of row
                    if (row.isDisplayed())
                    {
                        ListItem item = listItemFactory.getItem(row);
                        itemMap.put(item.getName(), item);
                    }
                    else
                    {
                        throw new IllegalStateException("browse list row is not visible");
                    }
                }
            }
            else
            {
                throw new IllegalStateException("Expected " + count + " rows and found " + rows.size());
            }
            return result;
        }, RETRY_COUNT, WebDriverException.class, IllegalStateException.class);
    }

    /**
     * Browse list size
     */
    public int size()
    {
        return itemMap.size();
    }

    /**
     * Indicates whether the item list contains the named item
     *
     * @param name name of the item
     * @return boolean true if contained, false otherwise
     */
    public boolean contains(String name)
    {
        return itemMap.containsKey(name);
    }

    /**
     * Get list item
     *
     * @param name name of the item
     * @return ListItem list item, null if none
     */
    public ListItem get(String name)
    {
        return itemMap.get(name);
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
        ListItem item = itemMap.get(name);
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

        for (Map.Entry<String, ListItem> entry : itemMap.entrySet())
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
