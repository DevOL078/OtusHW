package ru.otus.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<SmartValue<V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }


    @Override
    public Optional<V> get(K key) {
        SoftReference<SmartValue<V>> smartValueSoftReference = elements.get(key);
        if (smartValueSoftReference != null) {
            hit++;
            SmartValue<V> smartValue = smartValueSoftReference.get();
            if (smartValue != null) {
                smartValue.setAccessed();
                return Optional.of(smartValue.getValue());
            }
        } else {
            miss++;
        }
        return Optional.empty();
    }

    @Override
    public void put(K key, V element) {
        if (elements.size() == maxElements) {
            List<Map.Entry<K, SoftReference<SmartValue<V>>>> sortedEntries = elements.entrySet().stream().sorted((a, b) -> {
                long lastA = Objects.requireNonNull(a.getValue().get()).getLastAccessTime();
                long lastB = Objects.requireNonNull(b.getValue().get()).getLastAccessTime();
                return Long.compare(lastA, lastB);
            }).collect(Collectors.toList());

            elements.remove(sortedEntries.get(0).getKey());
        }

        elements.put(key, new SoftReference<>(new SmartValue<>(element)));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<SmartValue<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<SmartValue<V>> smartValueSoftReference = elements.get(key);
                if (smartValueSoftReference == null ||
                        smartValueSoftReference.get() == null ||
                        isT1BeforeT2(timeFunction.apply(smartValueSoftReference.get()),
                                System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }

            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

}
