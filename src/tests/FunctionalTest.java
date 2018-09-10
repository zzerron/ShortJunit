package tests;


import main.Helper;
import main.Shortener;
import org.junit.Assert;
import org.junit.Test;
import strategy.HashMapStorageStrategy;
import strategy.OurHashBiMapStorageStrategy;
import strategy.HashBiMapStorageStrategy;
import strategy.OurHashMapStorageStrategy;
import strategy.FileStorageStrategy;
import strategy.DualHashBidiMapStorageStrategy;




/*
 метод testStorage(main.Shortener shortener)должен:
 Создавать три строки. Текст 1 и 3 строк должен быть одинаковым.
 Получать и сохранять идентификаторы для всех трех строк с помощью shortener.
 Проверять, что идентификатор для 2 строки не равен идентификатору для 1 и 3 строк.

 Проверять, что идентификаторы для 1 и 3 строк равны.

 Получать три строки по трем идентификаторам с помощью shortener.
 Проверять, что строки, полученные в предыдущем пункте, эквивалентны оригинальным.

 тесты:
 testHashMapStorageStrategy()
 testOurHashMapStorageStrategy()
 testFileStorageStrategy()
 testHashBiMapStorageStrategy()
 testDualHashBidiMapStorageStrategy()
 testOurHashBiMapStorageStrategy()
 */

public class FunctionalTest {


    public void testStorage(Shortener shortener){
        String string1 = Helper.generateRandomString();
        String string2 = Helper.generateRandomString();
        String string3 = string1;

        Long id1 = shortener.getId(string1);
        Long id2 = shortener.getId(string2);
        Long id3 = shortener.getId(string3);

        Assert.assertNotEquals(id2, id1);
        Assert.assertNotEquals(id2, id3);

        Assert.assertEquals(id1, id3);

        String lastString1 = shortener.getString(id1);
        String lastString2 = shortener.getString(id2);
        String lastString3 = shortener.getString(id3);

        Assert.assertEquals(lastString1, string1);
        Assert.assertEquals(lastString2, string2);
        Assert.assertEquals(lastString3, string3);
    }
    @Test
    public void testHashMapStorageStrategy(){
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        Shortener shortener = new Shortener(hashMapStorageStrategy);
        testStorage(shortener);
    }
    @Test
    public void testOurHashMapStorageStrategy(){
        OurHashMapStorageStrategy ourHashMapStorageStrategy = new OurHashMapStorageStrategy();
        Shortener shortener = new Shortener(ourHashMapStorageStrategy);
        testStorage(shortener);
    }
    @Test
    public void testFileStorageStrategy(){
        FileStorageStrategy fileStorageStrategy = new FileStorageStrategy();
        Shortener shortener = new Shortener(fileStorageStrategy);
        testStorage(shortener);
    }
    @Test
    public void testHashBiMapStorageStrategy(){
        HashBiMapStorageStrategy hashBiMapStorageStrategy = new HashBiMapStorageStrategy();
        Shortener shortener = new Shortener(hashBiMapStorageStrategy);
        testStorage(shortener);
    }
    @Test
    public void testDualHashBidiMapStorageStrategy(){
        DualHashBidiMapStorageStrategy dualHashBidiMapStorageStrategy = new DualHashBidiMapStorageStrategy();
        Shortener shortener = new Shortener(dualHashBidiMapStorageStrategy);
        testStorage(shortener);
    }
    @Test
    public void testOurHashBiMapStorageStrategy(){
        OurHashBiMapStorageStrategy ourHashBiMapStorageStrategy = new OurHashBiMapStorageStrategy();
        Shortener shortener = new Shortener(ourHashBiMapStorageStrategy);
        testStorage(shortener);
    }
}
