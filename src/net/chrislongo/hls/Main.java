package net.chrislongo.hls;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: clongo
 * Date: Oct 1, 2010
 * Time: 12:22:32 PM
 */
public class Main
{
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Args: playlistUrl outFile");
            System.exit(1);
        }

        try
        {
            String playlistUrl = args[0];
            String outFile = args[1];

            File file = new File(outFile);

            if (file.exists())
            {
                System.out.printf("File '%s' already exists. Overwrite? [y/N] ", outFile);

                int ch = System.in.read();

                if (!(ch == 'y' || ch == 'Y'))
                {
                    System.exit(0);
                }

                file.delete();
            }

            PlaylistDownloader downloader =
                new PlaylistDownloader(playlistUrl);

            downloader.download(outFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}