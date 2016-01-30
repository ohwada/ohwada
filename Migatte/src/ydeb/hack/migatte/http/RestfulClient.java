package ydeb.hack.migatte.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ydeb.hack.migatte.util.DebugUtils;

import android.util.Log;

/**
 * <pre>
 * http の GET、POST、PUT、DELETE メソッドを実行する
 * 文字列、DOM形式、SAXパーサー、カスタム・パーサーに対応した
 * クラス・メソッドがある
 * </pre>
 */
public class RestfulClient {
    private static final String TAG = "Restful";

	/** BASIC認証のユーザ名 */
    public static String basicAuthUsername = "";

	/** BASIC認証のパスワード名 */
    public static String basicAuthPassword = "";

	/**
	 * GETメソッドを実行し、文字列を返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @return 文字列
	 */
	public static String Get(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpGet method = new HttpGet(fulluri);
		return EntityUtils.toString(DoRequest(method));
	}

	/**
	 * GETメソッドを実行し、DOMを返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param builder DOMビルダー
	 * @return DOM
	 */
	public static Document Get(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpGet method = new HttpGet(fulluri);
		return getDOM(DoRequest(method), builder);
	}

	/**
	 * GETメソッドを実行し、SAXパーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param handler SAXハンドラー
	 */
    public static void Get(String uri, HashMap<String,String> map, DefaultHandler handler) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        String fulluri;

        if(null == map){
            fulluri = uri;
        } else {
            fulluri = uri + packQueryString(map);
        }
        
        if (DebugUtils.isDebug()) {
        	Log.d(TAG, fulluri);
        }
        HttpGet method = new HttpGet(fulluri);
        parseBySAX(DoRequest(method), handler);
    }

	/**
	 * GETメソッドを実行し、カスタム・パーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param pullParser カスタム・パーサー
	 */
    public static void Get(String uri, HashMap<String,String> map, CustomPullParser pullParser) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        String fulluri;

        if(null == map){
            fulluri = uri;
        } else {
            fulluri = uri + packQueryString(map);
        }
        
        HttpGet method = new HttpGet(fulluri);
        parseByPullParser(DoRequest(method), pullParser);
    }

	/**
	 * POSTメソッドを実行し、文字列を返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @return 文字列
	 */
    public static String Post(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		HttpPost method = new HttpPost(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return EntityUtils.toString(DoRequest(method));
	}

	/**
	 * POSTメソッドを実行し、DOMを返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param builder DOMビルダー
	 * @return DOM
	 */
	public static Document Post(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		HttpPost method = new HttpPost(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return getDOM(DoRequest(method), builder);
	}

	/**
	 * POSTメソッドを実行し、SAXパーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param handler SAXハンドラー
	 */
    public static void Post(String uri, HashMap<String,String> map, DefaultHandler handler) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        HttpPost method = new HttpPost(uri);
        if(null != map){
            List<NameValuePair> paramList = packEntryParams(map);
            method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
        }
        parseBySAX(DoRequest(method), handler);
    }

	/**
	 * POSTメソッドを実行し、カスタム・パーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param pullParser カスタム・パーサー
	 */
    public static void Post(String uri, HashMap<String,String> map, CustomPullParser pullParser) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        HttpPost method = new HttpPost(uri);
        if(null != map){
            List<NameValuePair> paramList = packEntryParams(map);
            method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
        }
        parseByPullParser(DoRequest(method), pullParser);
    }

	/**
	 * PUTメソッドを実行し、文字列を返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @return 文字列
	 */
    public static String Put(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		HttpPut method = new HttpPut(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return EntityUtils.toString(DoRequest(method));
	}

	/**
	 * PUTメソッドを実行し、DOMを返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param builder DOMビルダー
	 * @return DOM
	 */
	public static Document Put(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		HttpPut method = new HttpPut(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return getDOM(DoRequest(method), builder);
	}

	/**
	 * PUTメソッドを実行し、SAXパーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param handler SAXハンドラー
	 */
    public static void Put(String uri, HashMap<String,String> map, DefaultHandler handler) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        HttpPut method = new HttpPut(uri);
        if(null != map){
            List<NameValuePair> paramList = packEntryParams(map);
            method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
        }
        parseBySAX(DoRequest(method), handler);
    }

	/**
	 * PUTメソッドを実行し、カスタム・パーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param pullParser カスタム・パーサー
	 */
    public static void Put(String uri, HashMap<String,String> map, CustomPullParser pullParser) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        HttpPut method = new HttpPut(uri);
        if(null != map){
            List<NameValuePair> paramList = packEntryParams(map);
            method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
        }
        parseByPullParser(DoRequest(method), pullParser);
    }

	/**
	 * DELETEメソッドを実行し、文字列を返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @return 文字列
	 */
    public static String Delete(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpDelete method = new HttpDelete(fulluri);
		return EntityUtils.toString(DoRequest(method));
	}

	/**
	 * DELETEメソッドを実行し、DOMを返す
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param builder DOMビルダー
	 * @return DOM
	 */
	public static Document Delete(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpDelete method = new HttpDelete(fulluri);
		return getDOM(DoRequest(method), builder);
	}

	/**
	 * DELETEメソッドを実行し、SAXパーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param handler SAXハンドラー
	 */
    public static void Delete(String uri, HashMap<String,String> map, DefaultHandler handler) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        String fulluri;

        if(null == map){
            fulluri = uri;
        } else {
            fulluri = uri + packQueryString(map);
        }
        
        HttpDelete method = new HttpDelete(fulluri);
        parseBySAX(DoRequest(method), handler);
    }

	/**
	 * DELETEメソッドを実行し、カスタム・パーサーで解析する
	 * @param uri URI
	 * @param map リクエスト・パラメータ
	 * @param pullParser カスタム・パーサー
	 */
    public static void Delete(String uri, HashMap<String,String> map, CustomPullParser pullParser) throws ClientProtocolException, IOException, SAXException, IllegalStateException, ParserConfigurationException {
        String fulluri;

        if(null == map){
            fulluri = uri;
        } else {
            fulluri = uri + packQueryString(map);
        }
        
        HttpDelete method = new HttpDelete(fulluri);
        parseByPullParser(DoRequest(method), pullParser);
    }

    /*
	 * DocumentBuilderFactory.newInstance()
	 * 	.setValidating(true)
	 * 	.setIgnoringElementContentWhitespace(true)
	 * 	.newDocumentBuilder()
	 *  .parse(hoge);
	 *  でうまく空ノードを取ってくれそうだけど、バリデータが実装されてないのか例外が出る。
	 *  また
	 *  Node.normalize()もなんか変
	 *  なので、自前で改行やスペースだけのテキストノードを削除する。
	 */
	/**
	 * 改行やスペースだけのテキストノードを削除する
	 * @param currentNode ノード
	 * @return 処理したノード
	 */
    public static Node RemoveEmptyNodes(Node currentNode) {
        NodeList list = currentNode.getChildNodes();
        int n = list.getLength();
        if(0 < n){
            for (int i = 0; i < n; i++) {
                Node childNode = list.item(i);
                String value = childNode.getNodeValue();
                // Log.v(TAG, "value : " + value);
                if(Node.TEXT_NODE == childNode.getNodeType() && value.trim().equals("")){
                	// Log.v(TAG, "remove " + Integer.toString(i) + "th node of " + currentNode.getNodeName());
                	currentNode.removeChild(childNode);
                }else{
                	RemoveEmptyNodes(childNode);
                }
            }
        }
        return currentNode;
    }

	/**
	 * <pre>
	 * HTTPプロトコルを実行し、応答結果を返す
	 * BASIC認証用のユーザ名が設定されていれば、BASIC認証を行う
	 * HTTPステータスが 200(OK) か 201(Created) なら応答結果を返す
	 * それ以外は例外処理を行う
	 * </pre>
	 * @param method (HttpUriRequest)
	 * @return 応答結果 (HttpEntity)
	 */
	private static HttpEntity DoRequest(HttpUriRequest method) throws ClientProtocolException, IOException {

		DefaultHttpClient client = new DefaultHttpClient();
//		HttpParams httpParams = client.getParams();
//		HttpConnectionParams.setConnectionTimeout(httpParams, 1);
//		client.setParams(httpParams);
		
		// BASIC認証用のユーザ名が設定されていれば、BASIC認証を行う
		if(!basicAuthUsername.equals("")){
			URI uri = method.getURI();
			client.getCredentialsProvider().setCredentials(
				new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword));
		}
		HttpResponse response = null;
		
		try {
			response = client.execute(method);
			int statuscode = response.getStatusLine().getStatusCode();
			
			//リクエストが成功 200 OK and 201 CREATED
			if (statuscode == HttpStatus.SC_OK | statuscode == HttpStatus.SC_CREATED){ 
				return response.getEntity();
			} else {
				throw new HttpResponseException(statuscode, "Response code is " + Integer.toString(statuscode));
			}
		}catch (RuntimeException e) {
			method.abort();
			Log.v(TAG, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * リクエスト・パラメータを List に変換する
	 * @param map リクエスト・パラメータ (HashMap)
	 * @return リスト (List)
	 */
	private static List<NameValuePair> packEntryParams(HashMap<String,String> map){
		if(null == map){
			throw new RuntimeException("map is null");
		}

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
		Map.Entry<String, String> entry;
		
		while(itr.hasNext()){
			entry = itr.next();
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return paramList;
	}

	/**
	 * リクエスト・パラメータを文字列に変換する
	 * @param map リクエスト・パラメータ (HashMap)
	 * @return 文字列
	 */
	private static String packQueryString(HashMap<String,String> map) throws UnsupportedEncodingException{
		if(null == map){
			throw new RuntimeException("map is null");
		}
		
		StringBuilder sb = new StringBuilder(100);
		Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
		Map.Entry<String, String> entry;
		
		while(itr.hasNext()){
			entry = itr.next();
			if(0 == sb.length()){
				sb.append("?");
			}else{
				sb.append("&");
			}
			sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			sb.append("=");
			sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		return sb.toString();
	}

	/**
	 * HTTPの応答結果を DOM に変換する
	 * @param entity 応答結果
	 * @param builder DOMビルダー
	 * @return DOM
	 */
	private static Document getDOM(HttpEntity entity, DocumentBuilder builder) throws IOException, SAXException{
		BufferedInputStream is = new BufferedInputStream(entity.getContent());
		Document doc = null;
		try {
			doc = builder.parse(is);
			return doc;
		} finally{
			is.close();
		}
	}

	/**
	 * HTTPの応答結果をSAXパーサーで解析する
	 * @param entity 応答結果
	 * @param handler SAXハンドラー
	 */
    private static void parseBySAX(HttpEntity entity, DefaultHandler handler) throws ParserConfigurationException, IllegalStateException, IOException, SAXException{
        BufferedInputStream is = new BufferedInputStream(entity.getContent());
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(is, handler);
        } finally{
            is.close();
        }
    }

	/**
	 * HTTPの応答結果をカスタム・パーサーで解析する
	 * @param entity 応答結果
	 * @param pullParser カスタム・パーサー
	 */
    private static void parseByPullParser(HttpEntity entity, CustomPullParser pullParser) throws ParserConfigurationException, IllegalStateException, IOException, SAXException{
        BufferedInputStream is = new BufferedInputStream(entity.getContent());
        try {
            pullParser.parseByPullParser(is);
        } finally{
            is.close();
        }
    }

	/**
	 * カスタム・パーサーのインターフェイス
	 */
    public interface CustomPullParser {
        void parseByPullParser(BufferedInputStream is);
    }

    /**
     * Example of CustomPullParser
     * 
    public class ExampleCustomPullParser implements CustomPullParser{
        public void parseByPullParser(BufferedInputStream is){
            XmlPullParserFactory factory;
            try {
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, "UTF-8");
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        System.out.println("Start document");
                    } else if (eventType == XmlPullParser.END_DOCUMENT) {
                        System.out.println("End document");
                    } else if (eventType == XmlPullParser.START_TAG) {
                        System.out.println("Start tag " + xpp.getName());
                    } else if (eventType == XmlPullParser.END_TAG) {
                        System.out.println("End tag " + xpp.getName());
                    } else if (eventType == XmlPullParser.TEXT) {
                        System.out.println("Text " + xpp.getText());
                    }
                    eventType = xpp.next();
                }
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    */

}
