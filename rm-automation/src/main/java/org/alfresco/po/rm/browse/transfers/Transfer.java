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
package org.alfresco.po.rm.browse.transfers;
import org.alfresco.po.rm.browse.fileplan.Actions;
import org.alfresco.po.share.browse.ListItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Transfer
 *
 * @author Tatiana Kalinovskaya
 */
@Scope("prototype")
@Component
public class Transfer extends ListItem implements Actions
{
    /**
     * Click on complete transfer action
     */
    public Transfers clickOnCompleteTransfer()
    {
        return (Transfers)clickOnAction(COMPLETE_TRANSFER);
    }

    /**
     * Click on complete accession action
     */
    public Transfers clickOnCompleteAccession()
    {
        return (Transfers)clickOnAction(COMPLETE_ACCESSION);
    }

}
