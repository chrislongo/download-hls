import org.junit.Test;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: clongo
 * Date: Oct 1, 2010
 * Time: 4:44:55 PM
 */

public class TestParser
{
    @Test
    public void parseUrl() throws Exception
    {
        URL url = new URL("http://chrislongo.net/video/ipad_2/prog_index.m3u8");
        M3u8Parser parser = new M3u8Parser(url);
    }
}
