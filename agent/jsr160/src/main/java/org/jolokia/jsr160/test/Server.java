package org.jolokia.jsr160.test;

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.security.KeyStore;
import java.util.HashMap;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class Server {
	public static void main(String[] args) {
		try {
			MBeanServer mbs = MBeanServerFactory.createMBeanServer();
			mbs = ManagementFactory.getPlatformMBeanServer();
			HashMap<String, Object> env = new HashMap();
			String keystore = "/Users/jxu/.keystore/osxjxu/osxjxu-tomcat-ssl-keystore.jks";
			char keystorepass[] = "changeit".toCharArray();
			char keypassword[] = "changeit".toCharArray();
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(keystore), keystorepass);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keypassword);
			SSLContext ctx = SSLContext.getInstance("TLSv1");
			ctx.init(kmf.getKeyManagers(), null, null);
			SSLSocketFactory ssf = ctx.getSocketFactory();
			env.put("jmx.remote.profiles", "TLS");
			env.put("jmx.remote.tls.socket.factory", ssf);
			env.put("jmx.remote.tls.enabled.protocols", "TLSv1");
//			env.put("jmx.remote.tls.enabled.cipher.suites", "SSL_RSA_WITH_NULL_MD5");

	        JMXServiceURL url = new JMXServiceURL("jmxmp", "localhost", 5555); 

			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
			cs.start();
	        System.out.format("service url - %s\n", cs.getAddress());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
