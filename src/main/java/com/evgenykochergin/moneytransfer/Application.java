package com.evgenykochergin.moneytransfer;

import com.evgenykochergin.moneytransfer.guice.module.CoreModule;
import com.evgenykochergin.moneytransfer.guice.module.GuiceServetModule;
import com.evgenykochergin.moneytransfer.webserver.WebServer;
import com.google.inject.Guice;

public class Application {

    public static void main(String[] args) throws Exception {
        new WebServer(
                8080,
                () -> Guice.createInjector(
                        new GuiceServetModule(),
                        new CoreModule())
        ).start();
    }


}
