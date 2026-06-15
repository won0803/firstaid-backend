package seeya.insight.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESCipher {
    private static volatile AESCipher INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(AESCipher.class);
    private static byte[] skeyArray = null;

    // Random한 키 생성
    public static byte[] getAESRandomKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            generator.init(128, random);
            Key secretKey = generator.generateKey();

            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("detail", nsae);
            return null;
        }
    }

    // 지정 Key 생성
    public static byte[] getSelectKey() {
        String key = "AirportDashboard"; // 16자리 이여야 함.ㄱ

        return key.getBytes();
    }

    // 암호화
    public static String encrypt(String message) {
        try {
            if (skeyArray == null) {
                // 랜덤키 사용
                // skeyArray = getAESRandomKey();
                // 지정키 사용
                skeyArray = getSelectKey();
            }
            SecretKeySpec skeySpec = new SecretKeySpec(skeyArray, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(message.getBytes()); // encrypt

            String encodeString = DatatypeConverter.printBase64Binary(encrypted); // Converts an array of bytes into a
                                                                                  // string.

            return encodeString;
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("detail", nsae);
            return null;
        } catch (NoSuchPaddingException nspe) {
            logger.error("detail", nspe);
            return null;
        } catch (InvalidKeyException ike) {
            logger.error("detail", ike);
            return null;
        } catch (IllegalBlockSizeException ibse) {
            logger.error("detail", ibse);
            return null;
        } catch (BadPaddingException bpe) {
            logger.error("detail", bpe);
            return null;
        }
    }

    // 복호화
    public static String decrypt(String message) {
        try {
            if (skeyArray == null) {
                return null;
            } else {
                SecretKeySpec skeySpec = new SecretKeySpec(skeyArray, "AES");

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);

                byte[] decodeBytes = DatatypeConverter.parseBase64Binary(message); // Converts the string argument into
                                                                                   // an array of bytes.
                byte[] decryptBytes = cipher.doFinal(decodeBytes); // decrypt

                return new String(decryptBytes);
            }
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("detail", nsae);
            return null;
        } catch (NoSuchPaddingException nspe) {
            logger.error("detail", nspe);
            return null;
        } catch (InvalidKeyException ike) {
            logger.error("detail", ike);
            return null;
        } catch (IllegalBlockSizeException ibse) {
            logger.error("detail", ibse);
            return null;
        } catch (BadPaddingException bpe) {
            logger.error("detail", bpe);
            return null;
        }
    }

    public static AESCipher getInstance() {
        if (INSTANCE == null) {
            synchronized (AESCipher.class) {
                if (INSTANCE == null)
                    INSTANCE = new AESCipher();
            }
        }
        return INSTANCE;
    }
}