package com.fineapple.exceptionTest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 테스트 중복 코드 제거용 커스텀 에너테이션
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@ExtendWith(ExceptionTestExtension.class)
public @interface ExceptionTest {
    Class<? extends Throwable> expected() default Exception.class;
    String message() default "";
}
