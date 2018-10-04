package com.sungwoo.tcp.parallel;

import com.sungwoo.tcp.entity.SalaryEntity;
import com.sungwoo.tcp.repository.EmpoyeesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Producer implements Runnable{

    @Autowired
    private BlockingQueue queue;

    @Autowired
    private EmpoyeesRepository empoyeesRepository;

    private static final int INITIAL_PAGE = 0;

    private static final int PER_SIZE = 10000;

    @Transactional
    @Override
    public void run() {
        long started = System.currentTimeMillis();

        int page = INITIAL_PAGE;
        long totalSize = -1;
        do {
            Pageable pageable = PageRequest.of(page, PER_SIZE);
            Page<SalaryEntity> entityPage = empoyeesRepository.findAll(pageable);
            List<SalaryEntity> list = entityPage.stream().collect(Collectors.toList());
            log.info("total size : " + entityPage.getTotalElements());
            if(totalSize < 0) totalSize = entityPage.getTotalElements();
            log.info("findAll elapsed time : " + (System.currentTimeMillis() - started) + " ms. ");
            log.info("findAll list size : " + list.size());
            ListIterator<SalaryEntity> listIt = list.listIterator();
            while(listIt.hasNext()) {
                SalaryEntity each = listIt.next();
                queue.offer(each);
            }
            if(page > totalSize) {
                long dividend = totalSize % (long)PER_SIZE;
                page += dividend;
            } else {
                page += PER_SIZE;
            }
        }
        while(page < totalSize);
    }

    private void init() {

    }
}
