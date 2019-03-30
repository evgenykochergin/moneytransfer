package com.evgenykochergin.moneytransfer.guice.module;

import com.evgenykochergin.moneytransfer.resource.GreetingResource;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ResourceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GreetingResource.class).in(Singleton.class);
    }
}
