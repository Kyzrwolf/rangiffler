package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.api.core.ThreadSafeCookieStore;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CookiesExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        ThreadSafeCookieStore.INSTANCE.removeAll();
    }
}
