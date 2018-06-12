package com.gsnathan.torchlight;

/**
 * Created by Gokul Swaminathan on 3/28/2018.
 */

public class MorseCode {

    private static String[] alpha = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "0", "!", ",", "?", ".", "'"};

    private static String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
            "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.",
            "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-",
            "-.--", "--..", ".----", "..---", "...--", "....-", ".....",
            "-....", "--...", "---..", "----.", "-----", "-.-.--", "--..--", "..--..", ".-.-.-", ".----.",};

    public static String decode(String morseCode) {
        String build = "";
        String change = morseCode.trim();
        String[] words = change.split("   ");
        for (String word : words) {
            for (String letter : word.split(" ")) {
                for (int x = 0; x < morse.length; x++) {
                    if (letter.equals(morse[x]))
                        build = build + alpha[x];
                }
            }
            build += " ";
        }

        return build.toUpperCase();
    }

    public static String decodeEnglish(String englishCode) {
        String build = "";
        String change = englishCode.trim();
        String[] words = change.split(" ");
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                for (int x = 0; x < morse.length; x++) {
                    if (word.substring(i, i + 1).equalsIgnoreCase(alpha[x])) {
                        build = build + morse[x] + " ";
                    }
                }
            }
            build += "  ";
        }
        return build;
    }
}