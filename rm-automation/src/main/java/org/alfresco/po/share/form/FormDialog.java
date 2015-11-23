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

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.buttonset.SaveCancelButtonSet;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Form dialog
 *
 * @author Roy Wetherall
 */
public abstract class FormDialog extends Dialog
{
    /** button names */
    public static final String SAVE = "save";
    public static final String CANCEL = "cancel";

    /** button set */
    @FindBy(css = VISIBLE_DIALOG)
    private SaveCancelButtonSet buttonset;

    /** name text input */
    @FindBy(name = "prop_cm_name")
    private TextInput nameTextInput;

    /** title text input */
    @FindBy(name = "prop_cm_title")
    private TextInput titleTextInput;

    /** description text input */
    @FindBy(name = "prop_cm_description")
    private TextInput descriptionTextInput;

    /**
     * Set name value
     */
    @SuppressWarnings("unchecked")
    public <T extends FormDialog> T setName(String name)
    {
        clearAndType(nameTextInput, name);
        return (T) this;
    }

    /**
     * Get name value
     */
    public String getName()
    {
        return nameTextInput.getText();
    }

    /**
     * Set title value
     */
    @SuppressWarnings("unchecked")
    public <T extends FormDialog> T setTitle(String title)
    {
        clearAndType(titleTextInput, title);
        return (T) this;
    }

    /**
     * Get description value
     */
    public String getDescription()
    {
        return descriptionTextInput.getWrappedElement().getAttribute("value");
    }

    /**
     * Set description value
     */
    @SuppressWarnings("unchecked")
    public <T extends FormDialog> T setDescription(String description)
    {
        clearAndType(descriptionTextInput, description);
        return (T) this;
    }

    /**
     * Get title value
     */
    public String getTitle()
    {
        return titleTextInput.getText();
    }

    /**
     * Click on save
     */
    public Renderable clickOnSave()
    {
        return buttonset.click(SAVE);
    }

    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        return buttonset.click(CANCEL);
    }


    public boolean isNameRequired()
    {
        return (nameTextInput.getWrappedElement().getAttribute("alf-validation-msg")) != null;
    }

    public boolean isTitleRequired()
    {
        return (titleTextInput.getWrappedElement().getAttribute("alf-validation-msg")) != null;
    }

    public boolean isDescriptionRequired()
    {
        return (descriptionTextInput.getWrappedElement().getAttribute("alf-validation-msg")) != null;
    }
}
