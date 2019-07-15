package be.civadis.store.base.multitenancy;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MultiSchemasCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String ctx = context.getEnvironment().getProperty("spring.liquibase.contexts");
        return isMultiSchemasActivated(ctx);
    }

    public static boolean isMultiSchemasActivated(String liquibaseContext){
        return !liquibaseContext.contentEquals("test");
    }

}
