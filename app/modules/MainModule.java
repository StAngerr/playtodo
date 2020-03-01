package modules;

import com.google.inject.AbstractModule;
import repository.UserIconRepository;
import repository.UserIconRepositoryFactory;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import services.RequestValidationService;
import services.UserService;
import utils.HttpHelper;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(UserIconRepository.class).to(UserIconRepositoryFactory.class);
        requestInjection(UserService.class);
        requestInjection(RequestValidationService.class);
        requestInjection(HttpHelper.class);
    }
}
