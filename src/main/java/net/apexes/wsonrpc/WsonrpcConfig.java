/**
 * Copyright (C) 2014, Apexes.net. All rights reserved.
 * 
 *        http://www.apexes.net
 * 
 */
package net.apexes.wsonrpc;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

import net.apexes.wsonrpc.support.JacksonRpcHandler;

/**
 * 
 * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
 *
 */
public interface WsonrpcConfig {

    ExecutorService getExecutorService();
    
    RpcHandler getRpcHandler();

    BinaryWrapper getBinaryWrapper();

    /**
     * 返回超时时间，0表示永不超时。单位为TimeUnit.MILLISECONDS
     * 
     * @return
     */
    long getTimeout();

    /**
     * 
     * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
     *
     */
    public static class Builder {
    
        public static Builder create() {
            return new Builder();
        }
        
        private RpcHandler rpcHandler;
        private BinaryWrapper binaryWrapper;
        private long timeout;
        
        public WsonrpcConfig build(ExecutorService execService) {
        	if (binaryWrapper == null) {
        		binaryWrapper = new BinaryWrapper() {
        		    @Override
                    public InputStream wrap(InputStream ips) throws Exception {
                        return ips;
                    }

                    @Override
                    public OutputStream wrap(OutputStream ops) throws Exception {
                        return ops;
                    }
        		};
        	}
        	if (rpcHandler == null) {
        		rpcHandler = new JacksonRpcHandler();
        	}
        	return new SimpleWsonrpcConfig(execService, rpcHandler, binaryWrapper, timeout);
        }
        
        public Builder rpcHandler(RpcHandler rpcHandler) {
            this.rpcHandler = rpcHandler;
            return this;
        }
        
        public Builder binaryWrapper(BinaryWrapper binaryWrapper) {
            this.binaryWrapper = binaryWrapper;
            return this;
        }
        
        public Builder timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

    }
    
    /**
     * 
     * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
     *
     */
    static class SimpleWsonrpcConfig implements WsonrpcConfig {

        private final ExecutorService execService;
        private final RpcHandler rpcHandler;
        private final BinaryWrapper binaryWrapper;
        private final long timeout;

        public SimpleWsonrpcConfig(ExecutorService execService, RpcHandler rpcHandler, 
        		BinaryWrapper binaryWrapper, long timeout) {
            this.execService = execService;
            this.rpcHandler = rpcHandler;
            this.binaryWrapper = binaryWrapper;
            this.timeout = timeout;
        }

        @Override
        public ExecutorService getExecutorService() {
            return execService;
        }

    	@Override
    	public RpcHandler getRpcHandler() {
    		return rpcHandler;
    	}

        @Override
        public BinaryWrapper getBinaryWrapper() {
            return binaryWrapper;
        }

        @Override
        public long getTimeout() {
            return timeout;
        }

    }

}
