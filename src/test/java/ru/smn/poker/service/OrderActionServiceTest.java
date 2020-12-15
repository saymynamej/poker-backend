package ru.smn.poker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderActionServiceTest {

    @Autowired
    private OrderActionService orderActionService;

    private final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    public void test() {
//        orderActionService.start()
    }
}
