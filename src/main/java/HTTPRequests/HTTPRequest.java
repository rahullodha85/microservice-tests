package HTTPRequests;

import Utilities.KeyValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 461967 on 11/17/2015.
 */
public class HTTPRequest {
    /**
     *
     * @param serviceUrl
     * @param headerList
     * @param params
     * @return
     * @throws Exception
     */
    public static HttpResponse SendGet(String serviceUrl, List<KeyValuePair> headerList,
                                       List<KeyValuePair> params) throws Exception {
        String json = "";

        //Ensure Not NULL
        if (headerList == null) headerList = new ArrayList<KeyValuePair>();
        URI uri = URIBuilder(serviceUrl, params);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            request.addHeader("content-type", "application/json");

            for (KeyValuePair kv : headerList) {
                request.addHeader(kv.getKey(), kv.getValue());
            }

            HttpResponse result = httpClient.execute(request);
            json = EntityUtils.toString(result.getEntity(), "UTF-8");
            return result;
            //System.out.println(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param serviceUrl
     * @param headerList
     * @param params
     * @param jsonReturn
     * @return
     * @throws Exception
     */
    public static String SendGet(String serviceUrl, List<KeyValuePair> headerList,
                                 List<KeyValuePair> params, boolean jsonReturn) throws Exception {
        String json = "";

        //Ensure Not NULL
        if (headerList == null) headerList = new ArrayList<KeyValuePair>();
        URI uri = URIBuilder(serviceUrl, params);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            request.addHeader("content-type", "application/json");

            for (KeyValuePair kv : headerList) {
                request.addHeader(kv.getKey(), kv.getValue());
            }

            HttpResponse result = httpClient.execute(request);
            json = EntityUtils.toString(result.getEntity(), "UTF-8");

            //System.out.println(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    /**
     * @param url
     * @param headerList
     * @param jsonRequest
     * @return
     */
    public static String sendPost(String url, List<KeyValuePair> headerList, String jsonRequest, boolean jsonReturn) {
        String json = "";

        //Ensure Not NULL
        if (headerList == null) headerList = new ArrayList<KeyValuePair>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonRequest);
            request.addHeader("content-type", "application/json");
            for (KeyValuePair kv : headerList) {
                request.addHeader(kv.getKey(), kv.getValue());
            }
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null) {
                json = EntityUtils.toString(respEntity);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }

        return json;
    }

    /**
     * @param url
     * @param headerList
     * @param jsonRequest
     * @return
     */
    public static HttpResponse sendPost(String url, List<KeyValuePair> headerList, String jsonRequest) {
        String json = "";

        //Ensure Not NULL
        if (headerList == null) headerList = new ArrayList<KeyValuePair>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonRequest);
            request.addHeader("content-type", "application/json");
            for (KeyValuePair kv : headerList) {
                request.addHeader(kv.getKey(), kv.getValue());
            }
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
        }
    }

    private static URI URIBuilder(String serviceUrl, List<KeyValuePair> params) throws Exception{
        URIBuilder builder = new URIBuilder(serviceUrl);

        if (params != null) {
            ArrayList<NameValuePair> urlParams = new ArrayList<NameValuePair>();
            for (KeyValuePair kv : params) {
                urlParams.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
            }
            builder.addParameters(urlParams);
        }
        URI uri = builder.build();

        return uri;
    }
}
