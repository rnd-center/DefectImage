package com.mfafa.defectimage.Common;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Log4j2
@Component
public class Config {
	private static final String TAG = "CONFIG";

	@Autowired
	@Resource(name="prop")
	public Prop prop;


	
	@Autowired
	private HttpServletRequest request;

	@PostConstruct
	public void init() {
		try {
			prop.setPropFile("application.properties");

			// Login Config
			// ------------------------------
			Config.loginInfo = new JSONObject();
			Config.loginInfo.put("loginPW", prop.get("login.password"));


		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static JSONObject loginInfo = null;
}


