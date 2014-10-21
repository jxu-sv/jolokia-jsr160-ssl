package org.jolokia.jsr160.test;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class Client {
	public static void main(String[] args) {
		try {
			HashMap<String, Object> env = new HashMap();
			String truststore = "/Users/jxu/.keystore/osxjxu/osxjxu-tomcat-ssl-keystore.ts";
			char truststorepass[] = "changeit".toCharArray();

			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(truststore), truststorepass);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			SSLContext ctx = SSLContext.getInstance("TLSv1");
			ctx.init(null, tmf.getTrustManagers(), null);
			SSLSocketFactory ssf = ctx.getSocketFactory();
			env.put("jmx.remote.profiles", "TLS");
			env.put("jmx.remote.tls.socket.factory", ssf);
			env.put("jmx.remote.tls.enabled.protocols", "TLSv1");
//			env.put("jmx.remote.tls.enabled.cipher.suites", "SSL_RSA_WITH_NULL_MD5");

			JMXServiceURL url = new JMXServiceURL("jmxmp", "localhost", 5555);
			JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			String domains[] = mbsc.getDomains();
			for (int i = 0; i < domains.length; i++) {
				System.out.println("Domain[" + i + "] = " + domains[i]);
			}

			jmxc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
