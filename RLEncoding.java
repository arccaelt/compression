public class RLEncoding {

    public String encode(String raw) {
        StringBuilder encoded = new StringBuilder();

        int currentIndex = 1;
        int limit = raw.length();
        int digit = 0;

        int count = 1;
        char currentChar = raw.charAt(0);

        while (currentIndex < limit) {
            char letter = raw.charAt(currentIndex++);
            if (Character.isDigit(letter)) {
                digit = digit * 10 + (letter - '0');
            } else {
                if (digit > 0) {
                    encoded.append("\\").append(digit).append("\\");
                    digit = 0;
                }
                if (letter == currentChar) {
                    count++;
                } else {
                    encoded.append(count).append(currentChar);
                    count = 1;
                    currentChar = letter;
                }
            }
        }
        encoded.append(count).append(currentChar);

        return encoded.toString();
    }

    public String decode(String encoded) {
        StringBuilder decoded = new StringBuilder();

        int currentIndex = 0;
        int limit = encoded.length();
        int amount = 0;
        boolean isPossibleNumber = false;

        while (currentIndex < limit) {
            char letter = encoded.charAt(currentIndex++);
            if (letter == '\\') {
                isPossibleNumber = true;
            } else {
                if (Character.isDigit(letter)) {
                    amount = amount * 10 + (letter - '0');
                } else {
                    if (letter == '\\' && isPossibleNumber) {
                        decoded.append(amount);
                    } else {
                        for (int i = 0; i < amount; i++) {
                            decoded.append(letter);
                        }
                    }
                    amount = 0;
                }
            }
        }

        return decoded.toString();
    }
}
