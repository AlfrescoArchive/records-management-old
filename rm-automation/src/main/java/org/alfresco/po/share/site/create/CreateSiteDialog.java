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
package org.alfresco.po.share.site.create;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.buttonset.OkCancelButtonSet;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.rm.site.RMSiteDashboard;
import org.alfresco.po.share.site.CollaborationSiteDashboard;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Radio;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Create site dialog
 *
 * @author Roy Wetherall
 */
@Component
public class CreateSiteDialog extends Dialog
{
    /** buttons */
    @FindBy(css = VISIBLE_DIALOG)
    private OkCancelButtonSet buttonset;

    /** collaboration site dashboard */
    @Autowired
    private CollaborationSiteDashboard collaborationSiteDashboard;

    /** rm site dashboard */
    @Autowired
    private RMSiteDashboard rmSiteDashboard;

    /**
     * radio buttons
     */
    private final static int PRIVATE_RADIO_BUTTON_INDEX = 1;

    /**
     * site drop down list
     */
    @FindBy(name = "sitePreset")
    private Select selectSiteType;

    /**
     * compliance drop down list
     */
    @FindBy(name = "compliance")
    private Select selectCompliance;

    /**
     * name field
     */
    @FindBy(name = "title")
    private TextInput siteName;

    /**
     * URL field
     */
    @FindBy(name = "shortName")
    private TextInput siteURL;

    /**
     * Description field
     */
    @FindBy(name = "description")
    private TextInput siteDescription;

    /**
     * Visibility radio box
     */
    @FindBy(xpath = "//input[@type='radio']")
    private Radio siteVisibility;

    /**
     * Moderated check box
     */
    @FindBy(id = "alfresco-rm-createSite-instance-isModerated")
    private CheckBox moderatedVisibility;

    /**
     * Get site type
     */
    public SiteType getSiteType()
    {
        WebElement selectedElement = selectSiteType.getFirstSelectedOption();
        String value = selectedElement.getAttribute("value");
        return SiteType.fromValue(value);
    }

    /**
     * Get site compliance
     */
    public Compliance getCompliance()
    {
        WebElement selectedElement = selectCompliance.getFirstSelectedOption();
        String value = selectedElement.getAttribute("value");
        return Compliance.fromValue(value);
    }

    /**
     * Get site name
     */
    public String getSiteName()
    {
        return siteName.getText();
    }

    /**
     * Get site URL
     */
    public String getSiteURL()
    {
        return siteURL.getText();
    }

    /**
     * Get site Description
     */
    public String getSiteDescription()
    {
        return siteDescription.getWrappedElement().getAttribute("value");
    }

    /**
     * Get site Visibility
     */
    public String getVisibility()
    {
        return siteVisibility.getSelectedButton().getAttribute("id");
    }

    /**
     * Set site type
     */
    public CreateSiteDialog setSiteType(SiteType siteType)
    {
        selectSiteType.selectByValue(siteType.toString());
        return this;
    }

    /**
     * Set site compliance
     */
    public CreateSiteDialog setCompliance(Compliance compliance)
    {
        selectCompliance.selectByValue(compliance.toString());
        return this;
    }

    /**
     * Set site Name
     */
    public CreateSiteDialog setSiteName(String name)
    {
        clearAndType(siteName, name);
        return this;
    }

    /**
     * Set site URL
     */
    public CreateSiteDialog setSiteURL(String url)
    {
        clearAndType(siteURL, url);
        return this;
    }

    /**
     * Set site Description
     */
    public CreateSiteDialog setSiteDescription(String description)
    {
        clearAndType(siteDescription, description);
        return this;
    }

    /**
     * Check if siteName is enabled
     */
    public boolean isSiteNameInputEnabled()
    {
        return siteName.isEnabled();
    }

    /**
     * Check if siteURL is enabled
     */
    public boolean isSiteURLInputEnabled()
    {
        return siteURL.isEnabled();
    }

    /**
     * Check if Private Visibility is enabled
     */
    public boolean isPrivateVisibilityEnabled()
    {
        WebElement privateRadioButton = siteVisibility.getButtons().get(PRIVATE_RADIO_BUTTON_INDEX);
        return privateRadioButton.isEnabled();
    }

    /**
     * Check if Moderated Visibility is enabled
     */
    public boolean isModeratedVisibilityEnabled()
    {
        return moderatedVisibility.isEnabled();
    }

    /**
     * Check if compliance is displayed
     */
    public boolean isComplianceDisplayed()
    {
        return selectCompliance.isDisplayed();
    }

    public Renderable clickOnOk()
    {
        Renderable result = null;
        if (getSiteType().equals(SiteType.COLLABORATION_SITE))
        {
            result = collaborationSiteDashboard;
        }
        else
        {
            result = rmSiteDashboard;
        }

        return buttonset.click(OK, result);
    }

    public Renderable clickOnCancel()
    {
        return buttonset.click(CANCEL);
    }
}
