package org.slamon.android.handlers;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.slamon.TaskHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * An example task handler that uses HTTP GET to provided url and reports the status code
 */
public class HttpGetHandler extends TaskHandler {
    public HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

    @Override
    public Map<String, Object> execute(Map<String, Object> inputParams) throws Exception {
        String url = (String) inputParams.get("url");
        if (url == null) {
            throw new IllegalArgumentException("No url parameter found");
        }

        HttpRequest request = requestFactory.buildRequest(HttpMethods.GET, new GenericUrl(url), null);

        Map<String, Object> result = new HashMap<>();
        HttpResponse response = null;
        try {
            response = request.execute();
            result.put("status", response.getStatusCode());
        } catch (HttpResponseException e) {
            result.put("status", e.getStatusCode());
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }

        return result;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getName() {
        return "url_http_status";
    }
}
