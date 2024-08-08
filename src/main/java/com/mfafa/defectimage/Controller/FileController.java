package com.mfafa.defectimage.Controller;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mfafa.defectimage.Common.ContextUtil;
import com.mfafa.defectimage.Common.Logger;
import com.mfafa.defectimage.Common.Protocol;
import com.mfafa.defectimage.Common.Util;
import com.mfafa.defectimage.Model.Login;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class FileController {
	private static final String TAG="FILE_CONTROLLER";

	@Autowired
	private HttpServletRequest request;

	@PostMapping("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA + ";charset=UTF-8")
	public String UploadFile(MultipartHttpServletRequest Request, @RequestParam HashMap<String, Object> param, Model model) throws Exception {

		JSONObject jobj = new JSONObject();
		jobj.put(Protocol.Response.Code, "200");
		jobj.put(Protocol.Response.Result, false);
		jobj.put(Protocol.Response.Message, "");
		jobj.put(Protocol.Response.Value,"");

			try {
				String rootpath = Request.getServletContext().getRealPath("/") + "files";
				File rootdir = new File(rootpath);
				if (!rootdir.exists()) {
					rootdir.mkdir();
				}

				List<MultipartFile> defectFile = Request.getFiles("files");
				if (defectFile != null && defectFile.size() > 0 && defectFile.get(0).getSize() > 0) {
					String pathname = rootpath + "/" + Util.getDateNowDate();
					File pathdir = new File(pathname);
					if (!pathdir.exists()) {
						pathdir.mkdir();
					}

					List<String> list = new ArrayList<>();
					for(int j=0;j<defectFile.size();j++) {
						String OriName = defectFile.get(j).getOriginalFilename();
						if(OriName.substring(OriName.lastIndexOf(".") + 1)!=null && OriName.substring(OriName.lastIndexOf(".") + 1).length() > 0) {
							System.out.println(Util.getDateNowNum() + ": OriName >>> " + OriName);
							String realFileName =  OriName.substring(0,OriName.lastIndexOf("."))+ "_" + Util.getDateNowNum() + j + "." + OriName.substring(OriName.lastIndexOf(".") + 1).toLowerCase();
							String ext = OriName.substring(OriName.lastIndexOf(".") + 1).toLowerCase();

							File setFile = new File(pathname + "/" + realFileName);
							defectFile.get(j).transferTo(setFile);
							if (setFile.exists()) {
								Logger.Info(TAG, "FilePath : " + setFile.getAbsolutePath());
								list.add(setFile.getAbsolutePath().replace("\\", ""));
							}
						}else{
							jobj.put(Protocol.Response.Message, " Extension not found.");
						}
					}
					jobj.put(Protocol.Response.Result, true);
					jobj.put(Protocol.Response.Value, list.toString());

				}else{
					jobj.put(Protocol.Response.Message, " File not found.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return jobj.toString();
	}

	@PostMapping("/checkClientVersion")
	public String CheckClientVersion(HttpServletRequest Request, @RequestParam HashMap<String, String> param, Model model) throws Exception {

		JSONObject jobj = new JSONObject();
		jobj.put(Protocol.Response.Code, "200");
		jobj.put(Protocol.Response.Result, false);
		jobj.put(Protocol.Response.Message, "");
		jobj.put(Protocol.Response.Value,null);

		try {
			String IP = Request.getRemoteAddr();
			boolean NeedUpdate = false;

			String Identifier = param.get("Identifier");
			String RequestVersion = param.get("RequestVersion");

			if(Identifier != null && Identifier.length()>0 && RequestVersion != null && RequestVersion.length()>0){
				String rootpath = Request.getServletContext().getRealPath("/") + "clients";
				File rootdir = new File(rootpath);
				if (!rootdir.exists()) {
					rootdir.mkdir();
				}

				String jsonFilePath = rootpath + "/clients_history.json";

				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File(jsonFilePath);

				JsonNode root = objectMapper.readTree(file);
				ObjectNode currentVersion = (ObjectNode) root.get("currentVersion");

				String Version = currentVersion.get("version").asText();
				String ReleaseDate = currentVersion.get("releaseDate").asText();
				String Changes = currentVersion.get("changes").asText();
				String DownloadUrl = currentVersion.get("downloadUrl").asText();

				JSONObject value = new JSONObject();
				// 버전 체크
				if(Util.CheckNeedVersionUpdate(Version,RequestVersion)!=0){
					//업데이트 필요
					NeedUpdate = true;
					value.put(Protocol.Response.Version, Version);
					value.put(Protocol.Response.ReleaseDate, ReleaseDate);
					value.put(Protocol.Response.Changes, Changes);
					value.put(Protocol.Response.DownloadUrl, DownloadUrl);
				}
				// 리스폰스 생성
				value.put(Protocol.Response.NeedUpdate, NeedUpdate);

				jobj.put(Protocol.Response.Result, true);
				jobj.put(Protocol.Response.Value,value);
				//로그 추가
				Util.logRequest(Request, Identifier, RequestVersion, Version, IP, NeedUpdate);
			}else{
				jobj.put(Protocol.Response.Message, "Identifier or RequestVersion is missing.");
				Util.logRequestFail(Request, Identifier, RequestVersion, IP);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jobj.put(Protocol.Response.Message, "ClientVersion not found.");
		}
		return jobj.toString();
	}
}
