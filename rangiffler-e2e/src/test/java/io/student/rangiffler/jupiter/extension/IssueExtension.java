package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.config.GithubApiClient;
import io.student.rangiffler.jupiter.annotation.DisabledByIssue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

@Slf4j
public class IssueExtension implements ExecutionCondition {

    private final GithubApiClient githubApiClient = new GithubApiClient();

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                DisabledByIssue.class
        ).or(() -> AnnotationSupport.findAnnotation(
                context.getRequiredTestClass(),
                DisabledByIssue.class,
                SearchOption.INCLUDE_ENCLOSING_CLASSES
        )).map(byIssue -> {
            try {
                return "open".equals(githubApiClient.issueState(byIssue.value()))
                        ? ConditionEvaluationResult.disabled("Disabled by issue: " + byIssue.value())
                        : ConditionEvaluationResult.enabled("Issue is closed");
            } catch (Exception e) {
                log.warn("Failed to fetch issue state from GitHub", e);
                return ConditionEvaluationResult.enabled("Could not verify issue state: " + e.getMessage());
            }
        }).orElseGet(
                () -> ConditionEvaluationResult.enabled("No issue annotation found")
        );
    }
}
