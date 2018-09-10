package main;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import strategy.*;
import tests.FunctionalTest;
import tests.SpeedTest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        testStrategy(hashMapStorageStrategy, 10000);

        OurHashMapStorageStrategy ourHashMapStorageStrategy = new OurHashMapStorageStrategy();
        testStrategy(ourHashMapStorageStrategy, 10000);

        FileStorageStrategy fileStorageStrategy= new FileStorageStrategy();
        testStrategy(fileStorageStrategy, 100);

        OurHashBiMapStorageStrategy ourHashBiMapStorageStrategy = new OurHashBiMapStorageStrategy();
        testStrategy(ourHashBiMapStorageStrategy, 10000);

        HashBiMapStorageStrategy hashBiMapStorageStrategy = new HashBiMapStorageStrategy();
        testStrategy(hashBiMapStorageStrategy, 10000);

        DualHashBidiMapStorageStrategy dualHashBidiMapStorageStrategy = new DualHashBidiMapStorageStrategy();
        testStrategy(dualHashBidiMapStorageStrategy, 10000);

        System.out.println("");

        JUnitCore runner = new JUnitCore();
        Result result = runner.run(FunctionalTest.class);
        System.out.println("run tests: " + result.getRunCount());
        System.out.println("failed tests: " + result.getFailureCount());
        System.out.println("ignored tests: " + result.getIgnoreCount());
        System.out.println("success: " + result.wasSuccessful());
        System.out.println("");
        Result result2 = runner.run(SpeedTest.class);
        System.out.println("run tests: " + result2.getRunCount());
        System.out.println("failed tests: " + result2.getFailureCount());
        System.out.println("ignored tests: " + result2.getIgnoreCount());
        System.out.println("success: " + result2.wasSuccessful());
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings){
        Set<Long> set = new HashSet<>();
        for (String s : strings){
            set.add(shortener.getId(s));
        }
        return set;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys){
        Set<String> set = new HashSet<>();
        for (Long l : keys){
            set.add(shortener.getString(l));
        }
        return set;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber){
        Helper.printMessage(strategy.getClass().getSimpleName());

        Set<String> list = new HashSet<>();
        for (int i = 0; i < elementsNumber; i ++){
            list.add(Helper.generateRandomString());
        }

        Shortener shortener = new Shortener(strategy);

        Date start = new Date();
        Set<Long> ids = getIds(shortener,list);
        Date finish = new Date();
        Long delta = finish.getTime() - start.getTime();
        Helper.printMessage(delta.toString());

        start = new Date();
        Set<String> strings = getStrings(shortener,ids);
        finish = new Date();
        delta = finish.getTime() - start.getTime();
        Helper.printMessage(delta.toString());

        if (strings.equals(list)){
            Helper.printMessage("Тест пройден.");
        }
        else{
            Helper.printMessage("Тест не пройден.");
        }
    }
}
