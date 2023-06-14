import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class S {
    public static void main(String[] args) throws IOException {
        kk();
    }

    public static void kk() throws IOException {
        final HttpHost targetHost = new HttpHost( "192.168.1.123", 8082);
        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        AuthScope authScope = new AuthScope(targetHost);
        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials("elastic", "elastic"));

        // Create AuthCache instance
        final AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        authCache.put(targetHost, new BasicScheme());

        // Add AuthCache to the execution context
        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);


        final HttpGet request = new HttpGet("http://192.168.1.123:9200/_cat/indices");
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .build();
             CloseableHttpResponse response = (CloseableHttpResponse) client
                     .execute(request, context)) {
            DecompressingEntity entity = (DecompressingEntity) response.getEntity();
            InputStream content = entity.getContent();
            StringBuffer out = new StringBuffer();
            InputStreamReader inread = new InputStreamReader(content, "UTF-8");
            char[] b = new char[4096];
            for (int n; (n = inread.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
            System.out.println(out.toString());
        }
    }


}
