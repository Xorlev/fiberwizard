import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import com.google.common.util.concurrent.ListenableFuture;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 2014-05-30
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {
    private static final AsyncHttpClient client = new AsyncHttpClient();

    @GET
    @Suspendable
    public String getSleep() throws InterruptedException, SuspendExecution, IOException, ExecutionException {
        System.out.println(Fiber.isCurrentFiber());
        return new HttpGetCommand("http://www.google.com/").executeFiber();
    }

    // This
    public ListenableFuture<Response> getSite(String url) throws IOException {
        return ListenableFutureAdapter.adapt(client.prepareGet(url).execute());
    }

    // or thos
    public class HttpGetCommand extends FiberHystrixCommand<String> {
        private final String url;

        public HttpGetCommand(String url) {
            super(HystrixCommandGroupKey.Factory.asKey("HTTP"));
            this.url = url;
        }

        @Override
        protected String run() throws Exception {
            return client.prepareGet(url).execute().get().getResponseBody();
        }
    }
}
