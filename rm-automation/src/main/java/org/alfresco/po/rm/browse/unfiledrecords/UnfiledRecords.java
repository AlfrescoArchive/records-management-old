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
package org.alfresco.po.rm.browse.unfiledrecords;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.rm.browse.RMBrowsePage;
import org.alfresco.po.rm.browse.RMBrowsePlanList;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.dialog.RejectDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Unfiled records 
 * 
 * @author Roy Wetherall
 */
@Component
public class UnfiledRecords extends RMBrowsePage<RMBrowsePlanList, UnfiledRecordsToolbar> 
{
    @Autowired
    private RejectDialog rejectDialog;
    /**
     * Helper method to get the named record from the list
     */
    public Record getRecord(String recordName)
    {
        return getList().getByPartialName(recordName, Record.class);
    }

    public Renderable rejectRecordWithReason(String recordName, String reason, Renderable renderable)
    {
        return getRecord(recordName).clickOnAction(RecordActions.REJECT_RECORD, rejectDialog).rejectRecordWithReason(reason, renderable);
    }
}
