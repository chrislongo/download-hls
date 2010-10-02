/**
 * User: chris
 * Date: Oct 2, 2010
 * Time: 3:47:41 PM
 */
public class CryptoException extends RuntimeException
{
    public CryptoException()
    {
        super();
    }

    public CryptoException(String message)
    {
        super(message);
    }

    public CryptoException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CryptoException(Throwable cause)
    {
        super(cause);   
    }
}
