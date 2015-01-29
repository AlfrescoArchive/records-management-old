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
package org.alfresco.po.common.buttonset;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.po.common.Page;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 * Button set 
 * 
 * @author Roy Wetherall
 */
public abstract class ButtonSet extends HtmlElement 
{
    /** button map */
    private Map<String, Button> buttonMap = null;
    
    /**
     * Is the named button clickable
     * 
     * @param  name     button name
     * @return boolean  true if button is clickable, false otherwise
     */
    public boolean isButtonClickable(String name)
    {
        if (getButtonMap().containsKey(name))
        {
            return getButtonMap().get(name).isEnabled();
        }
        else
        {
            throw new RuntimeException("Button " + name + " does not exist.");
        }
    }
    
    /**
     * Get the named button
     * 
     * @param name		button name
     * @return Button	button, null if none
     */
    public Button getButton(String name)
    {
    	return getButtonMap().get(name);
    }
    
    /**
     * Click on the named button.
     * <p>
     * The last rendered page will be returned and rendered.
     * 
     * @param name button name
     * @return R   last rendered page
     */
    @SuppressWarnings("unchecked")
    public <R extends Renderable> R click(String name)
    {
        return click(name, (R)Page.getLastRenderedPage());
    }
    
    /**
     * Click on the named button.
     * <p>
     * A renderable object is provided.  This will be rendered after the click and returned.
     * 
     * @param name         button name
     * @param renderable   renderable object
     * @return R           renderable object
     */
    public <R extends Renderable> R click(String name, R renderable)
    {
        if (getButtonMap().containsKey(name))
        {
            Button button = getButtonMap().get(name);
            if (button.isEnabled())
            {
                // mouse over the button
                Utils.mouseOver(button);
                
                // click the button
                button.click();  
                
                // TODO check for error message (dialog only?)
                
                
                
                // render
                return renderable.render();
            }
            else
            {
                throw new RuntimeException("Can't click button " + name + ", because it isn't enabled.");
            }
        }
        else
        {
            throw new RuntimeException("Button " + name + " does not exist.");
        }
    }
    
    /**
     * Get the button map based on the declared fields
     */
    protected Map<String, Button> getButtonMap()
    {
        if (buttonMap == null)
        {
            buttonMap = new HashMap<String, Button>();
            for (Field field : getAllFields(getClass()))
            {
                field.setAccessible(true);
                
                try
                {
                    Object fieldValue = field.get(this);
                    if (Button.class.isInstance(fieldValue))
                    {
                        buttonMap.put(field.getName(), (Button)fieldValue);
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException("Unable to render button set.", e);
                }
            }
        }
        
        return buttonMap;
    }
    
    /**
     * Get all fields for this object
     */
    private List<Field> getAllFields(Class<?> clazz) 
    {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = clazz;
        while (i != null && i != Object.class) 
        {
            Collections.addAll(result, i.getDeclaredFields());
            i = i.getSuperclass();
        }

        return result;
    }
}
