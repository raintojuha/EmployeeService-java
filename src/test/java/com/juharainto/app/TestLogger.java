package com.juharainto.app;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public interface TestLogger {
    static final Logger LOG = Logger.getLogger(TestLogger.class.getName());

    @BeforeAll
    default void beforeAll(){
        System.out.println("Test beginning..");
    }

    @AfterAll
    default void afterAll(){
        System.out.println("Test concluded");
    }

    @BeforeEach
    default void beforeEach(TestInfo testInfo){
        System.out.println(String.format("Executing test: [%s]",
         testInfo.getDisplayName()));
    }
}
