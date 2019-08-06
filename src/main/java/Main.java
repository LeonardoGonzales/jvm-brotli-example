import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliInputStream;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    private final static String sourceFilePath = "src/main/java/Main.java";
    private final static String encodedPath = "src/encoded.br";
    private final static String decodedPath = "src/decoded.txt";

    private final static String altSourceFilePath = "../src/main/java/Main.java";
    private final static String altEncodedPath = "../src/encoded.br";
    private final static String altDecodedPath = "../src/decoded.txt";

    // We have to try two different paths, depending if we're executing from IDE or command line
    public static void main(String[] args) throws Exception {
        BrotliLoader.loadBrotli();

        try { // Executing with project dir as base (probably executing from IDE)
            encode(sourceFilePath, encodedPath);
            decode(encodedPath, decodedPath);
        } catch (FileNotFoundException e) {
            try { // Executing with target dir as base, so we need to go up one level
                encode(altSourceFilePath, altEncodedPath);
                decode(altEncodedPath, altDecodedPath);
            } catch (FileNotFoundException e2) { // This shouldn't happen unless the src folder was deleted
                e2.printStackTrace();
            }
        }
    }

    private static void encode(String in, String out) throws IOException {
        // Init file input and output
        FileInputStream inFile = new FileInputStream(in);
        FileOutputStream outFile = new FileOutputStream(out);

        // If being used to compress streams in real-time, I do not advise a quality setting above 4 due to performance
        Encoder.Parameters params = new Encoder.Parameters().setQuality(4);

        // Initialize compressor by binding it to our file output stream
        BrotliOutputStream brotliOutputStream = new BrotliOutputStream(outFile, params);

        int read = inFile.read();
        while (read > -1) { // -1 means EOF
            brotliOutputStream.write(read);
            read = inFile.read();
        }

        // It's important to close the BrotliOutputStream object. This also closes the underlying FileOutputStream
        brotliOutputStream.close();
        inFile.close();
        System.out.println("Created new file '" + out + "'");
    }

    private static void decode(String in, String out) throws Exception {
        // Init file input and output
        FileInputStream inFile = new FileInputStream(in);
        FileOutputStream outFile = new FileOutputStream(out);

        // Initialize decompressor by binding it to our file input stream
        BrotliInputStream brotliInputStream = new BrotliInputStream(inFile);

        int read = brotliInputStream.read();
        while (read > -1) { // -1 means EOF
            outFile.write(read);
            read = brotliInputStream.read();
        }

        // It's important to close the BrotliInputStream object. This also closes the underlying FileInputStream
        brotliInputStream.close();
        outFile.close();
        System.out.println("Created new file '" + out + "'");
    }

}
