package yapi.datastructures;

import yapi.datastructures.tuples.Pair;
import yapi.internal.runtimeexceptions.YAPIRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FastDynamicArray<T> implements Iterable<T> {

    public static void main(String[] args) {
        FastDynamicArray<String> fastDynamicArray = new FastDynamicArray<>(CopyOnWriteArrayList.class);
        fastDynamicArray.add("Hello World1!");
        fastDynamicArray.add("Hello World2!");
        fastDynamicArray.add("Hello World3!");
        fastDynamicArray.add("Hello World4!");
        fastDynamicArray.add("Hello World5!");
        fastDynamicArray.add("Hello World6!");

        System.out.println(fastDynamicArray.list + " " + fastDynamicArray.size() + " " + fastDynamicArray.list.size());
        fastDynamicArray.remove(1);
        fastDynamicArray.remove(2);
        System.out.println(fastDynamicArray.list + " " + fastDynamicArray.size() + " " + fastDynamicArray.list.size());
        fastDynamicArray.remove(0);
        fastDynamicArray.add(0, "Hello World7!");
        // fastDynamicArray.clean();
        System.out.println(fastDynamicArray.list + " " + fastDynamicArray.size() + " " + fastDynamicArray.list.size());
        fastDynamicArray.forEach(System.out::println);

        System.out.println();
        for (String s : fastDynamicArray) {
            System.out.println(s + " " + fastDynamicArray.list);
            // fastDynamicArray.remove(s);
            fastDynamicArray.add(0, s);
        }
        System.out.println(fastDynamicArray);
    }

    private enum IteratorFixMode {
        REMOVE,
        INSERT
    }

    private List<Pair<T, Boolean>> list;
    private Set<Integer> removedIndices = new HashSet<>();
    private final Map<ListIterator<T>, BiConsumer<IteratorFixMode, Integer>> iteratorFixMap = new HashMap<>();

    public FastDynamicArray() {
        list = new ArrayList<>();
    }

    @SuppressWarnings({"unchecked"})
    public FastDynamicArray(Class<? extends List> backingClass) {
        try {
            list = backingClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new YAPIRuntimeException(e.getMessage(), e);
        }
    }

    private int shiftToIndex(int index) {
        AtomicInteger atomicInteger = new AtomicInteger(index);
        removedIndices.forEach(i -> {
            if (atomicInteger.intValue() >= i) atomicInteger.incrementAndGet();
        });
        return atomicInteger.get();
    }

    public void add(T t) {
        list.add(new Pair<>(t, false));
    }

    public void add(int i, T t) {
        fixIteratorsAfterInternalInsert(i);
        i = shiftToIndex(i);
        list.add(i, new Pair<>(t, false));

        int finalI = i;
        removedIndices = removedIndices.stream().map(r -> r >= finalI ? r + 1 : r).collect(Collectors.toSet());
    }

    public void set(int i, T t) {
        i = shiftToIndex(i);
        list.set(i, new Pair<>(t, false));
    }

    public void remove(int i) {
        i = shiftToIndex(i);
        removedIndices.add(i);
        list.set(i, new Pair<>(null, true));
        if (removedIndices.size() > list.size() / 2) clean();
        fixIteratorsAfterRemove();
    }

    public T get(int i) {
        return internalGet(i).getL();
    }

    public boolean contains(T t) {
        return list.contains(new Pair<>(t, false));
    }

    public int indexOf(T t) {
        int index = 0;
        for (Pair<T, Boolean> tBooleanPair : list) {
            if (tBooleanPair.getR()) {
                continue;
            }
            if (tBooleanPair.getL() == t) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int lastIndexOf(T t) {
        return list.lastIndexOf(new Pair<>(t, false));
    }

    private Pair<T, Boolean> internalGet(int i) {
        return list.get(shiftToIndex(i));
    }

    public void clean() {
        if (removedIndices.isEmpty()) return;
        if (removedIndices.size() >= list.size()) {
            clear();
            return;
        }
        List<Pair<T, Boolean>> pairList = new ArrayList<>(list.size() - removedIndices.size());
        for (int i = 0; i < list.size() - removedIndices.size(); i++) {
            pairList.add(internalGet(i));
        }
        list = pairList;
        removedIndices.clear();
    }

    public void clear() {
        list.clear();
        removedIndices.clear();
    }

    public boolean remove(T t) {
        int index = indexOf(t);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    public boolean removeAll(Collection<T> collection) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        collection.forEach(t -> {
            if (remove(t)) atomicBoolean.set(true);
        });
        return atomicBoolean.get();
    }

    public boolean removeIf(Predicate<T> predicate) {
        return false;
    }

    public int size() {
        return list.size() - removedIndices.size();
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public boolean isNotEmpty() {
        return size() > 0;
    }

    private void fixIteratorsAfterRemove() {
        iteratorFixMap.forEach((listIterator, iteratorFixModeConsumer) -> iteratorFixModeConsumer.accept(IteratorFixMode.REMOVE, -1));
    }

    private void fixIteratorsAfterInternalInsert(int index) {
        iteratorFixMap.forEach((listIterator, iteratorFixModeConsumer) -> iteratorFixModeConsumer.accept(IteratorFixMode.INSERT, index));
    }

    public ListIterator<T> listIterator() {
        return new ListIterator<>() {
            int index = 0;

            {
                iteratorFixMap.put(this, (iteratorFixMode, i) -> {
                    switch (iteratorFixMode) {
                        case REMOVE:
                            index--;
                            break;
                        case INSERT:
                            if (index > i) index++;
                            break;
                    }
                });
            }

            @Override
            public boolean hasNext() {
                if (index >= size()) iteratorFixMap.remove(this);
                return index < size();
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                if (index + 1 > size()) iteratorFixMap.remove(this);
                return get(index++);
            }

            @Override
            public boolean hasPrevious() {
                if (index > size()) return false;
                return index > 0;
            }

            @Override
            public T previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                return get(--index);
            }

            @Override
            public int nextIndex() {
                return Math.min(index, size());
            }

            @Override
            public int previousIndex() {
                return Math.max(index, 0);
            }

            @Override
            public void remove() {
                FastDynamicArray.this.remove(index);
            }

            @Override
            public void set(T t) {
                FastDynamicArray.this.set(index, t);
            }

            @Override
            public void add(T t) {
                FastDynamicArray.this.add(index, t);
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T t : this) {
            action.accept(t);
        }
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder().append("FastDynamicArray[");
        boolean b = false;
        for (T t : this) {
            if (b) st.append(", ");
            b = true;
            st.append(t.toString());
        }
        return st.append("]").toString();
    }

}
