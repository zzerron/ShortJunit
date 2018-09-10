package tests;


import main.Helper;
import main.Shortener;
import main.Solution;
import org.junit.Assert;
import org.junit.Test;
import strategy.HashBiMapStorageStrategy;
import strategy.HashMapStorageStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/*
 метод long getTimeForGettingIds(main.Shortener shortener, Set<String> strings, Set<Long> ids).
Должен возвращать время в миллисекундах необходимое для получения идентификаторов для всех строк из strings.
Идентификаторы должны быть записаны в ids.

 метод long getTimeForGettingStrings(main.Shortener shortener, Set<Long> ids, Set<String> strings).
 Должен возвращать время в миллисекундах необходимое для получения строк для всех строк из ids.
 Строки должны быть записаны в strings.

 тест testHashMapStorage() должен:

1. Создавать два объекта типа main.Shortener,
 один на базе HashMapStorageStrategy, второй на базе HashBiMapStorageStrategy. shortener1 и shortener2.
2. Генерировать с помощью main.Helper 10000 строк и помещать их в сет со строками, назовем его origStrings.
3. Получать время получения идентификаторов для origStrings
 (вызывать метод getTimeForGettingIds для shortener1, а затем для shortener2).
4. Проверять с помощью junit, что время, полученное в предыдущем пункте для shortener1 больше, чем для shortener2.
5. Получать время получения строк (вызывать метод getTimeForGettingStrings для shortener1 и shortener2).
6. Проверять с помощью junit, что время, полученное в предыдущем пункте
 для shortener1 примерно равно времени для shortener2.
 Используй метод assertEquals(float expected, float actual, float delta).
 В качестве delta можно использовать 30, этого вполне достаточно для наших экспериментов.
 */

public class SpeedTest {
    public long getTimeForGettingIds(Shortener shortener, Set<String> strings, Set<Long> ids){
        Date start = new Date();
        ids = Solution.getIds(shortener,strings);
        Date finish = new Date();
        Long delta = finish.getTime() - start.getTime();
        return delta;
    }
    public long getTimeForGettingStrings(Shortener shortener, Set<Long> ids, Set<String> strings){
        Date start = new Date();
        strings = Solution.getStrings(shortener,ids);
        Date finish = new Date();
        Long delta = finish.getTime() - start.getTime();
        return delta;
    }

    @Test
    public void testHashMapStorage(){
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        Shortener shortener1 = new Shortener(hashMapStorageStrategy);
        HashBiMapStorageStrategy hashBiMapStorageStrategy = new HashBiMapStorageStrategy();
        Shortener shortener2 = new Shortener(hashBiMapStorageStrategy);

        Set<String> origStrings = new HashSet<>();
        for (int i = 0; i < 10000; i ++){
            origStrings.add(Helper.generateRandomString());
        }

        Set<Long> ids1 = new HashSet<>();
        Set<Long> ids2 = new HashSet<>();

        long t1 = getTimeForGettingIds(shortener1, origStrings, ids1);
        long t2 = getTimeForGettingIds(shortener2, origStrings, ids2);

        Assert.assertTrue(t1 > t2);

        t1 = getTimeForGettingStrings(shortener1, ids1, new HashSet<String>());
        t2 = getTimeForGettingStrings(shortener2, ids2, new HashSet<String>());

        Assert.assertEquals(t1, t2, 30);
    }
}
