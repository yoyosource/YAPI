// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.streams;

import yapi.streams.functions.Mapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {

    public static final Predicate<Object> filterInteger = o -> o instanceof Integer;
    public static final Predicate<Object> filterByte = o -> o instanceof Byte;
    public static final Predicate<Object> filterLong = o -> o instanceof Long;
    public static final Predicate<Object> filterShort = o -> o instanceof Short;
    public static final Predicate<Object> filterBoolean = o -> o instanceof Boolean;
    public static final Predicate<Object> filterCharacter = o -> o instanceof Character;
    public static final Predicate<Object> filterString = o -> o instanceof String;

    public static final Predicate<Objects> filterNonNull = Objects::nonNull;
    public static final Predicate<Objects> removeNull = Objects::nonNull;
    public static final Predicate<Objects> filterIsNull = Objects::isNull;
    public static final Predicate<Objects> keepNull = Objects::isNull;

    public static final Function<Object, Integer> lazyMapInteger = o -> (Integer)o;
    public static final Function<Object, Byte> lazyMapByte = o -> (Byte) o;
    public static final Function<Object, Long> lazyMapLong = o -> (Long) o;
    public static final Function<Object, Short> lazyMapShort = o -> (Short) o;
    public static final Function<Object, Boolean> lazyMapBoolean = o -> (Boolean) o;
    public static final Function<Object, Character> lazyMapCharacter = o -> (Character) o;
    public static final Function<Object, String> lazyMapString = o -> (String) o;

    public static final Function<Object, Integer> mapInteger = o -> filterInteger.test(o) ? (Integer) o : null;
    public static final Function<Object, Byte> mapByte = o -> filterByte.test(o) ? (Byte) o : null;
    public static final Function<Object, Long> mapLong = o -> filterLong.test(o) ? (Long) o : null;
    public static final Function<Object, Short> mapShort = o -> filterShort.test(o) ? (Short) o : null;
    public static final Function<Object, Boolean> mapBoolean = o -> filterBoolean.test(o) ? (Boolean) o : null;
    public static final Function<Object, Character> mapCharacter = o -> filterCharacter.test(o) ? (Character) o : null;
    public static final Function<Object, String> mapString = o -> filterString.test(o) ? (String) o : null;

    private StreamUtils() {

    }

    public static <T> Stream<T> zip(Stream<T> s1, Stream<T> s2) {
        List<T> ts = s1.collect(Collectors.toList());
        ts.addAll(s2.collect(Collectors.toList()));
        return ts.stream();
    }

    public static <T> Stream<T> zipSorted(Stream<T> s1, Stream<T> s2, Comparator<T> comparator) {
        return zip(s1, s2).sorted(comparator);
    }

    public static <T> Stream<T> join(Stream<T> s1, Stream<T> s2, StreamAction<T> action) {
        List<T> ts1 = s1.collect(Collectors.toList());
        List<T> ts2 = s2.collect(Collectors.toList());
        int size = Math.min(ts1.size(), ts2.size());
        for (int i = 0; i < size; i++) {
            ts1.set(i, action.value(ts1.get(i), ts2.get(i)));
        }
        if (ts2.size() > size) {
            for (int i = size; i < ts2.size(); i++) {
                ts1.add(ts2.get(i));
            }
        }
        return ts1.stream();
    }

    public static <T> Stream<List<T>> chunk(Stream<T> s, int chunkSize) {
        List<T> ts = s.collect(Collectors.toList());
        List<List<T>> output = new ArrayList<>();
        while (ts.size() > chunkSize) {
            List<T> list = new ArrayList<>();
            output.add(list);
            while (list.size() < chunkSize) {
                list.add(ts.remove(0));
            }
        }
        if (!ts.isEmpty()) {
            List<T> list = new ArrayList<>();
            output.add(list);
            list.addAll(ts);
            ts.clear();
        }
        return output.stream();
    }

    public static <T> Stream<Stream<T>> subStream(Stream<List<T>> s) {
        List<List<T>> ts = s.collect(Collectors.toList());
        List<Stream<T>> streams = new ArrayList<>();
        for (List<T> list : ts) {
            streams.add(list.stream());
        }
        return streams.stream();
    }

    public static <T> Stream<Stream<T>> subStream(List<List<T>> ts) {
        List<Stream<T>> streams = new ArrayList<>();
        for (List<T> list : ts) {
            streams.add(list.stream());
        }
        return streams.stream();
    }

    public static <T, R> Stream<R> filter(Stream<T> stream, R r) {
        return stream.map(new Mapper<>(r)).filter(Objects::nonNull);
    }

}