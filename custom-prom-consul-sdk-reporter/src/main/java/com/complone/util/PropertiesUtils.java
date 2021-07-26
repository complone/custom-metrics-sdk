package com.complone.util;


/*
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
public class PropertiesUtils {

    // 声明配置文件
    private static String[] confProps = { "env.properties" };
    private static PropertiesConfiguration conf = null;

    */
/**
     * 获取所有配置对象
     *//*

    public static PropertiesConfiguration getConf() {
        conf = new PropertiesConfiguration();// 初始化对象
        */
/* 把所有props配置文件一次加载,共后续使用 *//*

        for (int i = 0; i < confProps.length; i++) {
            try {
                conf.load(confProps[i]);
            } catch (ConfigurationException e) {
                conf = null;
            }
        }
        return conf;
    }

    */
/*
     * PropsUtil getInt()
     *//*

    public static Integer getInt(String key) {
        */
/** 声明返回值 **//*

        Integer value = null;
        try {
            */
/* 获取value值 *//*

            conf = getConf();
            value = conf.getInt(key);
        } catch (Exception e) {
        }
        return value;
    }

    */
/*
     * PropsUtil getString()
     *//*

    public static String getString(String key) {
        */
/** 声明返回值 **//*

        String value = null;
        try {
            */
/* 获取value值 *//*

            conf = getConf();
            value = conf.getString(key);
        } catch (Exception e) {
        }
        return value;
    }

    */
/*
     * PropsUtil getLong()
     *//*

    public static Long getLong(String key) {
        */
/** 声明返回值 **//*

        Long value = null;
        try {
            */
/* 获取value值 *//*

            conf = getConf();
            value = conf.getLong(key);
        } catch (Exception e) {
        }
        return value;
    }

    */
/*
     * PropsUtil getBoolean()
     *//*

    public static boolean getBoolean(String key) {
        */
/** 声明返回值 **//*

        boolean value = true;
        try {
            */
/* 获取value值 *//*

            conf = getConf();
            value = conf.getBoolean(key);
        } catch (Exception e) {
            value = false;
        }
        return value;
    }

    */
/*
     * PropsUtil getList()
     *//*

    public static String[] getStringArray(String key) {
        */
/** 声明返回值 **//*

        String[] value = null;
        try {
            */
/* 获取value值 *//*

            conf = getConf();
            value = conf.getStringArray(key);
        } catch (Exception e) {
        }
        return value;
    }

    */
/*
     * PropsUtil getJsonHeaders()
     *//*

    public static JSONObject getJsonHeaders(String key) {
        JSONObject json = null;// 声明返回值
        try {
            */
/** 解析*.Properties文件 **//*

            conf = getConf();
            String value = conf.getString(key).replace("=", ",");
            json = JSON.parseObject(value);
        } catch (Exception e) {
        }
        return json;
    }
*/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/**
 *
 * @author
 *
 */
public class PropertiesUtils {

    private String properiesName = "";

    public PropertiesUtils() {

    }

    public PropertiesUtils(String fileName) {
        this.properiesName = fileName;
    }

    /**
     * 按key获取值
     * @param key
     * @return
     */
    public  String readProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(properiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public int getPropertyInt(String key){
        int value =0;
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(properiesName);
            Properties p = new Properties();
            p.load(is);
            value = Integer.parseInt(p.getProperty(key));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 获取整个配置信息
     * @return
     */
    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(properiesName);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    /**
     * key-value写入配置文件
     * @param key
     * @param value
     */
    public void writeProperty(String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(properiesName);
//            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
            p.load(is);
//            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());
            os = new FileOutputStream(properiesName);

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
                if (null != os)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {

        //System.out.println(getString("address"));

        // String[] tmp = getStringArray("Email_to");
        // for(String to:tmp){
        // System.out.println(to);
        // }

        // JSONObject json = getJsonHeaders("httpHead");
        // for(Entry<String, Object> entry:json.entrySet()){
        // System.out.println(entry.getKey() + "-----" + entry.getValue().toString());
        // }

        // System.out.println(getInt("dbcp.url"));

        // System.out.println(getInt("redis.minIdle"));
        // String[] tmp = getStringArray("redis.redisSlaveIp");
        // for(int i=0;i<tmp.length;i++){
        // System.out.println(tmp[i].split(":")[0]);
        // System.out.println(tmp[i].split(":")[1]);
        // }
    }
}



