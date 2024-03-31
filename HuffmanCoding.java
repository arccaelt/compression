import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

record Node(String symbol, int weight, Node left, Node right) {

    public Node join(Node other) {
        return new Node(symbol + other.symbol, weight + other.weight, this, other);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}

public class HuffmanCoding {

    /*
        The idea of huffman coding is to assign codes to each letter in a given text,
        with the most frequent letters receiving shorter codes, and the less frequent letters longer codes,
        thus allowing for a more efficient use of space.

        SAMPLE STRING = "Ana are mere"

        The first step is to compute how many times a letter appears in the given text.
            A = 1
            a = 2
            n = 1
            r = 2
            e = 3
            space = 2

       The next step is to compute a hoffman tree. A hoffman tree is a binary tree that'll use to get the code for each letter.
       Initially, each node will be a leaf node. Each node has:
            - a symbol
            - a weight
            - (optionally) at most two children nodes

       For the sample string, we'll start with:
            [A 1] [n 1] [a 2] [space 2]  [r 2] [e 3]

       We'll loop over these nodes until only one will be left. That node will be the root of the tree.
       For efficiency, use a priority queue.
       With each iteration, we'll take the lowest weighted two nodes and join them, putting the resulting node back into the queue.
            First iteration:
                We take [A 1] [n 1] and merge them resulting in [An 2] ( we merged their symbols and added their weights)
                [a 2] [space 2] [An 2] [r 2] [e 3]
            Second iteration:
                We take [a 2] and [space 2] and merge them in [aspace 4]
                [An 2] [r 2] [e 3] [aspace 4]
            Third iteration:
                We take [An 2] and [r 2] -> [Anr 4]
                [e 3] [aspace 4] [Anr 4]
            Forth iteration:
                We take [e 3] [aspace 4] -> [easpace 7]
                [Anr 4] [easpace 7]
            Last iteration:
                We take  [Anr 4] [easpace 7] -> [Anreaspace 10]

       We found the root of the tree.
       Not, if we want to get the code for a symbol, all we have to do is to traverse the tree, appending 0 if we take a left branch or 1 if we take a right branch,
       along the way until we get to the desired leaf node.
     */

    private Node root;

    public String encode(String raw) {
        Map<Character, Integer> frequency = getFrequency(raw);
        root = getRoot(frequency);
        Map<Character, String> codes = new HashMap<>();
        getHuffmanCodes(root, "", codes);
        System.out.println(codes);
        return encode(raw, codes);
    }

    private String encode(String text, Map<Character, String> huffmanCodes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0, len = text.length(); i < len; i++) {
            stringBuilder.append(huffmanCodes.get(text.charAt(i)));
        }

        return stringBuilder.toString();
    }

    // This method will build a look-up, allowing for an easier retrieval of the huffman code for each letter in the given text.
    private void getHuffmanCodes(Node root, String code, Map<Character, String> codes) {
        if (root == null) {
            return;
        } else {
            if (root.left() == null && root.right() == null) {
                codes.put(root.symbol().charAt(0), code);
            } else {
                getHuffmanCodes(root.left(), code + "0", codes);
                getHuffmanCodes(root.right(), code + "1", codes);
            }
        }
    }

    private Node getRoot(Map<Character, Integer> frequency) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::weight));

        // initialize the queue
        for (Map.Entry<Character, Integer> characterIntegerEntry : frequency.entrySet()) {
            priorityQueue.add(new Node(String.valueOf(characterIntegerEntry.getKey()), characterIntegerEntry.getValue(), null, null));
        }

        // loop over until we find the root
        while (priorityQueue.size() > 1) {
            Node firstNode = priorityQueue.remove();
            Node secondNode = priorityQueue.remove();
            priorityQueue.add(firstNode.join(secondNode));
        }

        return priorityQueue.peek();
    }

    private Map<Character, Integer> getFrequency(String text) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (int i = 0, len = text.length(); i < len; i++) {
            char letter = text.charAt(i);
            frequency.put(letter, frequency.getOrDefault(letter, 0) + 1);
        }
        return frequency;
    }

    /*
        To decode a string compressed with Huffman coding all we have to do is to traverse the tree we built to encode it in the first place.
        To do that, we iterate over the text from left to right, and we'll use a variable to store the current node we reached in the Huffman tree.
        Initially this variable points to root. If the current letter in the message is 0 then we go to the left child, if it's 1 then we go to the right child.
        If the current is a leaf then we decoded the current symbol, we append it to the decoded message and reset the variable back to root.
     */
    public String decode(String encoded) {
        StringBuilder stringBuilder = new StringBuilder();

        Node current = root;
        for (int i = 0, len = encoded.length(); i < len; i++) {
            if (encoded.charAt(i) == '0') {
                current = current.left();
            }
            if (encoded.charAt(i) == '1') {
                current = current.right();
            }

            // We've reached a leaf node therefore we must reset the current node
            if (current.isLeaf()) {
                stringBuilder.append(current.symbol());
                current = root;
            }
        }

        return stringBuilder.toString();
    }
}
