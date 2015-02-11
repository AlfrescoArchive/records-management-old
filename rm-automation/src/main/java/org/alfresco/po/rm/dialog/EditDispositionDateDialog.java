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
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * Edit Disposition Date dialog
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditDispositionDateDialog extends CalendarDialog
{
    /** update button */
    @FindBy(xpath=".//span[@class='button-group']/span[1]//button")
    private Button updateButton;

    /** cancel button */
    @FindBy (xpath=".//span[@class='button-group']/span[2]//button")
    private Button cancelButton;

    /** selected date */
    @FindBy (xpath = ".//td[contains(@class,'selected')]/a")
    private Link selectedCell;

    /** today date */
    @FindBy (xpath =".//td[contains(@class,'today')]/a")
    private Link todayCell;

    //TODO create method to click on any cell
    /**
     * Select Today Cell
     * @return EditDispositionDateDialog
     */
    public EditDispositionDateDialog selectTodayCell()
    {
        todayCell.click();
        return this;
    }

    public <T extends Renderable> T  clickOnUpdate(T renderable)
    {
        updateButton.click();
        Utils.waitForInvisibilityOf(By.xpath(".//span[@class='button-group']/span[1]//button"));
        return renderable.render();
    }
}
