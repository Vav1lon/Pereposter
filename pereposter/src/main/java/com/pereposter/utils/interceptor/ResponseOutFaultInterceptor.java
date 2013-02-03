package com.pereposter.utils.interceptor;

import com.pereposter.entity.RestResponse;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.*;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ResponseOutFaultInterceptor extends AbstractOutDatabindingInterceptor {

    public ResponseOutFaultInterceptor() {
        super(Phase.SETUP);
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        Exception exception = message.getContent(Exception.class);
        Exchange exchange = message.getExchange();

        resetOrigInterceptorChain(message);
        resetFault(exchange);

        Message outMessage = createOutMessage(exchange, exception);

        InterceptorChain chain = prepareNewInterceptorChain(exchange);
        chain.doIntercept(outMessage);

    }

    private InterceptorChain prepareNewInterceptorChain(Exchange exchange) {
        Message message = exchange.getOutMessage();

        InterceptorChain chain = OutgoingChainInterceptor.getOutInterceptorChain(exchange);
        message.setInterceptorChain(chain);

        return chain;
    }

    private Message createOutMessage(Exchange exchange, Exception exception) {
        Endpoint ep = exchange.get(Endpoint.class);

        Message outMessage = ep.getBinding().createMessage();
        outMessage.setExchange(exchange);
        //TODO: tmp impl, хз почему так работает. просто обект не работает, пытается вернуть xml
        outMessage.setContent(List.class, new ArrayList(Arrays.asList(new RestResponse(exception.getMessage()))));

        exchange.setOutMessage(outMessage);
        return outMessage;
    }

    private void resetFault(Exchange exchange) {
        exchange.put(Exception.class, null);
    }

    private void resetOrigInterceptorChain(Message message) {
        InterceptorChain chain = message.getInterceptorChain();
        for (Interceptor<?> interceptor : chain) {
            chain.remove(interceptor);
        }
        chain.reset();
    }

}
