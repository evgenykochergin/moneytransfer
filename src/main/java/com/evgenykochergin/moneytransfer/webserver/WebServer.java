package com.evgenykochergin.moneytransfer.webserver;

import com.evgenykochergin.moneytransfer.guice.InjectorProvider;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class WebServer implements AutoCloseable {
    private final InjectorProvider injectorProvider;
    private final Server server;

    private WebServer(InjectorProvider injectorProvider, Server server) {
        this.injectorProvider = injectorProvider;
        this.server = server;
    }

    public static WebServer start(int port, InjectorProvider injectorProvider) throws Exception {
        Server server = new Server(port);
        WebServer webServer = new WebServer(injectorProvider, server);
        webServer.configureServer();
        webServer.startServer();
        return webServer;
    }


    public void close() throws Exception {
        this.server.stop();
    }


    private void configureServer() {
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

    private void startServer() throws Exception {
        this.server.start();
    }

}

