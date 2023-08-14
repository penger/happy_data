package cn.happy.writer.batch;

import cn.happy.component.EsComponent;
import cn.happy.factory.EsFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;

import java.io.*;

public class Es2JsonTransformer {

    public static RestHighLevelClient getDefaultEsHighLevelClient(){
        EsComponent.EsParam esParam = EsFactory.getEsParam(EsFactory.DEFAULT_ES);
        return EsComponent.getRestHighLevelClient(esParam);
    }





    public static void main(String[] args) throws IOException {
        RestHighLevelClient client =getDefaultEsHighLevelClient();
        File file = new File("d://a.json");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BulkRequest bulkRequest = new BulkRequest();
        String line ;
        while((line=reader.readLine())!=null){
            JsonObject jsonObject = JsonParser.parseString(line).getAsJsonObject();
            String id = jsonObject.get("_id").getAsString();
            JsonObject sourceObject = jsonObject.get("_source").getAsJsonObject();
            String json = new Gson().toJson(sourceObject);
            IndexRequest indexRequest = new IndexRequest("document_news_copy").id(id).source(json,XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if(bulkResponse.hasFailures()){
            for (BulkItemResponse item : bulkResponse) {
                if(item.isFailed()){
                    BulkItemResponse.Failure failure = item.getFailure();
                    System.out.println("failed index id :"+ failure.getId()+ " message is "+ item.getFailureMessage());
                }
            }
        }else{
            System.out.println("bulk success");
        }
        reader.close();
        client.close();


    }

    public static void exportEsRecord2Json() throws IOException {
        RestHighLevelClient client =getDefaultEsHighLevelClient();
        String index = "document_news";
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        request.scroll(TimeValue.timeValueMinutes(1L));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        String scrollId = response.getScrollId();
        SearchHit[] hits = response.getHits().getHits();
        File file = new File("d://a.json");
        if(file.exists()) file.delete();
        FileWriter fileWriter = new FileWriter(file);

        for (SearchHit hit : hits) {
            String id = hit.getId();
            String sourceWithId = "{\"_id\": \"" + id + "\", \"_source\": " + hit.getSourceAsString() + "}";
            fileWriter.write(sourceWithId);
            fileWriter.write("\n");
        }
        while(hits != null && hits.length > 0 ){
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(TimeValue.timeValueMinutes(1L));
            SearchResponse searchResponse = client.scroll(searchScrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits().getHits();
            for (SearchHit hit : hits) {
                String id = hit.getId();
                String sourceWithId = "{\"_id\": \"" + id + "\", \"_source\": " + hit.getSourceAsString() + "}";
                fileWriter.write(sourceWithId);
                fileWriter.write("\n");
            }

        }
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        client.clearScroll(clearScrollRequest,RequestOptions.DEFAULT);
        fileWriter.close();
        client.close();
    }

}
