package com.mfafa.defectimage.Common;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
@Service("prop")
public class Prop {
	private Properties _properties;

	public void setPropFile(String proFile) throws IOException {
		ClassPathResource resource = new ClassPathResource(proFile);
		InputStream is = resource.getInputStream();

		_properties = new Properties();
		_properties.load(is);

		try { is.close(); } catch (Exception ignored) {}
	}

	public String get(String key) {
		return _properties.getProperty(key, "");
	}
}
