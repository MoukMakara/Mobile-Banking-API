package co.istad.mbanking.util;

import java.util.Random;

public class RandomUtil {

    public static String random6Digits() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

}
