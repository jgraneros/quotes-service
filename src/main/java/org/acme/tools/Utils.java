package org.acme.tools;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

//@Blocking
@Slf4j
@ApplicationScoped
public class Utils {

    @Blocking
    public static void getCurrentThread() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        log.info("El nombre del hilo actual es: " + currentThread.getName());
        log.info("El ID del hilo actual es: " + currentThread.getId());
    }


}
