/*
 * Copyright (c) Christopher A Longo
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

import java.io.File;

public class TestDownload {
    @Test
    public void download() throws Exception {
        File outDir = new File("out");
        if(!outDir.exists())
            outDir.mkdir();

        PlaylistDownloader downloader = new PlaylistDownloader("file:./data/prog_index.m3u8");
        downloader.download("out/out.ts");
    }
}
