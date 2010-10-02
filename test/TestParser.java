import net.chrislongo.hls.PlaylistDownloader;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: clongo
 * Date: Oct 1, 2010
 * Time: 4:44:55 PM
 */

public class TestParser
{
    @Test
    public void download() throws Exception
    {
        PlaylistDownloader downloader =
            new PlaylistDownloader("http://devimages.apple.com/iphone/samples/bipbop/gear4/prog_index.m3u8");

        downloader.download("/Users/chris/Downloads/out.ts");
     }
}
