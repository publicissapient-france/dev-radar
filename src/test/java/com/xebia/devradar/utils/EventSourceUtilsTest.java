/**
 * 
 */
package com.xebia.devradar.utils;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import com.xebia.devradar.domain.EventSource;



/**
 * @author Alexandre Dutra
 *
 */
public class EventSourceUtilsTest {

    @Test
    public void test() {
        final ServiceLoader<EventSource> eventSourceLoader = ServiceLoader.load(EventSource.class);
        final Iterator<EventSource> iterator = eventSourceLoader.iterator();
        while(iterator.hasNext()) {
            final EventSource eventSource = iterator.next();
            System.out.println(eventSource);
        }

    }
}
