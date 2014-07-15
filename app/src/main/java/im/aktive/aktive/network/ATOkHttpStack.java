package im.aktive.aktive.network;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * Created by hoangtran on 13/7/14.
 */
public class ATOkHttpStack extends HurlStack {
    private final OkHttpClient client;

    public ATOkHttpStack() {
        this(new OkHttpClient());
    }

    public ATOkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.client = client;
        WebkitCookieManagerProxy coreCookieManager = new WebkitCookieManagerProxy(null, java.net.CookiePolicy.ACCEPT_ALL);
        java.net.CookieHandler.setDefault(coreCookieManager);
        client = new OkHttpClient();
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
        client.setSslSocketFactory(sslContext.getSocketFactory());
        client.setCookieHandler(CookieHandler.getDefault());
    }

    @Override protected HttpURLConnection createConnection(URL url) throws IOException {
        return client.open(url);
    }

    public void clearCookies() {
        WebkitCookieManagerProxy coreCookieManager = (WebkitCookieManagerProxy)client.getCookieHandler();
        coreCookieManager.resetCookieStore();
    }

    public class WebkitCookieManagerProxy extends CookieManager
    {
        private android.webkit.CookieManager webkitCookieManager;

        public WebkitCookieManagerProxy()
        {
            this(null, null);
        }

        WebkitCookieManagerProxy(CookieStore store, CookiePolicy cookiePolicy)
        {
            super(null, cookiePolicy);

            this.webkitCookieManager = android.webkit.CookieManager.getInstance();
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException
        {
            // make sure our args are valid
            if ((uri == null) || (responseHeaders == null)) return;

            // save our url once
            String url = uri.toString();

            // go over the headers
            for (String headerKey : responseHeaders.keySet())
            {
                // ignore headers which aren't cookie related
                if ((headerKey == null) || !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie"))) continue;

                // process each of the headers
                for (String headerValue : responseHeaders.get(headerKey))
                {
                    this.webkitCookieManager.setCookie(url, headerValue);
                }
            }
        }

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException
        {
            // make sure our args are valid
            if ((uri == null) || (requestHeaders == null)) throw new IllegalArgumentException("Argument is null");

            // save our url once
            String url = uri.toString();

            // prepare our response
            Map<String, List<String>> res = new java.util.HashMap<String, List<String>>();

            // get the cookie
            String cookie = this.webkitCookieManager.getCookie(url);

            // return it
            if (cookie != null) res.put("Cookie", Arrays.asList(cookie));
            return res;
        }

        public void resetCookieStore()
        {
            webkitCookieManager.removeAllCookie();
        }

        @Override
        public CookieStore getCookieStore()
        {
            // we don't want anyone to work with this cookie store directly
            throw new UnsupportedOperationException();
        }
    }
}
