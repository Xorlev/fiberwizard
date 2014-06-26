import co.paralleluniverse.fibers.dropwizard.FiberApplication;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * 2014-05-30
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class FiberApp extends FiberApplication<Configuration> {
    @Override
    public void fiberRun(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(TestResource.class);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {

    }

    public static void main(String[] args) throws Exception {
        new FiberApp().run(args);
    }
}
