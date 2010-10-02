import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: chris
 * Date: Oct 2, 2010
 * Time: 1:56:10 PM
 */
public class PlaylistDownloader
{
    private URL url;
    private String playlist;
    private Crypto crypto;

    private static String EXT_X_KEY = "#EXT-X-KEY";

    public PlaylistDownloader(String playlistUrl) throws MalformedURLException
    {
        this.url = new URL(playlistUrl);
        this.crypto = new Crypto();
    }

    public void download(String outfile) throws IOException
    {
        fetchPlaylist();

        String line;
        BufferedReader reader = new BufferedReader(new StringReader(playlist));

        while ((line = reader.readLine()) != null)
        {
            line = line.trim();

            if (line.startsWith(EXT_X_KEY))
            {
                crypto.updateKeyString(line);

                System.out.println(String.format("Current Key / IV: %s / %s",
                    crypto.getCurrentKey(),
                    crypto.getCurrentIV()));
            }
            else if (line.length() > 0 && !line.startsWith("#"))
            {
                URL segmentUrl;

                if (!line.startsWith("http"))
                {
                    String urlString = url.toString();
                    int index = urlString.lastIndexOf('/');
                    String baseUrl = urlString.substring(0, ++index);

                    segmentUrl = new URL(baseUrl + line);
                }
                else
                {
                    segmentUrl = new URL(line);
                }

                download(segmentUrl, outfile);
            }
        }
    }

    private void download(URL segmentUrl, String outFile) throws IOException
    {
        byte[] buffer = new byte[512];

        InputStream is = crypto.hasKey()
            ? crypto.wrapInputStream(segmentUrl.openStream())
            : segmentUrl.openStream();

        File file = new File(outFile);

        FileOutputStream out = new FileOutputStream(outFile, file.exists());

        System.out.println("Downloading segment: " + segmentUrl);

        int read;

        while ((read = is.read(buffer)) >= 0)
        {
            out.write(buffer, 0, read);
        }

        is.close();
        out.close();
    }

    private void fetchPlaylist() throws IOException
    {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
            sb.append("\n");
        }

        reader.close();

        playlist = sb.toString();
    }
}
