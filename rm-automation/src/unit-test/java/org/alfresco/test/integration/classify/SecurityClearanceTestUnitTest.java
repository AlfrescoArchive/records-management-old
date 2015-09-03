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
package org.alfresco.test.integration.classify;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

/**
 * Unit tests for methods in the {@link SecurityClearanceTest}
 *
 * @author tpage
 * @since 2.4.a
 */
public class SecurityClearanceTestUnitTest
{
    private SecurityClearanceTest securityClearanceTest = new SecurityClearanceTest();

    /** Check that strings are sorted by user id, with capitals before lower case. */
    @Test (groups = {"unit-test"})
    public void testCheckUserOrdering_success()
    {
        List<String> displayedUsers = Arrays.asList("System (System)", "Alice Beacher (abeacher)", "Adam Zookeeper (azook)");

        securityClearanceTest.checkUserOrdering(displayedUsers);
    }

    /** Check that incorrect sorting causes an exception. */
    @Test (groups = {"unit-test"}, expectedExceptions = {AssertionError.class})
    public void testCheckUserOrdering_fail()
    {
        List<String> displayedUsers = Arrays.asList("System (System)", "Adam Zookeeper (azook)", "Alice Beacher (abeacher)");

        securityClearanceTest.checkUserOrdering(displayedUsers);
    }
}
