import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: Oct 1, 2010
 * Time: 7:45:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TsDownloader
{
    private KeyInfo keyInfo;
    private Cipher cipher;
 
    public TsDownloader(KeyInfo keyInfo)
    {
        this.keyInfo = keyInfo;
        initCrypto();
    }

    private void initCrypto()
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        AlgorithmParameterSpec spec = new IvParameterSpec(keyInfo.getIV());

        try
        {
            SecretKey secretKey = new SecretKeySpec(keyInfo.getKey(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void download(URL segmentUrl, String outFile) throws IOException
    {
        byte[] buffer = new byte[512];

        InputStream cis = new CipherInputStream(segmentUrl.openStream(), cipher);

        File file = new File(outFile);

        FileOutputStream out = new FileOutputStream(outFile, file.exists());

        System.out.println("segmentUrl = " + segmentUrl);

        int read;

        while ((read = cis.read(buffer)) >= 0)
        {
            out.write(buffer, 0, read);
        }

        System.out.println("---");

        cis.close();
        out.close();
    }
}
