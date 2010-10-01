import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: clongo
 * Date: Oct 1, 2010
 * Time: 4:32:18 PM
 */
public class M3u8Parser
{
    private URL url;
    private KeyInfo keyInfo;
    private List<String> segments;

    private static String EXT_X_KEY = "#EXT-X-KEY";
    private static String EXTINF = "#EXTINF";
                                           
    public M3u8Parser(URL url) throws IOException
    {
        this.url = url;
        parse();
    }

    public URL getUrl()
    {
        return url;
    }

    public boolean isEncrypted()
    {
        return (keyInfo != null);
    }

    public KeyInfo getKeyInfo()
    {
        return keyInfo;
    }

    public List<String> getSegments()
    {
        return segments;
    }

    private void parse() throws IOException
    {
        URLConnection con = url.openConnection();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String line;

        while((line = reader.readLine()) != null)
        {
          //  System.out.println("line = " + line);

            if(line.startsWith(EXT_X_KEY))
            {
                keyInfo = new KeyInfo(line);
                System.out.println("keyInfo = " + keyInfo);
            }
        }
    }
}
