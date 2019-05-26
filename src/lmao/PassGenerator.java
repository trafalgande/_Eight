package lmao;

import java.security.SecureRandom;
import java.util.Random;

public class PassGenerator {
        private static final Random RANDOM = new SecureRandom();
        private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        public String generatePassword(int length) {

            StringBuilder returnValue = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
            }
            return returnValue.toString();
    }
}
