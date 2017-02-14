package com.skr.datagather;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author hyw
 * @since 2017/2/13
 */
public class MyHelloServerHanler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// �յ���Ϣֱ�Ӵ�ӡ���
		System.out.println(ctx.channel().remoteAddress() + " say :" + msg);
		// ���ؿͻ���
		// ע��:�ַ���������"\n"�Ǳ���ġ���Ϊ������ǰ��Ľ�����DelimiterBasedFrameDecoder��һ�������ַ�����βΪ��\n������β�ġ�����û������ַ��Ļ��������������⡣
		ctx.writeAndFlush("���Ƿ���ˣ����������Ϣ!\n");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RamoteAddress: " + ctx.channel().remoteAddress() + " active!");
		ctx.writeAndFlush("���Ƿ���ˣ��������ӳɹ���\n");
		super.channelActive(ctx);
	}
}
