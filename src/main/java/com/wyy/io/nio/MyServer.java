package com.wyy.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class MyServer {

	public static void main(String[] args) throws IOException {
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(9090));
		serverSocketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocate(256);
		while(true){
		    SocketChannel socketChannel =
		            serverSocketChannel.accept();
		    if(socketChannel!=null) {
			    buf.clear();
			    int bytesRead  = socketChannel.read(buf);
			    while(bytesRead>0){
                    buf.flip();
                    while(buf.hasRemaining()){
                        System.out.print((char)buf.get());
                    }
                    System.out.println();
                    buf.clear();
                    bytesRead = socketChannel.read(buf);
                }
		    }
		 
		}


	}

}
