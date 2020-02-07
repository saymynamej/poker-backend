package ru.sm.poker.util;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class WaitUtil {

    public static void waitOfCondition(BooleanSupplier supplier) {
        try {
            waitCondition(3L, TimeUnit.SECONDS, supplier);
        } catch (Exception e) {
//            throw new RuntimeException("error while wait of condition", e);
        }
    }

    public static void waitOfCondition(Long timeout, TimeUnit timeUnit, BooleanSupplier supplier) {
        try {
            waitCondition(timeout, timeUnit, supplier);
        } catch (Exception e) {
            throw new RuntimeException("error while wait of condition", e);
        }
    }

    private static void waitCondition(Long timeout, TimeUnit timeUnit, BooleanSupplier supplier) {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime current = start;

        while (!Thread.currentThread().isInterrupted() && diff(current, start) < TimeUnit.MILLISECONDS.convert(timeout, timeUnit)) {
            try {
                if (supplier.getAsBoolean())
                    return;
                else {
                    Thread.sleep(250);
                }
                current = ZonedDateTime.now();
            } catch (Exception e) {
                throw new RuntimeException("error while wait of condition:" + e.getMessage());
            }
        }

        if (!supplier.getAsBoolean())
            throw new RuntimeException("condition is failed");
    }

    private static Long diff(ZonedDateTime current, ZonedDateTime start) {
        return Duration.between(start, current).toMillis();
    }
}
