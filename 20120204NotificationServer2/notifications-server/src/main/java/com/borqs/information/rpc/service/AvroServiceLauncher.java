package com.borqs.information.rpc.service;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AvroServiceLauncher {
	private AvroIpcServiceProxy proxy;

	public static class ShutdownHookThread extends Thread {
		private AvroIpcServiceProxy proxy;

		public ShutdownHookThread(AvroIpcServiceProxy proxy) {
			this.proxy = proxy;
		}

		@Override
		public void run() {
			if (null != this.proxy) {
				this.proxy.stop();
			}
		}

	}

	public void init(String[] args) throws Exception {
		System.out.println("execute init(args) method");

		URL location = AvroServiceLauncher.class.getProtectionDomain()
				.getCodeSource().getLocation();
		String path = location.getPath();
		System.out.println("class location is:" + path);

		URI uri = new File(path).getParentFile().getParentFile().toURI();
		System.out.println("application location is:" + uri);

		// iterator configuration files
		File configFiles = new File(uri.getPath() + "conf");
		System.out.println("config path is " + configFiles.getAbsolutePath());
		String[] contextFiles = configFiles.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.matches("^applicationContext.*thrift\\.xml$")) {
					return false;
				}
				if (name.matches("^applicationContext.*\\.xml$")) {
					return true;
				}
				return false;
			}
		});

		String contextPath = uri + "conf" + File.separator;
		for (int i = 0; null != contextFiles && i < contextFiles.length; i++) {
			contextFiles[i] = contextPath + contextFiles[i];
		}
		System.out.println(Arrays.toString(contextFiles));

		// contextPaths = new String[] {
		// uri+"/conf/applicationContext.xml",
		// uri+"/conf/applicationContext-mongo-dao.xml",
		// uri+"/conf/applicationContext-jms.xml",
		// uri+"/conf/applicationContext-push.xml",
		// uri+"/conf/applicationContext-rpc.xml"
		// };

		PropertyConfigurator.configure(uri.getPath() + "conf/log4j.properties");
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
				contextFiles);
		proxy = (AvroIpcServiceProxy) context
				.getBean("informationsAvroIpcServiceProxy");
	}

	public void start() throws Exception {
		System.out.println("execute start method！");
		if (null != proxy) {
			proxy.start();
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(proxy));
		} else {
			System.err.println("failed to execute start method！");
		}
	}

	public void stop() throws Exception {
		System.out.println("execute stop method！");
		if (null != proxy) {
			proxy.stop();
		}
	}

	public void destroy() throws Exception {
		System.out.println("execute destroy method!");
	}

	private static AvroServiceLauncher launcher = new AvroServiceLauncher();

	static void winstart(String[] args) {
		try {
			launcher.init(args);
			launcher.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (launcher) {
			try {
				launcher.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static void winstop(String[] args) {
		try {
			launcher.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		synchronized (launcher) {
			launcher.notify();
		}
	}
}
