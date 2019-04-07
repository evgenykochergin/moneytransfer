package com.evgenykochergin.moneytransfer;

import com.evgenykochergin.moneytransfer.guice.module.CoreModule;
import com.evgenykochergin.moneytransfer.guice.module.GuiceServetModule;
import com.evgenykochergin.moneytransfer.guice.module.AccountModule;
import com.evgenykochergin.moneytransfer.guice.module.TestDataAccountModule;
import com.evgenykochergin.moneytransfer.webserver.WebServer;
import com.google.inject.Guice;

public class Application {

    public static void main(String[] args) throws Exception {
        WebServer.start(
                8080,
                () -> Guice.createInjector(
                        new GuiceServetModule(),
                        new CoreModule(),
                        new AccountModule(),
                        new TestDataAccountModule())
        );
    }


}
