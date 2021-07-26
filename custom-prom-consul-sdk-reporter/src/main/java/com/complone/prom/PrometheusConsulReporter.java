package com.complone.prom;

import com.complone.consul.ConsulClient;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.metrics.MetricConfig;
import org.apache.flink.metrics.reporter.InstantiateViaFactory;
import org.apache.flink.util.NetUtils;
import org.apache.flink.util.Preconditions;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

@PublicEvolving
@InstantiateViaFactory(factoryClassName = "com.complone.prom.PrometheusConsulReporterFactory")
public class PrometheusConsulReporter extends AbstractPrometheusConsulReporter {
    private static final String DEFAULT_PORT_RANGE = "20001-65535";
    private  final String SERVICE_NAME = "serviceName";
    private static final String DEFAULT_SERVICE_NAME = "flink";
    private final String CONSUL_TOKEN = "consulToken";
    private HTTPServer httpServer;
    private ConsulClient consulClient;
    private int port;
    private String ip;

    @VisibleForTesting
    int getPort() {
        Preconditions.checkState(httpServer != null, "Server has not been initialized.");
        return port;
    }

    @Override
    public void open(MetricConfig config) {
        super.open(config);
        this.ip = getLocalIP();

        //String portsConfig = config.getString(ARG_PORT, DEFAULT_PORT);
        String portsConfig = DEFAULT_PORT_RANGE;
        String jobName = config.getString(SERVICE_NAME,DEFAULT_SERVICE_NAME);
        String consulToken = config.getString(CONSUL_TOKEN,"");

        consulClient =  new ConsulClient(consulToken);
        Iterator<Integer> ports = NetUtils.getPortRangeFromString(portsConfig);

        while (ports.hasNext()) {
            int port = ports.next();
            try {
                // internally accesses CollectorRegistry.defaultRegistry
                httpServer = new HTTPServer(port);
                this.port = port;
                log.info("Started PrometheusReporter HTTP server on port {}.", port);
                break;
            } catch (IOException ioe) { //assume port conflict
                log.debug("Could not start PrometheusReporter HTTP server on port {}.", port, ioe);
            }
        }
        if (httpServer == null) {
            throw new RuntimeException("Could not start PrometheusReporter HTTP server on any configured port. Ports: " + portsConfig);
        }
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        //regist to consul
        this.consulClient.registerToConsul(this.ip+":"+this.port,jobName+"_"+df.format(date),this.ip,this.port,"flink");
    }

    @Override
    public void close() {
        if (httpServer != null) {
            httpServer.stop();
            log.info("PassService service form consul ,addr :{}:{}",this.ip,this.port);
            this.consulClient.passService(this.ip+":"+this.port);
        }
        super.close();

    }

    private String getLocalIP() {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not getLocal addr .",e);
        }
        return ip;
    }
}
