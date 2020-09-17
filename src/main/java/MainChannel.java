import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliDecoderChannel;
import com.nixxcode.jvmbrotli.enc.BrotliEncoderChannel;
import com.nixxcode.jvmbrotli.enc.Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author lgonzales
 * @since 17/09/2020
 */
public class MainChannel {

    public static void main(String[] args) throws IOException {
        if (BrotliLoader.isBrotliAvailable()) { // make sure you always check if brotli is available
            String testInput = "Brotli is a data format specification for data streams " +
                    "compressed with a specific combination of the general-purpose LZ77 " +
                    "lossless compression algorithm, Huffman coding and 2nd order context " +
                    "modelling. Brotli is a compression algorithm developed by Google " +
                    "and works best for text compression ";
            byte[] input = testInput.getBytes();
            byte[] compressed = brotliCompress(input);
            byte[] result = brotliDecompress(compressed);
            String resultStr = new String(result);

            if(resultStr.equals(testInput)){
                System.out.println("worked fine");
                System.out.println("Input Size: "+input.length);
                System.out.println("Compressed Size: "+compressed.length);
            } else {
                System.out.println("Something went wrong");
            }

        } else {
            System.out.println("please, install brotli");
        }
    }

    static byte[] brotliCompress(byte[] in) throws IOException {
        Encoder.Parameters params = new Encoder.Parameters(); //default quality config
        try (ByteArrayOutputStream dst = new ByteArrayOutputStream()) {
            try (WritableByteChannel encoder =
                         new BrotliEncoderChannel(Channels.newChannel(dst), params)) {
                encoder.write(ByteBuffer.wrap(in));
            }
            return dst.toByteArray();
        }
    }

    static byte[] brotliDecompress(byte[] in) throws IOException {
        try (ReadableByteChannel src = Channels.newChannel(new ByteArrayInputStream(in));
             ReadableByteChannel decoder = new BrotliDecoderChannel(src);
             InputStream resultStream = Channels.newInputStream(decoder);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int read = resultStream.read();
            while (read > -1) { // -1 means EOF
                out.write(read);
                read = resultStream.read();
            }
            out.flush();
            return out.toByteArray();
        }
    }
}
