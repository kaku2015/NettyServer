package com.skr.datagather;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyHelloServerHanler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// String recStr = new String(msg.getBytes(), Charset.forName("UTF-8"));
		// 收到消息直接打印输出
		System.out.println(ctx.channel().remoteAddress() + " say :" + msg);
		// 注意:字符串最后面的"\n"是必须的。因为我们在前面的解码器DelimiterBasedFrameDecoder是一个根据字符串结尾为“\n”来结尾的。假如没有这个字符的话。解码会出现问题。
		// 返回客户端
		ctx.writeAndFlush("我是服务端，接收你的消息!\n");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RamoteAddress: " + ctx.channel().remoteAddress() + " active!");
		// ctx.writeAndFlush("Welcome to " +
		// InetAddress.getLocalHost().getHostName() + "'s service!\n");
		ctx.writeAndFlush("我是服务端，建立连接成功！\n");
		super.channelActive(ctx);
	}
}
