package cat.institutmarianao.ticketing.components.https.client;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class CustomHttpClientFactory implements FactoryBean<HttpClient> {

	@Override
	public HttpClient getObject() throws Exception {
		KeyStore keyStore = KeyStore.getInstance(ResourceUtils.getFile("classpath:keystore/ticketingws.p12"),
				"marianao".toCharArray());

		SSLContext sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore, "marianao".toCharArray())
				.loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

		HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		return httpClient;
	}

	@Override
	public Class<HttpClient> getObjectType() {
		return HttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}