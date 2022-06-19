package com.harsh.emailservice.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Interface to implement custom request validation for allowed maximum email recipients
 */
@Constraint(validatedBy = MaxEmailRecipientsLimitValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MaxEmailRecipientsLimit {

    String message() default "Max recipients cannot exceed 1000";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
