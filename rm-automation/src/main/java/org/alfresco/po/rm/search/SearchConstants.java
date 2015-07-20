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
    // saved searches section
    final static String INCOMPLETE_RECORDS = "Incomplete Records";
    final static String ELIGIBLE_FOR_CUT_OFF = "Records And Record Folders Eligible For Cut Off";
    final static String ELIGIBLE_FOR_DESTRUCTION = "Records And Record Folders Eligible For Destruction";
    final static String ELIGIBLE_FOR_TRANSFER = "Records and Record Folders Eligible For Transfer";
    final static String ON_HOLD = "Records and Record Folders On Hold";
    final static String DUE_FOR_REVIEW = "Vital Records Due For Review";
    final static String CUSTOM_SEARCH = "Custom Search";
    
    // metadata section
    final static String MODIFIER = "modifier";
    final static String HAS_DISPOSITION_SCHEDULE = "hasDispositionSchedule";
            
    // components section
    final static String INCLUDE_INCOMPLETE = "undeclared";
    final static String SHOW_RECORD_CATEGORIES = "categories";
    
    // selector used to retrieve the results names in the search results page
    final static String RESULTS_NAMES_SELECTOR = "td[headers*='-th-name']>div";
}
