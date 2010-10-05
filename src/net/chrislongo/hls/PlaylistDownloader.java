/*
 * Copyright (c) 2010 Christopher A Longo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.chrislongo.hls;

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
        this.crypto = new Crypto(getBaseUrl(this.url));
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

                System.out.printf("Current Key: %s\n", crypto.getCurrentKey());
                System.out.printf("Current IV:  %s\n", crypto.getCurrentIV());
            }
            else if (line.length() > 0 && !line.startsWith("#"))
            {
                URL segmentUrl;

                if (!line.startsWith("http"))
                {
                    String baseUrl = getBaseUrl(this.url);
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

    private String getBaseUrl(URL url)
    {
        String urlString = url.toString();
        int index = urlString.lastIndexOf('/');
        return  urlString.substring(0, ++index);
    }

    private void download(URL segmentUrl, String outFile) throws IOException
    {
        byte[] buffer = new byte[512];

        InputStream is = crypto.hasKey()
            ? crypto.wrapInputStream(segmentUrl.openStream())
            : segmentUrl.openStream();

        File file = new File(outFile);

        FileOutputStream out = new FileOutputStream(outFile, file.exists());

        System.out.printf("Downloading segment: %s\n", segmentUrl);

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
