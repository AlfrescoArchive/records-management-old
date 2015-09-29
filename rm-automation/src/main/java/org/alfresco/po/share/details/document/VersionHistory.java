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
package org.alfresco.po.share.details.document;

import java.util.List;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.rm.dialog.RevertDialog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The version history panel.
 *
 * @author Tom Page
 * @since 2.4.a
 */
@Component
public class VersionHistory extends Renderable
{
    private static final By DOCUMENT_VERSION_SELECTOR = By.cssSelector(".document-version");
    private static final By REVERT_BUTTON_SELECTOR = By.cssSelector(".revert");

    @FindBy(css=".version-list")
    private List<WebElement> versionList;
    @Autowired
    private RevertDialog revertDialog;

    /**
     * Revert to a specified version of the document. Silently fail if the version doesn't exist (TODO Discuss nicest
     * way to fix this!)
     *
     * @param documentVersion The document version to revert to (e.g. "1.0").
     * @return The reversion dialog.
     */
    public RevertDialog revertToVersion(String documentVersion)
    {
        versionList.stream()
            .filter(row -> row.findElements(DOCUMENT_VERSION_SELECTOR)
                              .get(0)
                              .getText()
                              .equals(documentVersion))
            .findAny()
            .ifPresent(row -> row.findElement(REVERT_BUTTON_SELECTOR).click());
        return revertDialog.render();
    }

    /**
     * Count the number of versions of a document.
     *
     * @return The number of versions of the current document.
     */
    public int countVersions()
    {
        return versionList.size();
    }
}
