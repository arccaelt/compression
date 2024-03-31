import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        String text = getText();
        huffmanExperiment(text);
    }

    private static void huffmanExperiment(String text) throws IOException {
        System.out.printf("Raw size: %d chars\n", text.length());

        HuffmanCoding huffmanCoding = new HuffmanCoding();
        String encoded = huffmanCoding.encode(text);

        int i = 0;
        int current = 0;
        int limit = encoded.length();
        ArrayList<Byte> bits = new ArrayList<>();

        while (i < limit) {
            int bit = Integer.valueOf(encoded.charAt(i++));
            current = (current << 1) | bit;
            if (i % 8 == 0) {
                bits.add((byte) current);
                current = 0;
            }
        }
        if (current > 0) {
            bits.add((byte) current);
        }
        FileOutputStream fos = new FileOutputStream("ENCODED");
        byte[] bytes = new byte[bits.size()];
        for (int j = 0; j < bits.size(); j++) {
            bytes[j] = bits.get(j);
        }
        fos.write(bytes, 0, bytes.length);

        System.out.printf("Size after encoding with HuffmanCoding: %d chars, that's a %d chars diff\n", encoded.length(), encoded.length() - text.length());
    }

    private static void rleExperiment(String text) {
        System.out.printf("Raw size: %d chars\n", text.length());

        RLEncoding rleEncoding = new RLEncoding();
        String rleEncoded = rleEncoding.encode(text);
        String rleDecoded = rleEncoding.decode(rleEncoded);

//        System.out.println(rleEncoded.substring(1000));
//        System.out.println(rleDecoded.substring(1000));

        System.out.printf("Size after encoding with RLE: %d chars, that's a %d chars diff\n", rleEncoded.length(), rleEncoded.length() - text.length());
    }

    private static String getText() throws IOException {
        return Files.readString(Paths.get("STUFF"));
    }

}
