package seeya.insight.app.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HttpConnection {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpConnection.class);
   
	/**
     * 1. 개요 : HTTP 프로토콜을 이용한 GET 방식의 API 호출
     * 2. 입력데이터 : 
     * 3. 출력데이터 : 리턴페이지(String)
     * 4. 최초작성일 : 2017. 5. 23.
     */
    public static String httpGet(String connUrl){
    	String result = null;
    	
    	try
    	{
    		URI uri = new URI(connUrl);
    		HttpClient client = HttpClientBuilder.create().build();  
    		HttpGet httpGet = new HttpGet(uri);
    		HttpResponse responseGet = client.execute(httpGet);
    		HttpEntity resEntityGet = responseGet.getEntity();
    		if (resEntityGet != null) {
    			// 결과를 처리합니다.
    			result = EntityUtils.toString(resEntityGet);
    		}
    	} catch (URISyntaxException use){
    		logger.error("detail", use);
    		//result = ResultMessage.CONNECTION_URL_SYNTAX_ERROR.msg;
    	} catch(ClientProtocolException cpe){
    		logger.error("detail", cpe);
    		//result = ResultMessage.CLIENT_PROTOCOL_ERROR.msg;
    	} catch(IOException ioe){
    		logger.error("detail", ioe);
    		//result = ResultMessage.ERROR.msg;
    	} 
    	
    	return result;
    }
    
    /**
     * 1. 개요 : HTTP 프로토콜을 이용한 GET 방식의 API 호출
     * 2. 입력데이터 : 
     * 3. 출력데이터 : 리턴페이지(String)
     * 4. 최초작성일 : 2017. 5. 23.
     */
    public static String httpGet(String connUrl, String accessToken){
    	String result = null;
    	
    	try
    	{
    		URI uri = new URI(connUrl);
    		HttpClient client = HttpClientBuilder.create().build();
    		HttpGet httpGet = new HttpGet(uri);
    		httpGet.addHeader("X-Auth-Token", accessToken);
    		HttpResponse responseGet = client.execute(httpGet);
    		HttpEntity resEntityGet = responseGet.getEntity();
    		if (resEntityGet != null) {
    			// 결과를 처리합니다.
    			result = EntityUtils.toString(resEntityGet);
    		}
    	} catch (URISyntaxException use){
    		logger.error("detail", use);
    	} catch(ClientProtocolException cpe){
    		logger.error("detail", cpe);
    	} catch(IOException ioe){
    		logger.error("detail", ioe);
    	} 
    	
    	return result;
    }

	/**
	 * 1. 개요 : HTTP 프로토콜을 이용한 GET 방식의 API 호출
	 * 2. 입력데이터 :
	 * 3. 출력데이터 : 리턴페이지(String)
	 * 4. 최초작성일 : 2017. 5. 23.
	 */
	public static HttpEntity httpGetStream(String connUrl){
		HttpEntity resEntityGet = null;

		try
		{
			URI uri = new URI(connUrl);
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse responseGet = client.execute(httpGet);
			resEntityGet = responseGet.getEntity();
//			if (resEntityGet != null) {
//				// 결과를 처리합니다.
//				result = EntityUtils.toString(resEntityGet);
//			}
		} catch (URISyntaxException use){
			logger.error("detail", use);
		} catch(ClientProtocolException cpe){
			logger.error("detail", cpe);
		} catch(IOException ioe){
			logger.error("detail", ioe);
		}

		return resEntityGet;
	}
    
    /**
     * 1. 개요 : HTTPS 프로토콜을 이용한 GET 방식의 API 호출
     * 2. 입력데이터 : 
     * 3. 출력데이터 : 리턴페이지(String)
     * 4. 최초작성일 : 2017. 5. 23.
     */
    public static String httpsGet(String connUrl){
    	String result = null;
    	
    	try
    	{
    		// 모든 인증서를 허용한다.
    		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HttpClientBuilder builder = HttpClientBuilder.create();
            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext.getSocketFactory(), new NoopHostnameVerifier());
            builder.setSSLSocketFactory(sslConnectionFactory);
            Registry<ConnectionSocketFactory> registry = 
                    RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionFactory)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
            builder.setConnectionManager(ccm);
            CloseableHttpClient httpClient = builder.build();
    		
    		URI uri = new URI(connUrl);
    		HttpGet httpGet = new HttpGet(uri);
    		CloseableHttpResponse response = httpClient.execute(httpGet);
    		HttpEntity resEntityGet = response.getEntity();
    		if (resEntityGet != null) {
    			// 결과를 처리합니다.
    			result = EntityUtils.toString(resEntityGet);
    		}
    	} catch (NoSuchAlgorithmException nsae){
    		nsae.printStackTrace();
    	} catch (KeyManagementException kme){
    		kme.printStackTrace();
    	} catch(KeyStoreException kme){
    		kme.printStackTrace();
    	} catch (URISyntaxException use){
    		logger.error("detail", use);
    	} catch(ClientProtocolException cpe){
    		logger.error("detail", cpe);
    	} catch(IOException ioe){
    		logger.error("detail", ioe);
    	} 
    	
    	return result;
    }
    
    /**
     * 1. 개요 : HTTPS 프로토콜을 이용한 GET 방식의 API 호출
     * 2. 입력데이터 : 
     * 3. 출력데이터 : 리턴페이지(String)
     * 4. 최초작성일 : 2017. 5. 23.
     */
    public static String httpsGet(String connUrl, String accessToken){
    	String result = null;
    	
    	try
    	{
    		// 모든 인증서를 허용한다.
    		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HttpClientBuilder builder = HttpClientBuilder.create();
            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext.getSocketFactory(), new NoopHostnameVerifier());
            builder.setSSLSocketFactory(sslConnectionFactory);
            Registry<ConnectionSocketFactory> registry = 
                    RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionFactory)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
            builder.setConnectionManager(ccm);
            CloseableHttpClient httpClient = builder.build();
    		
    		URI uri = new URI(connUrl);
    		HttpGet httpGet = new HttpGet(uri);
    		httpGet.addHeader("X-TM-AccessToken", accessToken);
    		CloseableHttpResponse response = httpClient.execute(httpGet);
    		HttpEntity resEntityGet = response.getEntity();
    		if (resEntityGet != null) {
    			// 결과를 처리합니다.
    			result = EntityUtils.toString(resEntityGet);
    		}
    	} catch (NoSuchAlgorithmException nsae){
    		nsae.printStackTrace();
    	} catch (KeyManagementException kme){
    		kme.printStackTrace();
    	} catch(KeyStoreException kme){
    		kme.printStackTrace();
    	} catch (URISyntaxException use){
    		logger.error("detail", use);
    	} catch(ClientProtocolException cpe){
    		logger.error("detail", cpe);
    	} catch(IOException ioe){
    		logger.error("detail", ioe);
    	} 
    	return result;
    }
    
    /**
     * 1. 개요 : HTTP 프로토콜을 이용한 POST 방식의 API 호출
     * 2. 입력데이터 : 
     * 3. 출력데이터 : 리턴페이지(String)
     * 4. 최초작성일 : 2017. 5. 23.
     */
    public static String httpPost(String connUrl, HashMap<String, String> paramMap){
    	String result = null;
    	
    	try
    	{
    		URI uri = new URI(connUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			if(paramMap != null){
				Set<String> keys = paramMap.keySet();
				Iterator<String> iter = keys.iterator();
				while(iter.hasNext()){
					String strKey = iter.next();
					params.add(new BasicNameValuePair(strKey, paramMap.get(strKey)));
				}
			}
    		
			HttpClient client = HttpClientBuilder.create().build();  
			HttpPost httpPost = new HttpPost(uri);    	  
    	 
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, "UTF-8");
			httpPost.setEntity(ent);
			HttpResponse responsePOST = client.execute(httpPost);  
			HttpEntity resEntity = responsePOST.getEntity();
    	 
			if (resEntity != null){    
				result = EntityUtils.toString(resEntity);
			}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	return result;
	}
    
    /**
     * 1. 개요 : HTTP 프로토콜을 이용한 POST 방식의 API 호출
     * 2. 입력데이터 : 
     * 3. 출력데이터 : 리턴페이지(String)
     * 4. 최초작성일 : 2017. 5. 23.
     */
    public static String httpPost(String connUrl){
    	String result = null;
    	
    	try
    	{
    		URI uri = new URI(connUrl);
    		HttpClient client = HttpClientBuilder.create().build();  
    		HttpPost httpPost = new HttpPost(uri);    	  
    		HttpResponse responsePOST = client.execute(httpPost);  
    		HttpEntity resEntity = responsePOST.getEntity();
    		
    		if (resEntity != null){    
    			result = EntityUtils.toString(resEntity);
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	return result;
    }
}
