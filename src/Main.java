import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

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
        if(args.length < 2)
        {
            System.out.println("Args: playlistUrl outFile");
        }
        else
        {
            try
            {
                String playlistUrl = args[0];
                String outFile = args[1];

                File file = new File(outFile);

                if(file.exists())
                {
                    System.out.print("Input file exists.  Overwrite [y/n]? ");

                    int ch = System.in.read();

                    if(!(ch == 'y' || ch == 'Y'))
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
            }
        }

    }
}