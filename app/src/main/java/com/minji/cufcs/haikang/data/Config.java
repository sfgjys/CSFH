package com.minji.cufcs.haikang.data;


import android.content.Context;
import android.content.SharedPreferences;

import com.minji.cufcs.base.BaseApplication;

public final class Config {
    private static Config       ins              = null;
    private SharedPreferences   sp;
    /**
     * 配置文件文件名
     */
    private static final String CONFIG_FILE_NAME = "demo_conf";
    /**
     * 服务器地址
     */
    private static final String SERVER_ADDR      = "server_addr";

    /**
     * 默认登录地址，这个地址是我们公司测试地址，开发者可以根据实际情况进行修改
     */
    private static final String DEF_SERVER       = "http://223.112.181.214:8443";

    private Config() 
    {
        sp = BaseApplication.getIns().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static Config getIns() {
    	if(null == ins)
    	{
    		ins = new Config();
    	}
        return ins;
    }

    /**
      * 设置服务器地址
      * @param serverAdrr
      * @since V1.0
      */
    public void setServerAddr(String serverAdrr) {
        sp.edit().putString(SERVER_ADDR, serverAdrr).commit();
    }

    /**
      * 获取服务器地址
      * @return
      * @since V1.0
      */
    public String getServerAddr() {
        return sp.getString(SERVER_ADDR, DEF_SERVER);
    }

}
