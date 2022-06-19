package com.harsh.emailservice.constraints;

import com.harsh.emailservice.dto.EmailRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class MaxEmailRecipientsLimitValidator implements ConstraintValidator<MaxEmailRecipientsLimit, EmailRequest> {

    /**
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(MaxEmailRecipientsLimit constraintAnnotation) {
        //No initialization required
    }


    /**
     * This method checks whether the count of email addresses across "to", "cc", "bcc" is less than or equal to 1000, which
     * is the limit imposed by endpoints.
     * @param emailRequest   EmailRequest object to validate
     * @param context context in which the constraint is evaluated
     * @return true, if count is less than or equal to 1000, else false
     */
    @Override
    public boolean isValid(EmailRequest emailRequest, ConstraintValidatorContext context) {

        Set<String> to = emailRequest.getTo();
        Set<String> cc = emailRequest.getCc();
        Set<String> bcc = emailRequest.getBcc();
        int toSize = 0;
        int ccSize = 0;
        int bccSize = 0;
        if(to!=null){
            toSize = to.size();
        }
        if(cc!=null){
            ccSize = cc.size();
        }
        if(bcc!=null){
            bccSize = bcc.size();
        }
        return (toSize + bccSize + ccSize) <= 1000;
    }

}
