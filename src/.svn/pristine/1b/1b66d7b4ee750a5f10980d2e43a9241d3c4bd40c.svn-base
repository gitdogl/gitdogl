package weaver.interfaces.encode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class FBRSA implements IEncode {
    private static final int FRAGMENT_LENGTH = 245;

    public String decode(String paramString) {
        return "";
    }

    public boolean setPwd(String paramString) {
        return true;
    }

    public boolean setIv(String paramString) {
        return true;
    }

    public String encode(String paramString) {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA284wavItAjiQkJRzLAtYfXFMqU/QhUv4\n" +
                "5f0ERN2mpLqhEb57LPMNcsWgZvgNUk7/Dp3hiUQbuXpJNjxARqTuSe4Q2Vw2TIhjD03+FMXogAkx\n" +
                "MCQO2DIgiRT+T4W2VARbdnX70eDbXcP3tVSn6UJM8EAEFm89A7MR3wujo9VBhSEHqN6u3kp4eWAd\n" +
                "6ha3LEPtfBPlK1tOhMDjXsWDseJ+hRPOM3hyofme9VGfLinydCrIoxCmFU/pXv/KdhJXn4Cw7Hq4\n" +
                "ilmm4xuBA+qfUU/whFcHKvVq/Pc/HBOv3sBwVLYmQ2A3SWQUaKvFqR3x46DN4eW9O8xbj7gtNZO2\n" +
                "/ZIhnwIDAQAB";

        String encrypt = encrypt(paramString, publicKey);
        System.out.println("encrypted username: " + encrypt);

        String encode = null;
        try {
            encode = URLEncoder.encode(encrypt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("ssoToken: " + encode);
        return encode;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String text = System.getProperty("text");
        String key = System.getProperty("publicKey");
        String username = text == null ? "123456" : text;
        String defaultKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA284wavItAjiQkJRzLAtYfXFMqU/QhUv4\n" +
                "5f0ERN2mpLqhEb57LPMNcsWgZvgNUk7/Dp3hiUQbuXpJNjxARqTuSe4Q2Vw2TIhjD03+FMXogAkx\n" +
                "MCQO2DIgiRT+T4W2VARbdnX70eDbXcP3tVSn6UJM8EAEFm89A7MR3wujo9VBhSEHqN6u3kp4eWAd\n" +
                "6ha3LEPtfBPlK1tOhMDjXsWDseJ+hRPOM3hyofme9VGfLinydCrIoxCmFU/pXv/KdhJXn4Cw7Hq4\n" +
                "ilmm4xuBA+qfUU/whFcHKvVq/Pc/HBOv3sBwVLYmQ2A3SWQUaKvFqR3x46DN4eW9O8xbj7gtNZO2\n" +
                "/ZIhnwIDAQAB";

        String publicKey = key == null ? defaultKey : key;

        String encrypt = encrypt(username, publicKey);
        System.out.println("encrypted username: " + encrypt);

        String encode = URLEncoder.encode(encrypt, "UTF-8");
        System.out.println("ssoToken: " + encode);
    }

    public static String encrypt(String plainText, String customPublicKey) {
        return encrypt(plainText, string2PublicKey(customPublicKey));
    }

    public static byte[] encrypt(byte[] plainTextData, Key publicKey) {
        if (plainTextData.length == 0)
            return plainTextData;
        try {
            Cipher c1 = Cipher.getInstance("RSA");
            c1.init(1, publicKey);
            return dealEncryptFragment(plainTextData, c1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String plainText, Key publicKey) {
        if ((plainText == null) || ("".equals(plainText))) {
            return plainText;
        }
        byte[] publicEncrypt = encrypt(plainText.getBytes(StandardCharsets.UTF_8), publicKey);
        return Base64.getEncoder().encodeToString(publicEncrypt);
    }

    public static PublicKey string2PublicKey(String pubStr) {
        try {
            byte[] keyBytes = base642Byte(pubStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] base642Byte(String base64Key) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }

    private static byte[] dealEncryptFragment(byte[] data, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] result = new byte[0];

        for (int i = 0; i < data.length; i += 245) {
            byte[] fragment = subarray(data, i, i + 245);
            byte[] update = cipher.doFinal(fragment);
            result = addAll(result, update);
        }
        return result;
    }

    public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }

        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }

        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new byte[0];
        }
        byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    public static byte[] addAll(byte[] array1, byte[] array2) {
        if (array1 == null)
            return clone(array2);
        if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static byte[] clone(byte[] array) {
        return array == null ? null : (byte[]) (byte[]) array.clone();
    }

    static class BASE64Decoder extends CharacterDecoder {
        private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        private static final byte[] pem_convert_array = new byte[256];
        byte[] decode_buffer = new byte[4];

        protected int bytesPerAtom() {
            return 4;
        }

        protected int bytesPerLine() {
            return 72;
        }

        protected void decodeAtom(PushbackInputStream var1, OutputStream var2, int var3) throws Exception {
            byte var5 = -1;
            byte var6 = -1;
            byte var7 = -1;
            byte var8 = -1;
            int var4;
            if (var3 < 2) {
                throw new Exception("BASE64Decoder: Not enough bytes for an atom.");
            }
            do {
                var4 = var1.read();
                if (var4 == -1)
                    throw new Exception();
            }
            while ((var4 == 10) || (var4 == 13));

            this.decode_buffer[0] = (byte) var4;
            var4 = readFully(var1, this.decode_buffer, 1, var3 - 1);
            if (var4 == -1) {
                throw new Exception();
            }
            if ((var3 > 3) && (this.decode_buffer[3] == 61)) {
                var3 = 3;
            }

            if ((var3 > 2) && (this.decode_buffer[2] == 61)) {
                var3 = 2;
            }

            switch (var3) {
                case 4:
                    var8 = pem_convert_array[(this.decode_buffer[3] & 0xFF)];
                case 3:
                    var7 = pem_convert_array[(this.decode_buffer[2] & 0xFF)];
                case 2:
                    var6 = pem_convert_array[(this.decode_buffer[1] & 0xFF)];
                    var5 = pem_convert_array[(this.decode_buffer[0] & 0xFF)];
            }
            switch (var3) {
                case 2:
                    var2.write((byte) (var5 << 2 & 0xFC | var6 >>> 4 & 0x3));
                    break;
                case 3:
                    var2.write((byte) (var5 << 2 & 0xFC | var6 >>> 4 & 0x3));
                    var2.write((byte) (var6 << 4 & 0xF0 | var7 >>> 2 & 0xF));
                    break;
                case 4:
                    var2.write((byte) (var5 << 2 & 0xFC | var6 >>> 4 & 0x3));
                    var2.write((byte) (var6 << 4 & 0xF0 | var7 >>> 2 & 0xF));
                    var2.write((byte) (var7 << 6 & 0xC0 | var8 & 0x3F));
            }
        }

        static {
            for (int var0 = 0; var0 < 255; var0++) {
                pem_convert_array[var0] = -1;
            }

            for (int var0 = 0; var0 < pem_array.length; var0++)
                pem_convert_array[pem_array[var0]] = (byte) var0;
        }
    }

    static abstract class CharacterDecoder {
        protected abstract int bytesPerAtom();

        protected abstract int bytesPerLine();

        protected void decodeBufferPrefix(PushbackInputStream var1, OutputStream var2)
                throws Exception {
        }

        protected void decodeBufferSuffix(PushbackInputStream var1, OutputStream var2)
                throws Exception {
        }

        protected int decodeLinePrefix(PushbackInputStream var1, OutputStream var2)
                throws Exception {
            return bytesPerLine();
        }

        protected void decodeLineSuffix(PushbackInputStream var1, OutputStream var2) throws Exception {
        }

        protected void decodeAtom(PushbackInputStream var1, OutputStream var2, int var3) throws Exception {
            throw new Exception();
        }

        protected int readFully(InputStream var1, byte[] var2, int var3, int var4) throws Exception {
            for (int var5 = 0; var5 < var4; var5++) {
                int var6 = var1.read();
                if (var6 == -1) {
                    return var5 == 0 ? -1 : var5;
                }

                var2[(var5 + var3)] = (byte) var6;
            }

            return var4;
        }

        public void decodeBuffer(InputStream var1, OutputStream var2) throws Exception {
            int var4 = 0;
            int var3 = 0;
            PushbackInputStream var5 = new PushbackInputStream(var1);
            decodeBufferPrefix(var5, var2);
            try {
                while (true) {
                    int var6 = decodeLinePrefix(var5, var2);

                    for (var3 = 0; var3 + bytesPerAtom() < var6; var3 += bytesPerAtom()) {
                        decodeAtom(var5, var2, bytesPerAtom());
                        var4 += bytesPerAtom();
                    }

                    if (var3 + bytesPerAtom() == var6) {
                        decodeAtom(var5, var2, bytesPerAtom());
                        var4 += bytesPerAtom();
                    } else {
                        decodeAtom(var5, var2, var6 - var3);
                        var4 += var6 - var3;
                    }

                    decodeLineSuffix(var5, var2);
                }
            } catch (Exception var8) {
                decodeBufferSuffix(var5, var2);
            }
        }

        public byte[] decodeBuffer(String var1)
                throws Exception {
            byte[] var2 = new byte[var1.length()];
            var1.getBytes(0, var1.length(), var2, 0);
            ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            decodeBuffer(var3, var4);
            return var4.toByteArray();
        }

        public byte[] decodeBuffer(InputStream var1) throws Exception {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            decodeBuffer(var1, var2);
            return var2.toByteArray();
        }

        public ByteBuffer decodeBufferToByteBuffer(String var1) throws Exception {
            return ByteBuffer.wrap(decodeBuffer(var1));
        }

        public ByteBuffer decodeBufferToByteBuffer(InputStream var1) throws Exception {
            return ByteBuffer.wrap(decodeBuffer(var1));
        }
    }
}
