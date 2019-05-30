package com.yyz.es.es.senior;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
/**
 * 批量修改
 * @author asus
 *
 */
public class BulkUploadSalesDateApp {
	public static void main(String[] args) throws Exception{
		Settings settings=Settings.builder()
				.put("cluster.name","elasticsearch")
				.build();
		TransportClient client=new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
		BulkRequestBuilder prepareBulk = client.prepareBulk();
		IndexRequestBuilder indexRequestBuilder=client.prepareIndex("car_shop","sales","3")
				.setSource(XContentFactory.jsonBuilder()
						.startObject()
							.field("brand","东瀛战神")
							.field("name","gtr")
							.field("price","1800000")
							.field("produce_date","2011-10-10")
							.field("sales_price","1700000")
							.field("sales_date","2019-10-10")
						.endObject());
		prepareBulk.add(indexRequestBuilder);
		UpdateRequestBuilder updateprepareUpdate = client.prepareUpdate("car_shop","sales","1")
				.setDoc(XContentFactory.jsonBuilder()
					.startObject()
						.field("price","290000")
					.endObject());
		prepareBulk.add(updateprepareUpdate);
		DeleteRequestBuilder deleteRequestBuilder=client.prepareDelete("car_shop","sales","2");
		prepareBulk.add(deleteRequestBuilder);
		BulkResponse bulkResponse = prepareBulk.get();
		for(BulkItemResponse bulkItemResponse:bulkResponse.getItems()) {
			System.out.println(bulkItemResponse.getVersion());
		}
		client.close();
	}
	
}
