import java.util.*;

public class Hashmap<E, T> implements Map <E, T>   {

    private final static int DEFAULT_SIZE = 16;
    private final static double DEFAULT_RESIZE_PERCENT = 0.5;

    private int size;
    private int capacity;
    private final double resizePercent;

    ArrayList<HashLink> buckets;

    public Hashmap(int startingSize, double resizePercent) {
        this.resizePercent = resizePercent;
        this.capacity = startingSize;
        buckets = new ArrayList<>(Collections.nCopies(capacity, null));

    }

    public Hashmap() {
        capacity = DEFAULT_SIZE;
        resizePercent = DEFAULT_RESIZE_PERCENT;
        buckets = new ArrayList<>(Collections.nCopies(capacity, null));
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

    @Override
    public T remove(Object key) {
        int hash = key.hashCode();
        HashLink item = buckets.get(hash % capacity);
        HashLink last = null;

        while (item != null) {
            if(item.key.equals(key)) {
                if(last != null)
                    last.next = item.next;
                else
                    buckets.set(hash % capacity, item.next);
                size--;
                return item.value;
            }
            last = item;
            item = item.next;
        }
        return null;
    }

    @Override
    public T get (Object key) {
        int hash = key.hashCode();
        HashLink item = buckets.get(hash % capacity);

        while(item != null) {
            if(item.key.equals(key))
                return item.value;
            item = item.next;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        HashLink item = buckets.get(key.hashCode() % size);

        while (item != null) {
            if (item.key.equals(key))
                return true;
            item = item.next;
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(HashLink item : buckets) {
            while (item != null) {
                if (item.value.equals(value))
                    return true;
                item = item.next;
            }
        }

        return false;
    }

    @Override
    public T put(E key, T value) {
        int hash = key.hashCode();

        return putLink(new HashLink(key, value, hash, null));
    }

    @Override
    public void putAll(Map<? extends E, ? extends T> m) {
        for(E key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public void clear() {
        buckets = new ArrayList<>(capacity);
        size = 0;
    }

    @Override
    public Set<E> keySet() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Collection<T> values() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Set<Entry<E, T>> entrySet() {
        throw new UnsupportedOperationException("not implemented");
    }

    private T putLink(HashLink link) {
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
                return link.value;
            }
            last = item;
            item = item.next;
        }

        if(last != null) {
            last.next = link;
        } else {
            buckets.set(link.hash % capacity, link);
        }
        return link.value;
    }

    private void resize() {
        ArrayList<HashLink> newBuckets = new ArrayList<>(Collections.nCopies(capacity * 2, null));
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
