package com.harsh.emailservice.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
/**
 * Interface to implement custom request validation for uniqueness of email recipients
 */
@Constraint(validatedBy = UniqueEmailRecipientsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueEmailRecipients {

    String message() default "Contents of the fields must be unique";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
