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
package org.alfresco.po.rm.actions.viewaudit;

import java.util.List;

import org.alfresco.po.common.Page;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * View audit log page
 * 
 * @author Tatiana Kalinovskaya
 */
@Component
public class AuditLogPage extends Page
{
    /** export button */
    @FindBy(css = "button[id$=export-button]")
    private Button export;

    /** file as record button */
    @FindBy(css = "button[id$=audit-file-record-button]")
    private Button fileAsRecord;
    
    /** audit data */
    @FindBy(css = "div[id$='default-audit']")
    private WebElement auditData;

    /** audit entries block */
    @FindBy(xpath="//div[@class='audit-entry']")
    private List<AuditEntry> auditEntryList;

    /** audit page header*/
    @FindBy(xpath="//h1")
    private WebElement auditPageHeader;

    /** parent window */
    private String parentWindow;
    
    /** parent renderable */
    private Renderable parentRenderable;

    /**
     * @see org.alfresco.po.common.Page#render()
     */
    @Override
    public <T extends Renderable> T render()
    {
        // stash the original renderable page
        parentRenderable = SharePage.getLastRenderedPage();
        
        // switch to the newly opened popup window
        parentWindow = webDriver.getWindowHandle();
        boolean switched = false;
        for (String winHandle : webDriver.getWindowHandles())
        {
            if (!winHandle.equals(parentWindow))
            {
                webDriver.switchTo().window(winHandle);
                switched = true;
                break;
            }
        }
        
        // check that control has been passed to the audit pop-up window
        if (switched == false)
        {
            throw new RuntimeException("Unable to render audit log page, because pop-up window was not found.");
        }
        
        // render
        return super.render();
    }
    
    /** 
     * Close view audit and return to parent window
     */
    public Renderable close()
    {
        webDriver.close();
        webDriver.switchTo().window(parentWindow);      
        return parentRenderable.render();
    }

    /**
     * Is export button displayed
     */
    public boolean isExportButtonDisplayed()
    {
        return export.isDisplayed();
    }

    /**
     * Is export button enabled
     */
    public boolean isExportButtonEnabled()
    {
        return export.isEnabled();
    }

    /**
     * Is file as record button displayed
     */
    public boolean isFileAsRecordButtonDisplayed()
    {
        return fileAsRecord.isDisplayed();
    }

    /**
     * Is file as button enabled
     */
    public boolean isFileAsRecordButtonEnabled()
    {
        return fileAsRecord.isEnabled();
    }

    public AuditEntry getAuditEntry(int count)
    {
        return auditEntryList.get(count);
    }

    public int getAuditEntryCount()
    {
        return auditEntryList.size();
    }

    public String getAuditPageHeader()
    {
        return auditPageHeader.getText();
    }
}
