//package HTTPRequests;
//
//import Utilities.KeyValuePair;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by 461967 on 11/18/2015.
// */
//
//public class HTTPPostRequest{
//    public static String sendPost(String url, List<KeyValuePair> headerList, String jsonRequest) {
//        String json = "";
//
//        //Ensure Not NULL
//        if (headerList == null) headerList = new ArrayList<KeyValuePair>();
//
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
//            HttpPost request = new HttpPost(url);
//            StringEntity entity = new StringEntity(jsonRequest);
//            request.addHeader("content-type", "application/json");
//            for (KeyValuePair kv : headerList) {
//                request.addHeader(kv.getKey(), kv.getValue());
//            }
//            request.setEntity(entity);
//            HttpResponse response = httpClient.execute(request);
//            HttpEntity respEntity = response.getEntity();
//            if (respEntity != null) {
//                json = EntityUtils.toString(respEntity);
//            }
//            //  System.out.println(json);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//        }
//
//        return json;
//    }
//}
//
////public class HTTPPostRequest extends HTTPRequestBase {
////    private String _body;
////    private String[] _param;
////
////    public HTTPPostRequest(String url, String body, String[] param){
////        _url=url;
////        _requestMethod="POST";
////        _responseCode=null;
////        _response=null;
////        _param=param;
////        _body=body;
////    }
////
////    public String[] get_param(){
////        return _param;
////    }
////
////    public String get_body(){
////        return _body;
////    }
////
////    public String SendPost() throws Exception {
////        try {
////            URL url = new URL(_url);
////            HttpURLConnection con = (HttpURLConnection) url.openConnection();
////            con.setRequestMethod("POST");
////            con.addRequestProperty("User-Agent", "Test");
////            con.addRequestProperty("Content-Type", "application/json");
////            int i=0;
////            for(String item:get_param()){
////                con.addRequestProperty("cookie"+i,item.replace("; path=/",""));
////            }
////            con.setDoOutput(true);
////            OutputStream os = con.getOutputStream();
////            os.write(get_body().getBytes());
////            os.flush();
////
////            InputStream is = con.getInputStream();
////            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
////            StringBuilder response = new StringBuilder();
////            String line;
////            while((line=rd.readLine())!=null){
////                response.append(line);
////                response.append('\r');
////            }
////            rd.close();
//////            System.out.print(response.toString());
////            return response.toString();
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
////}
