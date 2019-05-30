package com.yyz.es.es.senior;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
/**
 * 批量查询数据scroll
 * @author asus
 *
 */
public class ScrollDownloadApp {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Settings settings=Settings.builder()
				.put("cluster.name","elasticsearch")
				.build();
		TransportClient client=new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
		SearchResponse searchResponse = client.prepareSearch("car_shop")
		.setTypes("sales")
		.setQuery(QueryBuilders.termQuery("brand.keyword", "宝马"))
		.setScroll(new TimeValue(60000))
		.setSize(1)
		.get();
		do {
			int batchCount=0;
			for(SearchHit searchHit:searchResponse.getHits().getHits()) {
				System.out.println("batchCount"+ "  "+ ++batchCount);
				System.out.println(searchHit.getSourceAsString());
			}
			searchResponse=client.prepareSearchScroll(searchResponse.getScrollId())
			.setScroll(new TimeValue(60000))
			.execute()
			.actionGet();
			
		}while(searchResponse.getHits().getHits().length!=0);
		
		client.close();
	}
}
