package com.evgenykochergin.moneytransfer.guice.module;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceServetModule extends ServletModule {

    protected void configureServlets() {
        serve("*").with(GuiceContainer.class);
        bind(JacksonJsonProvider.class).in(Singleton.class);
    }
}
