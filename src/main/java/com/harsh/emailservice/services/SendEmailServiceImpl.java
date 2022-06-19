package com.harsh.emailservice.services;


import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.enums.ClientStatus;
import com.harsh.emailservice.enums.HandlerState;
import com.harsh.emailservice.exceptions.EmailClientException;
import com.harsh.emailservice.exceptions.EmailClientFatalException;
import com.harsh.emailservice.services.handlers.EmailClientHandler;
import com.harsh.emailservice.services.interfaces.SendEmailService;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * This class provides the service to send email using one or multiple clients with a failover.
 */
@Component
@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private List<EmailClientHandler> emailClientHandlerList;
    private MultiValuedMap<HandlerState, EmailClientHandler> emailClientHandlersMap;
    private static final Logger logger = LoggerFactory.getLogger(SendEmailServiceImpl.class);

    private HandlerState currentStatus;


    @PostConstruct
    public void intialiseEmailHandlerMap(){
        emailClientHandlersMap = new ArrayListValuedHashMap<>();
        emailClientHandlerList.forEach(emailClientHandler -> emailClientHandlersMap.put(HandlerState.ALIVE, emailClientHandler));
    }

    /**
     * This method sends the request to send email to one of the endpoints using the handlers configured.
     * @param emailRequest
     * @return - In case of success, returns Mono with response, else mono with error
     */
    public Mono<EmailResponse> sendEmail(EmailRequest emailRequest) {

        EmailClientHandler emailClientHandler = getEmailClientHandlerFromMap();
        String handlerName;

        if(emailClientHandler != null){

             handlerName = emailClientHandler.getClientType().name();
            logger.info("Working with handler {}",handlerName );
            try {

                return emailClientHandler.sendEmail(emailRequest)
                        .doOnNext(response -> resetEmailHandlerClientMap())
                        .onErrorResume(exception -> {
                            logger.info("Request erred with handler {} looking for alternatives", handlerName);
                            updateEmailHandlerStatus(emailClientHandler, currentStatus);

                           if(isEmailHandlerClientAvailable()){
                               //send request using another email handler
                               return sendEmail(emailRequest);
                           }else{
                               //return error, if no handler is available
                               logger.error("No more healthy handlers available to retry");
                               if(!(exception instanceof EmailClientFatalException) || !(exception instanceof EmailClientException)){
                                   logger.error("Error occurred while initializing the handlers", exception);
                                   return Mono.error(new EmailClientFatalException("Fatal Error Occurred"));
                               }
                               return Mono.error(exception);
                           }
                        });
            } catch (Exception e) {
                logger.error("Error occurred in servicing the request", e);
                return Mono.error(e);
            }
        }else {
            logger.error("Cannot service request, as no Email clients are configured");
            return Mono.error(new EmailClientFatalException("No Email Clients available"));
        }
    }

    /**
     * This method sets the status for each handler, in case of error.
     * @param emailClientHandler
     * @param oldState
     */
    private void updateEmailHandlerStatus(EmailClientHandler emailClientHandler, HandlerState oldState){
        ClientStatus status = emailClientHandler.getStatus();
        if(status.equals(ClientStatus.FAILED_INIT)){
            emailClientHandlersMap.removeMapping(oldState, emailClientHandler);
        } else if (status.equals(ClientStatus.ERRORED)) {
            emailClientHandlersMap.removeMapping(oldState, emailClientHandler);
            emailClientHandlersMap.put(HandlerState.ERRORED_USED, emailClientHandler);
        }
    }

    /**
     * This method resets the status of all handlers with status ERROR_USED to ERROR_UNUSED
     */
    private void resetEmailHandlerClientMap(){
        Collection<EmailClientHandler> erroredUsedCollection = emailClientHandlersMap.get(HandlerState.ERRORED_USED);
        if(erroredUsedCollection != null && !erroredUsedCollection.isEmpty()){
            emailClientHandlersMap.putAll(HandlerState.ERRORED_UNUSED, erroredUsedCollection);
            emailClientHandlersMap.remove(HandlerState.ERRORED_USED);
        }

    }

    /**
     * This method checks if any Email Handler client are available for the use.
     * @return - true, if any clients with status ALIVE or ERROR_UNUSED, else false
     */
    private boolean isEmailHandlerClientAvailable(){
        boolean isAvailable = true;
        Collection<EmailClientHandler> aliveCollection = emailClientHandlersMap.get(HandlerState.ALIVE);
        if(aliveCollection == null || aliveCollection.isEmpty()) {
            Collection<EmailClientHandler> erroredUnusedCollection = emailClientHandlersMap.get(HandlerState.ERRORED_USED);
            if(erroredUnusedCollection == null || erroredUnusedCollection.isEmpty()){
                isAvailable = false;
            }
        }
        return isAvailable;
    }

    /**
     * This method checks if any Email Handler is available for use.
     *  It first looks with status as ALIVE, in the map. If no handlers are alive,
     *  then it checks with status ERROR_UNUSED.
     * @return - Available email client handler
     */
    private EmailClientHandler getEmailClientHandlerFromMap(){
        Collection<EmailClientHandler> emailClientHandlerCollection = emailClientHandlersMap.get(HandlerState.ALIVE);
        EmailClientHandler emailClientHandler = getEmailClientHandlerFromCollection(emailClientHandlerCollection);

        if(emailClientHandler!=null){
                currentStatus = HandlerState.ALIVE;
        }
        else{
            emailClientHandlerCollection = emailClientHandlersMap.get(HandlerState.ERRORED_UNUSED);
            emailClientHandler = getEmailClientHandlerFromCollection(emailClientHandlerCollection);
            currentStatus = HandlerState.ERRORED_UNUSED;
        }

        return emailClientHandler;
    }

    /**
     * This method returns the first object from the Collection<EmailClientHandler></EmailClientHandler>
     * @param handlerCollection
     * @return - The first object from the Collection<EmailClientHandler></EmailClientHandler>
     */
    private EmailClientHandler getEmailClientHandlerFromCollection(Collection<EmailClientHandler> handlerCollection){
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
