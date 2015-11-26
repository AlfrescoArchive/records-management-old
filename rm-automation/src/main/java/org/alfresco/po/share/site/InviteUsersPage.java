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
package org.alfresco.po.share.site;

import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.stereotype.Component;

/**
 * Invite new users to a collaboration site.
 *
 * @author tpage
 * @since 2.4.a
 */
@Component
public class InviteUsersPage extends SharePage
{
    @FindBy(css = "input[id$='default-search-text']")
    private WebElement userSearchBox;
    @FindBy(css = "button[id$='default-search-button-button']")
    private WebElement userSearchButton;
    @FindBy(css = "#template_x002e_people-finder_x002e_invite_x0023_default-results")
    private WebElement userSearchResultList;
    @FindBy(css = "#template_x002e_invitationlist_x002e_invite_x0023_default-inviteelist")
    private WebElement userRoleList;
    @FindBy(css = "#template_x002e_invitationlist_x002e_invite_x0023_default-invite-button-button")
    private WebElement inviteButton;

    @Override
    protected String getPageURL(String... context)
    {
        String siteId = context[0];
        return "/page/site/" + siteId + "/invite";
    }
}
