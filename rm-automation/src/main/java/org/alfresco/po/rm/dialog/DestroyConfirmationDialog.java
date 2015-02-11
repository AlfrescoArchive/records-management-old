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
package org.alfresco.po.rm.dialog;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
/**
 * Destroy Confirmation dialog
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class DestroyConfirmationDialog extends Renderable
{
    /** ok button */
    @FindBy(xpath=".//span[@class='button-group']/span[1]//button")
    private Button okButton;

    /** cancel button */
    @FindBy (xpath=".//span[@class='button-group']/span[2]//button")
    private Button cancelButton;

    /* file plan */
    @Autowired
    FilePlan filePlan;

    public void clickOnOk()
    {
        okButton.click();
    }

    public FilePlan clickOnOkOk()
    {
        clickOnOk();
        this.render();
        clickOnOk();
        Utils.waitForVisibilityOf(By.cssSelector("img[alt='rm-destroyed']"));

        return filePlan.render();
    }
}
