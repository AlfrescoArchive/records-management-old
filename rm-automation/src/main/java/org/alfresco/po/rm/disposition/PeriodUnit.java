package org.alfresco.po.rm.disposition;

/**
 * Period unit enumeration
 *
 * @author Tatiana Kalinovskaya
 */
public enum PeriodUnit
{
    DAY ("day"),
    END_OF_FINANCIAL_MONTH ("fmend"),
    END_OF_FINANCIAL_QUARTER ("fqend"),
    END_OF_FINANCIAL_YEAR ("fyend"),
    END_OF_MONTH ("monthend"),
    END_OF_QUARTER ("quarterend"),
    END_OF_YEAR ("yearend"),
    IMMEDIATELY ("immediately"),
    MONTH ("month"),
    NONE ("none"),
    QUARTER ("quarter"),
    WEEK ("week"),
    XML_DURATION ("duration"),
    YEAR ("year");

    private String value;

    PeriodUnit(String value)
    {
        this.value = value;
    }

    public static final PeriodUnit fromValue(String value)
    {
        switch (value)
        {
            case "day": return DAY;
            case "fmend": return END_OF_FINANCIAL_MONTH;
            case "fqend": return END_OF_FINANCIAL_QUARTER;
            case "fyend": return END_OF_FINANCIAL_YEAR;
            case "monthend": return END_OF_MONTH;
            case "quarterend": return END_OF_QUARTER;
            case "yearend": return END_OF_YEAR;
            case "immediately": return IMMEDIATELY;
            case "month": return MONTH;
            case "none": return NONE;
            case "quarter": return QUARTER;
            case "week": return WEEK;
            case "duration": return XML_DURATION;
            case "year": return YEAR;

            default:
                return IMMEDIATELY;
        }
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}
