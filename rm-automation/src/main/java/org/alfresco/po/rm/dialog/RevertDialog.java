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
package org.alfresco.po.rm.dialog;

import org.alfresco.po.common.Dialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * The dialog that appears when a version is selected to revert to.
 *
 * @author Tom Page
 * @since 2.4.a
 */
@Component
public class RevertDialog extends Dialog
{
    @FindBy(css="#alfresco-revertVersion-instance-ok-button-button")
    private WebElement okButton;
    @FindBy(css="#alfresco-revertVersion-instance-cancel-button-button")
    private WebElement revertButton;

    /** Click the ok button on the revert dialog. */
    public void clickOK()
    {
        okButton.click();
    }
}
