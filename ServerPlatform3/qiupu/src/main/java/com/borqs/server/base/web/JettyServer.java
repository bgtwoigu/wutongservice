package com.borqs.server.base.web;


import com.borqs.server.base.conf.Configuration;
import com.borqs.server.base.log.TelnetAppenderService;
import com.borqs.server.base.mq.MQCollection;
import com.borqs.server.base.net.HostAndPort;
import com.borqs.server.base.util.ClassUtils2;
import com.borqs.server.base.util.ProcessUtils;
import com.borqs.server.base.util.StringUtils2;
import com.borqs.server.base.util.json.JsonUtils;
import com.borqs.server.base.web.webmethod.DocumentTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JettyServer {
    private static void configureServer(Server server, Configuration conf) {
        String bindAddrs= conf.getString("server.address", "*:8080");

        // Configure host and port
        for (String s : StringUtils.split(bindAddrs, ",")) {
            HostAndPort bindAddr = HostAndPort.parse(s);
            Connector connector = new SelectChannelConnector();
            if (bindAddr.host != null)
                connector.setHost(bindAddr.host);

            connector.setPort(bindAddr.port);

            server.addConnector(connector);
        }


        // TODO: Configure other

        initServlet(server, conf);
        initWarWebApp(server, conf);
        initDirWebApp(server, conf);
    }

    private static void initServlet(Server server, Configuration conf) {
        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/");
        server.setHandler(servletContext);
        for (Map.Entry<String, Object> e : conf.entrySet()) {
            String k = e.getKey(); String v = ObjectUtils.toString(e.getValue(), "");
            if (!k.matches("^servlet\\.\\w+\\.class$"))
                continue;

            String servletName = StringUtils.split(k, '.')[1];
            String pathSpecs = conf.getString("servlet." + servletName + ".path", "/*");

            Servlet servlet = (Servlet) ClassUtils2.newInstance(v.trim());
            ServletHolder holder = new ServletHolder(servlet);
            holder.setName(servletName);
            holder.setInitParameters(conf.toStrStr());
            for (String pathSpec : StringUtils2.splitList(pathSpecs, ",", true)) {
                servletContext.addServlet(holder, pathSpec);
            }
        }
        servletContext.addServlet(new ServletHolder(DocumentTemplate.class), "/document/template");
    }

    private static void initWarWebApp(Server server, Configuration conf) {
        for (Map.Entry<String, Object> e : conf.entrySet()) {
            String k = e.getKey(); String v = ObjectUtils.toString(e.getValue(), "");
            if (!k.matches("^webapp\\.\\w+\\.war$"))
                continue;

            String webAppName = StringUtils.split(k, ".")[1];
            String pathSpecs = conf.checkGetString("webapp." + webAppName + ".path");

            WebAppContext webAppContext = new WebAppContext();
            webAppContext.setContextPath(pathSpecs);
            webAppContext.setWar(v);
            server.setHandler(webAppContext);
        }
    }

    @SuppressWarnings("unchecked")
    private static void initDirWebApp(Server server, Configuration conf) {
        for (Map.Entry<String, Object> e : conf.entrySet()) {
            String k = e.getKey(); String v = ObjectUtils.toString(e.getValue(), "");
            if (!k.matches("^webapp\\.\\w+\\.dir"))
                continue;

            String webAppName = StringUtils.split(k, ".")[1];
            LinkedHashMap<String, String> dirs = (LinkedHashMap<String, String>) JsonUtils.fromJson(v, LinkedHashMap.class);
            String pathSpecs = conf.checkGetString("webapp." + webAppName + ".path");


            WebAppContext webAppContext = new WebAppContext();
            webAppContext.setDescriptor(dirs.get("web.xml"));
            webAppContext.setResourceBase(dirs.get("resource"));
            webAppContext.setContextPath(pathSpecs);

            server.setHandler(webAppContext);
        }
    }

    public static void run(Configuration conf) {
        Server server = new Server();
        configureServer(server, conf);
        TelnetAppenderService telnetLog = TelnetAppenderService.getInstance();

        try {
            MQCollection.initMQs(conf);
            if (conf.getBoolean("log.telnet", false))
                telnetLog.start();


            server.start();
            server.join();
        } catch (Exception e) {
            throw new WebException(e, "Run Jetty server error");
        } finally {
            if (telnetLog.isStarted())
                telnetLog.stop();

            server.destroy();
            MQCollection.destroyMQs();
        }
    }

    private static void printHelp() {
        System.out.printf("%s -c configPath1 [-c configPath2 ...]\n", JettyServer.class.getName());
    }

    public static void main(String[] args) throws IOException {
        if (ArrayUtils.contains(args, "-h")) {
            printHelp();
            return;
        }

        Configuration conf = Configuration.loadArgs(args);
        conf.expandMacros();
        
        //pid
        String pidDirStr = FileUtils.getUserDirectoryPath() + "/.bpid";
        File pidDir = new File(pidDirStr);
        if(!pidDir.exists())
        {
        	FileUtils.forceMkdir(pidDir);
        }
        ProcessUtils.writeProcessId(pidDirStr + "/main.pid");
        
        run(conf);        
    }
}
