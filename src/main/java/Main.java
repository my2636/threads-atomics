import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;


public class Main {
    static AtomicInteger[] goodNickCounts = new AtomicInteger[]{new AtomicInteger(), new AtomicInteger(), new AtomicInteger()};

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<String> goodTexts = Arrays.stream(texts).filter(Main::isGoodNick).toList();

        List<Predicate<String>> functions = new ArrayList<>();

        functions.add(s -> s.length() == 3);
        functions.add(s -> s.length() == 4);
        functions.add(s -> s.length() == 5);

        for (Predicate<String> p : functions) {
            int pIndex = functions.indexOf(p);
            new Thread(() -> {
                for (String t : goodTexts) {
                    if (p.test(t)) {
                        goodNickCounts[pIndex].getAndIncrement();
                    }
                }
            }).start();
        }

        for (int i = 0; i < goodNickCounts.length; i++) {
            System.out.printf("Красивых слов с длиной %d: %s шт\n", i + 3, goodNickCounts[i]);
        }
    }

    static boolean isGoodNick(String s) {
        return s.contentEquals(new StringBuilder(s).reverse()) || (s.chars().allMatch(x -> x == s.charAt(0)) ||
                s.chars().anyMatch(x -> x != s.charAt(0)) && IntStream.range(0, s.length() - 1).allMatch(i -> s.charAt(i) <= s.charAt(i + 1)));
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}