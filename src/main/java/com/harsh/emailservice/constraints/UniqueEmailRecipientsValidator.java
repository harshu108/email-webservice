package com.harsh.emailservice.constraints;

import com.harsh.emailservice.dto.EmailRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.Set;

public class UniqueEmailRecipientsValidator implements ConstraintValidator<UniqueEmailRecipients, EmailRequest> {

    /**
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(UniqueEmailRecipients constraintAnnotation) {
        //No initialization required
    }

    /**
     * This method checks whether the email addresses across "to", "cc", "bcc" are unique, as required by endpoints.
     * @param emailRequest   EmailRequest object to validate
     * @param context context in which the constraint is evaluated
     * @return - true, if email recipients are unique across "to", "cc", "bcc", else false
     */
    @Override
    public boolean isValid(EmailRequest emailRequest, ConstraintValidatorContext context) {

        Set<String> to = emailRequest.getTo();
        Set<String> cc = emailRequest.getCc();
        Set<String> bcc = emailRequest.getBcc();

        if(to !=null && cc!=null && bcc !=null ){
            return  Collections.disjoint(to,cc) && Collections.disjoint(cc, bcc) && Collections.disjoint(to, bcc);
        } else if (to!=null && cc != null) {
            return  Collections.disjoint(to,cc);
        } else if(to!=null && bcc!=null){
            return Collections.disjoint(to, bcc);
        }else if(cc!=null && bcc!=null){
            return Collections.disjoint(cc, bcc);
        }else{
            return true;
        }
    }

}
