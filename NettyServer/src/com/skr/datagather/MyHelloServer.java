package com.skr.datagather;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyHelloServer {  
	  
    private static final int PORT = 7882;  
    
    public static void main(String[] args) {  
        EventLoopGroup parentGroup = new NioEventLoopGroup();  
        EventLoopGroup childrenGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap serverBootstrap = new ServerBootstrap();  
            serverBootstrap.group(parentGroup, childrenGroup);  
            serverBootstrap.channel(NioServerSocketChannel.class);  
            //��ӹ����߳�  
            serverBootstrap.childHandler(new MyHelloServerInitializer());  
            // �������󶨶˿ڼ���  
            ChannelFuture cf = serverBootstrap.bind(PORT).sync();  
            // �����������رռ���  
            cf.channel().closeFuture().sync();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            parentGroup.shutdownGracefully();  
            childrenGroup.shutdownGracefully();  
        }  
    }  
}  
