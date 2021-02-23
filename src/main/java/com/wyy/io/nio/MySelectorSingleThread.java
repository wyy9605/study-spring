package com.wyy.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MySelectorSingleThread {

	private ServerSocketChannel server =null;
	private Selector selector = null;
	int port = 9090;
	
	public void initServer() {
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.bind(new InetSocketAddress(port));
			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		initServer();
		System.out.println("服务启动了。。。。");
		try {
			while(true) {
				while(selector.select(0)>0) {//是否有事件
					Set<SelectionKey> selectionKeys =  selector.selectedKeys();//从多路复用器取出有效的key
					
					Iterator<SelectionKey> iter = selectionKeys.iterator();
					while(iter.hasNext()) {
						SelectionKey key = iter.next();
						iter.remove();
						if(key.isAcceptable()) {
							acceptHandler(key);
						}else if(key.isReadable()) {
							readHander(key);
						}
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void acceptHandler(SelectionKey key) {
		try {
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			SocketChannel client = ssc.accept();
			client.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(2);
			client.register(selector, SelectionKey.OP_READ,buffer);
			System.out.println("--------------");
			System.out.println("新客户端："+client.getRemoteAddress());
			System.out.println("--------------");
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readHander(SelectionKey key) {
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		buffer.clear();
		int read = 0;
		try {
			while(true) {
				read = client.read(buffer);
				if(read>0) {
					buffer.flip();
					while(buffer.hasRemaining()) {
						client.write(buffer);
						
					}
					buffer.clear();
				}else if (read ==0) {
					break;
				}else {
					client.close();
					break;
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		MySelectorSingleThread s = new MySelectorSingleThread();
		s.start();
	}
}
