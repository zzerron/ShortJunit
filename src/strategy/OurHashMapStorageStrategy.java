package strategy;

public class OurHashMapStorageStrategy implements StorageStrategy{
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    private  int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private float loadFactor = DEFAULT_LOAD_FACTOR;

    public int hash(Long k){
        return k.hashCode();
//        int h;
//        return (k == null) ? 0 : (h = k.hashCode()) ^ (h >>> 16);
    }

    public int indexFor(int hash, int length){
        return (length - 1) & hash;
    }

    public Entry getEntry(Long key){
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        for (Entry f = table[indexFor(hash, table.length)]; f != null; f = f.next){
            Long thisKey = f.getKey();
            if ((hash == f.hash) && (thisKey == key || thisKey.equals(key)))
                return f;
        }
        return null;
    }

    public void resize(int newCapacity){
//        if (table.length == Integer.MAX_VALUE){
        if (table.length == (1 << 30)){
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
//        threshold = (int)(newCapacity * loadFactor);
        threshold = (int)Math.min(newCapacity * loadFactor, (1 << 30) + 1);
    }

    public void transfer(Entry[] newTable){
//        Entry[] src = table;
//        int newCap = newTable.length;
//        for (int i = 0; i <= src.length; i++){
//            Entry e = src[i];
//            if (e != null){
//                src[i] = null;
//                do {
//                    Entry next = e.next;
//                    int j = indexFor(e.hash, newCap);
//                    e.next = newTable[j];
//                    newTable[j] = e;
//                    e = next;
//                } while (e != null);
//            }
//        }
        int newCapacity = newTable.length;
        for (Entry e : table) {
            while(null != e) {
                Entry next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    public void addEntry(int hash, Long key, String value, int bucketIndex){
//        Entry newE = table[bucketIndex];
//        table[bucketIndex] = new Entry(hash, key, value, newE);
//        if (size++ >= threshold){
//            resize(2 * table.length);
//        }
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    public void createEntry(int hash, Long key, String value, int bucketIndex){
        Entry newE = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, newE);
        size ++;
    }

    @Override
    public boolean containsKey(Long key) {
//        if (getEntry(key) == null)
//            return false;
//        else
//            return true;
        return getEntry(key)!=null;
    }

    @Override
    public boolean containsValue(String value) {
        if (value == null){
//            Entry[] tab = table;
//            for (int i = 0; i < tab.length ; i++)
//                for (Entry e = tab[i] ; e != null ; e = e.next)
//                    if (e.value == null)
//                        return true;
            return false;
        }

        Entry[] tab = table;
        for (int i = 0; i < tab.length ; i++)
            for (Entry e = tab[i] ; e != null ; e = e.next)
                if (value.equals(e.value))
                    return true;
        return false;
    }

    @Override
    public void put(Long key, String value) {
//        if (key == null){
//            for (Entry e = table[0]; e != null; e = e.next) {
//                if (e.key == null) {
//                    String oldValue = e.value;
//                    e.value = value;
//                }
//            }
//            addEntry(0, null, value, 0);
//        }
//        int hash = hash(key);
//        int i = indexFor(hash, table.length);
//        for (Entry e = table[i]; e != null; e = e.next) {
//            Long k;
//            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
//                String oldValue = e.value;
//                e.value = value;
//            }
//        }
//
//        addEntry(hash, key, value, i);
        addEntry(hash(key), key, value, indexFor(hash(key),table.length));
    }

    @Override
    public Long getKey(String value) {
        if (value == null)
            return 0l;
        Entry[] tab = table;
        for (int i = 0; i < tab.length ; i++)
            for (Entry e = tab[i] ; e != null ; e = e.next)
                if (value.equals(e.value))
                    return e.getKey();
        return null;
    }

    @Override
    public String getValue(Long key) {
        return null == getEntry(key) ? null : getEntry(key).getValue();
    }
}
