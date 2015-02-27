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
package org.alfresco.po.common;

import org.alfresco.po.common.buttonset.ConfirmationPromptButtonSet;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Confirmation Prompt Page Object
 * 
 * @author Roy Wetherall
 */
@Component
public class ConfirmationPrompt extends Renderable
                            implements StandardButtons
{
    /** confirmation prompt button set */
    @FindBy(css="span.button-group")
    private ConfirmationPromptButtonSet buttons;

    /**
     * Click on confirm
     */
    public Renderable clickOnConfirm()
    {
        return buttons.click(CONFIRM).render();
    }
    
    /**
     * Click on confirm
     */
    public <T extends Renderable> T clickOnConfirm(T renderable)
    {
        return buttons.click(CONFIRM, renderable);
    }
    
    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        return buttons.click(CANCEL);
    }
    
    /**
     * Click on cancel
     */
    public <T extends Renderable> T clickOnCancel(T renderable)
    {
        return buttons.click(CANCEL, renderable);
    }
}
