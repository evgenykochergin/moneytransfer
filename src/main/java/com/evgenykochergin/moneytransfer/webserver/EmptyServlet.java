package com.evgenykochergin.moneytransfer.webserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An Empty, "do nothing servlet" to add to the context. Otherwise, the filters
 * will never kick in.
 */
public class EmptyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        throw new IllegalStateException("Unable to service request");
    }
}
