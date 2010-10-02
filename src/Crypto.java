import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: chris
 * Date: Oct 2, 2010
 * Time: 2:17:58 PM
 */
public class Crypto
{
    private Cipher cipher;
    private String keyUrl;
    private byte[] key;
    private byte[] iv;

    public Crypto()
    {
    }

    public CipherInputStream wrapInputStream(InputStream in)
    {
        return new CipherInputStream(in, cipher);
    }

    public boolean hasKey()
    {
        return (key != null);    
    }

    public String getCurrentKey()
    {
        return arrayToString(key);
    }

    public String getCurrentIV()
    {
        return arrayToString(iv);
    }

    public void updateKeyString(String keyString) throws CryptoException
    {
        String regex =
            "METHOD=([A-Z0-9-]+)" +
            ",URI=\"(https?://([-\\w\\.]+)+(:\\d+)?(/([\\w/_~\\.]*(\\?\\S+)?)?)?)\"" +
            ",IV=0x([0-9a-f]+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(keyString);

        while(matcher.find())
        {
            try
            {
                String newKeyUrl = matcher.group(2);

                if(!newKeyUrl.equals(keyUrl))
                {
                    keyUrl = newKeyUrl;
                    fetchKey();
                }
            }
            catch (Exception e)
            {
               throw new CryptoException(e);
            }

            String ivString = matcher.group(8);
            iv = unpack(ivString);
        }

        init();
    }

    private void fetchKey() throws IOException
    {
        this.key = new byte[16];

        URLConnection con = new URL(this.keyUrl).openConnection();
        InputStream in = new BufferedInputStream(con.getInputStream());

        int read = 0;
        int offset = 0;

        while(offset < 16)
        {
            if((read = in.read(this.key, offset, key.length - offset)) == -1)
                break;

            offset += read;
        }

        in.close();
    }

    private void init() throws CryptoException
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        AlgorithmParameterSpec spec = new IvParameterSpec(iv);

        try
        {
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        }
        catch (Exception e)
        {
           throw new CryptoException(e);
        }
    }

    private byte[] unpack(String hexString)
    {
        byte[] array = new byte[16];

        BigInteger bigInt = new BigInteger(hexString, 16);
        BigInteger andValue = new BigInteger("ff", 16);

        for(int i = 0, index = 15; i < 16; i++, index--)
        {
            BigInteger shift = bigInt.shiftRight(i * 8);
            shift = shift.and(andValue);
            array[index] = shift.byteValue();
        }

        return array;
    }

    private String arrayToString(byte[] array)
    {
        StringBuilder sb = new StringBuilder();

        if(array != null)
        {
            for (int i = 0; i < array.length; i++)
            {
                byte b = array[i];
                sb.append(String.format("%02x", b));
            }
        }
        
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "Crypto{" +
            "cipher=" + cipher +
            ", keyUrl='" + keyUrl + '\'' +
            ", key=" + arrayToString(key) +
            ", iv=" + arrayToString(iv) +
            '}';
    }
}
