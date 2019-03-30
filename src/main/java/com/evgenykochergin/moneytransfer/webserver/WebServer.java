package com.evgenykochergin.moneytransfer.webserver;

import com.evgenykochergin.moneytransfer.guice.InjectorProvider;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class WebServer {

    private final int port;
    private final InjectorProvider injectorProvider;

    public WebServer(int port, InjectorProvider injectorProvider) {
        this.port = port;
        this.injectorProvider = injectorProvider;
    }

    public void start() throws Exception {
        Server server = new Server(port);
        configureServer(server);
        server.start();
    }


    private void configureServer(Server server) {
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        root.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return injectorProvider.createInjector();
            }
        });
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(EmptyServlet.class, "/*");
    }
}

