import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String text = getText();
        huffmanExperiment(text);
    }

    private static void huffmanExperiment(String text) throws IOException {
        System.out.printf("Raw size: %d chars\n", text.length());

        HuffmanCoding huffmanCoding = new HuffmanCoding();
        String encoded = huffmanCoding.encode(text);

        writeEncodedHuffmanData(encoded);
        String readHuffman = readEncodedHuffman();


        System.out.println(huffmanCoding.decode(readHuffman).substring(0, 100));


        System.out.printf("Size after encoding with HuffmanCoding: %d chars, that's a %d chars diff\n", encoded.length(), encoded.length() - text.length());
    }

    private static void writeEncodedHuffmanData(String encoded) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("DATA")) {
            byte[] bytes = convertBytesToByteArray(mergeAllBitsIntoBytes(encoded));
            fos.write(bytes, 0, bytes.length);
        }
    }

    /*
        Java doesn't have a method for writing individual bits to a file, so what we have to do is to
        merge 8 bits into a byte and write that into the file. Remember, Huffman coding produces a list of codes made out of bits.

        NOTE: If we'd try to write the data as it was outputted by Huffman coding then the resulting file would be larger than the initial one,
        that's because for each bit we'll use 7 times more memory, because each bit would take a whole byte of memory.
        So, writing:
            01100
        would end up writing the following bytes
            00000000 00000001 00000001 00000000 00000000

        That's why these bits will be merged into a byte, and only that byte will be written to the file, i.e:
            01010101
     */
    private static List<Byte> mergeAllBitsIntoBytes(String encoded) {
        int i = 0;
        byte current = 0;
        int limit = encoded.length();
        int bitIndex = 0;
        List<Byte> bits = new ArrayList<>();

        while (i < limit) {
            int bit = encoded.charAt(i++) == '0' ? 0 : 1;
            current |= (byte) (bit << bitIndex++);
            if (bitIndex == 8) {
                bits.add(current);
                current = 0;
                bitIndex = 0;
            }
        }
        if (current > 0) {
            bits.add(current);
        }
        return bits;
    }

    private static byte[] convertBytesToByteArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int j = 0; j < byteList.size(); j++) {
            bytes[j] = byteList.get(j);
        }
        return bytes;
    }

    /*
        We have merged all the bits produced by huffman coding to bytes, so now we read all bytes, and for each one
        we'll decompose it into individual bits using bitwise operators.
     */
    private static String readEncodedHuffman() throws IOException {
        StringBuilder encodedBytes = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        try (FileInputStream fin = new FileInputStream("data")) {
            byte[] allBytes = fin.readAllBytes();
            for (byte encodedByte : allBytes) {
                for (int index = 0; index < 8; index++) {
                    int currentBit = (encodedByte >> index) & 1;
                    if (currentBit == 0) {
                        temp.append("0");
                    } else {
                        temp.append("1");
                    }
                }
                encodedBytes.append(temp);
                temp.setLength(0);
            }
        }
        if (!temp.isEmpty()) {
            encodedBytes.append(temp);
        }
        return encodedBytes.toString();
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
        return Files.readString(Paths.get("DATA"));
    }

}
