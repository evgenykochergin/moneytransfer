package com.evgenykochergin.moneytransfer.guice.module;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceServetModule extends ServletModule {

    protected void configureServlets() {
        serve("*").with(GuiceContainer.class);
    }
}
