package org.alfresco.po.share.userdashboard;

import static org.junit.Assert.assertNotNull;

import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by tatiana.kalinovskaya on 04.11.2014.
 */
@Test (groups = {"unit-test"})
public class UserDashboardPageUnitTest extends BaseTest

{
    @Autowired
    private MySitesDashlet mySitesDashlet;

    @Test
    private void verifyUserDashBoard()
    {
        // verify "my sites" dashlet is on the UserDashboard
        MySitesDashlet mySitesDashlet = openPage(userDashboardPage).getMySitesDashlet();
        assertNotNull(mySitesDashlet);
    }
}
