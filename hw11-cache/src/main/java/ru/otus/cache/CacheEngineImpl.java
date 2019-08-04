package ru.otus.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

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
    public SmartValue<V> get(K key) {
        SoftReference<SmartValue<V>> smartValueSoftReference = elements.get(key);
        SmartValue<V> smartValue = null;
        if (smartValueSoftReference != null) {
            hit++;
            smartValue = smartValueSoftReference.get();
            if (smartValue != null) {
                smartValue.setAccessed();
            }
        } else {
            miss++;
        }
        return smartValue;
    }

    @Override
    public void put(K key, SmartValue<V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        elements.put(key, new SoftReference<>(element));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() +  lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if(idleTimeMs != 0) {
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
                if(smartValueSoftReference == null ||
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
