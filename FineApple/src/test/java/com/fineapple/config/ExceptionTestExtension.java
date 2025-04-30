package com.fineapple.config;

import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;

/**
 * 커스텀 에너테이션에 테스트 실행 확장 커스텸 익스텐션 예외용
 */
public class ExceptionTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestExecutionExceptionHandler {

    private Throwable throwable;

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Method method = context.getRequiredTestMethod();
        ExceptionTest annotation = method.getAnnotation(ExceptionTest.class);

        if (throwable == null) {
            throw new AssertionError("예외가 발생하지 않았습니다. 예상 예외: " + annotation.expected().getSimpleName());
        }

        if (!annotation.expected().isInstance(throwable)) {
            throw new AssertionError("예상 예외: " + annotation.expected().getSimpleName()
                    + ", 실제 예외: " + throwable.getClass().getSimpleName());
        }

        if (!annotation.message().isEmpty() && !annotation.message().equals(throwable.getMessage())) {
            throw new AssertionError("예상 메시지: \"" + annotation.message()
                    + "\", 실제 메시지: \"" + throwable.getMessage() + "\"");
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {

    }
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        this.throwable = throwable;
    }
}
