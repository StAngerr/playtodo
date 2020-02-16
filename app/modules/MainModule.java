package modules;

import com.google.inject.AbstractModule;
import repository.UserRepository;
import repository.UserRepositoryImpl;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
    }
}
