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
import java.util.ArrayList;
import java.util.List;

/**
 * User: chris
 * Date: Oct 2, 2010
 * Time: 1:56:10 PM
 */
public class PlaylistDownloader
{
    private URL url;
    private List<String> playlist;
    private Crypto crypto;

    private boolean silent = false;

    private static String EXT_X_KEY = "#EXT-X-KEY";
    private static final String BANDWIDTH = "BANDWIDTH";

    public PlaylistDownloader(String playlistUrl) throws MalformedURLException
    {
        this.url = new URL(playlistUrl);
        this.playlist = new ArrayList<String>();
    }

    public boolean isSilent()
    {
        return silent;
    }

    public void setSilent(boolean silent)
    {
        this.silent = silent;
    }

    public void download() throws IOException
    {
        download(null);
    }

    public void download(String outfile) throws IOException
    {
        fetchPlaylist();

        this.crypto = new Crypto(getBaseUrl(this.url));

        for (String line : playlist)
        {
            line = line.trim();

            if (line.startsWith(EXT_X_KEY))
            {
                crypto.updateKeyString(line);

                if(!silent)
                {
                    System.out.printf("Current Key: %s\n", crypto.getCurrentKey());
                    System.out.printf("Current IV:  %s\n", crypto.getCurrentIV());
                }
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

                downloadInternal(segmentUrl, outfile);
            }
        }
    }

    private String getBaseUrl(URL url)
    {
        String urlString = url.toString();
        int index = urlString.lastIndexOf('/');
        return  urlString.substring(0, ++index);
    }

    private void downloadInternal(URL segmentUrl, String outFile) throws IOException
    {
        byte[] buffer = new byte[512];

        InputStream is = crypto.hasKey()
            ? crypto.wrapInputStream(segmentUrl.openStream())
            : segmentUrl.openStream();


        FileOutputStream out;

        if(outFile != null)
        {
            File file = new File(outFile);
            out = new FileOutputStream(outFile, file.exists());
        }
        else
        {
            String path = segmentUrl.getPath();
            int pos = path.lastIndexOf('/');
            out = new FileOutputStream(path.substring(++pos));
        }

        if(!silent)
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

        boolean isMaster = false;

        long maxRate = 0L;
        int maxRateIndex = 0;

        String line;
        int index = 0;

        while ((line = reader.readLine()) != null)
        {
            playlist.add(line);

            if(line.contains(BANDWIDTH))
                isMaster = true;

            if(isMaster && line.contains(BANDWIDTH))
            {
                try
                {
                    int pos = line.lastIndexOf("=");
                    long bandwidth = Long.parseLong(line.substring(++pos));

                    maxRate = Math.max(bandwidth, maxRate);

                    if(bandwidth == maxRate)
                        maxRateIndex = index + 1;
                }
                catch (NumberFormatException ignore) {}
            }

            index++;
        }

        reader.close();

        if(isMaster)
        {
            if(!silent)
            {
                System.out.printf("Found master playlist, fetching highest stream at %dKb/s\n",
                        maxRate / 1024);
            }

            this.url = updateUrlForSubPlaylist(playlist.get(maxRateIndex));
            this.playlist.clear();

            fetchPlaylist();
        }
    }

    private URL updateUrlForSubPlaylist(String sub) throws MalformedURLException
    {
        String newUrl;
  
        if (!sub.startsWith("http"))
        {
            newUrl = getBaseUrl(this.url) + sub;
        }
        else
        {
            newUrl = sub;
        }

        return new URL(newUrl);
    }
}
