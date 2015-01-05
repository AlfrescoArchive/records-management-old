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
package org.alfresco.po.rm.dialog.copymovelinkfile;

import org.alfresco.po.common.renderable.Renderable;
import org.springframework.stereotype.Component;

/**
 * File report dialog
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class FileReportDialog extends CopyMoveLinkFileDialog
{
    /**
     * @see CopyMoveLinkFileDialog#select(java.lang.String)
     */
    @Override
    public FileReportDialog select(String name)
    {
        return (FileReportDialog)super.select(name);
    }

    /**
     * Click on copy button
     */
    public Renderable clickOnFileReport()
    {
        return buttonset.click(OK);
    }

}
