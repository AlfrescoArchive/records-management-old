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
package org.alfresco.po.rm.details.event;

import org.alfresco.po.common.buttonset.OkCancelButtonSet;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Complete event dialog
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class CompleteEventDialog extends Renderable implements StandardButtons
{
    /** button set */
    @FindBy(css=".bdft")
    private OkCancelButtonSet buttonset;

    /**
     * Click on ok
     */
    public Renderable clickOnOk()
    {
        return buttonset.click(OK);
    }

    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        return buttonset.click(CANCEL);
    }

}
