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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class MySelectorMultiThreads {

	private ServerSocketChannel server = null;
	private Selector selector1 = null;
	private Selector selector2 = null;
	private Selector selector3 = null;
	int port = 9090;
	
	public void initServer() {
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.bind(new InetSocketAddress(port));
			
			selector1 = Selector.open();
			selector2 = Selector.open();
			selector3 = Selector.open();
			server.register(selector1, SelectionKey.OP_ACCEPT);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		MySelectorMultiThreads service = new MySelectorMultiThreads();
		service.initServer();
		NioThread boss = new NioThread(service.selector1, 2);
		boss.start();
		Thread.sleep(1000);
		NioThread worker1 = new NioThread(service.selector2);
		NioThread worker2 = new NioThread(service.selector3);
		worker1.start();
		worker2.start();
		while(true);

		

	}


}
class NioThread extends Thread{
	
	Selector selector = null;
	static int selectors = 0;
	
	int id = -1;
	
	static BlockingQueue<SocketChannel>[] queue;
	
	static AtomicInteger idx = new AtomicInteger();
	
	public NioThread(Selector sel,int n) {
		this.selector = sel;
		selectors = n;
		
		queue = new LinkedBlockingDeque[selectors];
		for(int i = 0;i<n;i++) {
			queue[i] = new LinkedBlockingDeque<>();
		}
		System.out.println("Boss 启动");
	}
	public NioThread(Selector sel) {
		this.selector = sel;
		id = idx.incrementAndGet()%selectors;
		System.out.println("Workder:"+id+" 启动");
		
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				while(selector.select(10)>0) {
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					
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
				
				if(id!=-1&&!queue[id].isEmpty()) {
					ByteBuffer buffer = ByteBuffer.allocate(8192);
					SocketChannel client = queue[id].take();
					client.register(selector, SelectionKey.OP_READ ,buffer);
					System.out.println("---------------------");
					System.out.println("新客户断："+client.socket().getPort()+"分配到worker:"+id);
					System.out.println("---------------------");
				}
				
			}
		}catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void acceptHandler(SelectionKey key) {
		try {
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			SocketChannel client = ssc.accept();
			client.configureBlocking(false);
		
			int num =idx.getAndIncrement()%selectors;
			
			queue[num].add(client);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readHander(SelectionKey key) {
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		buffer.clear();
		int read = 0;
		System.out.println("worker:"+id+"接收到信息");
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

	
}
