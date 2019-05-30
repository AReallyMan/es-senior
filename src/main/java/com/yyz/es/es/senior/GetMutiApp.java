package com.yyz.es.es.senior;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
/**
 * 一次拿出多个信息，进行对比
 * 批量读取mget，批量写入bulk
 * @author asus
 *
 */
public class GetMutiApp {
	public static void main(String[] args) throws Exception {
		Settings settings=Settings.builder()
				.put("cluster.name","elasticsearch")
				.build();
		TransportClient client=new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		MultiGetResponse multiGetResponse=client.prepareMultiGet()
				.add("car_shop","cars","1")
				.add("car_shop","cars","2")
				.get();
		for(MultiGetItemResponse multiGetItemResponse:multiGetResponse) {
			GetResponse response = multiGetItemResponse.getResponse();
			if(response.isExists()) {
				System.out.println(response.getSourceAsString());
			}
		}
		client.close();
	}
	
}
