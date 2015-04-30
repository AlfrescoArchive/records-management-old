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

import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Confirmation dialog shown when deleting a piece of content.
 *
 * @author tpage
 */
@Component
public class DeleteConfirmationDialog extends Renderable
{
    /** Here we assume the "Delete" button is the first in the dialog. */
    @FindBy(xpath=".//span[@class='button-group']/span[1]//button")
    private Button deleteButton;

    /** Here we assume the "Cancel" button is the second in the dialog. */
    @FindBy (xpath=".//span[@class='button-group']/span[2]//button")
    private Button cancelButton;

    /** Confirm the delete request. */
    public void confirmDelete()
    {
        deleteButton.click();
    }
}
