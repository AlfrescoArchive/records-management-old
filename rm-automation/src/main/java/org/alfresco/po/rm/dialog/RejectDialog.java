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
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.List;

import static org.alfresco.po.common.util.Utils.waitForVisibilityOf;

/**
 * Reject record dialog
 *
 * @author Oana Nechiforescu
 */

@Component
public class RejectDialog extends Dialog
{
    /** dialog selector */
    @FindBy(css="#userInput")
    private WebElement dialog;

    /** buttons selector */
    @FindBy(css="#userInput button")
    private List<Button> buttons;

    /** textarea selector */
    @FindBy(css="#userInput textarea")
    private WebElement reasonTextarea;

    public RejectDialog setReason(String reason)
    {
        reasonTextarea.clear();
        reasonTextarea.sendKeys(reason);
        return this;
    }

    public Renderable confirmRejection(Renderable renderable)
    {
        buttons.get(0).click();
        return renderable.render();
    }

    public Renderable cancelRejection(Renderable renderable)
    {
        buttons.get(1).click();
        return renderable.render();
    }

    public Renderable rejectRecordWithReason(String reason, Renderable renderable)
    {
        waitForVisibilityOf(dialog);
        setReason(reason);
        return confirmRejection(renderable);
    }


}
