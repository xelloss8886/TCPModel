package com.sungwoo.tcp.parallel;


import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component(value = "fluxConsumerManager")
public class FluxConsumerManager implements Callable<Disposable> {

    @Autowired
    private BlockingQueue queue;

    private static final int SIZE_PER_DEQUEUE = 100;

    private final CountDownLatch latch = new CountDownLatch(4);

    private interface Parallelism {
        void doParallel(List<Object> list);
    }

    class FluxableTask implements Parallelism {
        @Override
        public void doParallel(List<Object> list) {
            Flux.fromIterable(list)
                    .parallel(4)
                    .runOn(Schedulers.parallel())
                    .doOnNext((e) -> {
                        latch.countDown();
                    })
                    .sequential()
                    .log()
                    .subscribe();
        }
    }

    class SubscribableTask implements Parallelism {

        @Override
        public void doParallel(List<Object> list) {
            Flux.fromIterable(list)
                    .parallel(4)
                    .subscribe((e) -> {
                                System.out.println(String.valueOf(e));
                                latch.countDown();
                            },
                            (error) -> System.err.println(error),
                            () -> {
                                System.out.println("complete");
                            });
        }
    }

    class FlowableTask implements Parallelism {

        @Override
        public void doParallel(List<Object> list) {
            log.info("get Data in Parallelism : " + list.size());
            Flowable.fromIterable(list)
                    .onBackpressureBuffer()
                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                    .subscribe();
        }
    }

    @Override
    public Disposable call() {
        //getData from queue
        while(true) {
            if(queue.peek() == null) continue;
            List<Object> list = Stream.of(queue).limit(SIZE_PER_DEQUEUE).collect(Collectors.toList());
            Parallelism parallelism = new FlowableTask();
            parallelism.doParallel(list);
            log.info("!!!");
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
