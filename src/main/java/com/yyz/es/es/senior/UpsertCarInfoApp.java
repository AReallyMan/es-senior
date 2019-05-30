package com.yyz.es.es.senior;

import java.net.InetAddress;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
/**
 * //插入数据，如果第一次插入之前没有则为source中的内容，第二次执行因为已经有这个数据就执行doc中的update更新操作
 * @author asus
 *
 */
public class UpsertCarInfoApp {
	public static void main(String[] args) throws Exception{
		
		Settings settings=Settings.builder()
				.put("cluster.name","elasticsearch")
				.put("client.transport.sniff",true)
				.build();
		@SuppressWarnings("resource")
		TransportClient client=new PreBuiltTransportClient(settings)
				.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
		
		IndexRequest indexRequest=new IndexRequest("car_shop","cars","2")
				.source(XContentFactory.jsonBuilder()
						.startObject()
							.field("brand","奔驰")
							.field("name","奔驰320")
							.field("price","44000")
							.field("produce_date","2017-01-01")
						.endObject());
		UpdateRequest updateRequest=new UpdateRequest("car_shop","cars","2")
				.doc(XContentFactory.jsonBuilder()
						.startObject()
							.field("price","3000")
						.endObject())
				.upsert(indexRequest);
		UpdateResponse updateResponse=client.update(updateRequest).get();
		System.out.println(updateResponse.getVersion());
	}
}
