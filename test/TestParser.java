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
    public void parseUrl() throws Exception
    {
        PlaylistDownloader downloader =
            new PlaylistDownloader("http://devimages.apple.com/iphone/samples/bipbop/gear4/prog_index.m3u8");

        downloader.download("/Users/chris/Downloads/out.ts");

//        URL url = new URL("http://localhost/~chris/video/ipad_1/prog_index.m3u8");
//        M3u8Parser parser = new M3u8Parser(url);
//
//        List<String> segments = parser.getSegments();
//
//        TsDownloader downloader = new TsDownloader(parser.getKeyInfo());
//
//        System.out.println("parser.getKeyInfo() = " + parser.getKeyInfo());
//
//        Date now = new Date();
//
//        Format formatter = new SimpleDateFormat("yyyyMMdd");
//
//        for (int i = 0; i < segments.size(); i++)
//        {
//            String segment = segments.get(i);
//            URL segmentUrl = new URL(segment);
//
//            String path = String.format("/Users/chris/Downloads/segments/%s-%d.ts",
//                    formatter.format(now), now.getTime());
//
//            downloader.download(segmentUrl, path);
//        }
     }
}
