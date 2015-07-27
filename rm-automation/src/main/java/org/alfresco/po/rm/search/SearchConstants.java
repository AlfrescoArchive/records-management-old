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
package org.alfresco.po.rm.search;

/**
 * Records Search page used constants
 *
 * @author Oana Nechiforescu
 */
public class SearchConstants 
{
    public enum SearchOptionType 
    {
        // search option type selector part from Metadata section 
        METADATA("default-metadata-"),
        // search option type selector part from Components section 
        COMPONENTS("default-");

        //selector part used for finding the search options section
        String selector;

        SearchOptionType(String selector) 
        {
            this.selector = selector;
        }

        public String getSelector()
        {
            return selector;
        }
    }

    public enum SearchOption 
    {
        INCLUDE_INCOMPLETE("undeclared"),
        SHOW_RECORD_CATEGORIES("categories"),
        MODIFIER("modifier"),
        HAS_DISPOSITION_SCHEDULE("hasDispositionSchedule");
        
        //selector part used for finding the search option
        String optionSelector;

        SearchOption(String optionSelector) 
        {
            this.optionSelector = optionSelector;
        }

        public String getOptionSelector() 
        {
            return optionSelector;
        }
    }
    
    public enum SavedSearch
    {
        INCOMPLETE_RECORDS("Incomplete Records"),
        RECORDS_FOLDERS_ELIGIBLE_FOR_CUT_OFF("Records And Record Folders Eligible For Cut Off"),
        RECORDS_FOLDERS_ELIGIBLE_FOR_DESTRUCTION("Records And Record Folders Eligible For Destruction"),
        RECORDS_FOLDERS_ELIGIBLE_FOR_TRANSFER("Records and Record Folders Eligible For Transfer"),
        RECORDS_FOLDERS_ON_HOLD("Records and Record Folders On Hold"),
        VITAL_RECORDS_DUE_FOR_REVIEW("Vital Records Due For Review");

        String savedSearchLabel;

        SavedSearch(String savedSearchLabel) 
        {
            this.savedSearchLabel = savedSearchLabel;
        }

        public String getSavedSearchLabel()
        {
            return savedSearchLabel;
        }
    }
}
