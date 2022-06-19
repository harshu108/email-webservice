package com.harsh.emailservice.utils;

import com.harsh.emailservice.services.handlers.EmailClientHandler;

import java.util.Collection;
import java.util.Iterator;

public class MultiValuedMapUtils {

    public static EmailClientHandler getEmailClientHandlerFromCollection(Collection<EmailClientHandler> handlerCollection){
        EmailClientHandler handler = null;
        if(handlerCollection != null){
            Iterator<EmailClientHandler> handlerIterator = handlerCollection.iterator();
            if(handlerIterator.hasNext()){
                handler = handlerIterator.next();
            }
        }
        return handler;
    }
}
