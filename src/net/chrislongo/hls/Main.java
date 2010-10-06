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

import org.apache.commons.cli.*;

import java.io.File;

/**
 * User: chris
 * Date: Oct 1, 2010
 * Time: 12:22:32 PM
 */
public class Main
{
    private static final String ARG_IV = "iv";
    private static final String ARG_KEY = "key";
    private static final String OPT_HELP = "h";
    private static final String OPT_IV = "iv";
    private static final String OPT_KEY = "k";
    private static final String OPT_SILENT = "s";
    private static final String OPT_OUT_FILE = "o";
    private static final String ARG_OUT_FILE = "output file";
    private static final String OPT_OUT_FILE_LONG = "out-file";
    private static final String SYNTAX = "download-hls [options...] <url> [outfile]";

    public static void main(String[] args)
    {
        CommandLine commandLine = parseCommandLine(args);
        String[] commandLineArgs = commandLine.getArgs();

        try
        {
            String playlistUrl = commandLineArgs[0];

            String outFile = null;

            if(commandLine.hasOption(OPT_OUT_FILE))
            {
                outFile = commandLine.getOptionValue(OPT_OUT_FILE);

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
            }

            PlaylistDownloader downloader =
                new PlaylistDownloader(playlistUrl);

            downloader.setSilent(commandLine.hasOption(OPT_SILENT));

            downloader.download(outFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static CommandLine parseCommandLine(String[] args)
    {
        CommandLineParser parser = new PosixParser();
        CommandLine commandLine = null;

        Option help = new Option(OPT_HELP, "help", false, "print this message.");

        Option silent = new Option(OPT_SILENT, false, "silent mode.");

        Option key = OptionBuilder.withArgName(ARG_KEY)
            .withLongOpt(ARG_KEY)
            .hasArg()
            .withDescription("use this static AES-128 key for the stream.")
            .create(OPT_KEY);

        Option iv = OptionBuilder.withArgName(ARG_IV)
            .hasArg()
            .withDescription("use this static AES-128 IV for the stream.")
            .create(OPT_IV);

        Option outFile = OptionBuilder.withArgName(ARG_OUT_FILE)
            .withLongOpt(OPT_OUT_FILE_LONG)
            .hasArg()
            .withDescription("file to write to.")
            .create(OPT_OUT_FILE);

        Options options = new Options();

        options.addOption(help);
        options.addOption(silent);
        options.addOption(key);
        options.addOption(iv);
        options.addOption(outFile);

        try
        {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption(OPT_HELP) || (commandLine.getArgs().length < 1))
            {
                new HelpFormatter().printHelp(SYNTAX, options);
                System.exit(0);
            }
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp(SYNTAX, options);
            System.exit(1);
        }

        return commandLine;
    }
}