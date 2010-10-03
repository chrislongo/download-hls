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

/**
 * User: chris
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