package org.lliira.bookworm.core;

import org.testng.Assert;
import org.testng.annotations.Test;

import static net.lliira.bookworm.core.BookwormHelper.*;

public class BookwormHelperTest extends AbstractTest {

    @Test
    public void testGetIncrement() {
        Assert.assertEquals(getIncrement(0), PRESICION);
        Assert.assertEquals(getIncrement(1), 1);
        Assert.assertEquals(getIncrement(120), 10);
        Assert.assertEquals(getIncrement(3200), 100);
        Assert.assertEquals(getIncrement(345000), 1000);
        Assert.assertEquals(getIncrement(60000), 1000);
        Assert.assertEquals(getIncrement(7900000), 1000);
    }
}
