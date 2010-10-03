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

import net.chrislongo.hls.PlaylistDownloader;
import org.junit.Test;

/**
 * User: chris
 * Date: Oct 1, 2010
 * Time: 4:44:55 PM
 */

public class TestDownload
{
    @Test
    public void download() throws Exception
    {
        PlaylistDownloader downloader =
            new PlaylistDownloader("http://devimages.apple.com/iphone/samples/bipbop/gear4/prog_index.m3u8");

        downloader.download("out/out.ts");
    }
}
