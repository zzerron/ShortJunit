package strategy;

public class FileStorageStrategy implements StorageStrategy {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000L;
    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private long maxBucketSize;

    public FileStorageStrategy(){
        for (int i = 0; i < table.length; i++){
            table[i] = new FileBucket();
        }
    }

    public int hash(Long k){
        return k.hashCode();
    }

    public int indexFor(int hash, int length){
        return (length - 1) & hash;
    }

    public Entry getEntry(Long key){
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        int index = indexFor(hash, table.length);
        if (table[index] != null) {
            Entry entry = table[index].getEntry();
            while (entry != null) {
                if (entry.getKey().equals(key)) {
                    return entry;
                }
                entry = entry.next;
            }
        }
        return null;
    }

    public void resize(int newCapacity){
        if (table.length == (1 << 30)){
            bucketSizeLimit = Integer.MAX_VALUE;
            return;
        }

        FileBucket[] newTable = new FileBucket[newCapacity];
        for (int i = 0; i < newTable.length; i++)
        {
            newTable[i] = new FileBucket();
        }
        transfer(newTable);

        for (int i = 0; i < table.length; i++)
        {
            table[i].remove();
            table[i] = null;
        }
        table = newTable;
//        FileBucket[] newTable = new FileBucket[newCapacity];
//        transfer(newTable);
//        table = newTable;
    }

    public void transfer(FileBucket[] newTable){
        int newCapacity = newTable.length;
        for (FileBucket e : table) {
            Entry thisEntry = e.getEntry();
            while(null != thisEntry) {
                Entry next = thisEntry.next;
                int i = indexFor(thisEntry.hash, newCapacity);
                thisEntry.next = newTable[i].getEntry();
                newTable[i].putEntry(thisEntry);
                thisEntry = next;
            }
        }
//        for (int i = 0; i < table.length; i++) {
//            if (table[i] == null) continue;
//            Entry entry = table[i].getEntry();
//            while (entry != null) {
//                Entry next = entry.next;
//                int newIndex = indexFor(entry.hash, newTable.length);
//                if (newTable[newIndex] == null) {
//                    entry.next = null;
//                    newTable[newIndex] = new FileBucket();
//                }
//                else {
//                    entry.next = newTable[newIndex].getEntry();
//                }
//                newTable[newIndex].putEntry(entry);
//                entry = next;
//            }
//            table[i].remove();
//        }
    }

    public void addEntry(int hash, Long key, String value, int bucketIndex){

//        if ((size >= bucketSizeLimit) && (null != table[bucketIndex])) {
//            resize(2 * table.length);
//            hash = (null != key) ? hash(key) : 0;
//            bucketIndex = indexFor(hash, table.length);
//        }
//        createEntry(hash, key, value, bucketIndex);
        Entry entry = table[bucketIndex].getEntry();
        table[bucketIndex].putEntry(new Entry(hash, key, value, entry));
        size++;
        if (table[bucketIndex].getFileSize() > bucketSizeLimit) resize(2 * table.length);
    }

    public void createEntry(int hash, Long key, String value, int bucketIndex){
        table[bucketIndex] = new FileBucket();
        table[bucketIndex].putEntry(new Entry(hash,key,value,null));
        size ++;
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key)!=null;
    }

    @Override
    public boolean containsValue(String value) {
        FileBucket[] tab = table;
        for (FileBucket fileBucket : tab) {
            if (fileBucket == null){
                continue;
            }
            for (Entry e = fileBucket.getEntry(); e != null; e = e.next)
                if (value.equals(e.value))
                    return true;
        }
//        for (int i = 0; i < table.length; i++) {
//            if (table[i] == null) continue;
//            Entry entry = table[i].getEntry();
//            while (entry != null) {
//                if (entry.value.equals(value)) return true;
//                entry = entry.next;
//            }
//        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        if (key == null){
            return;
        }
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        if (table[i] != null) {
            for (Entry e = table[i].getEntry(); e != null; e = e.next) {
                if ((e.hash == hash) && (e.key.equals(key))) {
                    e.value = value;
                    return;
                }
            }
            addEntry(hash(key), key, value,  i);
        }
        else createEntry(hash,key,value,i);
    }

    @Override
    public Long getKey(String value) {

        if (value == null)
            return 0l;
        FileBucket[] tab = table;
        for (FileBucket f : tab)
            for (Entry e = f.getEntry() ; e != null ; e = e.next)
                if (value.equals(e.value))
                    return e.getKey();
        return null;
    }

    @Override
    public String getValue(Long key) {
            return null == getEntry(key) ? null : getEntry(key).getValue();
    }

    public long getBucketSizeLimit(){
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }
}
