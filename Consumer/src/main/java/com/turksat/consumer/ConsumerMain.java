package com.turksat.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.turksat.dblib.DBManager;
import com.turksat.dblib.model.Data;
import com.turksat.dblib.model.User;
import java.net.InetSocketAddress;
import net.spy.memcached.MemcachedClient;

public class ConsumerMain {

	public static void main(String[] argv) throws Exception {
		MemcachedClient spyMemCached = new MemcachedClient(new InetSocketAddress("localhost", 11211));
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare("login", false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume("login", false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			User user = User.fromBytes(delivery.getBody());

			Data data = new DBManager().checkLogin(user.getUserID(), user.getUserPassword());
			spyMemCached.set(user.getUUID() + ".login", 30, data);

			System.out.println(data + " inserted to cache. ID: " + user.getUserID());

			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}
}
