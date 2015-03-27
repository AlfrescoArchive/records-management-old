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

/**
 * Vital review period enumeration
 * 
 * @author Tatiana Kalinovskaya
 */
public enum VitalReviewPeriod
{
    DAY("day"),
    END_OF_FINANSIAL_MONTH("fmend"),
    END_OF_FINANSIAL_QUARTER("fqend"),
    END_OF_FINANCIAL_YEAR("yend"),
    END_OF_MONTH("monthend"),
    END_OF_QUARTER("quarterend"),
    END_OF_YEAR("yearend"),
    IMMEDIATELY("immediately"),
    MONTH("month"),
    NONE("none"),
    QUARTER("quarter"),
    WEEK("week"),
    YEAR("year"),
    NOT_SET("");

    private String value;

    VitalReviewPeriod(String value)
    {
        this.value = value;
    }

    public static final VitalReviewPeriod fromValue(String value)
    {
        switch (value)
        {
            case "day":
                return DAY;
            case "fmend":
                return END_OF_FINANSIAL_MONTH;
            case "fqend":
                return END_OF_FINANSIAL_QUARTER;
            case "fyend":
                return END_OF_FINANCIAL_YEAR;
            case "monthend":
                return END_OF_MONTH;
            case "quarterend":
                return END_OF_QUARTER;
            case "yearend":
                return END_OF_YEAR;
            case "immediately":
                return IMMEDIATELY;
            case "month":
                return MONTH;
            case "none":
                return NONE;
            case "quarter":
                return QUARTER;
            case "week":
                return WEEK;
            case "year":
                return YEAR;
            default:
        return NOT_SET;
}
}

@Override
public String toString()
        {
        return this.value;
        }

}
