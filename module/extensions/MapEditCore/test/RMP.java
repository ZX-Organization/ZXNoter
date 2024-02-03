import org.xxtea.XXTEA;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.InflaterInputStream;

public class RMP {
    public static void main(String[] args) throws IOException {
        String encodedData = new String(RMP.class.getResourceAsStream("jisumengxiang_5k_hd.rmp").readAllBytes(), StandardCharsets.UTF_8);

        // 解码Base64数据
        byte[] Base64DecodedBytes = Base64.getDecoder().decode(encodedData);

        String songName = "jisumengxiang_5k_hd";


        String key = "RMP4TT3RN" + songName;
        byte[] keyBytes = key.getBytes();

        // 解密数据
        byte[] decodedBytes = XXTEA.decrypt(Base64DecodedBytes, keyBytes);

        byte[] finalBase64DecodedBytes = Base64.getDecoder().decode(decodedBytes);

        byte[] decompressedData = zlibDecompress(finalBase64DecodedBytes);
        String decodedData = new String(decompressedData);
        System.out.println("Decoded data: " + decodedData);


    }

    public static byte[] zlibDecompress(byte[] compressedData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inflaterInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        inflaterInputStream.close();
        outputStream.close();

        return outputStream.toByteArray();
    }
}
