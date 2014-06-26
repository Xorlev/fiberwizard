import co.paralleluniverse.fibers.FiberAsync;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.futures.AsyncListenableFuture;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.quasar.NewFiberScheduler;

import java.util.concurrent.ExecutionException;

/**
 * 2014-06-26
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public abstract class FiberHystrixCommand<R> extends HystrixCommand<R> {

    protected FiberHystrixCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    protected FiberHystrixCommand(Setter setter) {
        super(setter);
    }


    public R executeFiber() throws InterruptedException, ExecutionException, SuspendExecution {
        return AsyncListenableFuture.get(
                HystrixUtil.toListenableFuture(toObservable(NewFiberScheduler.getDefaultInstance())));
    }
}
