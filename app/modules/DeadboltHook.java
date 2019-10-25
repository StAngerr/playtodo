package modules;
import be.objectify.deadbolt.java.cache.HandlerCache;
import controllers.MyHandlerCache;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

import javax.inject.Singleton;
public class DeadboltHook extends Module {
    @Override
    public Seq<Binding<?>> bindings(final Environment environment,
                                    final Configuration configuration) {
        return seq(bind(HandlerCache.class).to(MyHandlerCache.class).in(Singleton.class));
    }
}
