package controllers;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.http.adapter.HttpActionAdapter;

public class MyHttpAdapter implements HttpActionAdapter {
    private int a = 0;
    public MyHttpAdapter() {
        super();
        a = 10;
    }

    public Object adapt(int code, WebContext context) {
        if (code == 403) {
            return "Ok";
        }
        return null;
    }
}
