package nfyg;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc	httpclient工具类
 * @author	ljt
 * @time	2014-8-13 下午6:45:41
 */
public class HttpClientUtil {
	
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	
	/**
	 * DefaultHttpClient已过时，官方推荐使用CloseableHttpClient
	 */
	private static HttpClient httpClient = HttpClientBuilder.create().build();
	
	public static String ascii2native(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	public static String get(String url) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);

		try {
			// 状态为200相应成功
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				String jsonStr = EntityUtils.toString(response.getEntity());
				jsonStr = jsonStr.replace("\\/", "/");
				jsonStr = ascii2native(jsonStr);
//				logger.info("请求接口响应数据：" + jsonStr);
				return jsonStr;
			}
		} catch (Exception e) {
			logger.error("调用接口异常...",e);
		} finally{
			get.releaseConnection();
		}
		return null;
	}
	
	public static String getJson(String url) {
		HttpGet get = new HttpGet(url);
		try {
			// 状态为200相应成功
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				String jsonStr = EntityUtils.toString(response.getEntity(),"utf-8");
				return jsonStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			get.releaseConnection();
		}
		return null;
	}

    public static String post(String url,String ip) throws Exception{
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("ip", ip));
        post.setEntity(new UrlEncodedFormEntity(nvps));

        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.933.400 QQBrowser/9.4.8699.400");
        post.addHeader("Referer","http://qqzeng.com/ip/?ip=180.84.182.0");
        post.addHeader("X-Requested-With","XMLHttpRequest");
        post.addHeader("Origin","http://qqzeng.com");
        post.addHeader("Cookie","pgv_pvi=6278098944; pgv_si=s9775373312; f_ip=qqzeng");
        post.addHeader("Host","qqzeng.com");

        try {
            // 状态为200相应成功
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                byte[] b = new byte[is.available()];
                is.read(b);
                return new String(b,0,b.length,"utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            post.releaseConnection();
        }
        return null;
    }
	
	public static String send(String url, String param) {
//		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpPost post = new HttpPost(url);
//		logger.info("请求接口数据：" + param);
		System.out.println("请求接口数据：" + param);
		StringEntity se = new StringEntity(param, "UTF-8");
//		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
		post.setEntity(se);

		try {
			// 状态为200相应成功
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String jsonStr = EntityUtils.toString(response.getEntity(),"utf-8");
				System.out.println("请求接口响应数据：" + jsonStr);
//				logger.info("请求接口响应数据：" + jsonStr);
				return jsonStr;
			}
		} catch (Exception e) {
			logger.error("调用接口异常...",e);
		} finally{
			post.releaseConnection();
		}
		return null;
	}
	
}
