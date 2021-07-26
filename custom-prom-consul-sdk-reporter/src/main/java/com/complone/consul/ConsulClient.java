package com.complone.consul;

import com.complone.util.MD5Utils;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.complone.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ConsulClient {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public ArrayList<Consul> consul_arr;
    public PropertiesUtils proUtils;
    public  List <String> consul_address = new ArrayList<String>();
    private URL url;
    {
        consul_arr = new ArrayList<Consul>();
        proUtils = new PropertiesUtils("env.properties");

        String dns = proUtils.readProperty("dns");
        consul_address.add("127.0.0.1");
        log.info("zns:"+dns);
        SimpleDiscoveryProperties simpleDiscoveryProperties = new SimpleDiscoveryProperties();
        //dns is serviceId
        simpleDiscoveryProperties.setInstance(dns,"ip",8088);
        DiscoveryClient discoveryClient = new SimpleDiscoveryClient(simpleDiscoveryProperties);
        try{
            List<ServiceInstance> instanceList = discoveryClient.getInstances(dns);
            for(int i =0; i<instanceList.size();i++){
                log.info("instance for zns:"+instanceList.get(i).getHost());
                consul_address.add(instanceList.get(i).getHost());
            }
        }catch(Exception e){
            log.error("Encounter an error while using BNS:" +e.getMessage());
        }
    }
    public ConsulClient(String token){
        for(int i =0;i<consul_address.size();i++){
            int port = proUtils.getPropertyInt("port");
            try {
                //需要自动判断consul client 是否可用
                log.info("ip:"+consul_address.get(i));
                url = new URL("http", consul_address.get(i), port, "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            consul_arr.add(Consul.builder().withUrl(url).withAclToken(token).build());
        }
    }

    /**
     * 注册服务
     * @param serviceId 唯一
     * @param serviceName
     * @param serviceHost
     * @param servicePort
     * @param tag 服务标签
     */
    public void registerToConsul(String serviceId,String serviceName,String serviceHost,int servicePort,String tag){

        for (int i =0; i<consul_arr.size();i++){
            AgentClient agentClient = consul_arr.get(i).agentClient();
            Registration service = ImmutableRegistration.builder()
                    .id(MD5Utils.stringToMD5(serviceId))
                    .name(serviceName)
                    .address(serviceHost)
                    .port(servicePort)
                    .check(Registration.RegCheck.http("http://"+serviceHost+":"+String.valueOf(servicePort)+"/metrics",3L)) // registers with a TTL of 3 seconds
                    .tags(Collections.singletonList(tag))
                    .meta(Collections.singletonMap("version", "1.0"))
                    .build();
            agentClient.register(service);

        }

    }

    /**
     * 取消注册服务
     * @param serviceId
     * @throws NotRegisteredException
     */
    public void passService(String serviceId)  {
        for (int i =0; i<consul_arr.size();i++){
            AgentClient agentClient = consul_arr.get(i).agentClient();
            agentClient.deregister(MD5Utils.stringToMD5(serviceId));
        }
    }

    public static void main(String args[]){
        ConsulClient client = new ConsulClient("token1");
//        ConsulClient client = new ConsulClient("token2");

        //client.registerToConsul("ip:port","deptA_2","ip",port,"flink");
       client.passService("ip:port");

    }



}
