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
package org.alfresco.po.rm.dialog.create;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.share.form.FormDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * New hold dialog
 * 
 * @author Roy Wetherall
 */
@Component
public class NewHoldDialog extends FormDialog
{
    /** reason text input */
    @FindBy(name = "prop_rma_holdReason")
    private TextInput reasonInput;

    /**
     * Set hold reason
     */
    public NewHoldDialog setReason(String reason)
    {
        clearAndType(reasonInput, reason);
        return this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public NewHoldDialog setName(String name)
    {
        return (NewHoldDialog)super.setName(name);
    }
    
}
