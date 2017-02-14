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
	private static final String KEY_PASSWORD = "123456";// keyStore������.
	private static final String KEYSTORE_PATH = "src/kserver.keystore";// ��Կ��·��
	private static final String KEYSTORE_TRUST_PATH = "src/tserver.keystore";// ��������εĿͻ�����Կ��·��

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// �ڴ���������,SslHandler ����Ϊ ChannelPipeline �еĵ�һ��
		// ChannelHandler�����Ե�����pipeline.addFirst() ���⽫ȷ���������� ChannelHandler
		// Ӧ�����ǵ��߼������ݺ���ܺ�ŷ���,�Ӷ�ȷ�����ǵı仯�ǰ�ȫ�ġ�
		SSLEngine sslEngine = getSSLContext().createSSLEngine();
		sslEngine.setUseClientMode(false);
		// ˫����֤�����ڷ����������Ч
		sslEngine.setNeedClientAuth(true);
		pipeline.addFirst("ssl", new SslHandler(sslEngine));

		// ��("\n")Ϊ��β�ָ�� ������ DelimiterBasedFrameDecoder ��Ϣ�ָ�
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

		// �ַ������� �� ����
		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

		// �Լ����߼�Handler
		pipeline.addLast("handler", new MyHelloServerHanler());
	}

	public SSLContext getSSLContext() {
		SSLContext sslContext = null;
		try {
			// ��������ǲ���javaĬ�ϵ���Կ��JKS���ͣ�ͨ��KeyStore��ľ�̬�������ʵ������ָ����Կ������
			KeyStore serverKeyStore = KeyStore.getInstance("JKS");
			// �����ṩ����Կ���ļ��������������ʼ����Կ��ʵ��
			serverKeyStore.load(new FileInputStream(KEYSTORE_PATH), KEY_PASSWORD.toCharArray());

			KeyStore trustKeyStore = KeyStore.getInstance("JKS");
			trustKeyStore.load(new FileInputStream(KEYSTORE_TRUST_PATH), KEY_PASSWORD.toCharArray());

			// ȡ��SunX509˽Կ������
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			// ��֮ǰ��ʼ�������Կ��ʵ����ʼ��˽Կ������
			keyManagerFactory.init(serverKeyStore, KEY_PASSWORD.toCharArray());

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(trustKeyStore);

			// ���TLSЭ���SSLContextʵ��
			sslContext = SSLContext.getInstance("TLS");
			// ��ʼ��SSLContextʵ��
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sslContext;
	}
}
