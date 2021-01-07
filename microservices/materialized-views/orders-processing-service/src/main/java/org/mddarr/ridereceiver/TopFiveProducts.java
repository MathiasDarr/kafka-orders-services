package org.mddarr.ridereceiver;

import org.mddarr.products.AvroPurchaseCount;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class TopFiveProducts implements Iterable<AvroPurchaseCount> {
    private final Map<String, AvroPurchaseCount> currentSongs = new HashMap<>();

    private final TreeSet<AvroPurchaseCount> topFive = new TreeSet<>((o1, o2) -> {
        final int result = o2.getCount().compareTo(o1.getCount());
        if (result != 0) {
            return result;
        }
        return o1.getProductId().compareTo(o2.getProductId());
    });

    public void add(final AvroPurchaseCount avroPurchaseCount) {
        if(currentSongs.containsKey(avroPurchaseCount.getProductId())) {
            topFive.remove(currentSongs.remove(avroPurchaseCount.getProductId()));
        }
        topFive.add(avroPurchaseCount);
        currentSongs.put(avroPurchaseCount.getProductId(), avroPurchaseCount);
        if (topFive.size() > 5) {
            final AvroPurchaseCount last = topFive.last();
            currentSongs.remove(last.getProductId());
            topFive.remove(last);
        }
    }

    void remove(final AvroPurchaseCount value) {
        topFive.remove(value);
        currentSongs.remove(value.getProductId());
    }

    @Override
    public Iterator<AvroPurchaseCount> iterator() {
        return topFive.iterator();
    }
}