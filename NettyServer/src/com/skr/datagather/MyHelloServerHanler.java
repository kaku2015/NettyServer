package com.skr.datagather;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyHelloServerHanler extends SimpleChannelInboundHandler<String> {  
	  
    @Override  
    protected void channelRead0(ChannelHandlerContext ctx, String msg)  
            throws Exception {  
//        String recStr = new String(msg.getBytes(), Charset.forName("UTF-8"));  
        //收到消息直接打印输出  
        System.out.println(ctx.channel().remoteAddress()+" say :"+msg);  
        //返回客户端   
        ctx.writeAndFlush("我是服务端，接收你的消息!\n");  
    }  
  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("RamoteAddress: "+ctx.channel().remoteAddress()+" active!");  
//      ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "'s service!\n");  
        ctx.writeAndFlush("我是服务端，我是服务端！\n");  
        super.channelActive(ctx);  
    }  
}  
