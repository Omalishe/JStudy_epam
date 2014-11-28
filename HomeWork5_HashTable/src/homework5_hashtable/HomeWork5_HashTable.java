package homework5_hashtable;

class MyHashTable<K,V>{
    
    final static byte INIT_SIZE = 11;
    int size;
    Entry[] table;
    float loadBalance;
    int treshold;
    
    public MyHashTable(int init_size, float loadBalance) {
        this.table = new Entry[init_size];
        this.loadBalance = loadBalance;
        this.treshold = (int)(this.table.length*this.loadBalance);
    }

    public MyHashTable() {
        this(INIT_SIZE, 0.75f);
    }
    
    private void grow(){
        Entry[] oldTable = new Entry[table.length];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        int newLength = table.length + (table.length>>1)+1;
        this.table = new Entry[newLength];
        this.treshold = (int)(this.table.length*this.loadBalance);
        this.size=0;
        for (int i=0;i<oldTable.length;i++){
            Entry current = oldTable[i];
            while (current!=null){
                this.add((K)current.key,(V)current.data);
                current = current.next;
            }
        }
    }
    
    private int hash(K key){
        int returnValue=key.hashCode()%table.length;
        return returnValue;
    }
    
    public V get(K key){
        int index = hash(key);
        Entry current = table[index];
        if (current==null){
            return null;
        }
        while (!current.key.equals(key)) current = current.next;
        if (current!=null)return (V) current.data; else return null;
    }
    
    public void add(K key, V value){
        int index = hash(key);
        Entry current = table[index];
        if (current!=null) {
            if (current.key.equals(key)) return;
            current = current.next;
        }
        table[index]=new Entry(key, value, table[index]);
        size++;
        if (size>=treshold) grow();
    }

    
    
    class Entry<K,V>{
        final K key;
        V data;
        Entry next;

        public Entry(K key, V data, Entry next) {
            this.key = key;
            this.data = data;
            this.next = next;
        }

        public Entry() {
            this(null,null,null);
        }
        
    }
}

public class HomeWork5_HashTable {

    public static void main(String[] args) {
        MyHashTable<Integer,String> mht = new MyHashTable<>(5, 0.75f);
        mht.add(1,"one");
        mht.add(6,"six");
        mht.add(2,"two");
        mht.add(3,"three");
        
        
        System.out.println(mht.get(6));
        System.out.println(mht.get(3));
        System.out.println(mht.get(2));
    }
    
}
