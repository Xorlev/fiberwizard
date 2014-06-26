import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import rx.Observable;
import rx.Observer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 2014-02-28
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class HystrixUtil {
    public static String formatError(HystrixRuntimeException e) {
        checkNotNull(e);
        // [GetUsageCommand] failure=SHORT_CIRCUIT, causeChain=[AuthenticationClientException, NullPointerException], message="Failed to auth"
        Throwable cause = e.getCause();
        StringBuilder sb = new StringBuilder();
        sb
                .append("[").append(e.getImplementingClass().getSimpleName()).append("] (HystrixRuntimeException)")
                .append(" ")
                .append("failure=").append(e.getFailureType())
                .append(", ")
                .append("causalChain=").append(Throwables.getCausalChain(e));

        if (cause != null) {
            sb.append(", message=\"").append(e.getCause().getMessage()).append("\"");
        }

        return sb.toString();
    }

    /**
     * Takes a HystrixCommand and calls .observe(), then converts this into a ListenableFuture
     * @param command
     * @param <T> return type of HystrixCommand
     * @return SettableListenableFuture
     */
    public static <T> ListenableFuture<T> toListenableFuture(HystrixCommand<T> command) {
        return toListenableFuture(command.observe());
    }

    /**
     * Subscribes to an Observable and then uses {@link SettableFuture} as a Promise for the
     * subscription to set.
     *
     * Should only be used with single-value observables.
     *
     * @param observable
     * @param <T> type of Observable.
     * @return SettableListenableFuture
     */
    public static <T> ListenableFuture<T> toListenableFuture(Observable<T> observable) {
        final SettableFuture<T> listenableFuture = SettableFuture.create();
        observable.subscribe(new Observer<T>() {
                                 @Override
                                 public void onCompleted() {

                                 }

                                 @Override
                                 public void onError(Throwable e) {
                                    listenableFuture.setException(e);
                                 }

                                 @Override
                                 public void onNext(T args) {
                                    if (listenableFuture.isDone()) {
                                        listenableFuture.setException(new IllegalStateException("Observable should only have a single value"));
                                    } else {
                                        listenableFuture.set(args);
                                    }
                                 }
                             });

        return listenableFuture;
    }
}
