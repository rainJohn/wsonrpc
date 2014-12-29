/**
 * Copyright (C) 2014, Apexes.net. All rights reserved.
 * 
 *        http://www.apexes.net
 * 
 */
package net.apexes.wsonrpc.service;

import java.util.Collection;

import javax.websocket.Session;

import net.apexes.wsonrpc.WsonrpcRemote;

/**
 * 
 * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
 *
 */
public interface WsonrpcService {

    /**
     * 返回当前线程的Session
     * 
     * @return
     */
    Session getSession();

    /**
     * 返回所有客户端连接
     * 
     * @return
     */
    Collection<WsonrpcRemote> getRemotes();

    /**
     * 返回指定ID的客户端连接
     * 
     * @param sessionId
     * @return
     */
    WsonrpcRemote getRemote(String sessionId);

    /**
     * 
     * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
     *
     */
    public static final class Manager {

        private static WsonrpcService instance = new WsonrpcService() {

            @Override
            public Session getSession() {
                return WsonrpcServiceContext.Sessions.get();
            }

            @Override
            public Collection<WsonrpcRemote> getRemotes() {
                return WsonrpcServiceContext.Remotes.getRemotes();
            }

            @Override
            public WsonrpcRemote getRemote(String sessionId) {
                return WsonrpcServiceContext.Remotes.getRemote(sessionId);
            }

        };

        public static Session getSession() {
            return instance.getSession();
        }

        public static WsonrpcRemote getRemote(String sessionId) {
            return instance.getRemote(sessionId);
        }

        public static Collection<WsonrpcRemote> getRemotes() {
            return instance.getRemotes();
        }

        public static WsonrpcRemote getRemote() {
            Session session = instance.getSession();
            if (session != null) {
                return instance.getRemote(session.getId());
            }
            return null;
        }

    }

}