import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 2012-09-01
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class ListenableFutureAdapter<T> implements ListenableFuture {
    private final com.ning.http.client.ListenableFuture<T> ningFuture;

    @SuppressWarnings("unchecked")
    public static <T> ListenableFuture<T> adapt(com.ning.http.client.ListenableFuture<T> future) {
        return new ListenableFutureAdapter<T>(future);
    }

    private ListenableFutureAdapter(com.ning.http.client.ListenableFuture<T> future) {
        this.ningFuture = future;
    }

    public void addListener(Runnable runnable, Executor executor) {
        ningFuture.addListener(runnable, executor);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return ningFuture.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
        return ningFuture.isCancelled();
    }

    public boolean isDone() {
        return ningFuture.isDone();
    }

    public T get() throws InterruptedException, ExecutionException {
        return ningFuture.get();
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return ningFuture.get(timeout, unit);
    }
}
