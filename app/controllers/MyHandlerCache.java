package controllers;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;

public class MyHandlerCache implements HandlerCache {
    private final DeadboltHandler defaultHandler = new MyDeadboltHandler(null);

    @Override
    public DeadboltHandler apply(final String key)
    {
        return defaultHandler;
    }

    @Override
    public DeadboltHandler get()
    {
        return defaultHandler;
    }
}

