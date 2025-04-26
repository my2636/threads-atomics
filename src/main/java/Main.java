import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


public class Main {
    static AtomicInteger length3 = new AtomicInteger(0);
    static AtomicInteger length4 = new AtomicInteger(0);
    static AtomicInteger length5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread isPalindrome = new Thread(() -> {
            Arrays.stream(texts)
                    .filter((x -> x.contentEquals(new StringBuilder(x))))
                    .forEach(Main::countGoodNickLength);

        });

        Thread isOneLetter = new Thread(() -> {
            Arrays.stream(texts)
                    .filter(s -> s.chars().allMatch(x -> x == s.charAt(0)))
                    .forEach(Main::countGoodNickLength);

        });

        Thread isAscLetters = new Thread(() -> {
            Arrays.stream(texts)
                    .filter(s -> s.chars().anyMatch(x -> x != s.charAt(0))
                            && IntStream.range(0, s.length() - 1).allMatch(i -> s.charAt(i) <= s.charAt(i + 1)))
                    .forEach(Main::countGoodNickLength);

        });

        isPalindrome.start();
        isOneLetter.start();
        isAscLetters.start();

        isAscLetters.join();
        isOneLetter.join();
        isPalindrome.join();


        System.out.println("Красивых слов длиной 3: " + length3);
        System.out.println("Красивых слов длиной 4: " + length4);
        System.out.println("Красивых слов длиной 5: " + length5);
    }

    static void countGoodNickLength(String s) {
        switch (s.length()) {
            case 3:
                System.out.println(s.length() + "длина");
                System.out.println(s);
                System.out.println(length3.incrementAndGet());
                break;
            case 4:
                System.out.println(s.length() + "длина");
                System.out.println(s);
                length4.incrementAndGet();
                break;
            case 5:
                System.out.println(s.length() + "длина");
                System.out.println(s);
                length5.incrementAndGet();
                break;
        }
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