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
package org.alfresco.po.share.admin.usertrashcan;

import java.text.MessageFormat;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.share.page.SharePage;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * User trashcan page
 * 
 * @author Roy Wetherall
 */
@Component
public class UserTrashcanPage extends SharePage
{
    /** page url */
    private static final String PAGE_URL = "/page/user/{0}/user-trashcan";
    
    /** empty button */
    @FindBy(css="button[id$='empty-button-button']")
    private Button emptyButton;
    
    /** confirmation prompt */
    @Autowired
    private ConfirmationPrompt prompt;
    
    /**
     * Get the URL of the page
     */
    public String getPageURL(String ... context)
    {
        if (context.length == 1)
        {
            String user = context[0];
            if (StringUtils.isNotBlank(user))
            {
                return MessageFormat.format(PAGE_URL, user);
            }
            else
            {
                throw new RuntimeException("User is empty in page URL context.");
            }
        }
        else
        {
            throw new RuntimeException("User missing from page URL context.");
        }
    }
    
    /** 
     * Click on empty button
     */
    public ConfirmationPrompt clickOnEmpty()
    {
        emptyButton.click();
        return prompt.render();
    }
}
