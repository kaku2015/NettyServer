package com.skr.datagather;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyHelloServerHanler extends SimpleChannelInboundHandler<String> {  
	  
    @Override  
    protected void channelRead0(ChannelHandlerContext ctx, String msg)  
            throws Exception {  
//        String recStr = new String(msg.getBytes(), Charset.forName("UTF-8"));  
        //�յ���Ϣֱ�Ӵ�ӡ���  
        System.out.println(ctx.channel().remoteAddress()+" say :"+msg);  
        //���ؿͻ���   
        ctx.writeAndFlush("���Ƿ���ˣ����������Ϣ!\n");  
    }  
  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("RamoteAddress: "+ctx.channel().remoteAddress()+" active!");  
//      ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "'s service!\n");  
        ctx.writeAndFlush("���Ƿ���ˣ����Ƿ���ˣ�\n");  
        super.channelActive(ctx);  
    }  
}  
