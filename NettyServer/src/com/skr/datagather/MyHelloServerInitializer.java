package com.skr.datagather;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

/**
 * @author hyw
 * @since 2017/2/13
 */
public class MyHelloServerInitializer extends ChannelInitializer<SocketChannel> {
	private static final String KEY_PASSWORD = "123456";// keyStore的密码.
	private static final String KEYSTORE_PATH = "src/kserver.keystore";// 密钥库路径
	private static final String KEYSTORE_TRUST_PATH = "src/tserver.keystore";// 服务端信任的客户端密钥库路径

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// 在大多数情况下,SslHandler 将成为 ChannelPipeline 中的第一个
		// ChannelHandler，所以调用了pipeline.addFirst() 。这将确保所有其他 ChannelHandler
		// 应用他们的逻辑到数据后加密后才发生,从而确保他们的变化是安全的。
		SSLEngine sslEngine = getSSLContext().createSSLEngine();
		sslEngine.setUseClientMode(false);
		// 双向验证，仅在服务端设置有效
		sslEngine.setNeedClientAuth(true);
		pipeline.addFirst("ssl", new SslHandler(sslEngine));

		// 以("\n")为结尾分割的 解码器 DelimiterBasedFrameDecoder 消息分割
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

		// 字符串解码 和 编码
		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

		// 自己的逻辑Handler
		pipeline.addLast("handler", new MyHelloServerHanler());
	}

	public SSLContext getSSLContext() {
		SSLContext sslContext = null;
		try {
			// 服务端我们采用java默认的密钥库JKS类型，通过KeyStore类的静态方法获得实例并且指定密钥库类型
			KeyStore serverKeyStore = KeyStore.getInstance("JKS");
			// 利用提供的密钥库文件输入流和密码初始化密钥库实例
			serverKeyStore.load(new FileInputStream(KEYSTORE_PATH), KEY_PASSWORD.toCharArray());

			KeyStore trustKeyStore = KeyStore.getInstance("JKS");
			trustKeyStore.load(new FileInputStream(KEYSTORE_TRUST_PATH), KEY_PASSWORD.toCharArray());

			// 取得SunX509私钥管理器
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			// 用之前初始化后的密钥库实例初始化私钥管理器
			keyManagerFactory.init(serverKeyStore, KEY_PASSWORD.toCharArray());

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(trustKeyStore);

			// 获得TLS协议的SSLContext实例
			sslContext = SSLContext.getInstance("TLS");
			// 初始化SSLContext实例
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sslContext;
	}
}
