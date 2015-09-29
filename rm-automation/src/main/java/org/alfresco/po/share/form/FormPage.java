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
package org.alfresco.po.share.form;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Form dialog
 * 
 * @author Roy Wetherall
 */
public abstract class FormPage extends SharePage
{
    @FindBy(css="button[id$='submit-button']")
    private Button saveButton;
    
    @FindBy(css="button[id$='cancel-button']")
    private Button cancelButton;

    private Renderable returnPage;

    @Override
    public <T extends Renderable> T render()
    {
        returnPage = SharePage.getLastRenderedPage();
        return super.render();
    }
    
    /**
     * Get the URL of the page
     */
    public String getPageURL(String ... context)
    {
        throw new UnsupportedOperationException("This page does not have a direct access URL set.");
    }

    /**
     * Click on save
     */
    public Renderable clickOnSave()
    {
        saveButton.click();
        return returnPage.render();
    }

    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        cancelButton.click();
        return returnPage.render();
    }
}
