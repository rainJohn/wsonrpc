package net.apexes.wsonrpc.internal;

import java.lang.reflect.Type;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.apexes.wsonrpc.WsonException;
import net.apexes.wsonrpc.WsonrpcRemote;
import net.apexes.wsonrpc.WsonrpcSession;

/**
 * 
 * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
 *
 */
public class WsonrpcEndpoint implements WsonrpcRemote {

    private WsonrpcSession session;
    private ICaller caller;

    protected WsonrpcEndpoint() {
    }

    protected final void online(WsonrpcSession session, ICaller caller) {
        this.session = session;
        this.caller = caller;
    }
    
    protected final void offline() {
        this.session = null;
        this.caller = null;
    }
    
    protected WsonrpcSession getSession() {
        return session;
    }
    
    @Override
    public boolean isOpen() {
        return session != null && session.isOpen();
    }

    @Override
    public String getSessionId() {
        if (session != null) {
            return session.getId();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    @Override
    public void notify(String serviceName, String methodName, Object argument) throws Exception {
        verifyOnline();
        caller.notify(session, serviceName, methodName, argument);
    }

    @Override
    public Object invoke(String serviceName, String methodName, Object argument, Type returnType)
            throws Exception {
        return invoke(serviceName, methodName, argument, returnType, caller.getTimeout());
    }

    @Override
    public Object invoke(String serviceName, String methodName, Object argument, Type returnType, long timeout)
            throws Exception {
        Future<Object> future = asyncInvoke(serviceName, methodName, argument, returnType);
        return getValue(future, timeout);
    }

    @Override
    public <T> T invoke(String serviceName, String methodName, Object argument, Class<T> returnType)
            throws Exception {
        return invoke(serviceName, methodName, argument, returnType, caller.getTimeout());
    }

    @Override
    public <T> T invoke(String serviceName, String methodName, Object argument, Class<T> returnType,
            long timeout) throws Exception {
        Future<T> future = asyncInvoke(serviceName, methodName, argument, returnType);
        return getValue(future, timeout);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<T> asyncInvoke(String serviceName, String methodName, Object argument,
            Class<T> returnType) throws Exception {
        return (Future<T>) asyncInvoke(serviceName, methodName, argument, Type.class.cast(returnType));
    }

    @Override
    public synchronized Future<Object> asyncInvoke(String serviceName, String methodName, Object argument,
            Type returnType) throws Exception {
        verifyOnline();
        return caller.request(session, serviceName, methodName, argument, returnType);
    }

    private <T> T getValue(Future<T> future, long timeout) throws Exception {
        try {
            if (timeout <= 0) {
                return future.get();
            } else {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            }
        } catch (Exception ex) {
            WsonrpcContext.Futures.out(future);
            throw ex;
        }
    }
    
    private void verifyOnline() throws Exception {
        if (!isOpen()) {
            throw new WsonException("Connection is closed.");
        }
    }

}
