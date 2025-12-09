import java.util.ArrayList;

public class Hashmap<E, T> {

    private final static int DEFAULT_SIZE = 16;
    private final static double DEFAULT_RESIZE_PERCENT = 0.5;

    private int size;
    private int capacity;
    private final double resizePercent;

    ArrayList<HashLink> buckets;

    public Hashmap(int startingSize, double resizePercent) {
        this.resizePercent = resizePercent;
        this.capacity = startingSize;
        buckets = new ArrayList<>(capacity);
    }

    public Hashmap() {
        capacity = DEFAULT_SIZE;
        resizePercent = DEFAULT_RESIZE_PERCENT;
        buckets = new ArrayList<>(capacity);
    }

    class HashLink
    {
        public E key;
        public T value;
        public int hash;
        public HashLink next;

        public HashLink(E key, T value, int hash, HashLink next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        public HashLink() {
        }

    }

    public void remove(E key) {
        int hash = key.hashCode();
        HashLink item = buckets.get(hash % capacity);
        HashLink last = null;

        while (item != null) {
            if(item.key == key) {
                if(last != null)
                    last.next = item.next;
                else
                    buckets.set(hash % capacity, item.next);
                return;
            }
            last = item;
            item = item.next;
        }
    }

    public T get (E key) {
        int hash = key.hashCode();
        HashLink item = buckets.get(hash % capacity);

        while(item != null) {
            if(item.key == key)
                return item.value;
            item = item.next;
        }
        return null;
    }

    public Hashmap<E, T> put(E key, T value) {
        int hash = key.hashCode();

        return putLink(new HashLink(key, value, hash, null));
    }

    private Hashmap<E, T> putLink(HashLink link) {
        size++;
        if (size >= capacity * resizePercent)
            resize();

        HashLink item = buckets.get(link.hash % capacity);
        HashLink last = null;
        while (item != null) {
            if(item.key == link.key) {
                if(last != null)
                   last.next = link;
                else
                    buckets.set(link.hash % capacity, link);

                link.next = item.next;
                return this;
            }
            last = item;
            item = item.next;
        }

        if(last != null) {
            last.next = link;
        } else {
            buckets.set(link.hash % capacity, link);
        }
        return this;
    }

    private void resize() {
        ArrayList<HashLink> newBuckets = new ArrayList<>(size * 2);
        ArrayList<HashLink> oldBuckets = buckets;
        buckets = newBuckets;
        size = 0;
        capacity *= 2;

        for(HashLink bucket : oldBuckets) {
            HashLink item = bucket;
            while(item != null) {
                putLink(item);
                item = item.next;
            }
        }

    }

}
