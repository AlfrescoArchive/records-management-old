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
package org.alfresco.po.common.renderable;

import static org.alfresco.po.common.util.Utils.checkMandatoryParam;
import static org.alfresco.po.common.util.Utils.webDriverWait;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.po.common.annotations.RenderableChild;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.beans.factory.annotation.Autowired;

import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;

/**
 * Abstract base class for all renderable items
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
public abstract class Renderable
{
    /** web driver */
    @Autowired
	protected WebDriver webDriver;

	/** cached list of renderable children */
	private Set<Renderable> renderableChildren;

	/** renderable parent, null if none */
	protected Renderable renderableParent;

	/** last rendered */
	private static Renderable lastRendered;

	/**
	 * @return	last rendered renderable item
	 */
	protected static Renderable getLastRendered()
	{
		return lastRendered;
	}

	/**
     * Render method
     */
	@SuppressWarnings("unchecked")
    public <T extends Renderable> T render()
    {
    	// check the web driver
    	checkMandatoryParam("webDriver", webDriver);

    	// wait for page to load
    	waitForPageLoad();

    	// init elements
        PageFactory.initElements(new HtmlElementDecorator(webDriver), this);

        // render children
        renderChildren();

        // return this
        lastRendered = this;
        return (T)this;
    }

    /**
     * Helper to wait for the page to load
     */
    private void waitForPageLoad()
    {
        ExpectedCondition<Boolean> pageLoadCondition =
                driver -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
        webDriverWait().until(pageLoadCondition);
    }

    /**
     * Render children
     */
    protected void renderChildren()
    {
    	for (Renderable renderableChild : getRenderableChildren())
    	{
    	    renderableChild.renderableParent = this;
        	renderableChild.render();
		}
    }

    /**
     * Get the renderable children
     */
    private Set<Renderable> getRenderableChildren()
    {
    	if (renderableChildren == null)
    	{
    		renderableChildren = getAnnotatedFields(Renderable.class, RenderableChild.class).keySet();
    	}
    	return renderableChildren;
    }

    /**
     * Helper method to retrieve all the field values for a given annotation class
     *
     * @param   fieldClass          field class
     * @param   annotationClass     annotation class
     * @return  List<T>             list of field values
     */
    @SuppressWarnings("unchecked")
    private <T extends Object, A extends Annotation> Map<T, A> getAnnotatedFields(Class<T> fieldClass, Class<A> annotationClass)
    {
        Map<T, A> result = new HashMap<>();

        for (Field field : getAllFields(getClass()))
        {
            A annotation = field.getAnnotation(annotationClass);
            if (annotation != null)
            {
                try
                {
                    field.setAccessible(true);
                    Object fieldValue = field.get(this);
                    if (fieldClass.isInstance(fieldValue))
                    {
                        result.put((T)fieldValue, annotation);
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Get all fields for this object
     */
    protected List<Field> getAllFields(Class<?> clazz)
    {
        List<Field> result = new ArrayList<>();

        Class<?> i = clazz;
        while (i != null && i != Object.class)
        {
        	Collections.addAll(result, i.getDeclaredFields());
            i = i.getSuperclass();
        }

        return result;
    }
}
