package com.sungwoo.tcp.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FloawableManager {

    public void doFlowable(List<Object> list) {
        Flowable.fromIterable(list)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }
}
