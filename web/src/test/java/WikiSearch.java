import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class WikiSearch {
    public static void main(String[] args) throws IOException {
        kk();
    }

    public static void kk() throws IOException {

        final HttpGet request = new HttpGet("http://localhost/search?content=wikipedia_zh_all_maxi_2023-03&pattern=美国");
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .build();
             CloseableHttpResponse response = (CloseableHttpResponse) client
                     .execute(request)) {
            DecompressingEntity entity = (DecompressingEntity) response.getEntity();
            InputStream content = entity.getContent();
            List<String> lines = IOUtils.readLines(content, "utf-8");
            StringBuffer sb = new StringBuffer();
            for (String line : lines) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            String html = sb.toString();
            Document doc = Jsoup.parse(html);
//            String pages = Xsoup.compile("/html/body/div[1]/b[2]/text()").evaluate(doc).get();
            Elements elements = Xsoup.compile("/html/body/div[2]/ul/li").evaluate(doc).getElements();
            for (Element element : elements) {
                System.out.println(Xsoup.select(element, "/a/text()").get());
                System.out.println(Xsoup.select(element, "/a/@href").get());
                System.out.println(Xsoup.select(element, "/cite/html()").get());
                System.out.println(Xsoup.select(element, "/div[1]/text()").get());
                System.out.println(Xsoup.select(element, "/div[2]/text()").get());
            }
            System.out.println(elements.size());
//            System.out.println(pages);

        }
    }


}
