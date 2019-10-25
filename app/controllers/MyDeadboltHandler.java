package controllers;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

    public MyDeadboltHandler(ExecutionContextProvider ecProvider) {
        super(ecProvider);
    }
}
