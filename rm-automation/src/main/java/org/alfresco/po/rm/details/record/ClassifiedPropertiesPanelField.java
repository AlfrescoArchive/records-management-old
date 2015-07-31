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
package org.alfresco.po.rm.details.record;

/**
 * The fields exposed through the {@link ClassifiedPropertiesPanel}.
 *
 * @author Tom Page
 * @since 3.0.a
 */
public enum ClassifiedPropertiesPanelField
{
    ORIGINAL_CLASSIFICATION(0),
    CURRENT_CLASSIFICATION(1),
    CLASSIFIED_BY(2),
    CLASSIFICATION_AGENCY(3),
    CLASSIFICATION_REASON(4);

    /** The index of the field in the classified properties panel. */
    private int index;

    /** The enum's private constructor. */
    private ClassifiedPropertiesPanelField(int index)
    {
        this.index = index;
    }

    /**
     * The index of the field in the properties panel. Generally these are sequential, but occasionally tooltips match
     * the same path ({@link ClassifiedPropertiesPanel#CLASSIFIED_PROPERTIES_SELECTOR}).
     */
    public int getIndex()
    {
        return this.index;
    }
}
