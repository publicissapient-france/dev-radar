/**
 * 
 */
package com.xebia.devradar.pollers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;



/**
 * @author Alexandre Dutra
 *
 */
public class PollerServiceLocator {

    public static Set<PollerDescriptor> getSupportedPollers(){
        final Set<PollerDescriptor> pollerDescriptors = new HashSet<PollerDescriptor>();
        final ServiceLoader<PollerProvider> eventSourceLoader = ServiceLoader.load(PollerProvider.class);
        final Iterator<PollerProvider> iterator = eventSourceLoader.iterator();
        while(iterator.hasNext()) {
            final PollerProvider provider = iterator.next();
            pollerDescriptors.addAll(provider.getSupportedPollers());
        }
        return pollerDescriptors;
    }

    public static PollerDescriptor getPollerDescriptor(final Class<? extends Poller> pollerClass){
        final ServiceLoader<PollerProvider> eventSourceLoader = ServiceLoader.load(PollerProvider.class);
        final Iterator<PollerProvider> iterator = eventSourceLoader.iterator();
        while(iterator.hasNext()) {
            final PollerProvider provider = iterator.next();
            if(provider.isPollerSupported(pollerClass)) {
                return provider.getPollerDescriptor(pollerClass);
            }
        }
        return null;
    }
}
