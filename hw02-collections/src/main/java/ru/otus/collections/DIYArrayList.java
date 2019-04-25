package ru.otus.collections;

import java.util.*;
import java.util.function.UnaryOperator;

public class DIYArrayList<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private int capacity;
    private Object[] data;

    public DIYArrayList() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
    }

    public DIYArrayList(int capacity) {
        if(capacity >= 0) {
            this.data = new Object[capacity];
            this.capacity = capacity;
            this.size = 0;
        } else {
            throw new IllegalArgumentException("Negative capacity");
        }
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        if(size == capacity) {
            int newCapacity = 2 * capacity;
            data = grow(newCapacity);
            capacity = newCapacity;
        }
        data[size] = e;
        size += 1;
        return true;
    }

    private Object[] grow(int newCapacity) {
        return Arrays.copyOf(data, newCapacity);
    }

    @Override
    public boolean remove(Object o) {
        if(isEmpty()) {
            return false;
        }
        int index = findIndex(o);
        if(index != -1) {
            removeElementFromData(index);
            size -= 1;
            return true;
        } else {
            return false;
        }
    }

    private int findIndex(Object o) {
        for (int i = 0; i < size; ++i) {
            Object el = data[i];
            if (o == null && el == null) {
                return i;
            } else if (o != null && o.equals(el)) {
                return i;
            }
        }
        return -1;
    }

    private void removeElementFromData(int index) {
        for(int i = index; i < size - 1; ++i) {
            data[i] = data[i+1];
        }
        data[size - 1] = null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super E> c) {
        Arrays.sort((E[]) data, 0, size, c);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) data[index];
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldValue = (E)data[index];
        data[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DIYListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if(index > size || index < 0) {
            throw new IllegalStateException();
        }
        return new DIYListItr(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("DIYArrayList: [ ");
        for(int i = 0; i < size; ++i) {
            res.append(data[i].toString());
            if(i != size - 1) {
                res.append(", ");
            }
        }
        res.append(" ]");
        return res.toString();
    }

    private void checkIndex(int index) {
        if(index < 0 || index >= size) {
            throw new IllegalArgumentException();
        }
    }

    private class DIYListItr implements ListIterator<E> {

        int cursor;
        int lastRet = -1;

        DIYListItr(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            int i = cursor;
            if(i >= size) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return (E) data[lastRet = i];

        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            int i = cursor - 1;
            if(i < 0) {
                throw new NoSuchElementException();
            }
            cursor = i;
            return (E) data[lastRet = i];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            DIYArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if(lastRet < 0) {
                throw new IllegalStateException();
            }
            DIYArrayList.this.set(lastRet, e);

        }

        @Override
        public void add(E e) {
            int i = cursor;
            DIYArrayList.this.add(e);
            cursor = i + 1;
            lastRet = -1;
        }
    }

}
