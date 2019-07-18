import chatter.common.exception.ChatterException;
import chatter.common.util.ChatterUtil;
import org.junit.Test;

import java.io.File;
import java.util.Objects;
import org.junit.Assert;

public class ChatterUtilTest {

    @Test
    public void readFileTest() throws ChatterException {
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("test")).getFile());
        String fileStr = ChatterUtil.readFile(file.getPath());
        Assert.assertEquals("{\"port\":\"2001\"}", fileStr);
    }

    @Test(expected = ChatterException.class)
    public void readFileTest2() throws ChatterException {
        ChatterUtil.readFile("test2");
    }
}