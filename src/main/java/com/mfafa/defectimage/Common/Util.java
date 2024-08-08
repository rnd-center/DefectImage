package com.mfafa.defectimage.Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Util {
	private static int curYear, curMonth, curDay, curHour, curMinute, curSecond;
	private static Calendar mCalendar;

	static public String getLocalMacAddr() 
	{
		String result = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface netif = NetworkInterface.getByInetAddress(ip);
			if(netif!=null){
				byte[] mac = netif.getHardwareAddress();
				for(byte b : mac){
					result += String.format(":%02x", b);
				}
				result = result.substring(1);
			}else{
				result = "NetworkInterface is Nothing.";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return result;
	}

	static public String getTimeFormat(Timestamp tmp, String Format)
	{
		// Format ex) "yyyy-MM-dd HH:mm:ss"
		return  new SimpleDateFormat(Format).format(new java.sql.Date(tmp.getTime())) + "";
	}

	public static String formatDateString(String dateNowNum) {
		// 원래 문자열의 포맷
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		// 원하는 출력 문자열의 포맷
		SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			// 문자열을 Date 객체로 변환
			date = originalFormat.parse(dateNowNum);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		// Date 객체를 원하는 포맷의 문자열로 변환
		return targetFormat.format(date);
	}
	
	static public String getCurrentMillisec() {
		mCalendar = Calendar.getInstance();
		curYear = mCalendar.get(Calendar.YEAR);  
		curMonth = mCalendar.get(Calendar.MONTH)+1;  
		curDay = mCalendar.get(Calendar.DAY_OF_MONTH);    
		curHour = mCalendar.get(Calendar.HOUR_OF_DAY);  
		curMinute = mCalendar.get(Calendar.MINUTE);  
		curSecond = mCalendar.get(Calendar.SECOND);  
		String s_date = Integer.toString(curYear) +
				Integer.toString(curMonth) +
				Integer.toString(curDay) +
				Integer.toString(curHour) +
				Integer.toString(curMinute) +
				Integer.toString(curSecond) +
				Integer.toString(mCalendar.get(Calendar.MILLISECOND));
		
		return s_date;
	}

	static public String getDateNowMillisec() {
		mCalendar = Calendar.getInstance();
		curYear = mCalendar.get(Calendar.YEAR);  
		curMonth = mCalendar.get(Calendar.MONTH)+1;  
		curDay = mCalendar.get(Calendar.DAY_OF_MONTH);    
		curHour = mCalendar.get(Calendar.HOUR_OF_DAY);  
		curMinute = mCalendar.get(Calendar.MINUTE);  
		curSecond = mCalendar.get(Calendar.SECOND);  
		String s_date = curYear + "-" + 
				curMonth + "-" + 
				curDay + " " + 
				curHour + ":" + 
				curMinute + ":" + 
				curSecond +"." +
				mCalendar.get(Calendar.MILLISECOND);

		return s_date;
	}
	
	
	static public String getDateNow() {
		mCalendar = Calendar.getInstance();
		curYear = mCalendar.get(Calendar.YEAR);  
		curMonth = mCalendar.get(Calendar.MONTH)+1;  
		curDay = mCalendar.get(Calendar.DAY_OF_MONTH);    
		curHour = mCalendar.get(Calendar.HOUR_OF_DAY);  
		curMinute = mCalendar.get(Calendar.MINUTE);  
		curSecond = mCalendar.get(Calendar.SECOND);  
		String s_date = curYear + "-" + 
				curMonth + "-" + 
				curDay + " " + 
				curHour + ":" + 
				curMinute + ":" + 
				curSecond;

		return s_date;
	}
	
	static public String getDateNowDate() {
		mCalendar = Calendar.getInstance();
		curYear = mCalendar.get(Calendar.YEAR);
		curMonth = mCalendar.get(Calendar.MONTH)+1;
		curDay = mCalendar.get(Calendar.DAY_OF_MONTH);

		String Month = (Integer.toString(curMonth)).length()>1 ? Integer.toString(curMonth) : "0" + curMonth;
		String Day = (Integer.toString(curDay)).length()>1 ? Integer.toString(curDay) : "0" + curDay;

		String s_date = curYear + "-" +
				Month + "-" +
				Day;

		return s_date;
	}
	
	static public String getDateNowNum() {
		mCalendar = Calendar.getInstance();
		curYear = mCalendar.get(Calendar.YEAR);  
		curMonth = mCalendar.get(Calendar.MONTH)+1;  
		curDay = mCalendar.get(Calendar.DAY_OF_MONTH);    
		curHour = mCalendar.get(Calendar.HOUR_OF_DAY);  
		curMinute = mCalendar.get(Calendar.MINUTE);  
		curSecond = mCalendar.get(Calendar.SECOND);

		String Month = (Integer.toString(curMonth)).length()>1 ? Integer.toString(curMonth) : "0" + curMonth;
		String Day = (Integer.toString(curDay)).length()>1 ? Integer.toString(curDay) : "0" + curDay;
		String Hour = (Integer.toString(curHour)).length()>1 ? Integer.toString(curHour) : "0" + curHour;
		String Minute = (Integer.toString(curMinute)).length()>1 ? Integer.toString(curMinute) : "0" + curMinute;
		String Second = (Integer.toString(curSecond)).length()>1 ? Integer.toString(curSecond) : "0" + curSecond;

		/*String s_date = curYear+"-"+
				curMonth+"-"+
				curDay+"-"+
				curHour+
				curMinute+
				curSecond+"."+
				mCalendar.get(Calendar.MILLISECOND);*/

		String s_date = curYear+""+Month+""+Day+""+Hour+""+Minute+""+Second+"";

		return s_date;
	}

	public static boolean DirectoryCreate(String DirName)
	{
		boolean result = false;
		File desFile = new File(DirName);
		if(!desFile.isDirectory()) result = desFile.mkdirs(); 
		return result;
	}
	
	public static byte[] hexStringToByteArray(String str)
	{
		try {
			int len = str.length();
			byte[] data = new byte[len / 2];
			for(int i=0; i< len; i+=2) {
				data[i / 2] = (byte) (( Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i+1), 16));
			}
			return data;
		}catch(Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public static String byteArrayToHexString(byte[] bytes) {
		try {
			StringBuilder sb = new StringBuilder();
			for(byte b : bytes) {
				sb.append(String.format("%02x", b&0xff));
			}
		}catch(Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public static String FileList ="";
	private static void FilePathList(String strDirPath) { 
		File path = new File(strDirPath); 
		File[] fList = path.listFiles(); 
		for( int i = 0; i < fList.length; i++) { 
			if( fList[i].isFile() ) { 
				FileList += fList[i].getPath()+"\r\n";
				System.out.println( fList[i].getPath() ); 
			} else if( fList[i].isDirectory()) { 
				FilePathList( fList[i].getPath());  
				
			}
		}
	}
	
	public static void execute(String Query) {
		 String s;
        Process p;
        try {
        	//이 변수에 명령어를 넣어주면 된다. 
            String[] cmd = {"/bin/sh","-c", Query};
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
        }
    }

	public static boolean checkDownloadPermission(String DOWN_PERM, String FILE_TYPE) {
		try {System.out.println(DOWN_PERM);System.out.println(FILE_TYPE);System.out.println(DOWN_PERM != null);System.out.println(!"".equals(DOWN_PERM));System.out.println(DOWN_PERM.contains(FILE_TYPE));
			if(DOWN_PERM != null && !"".equals(DOWN_PERM) && DOWN_PERM.contains(FILE_TYPE)){
				return true;
			}
		}catch(Exception e) { e.printStackTrace(); }
		return false;
	}

	public static int CheckNeedVersionUpdate(String currentVersion, String requestVersion){
		boolean result = false;

		String[] levels1 = currentVersion.split("\\.");
		String[] levels2 = requestVersion.split("\\.");

		int length = Math.max(levels1.length, levels2.length);

		for (int i = 0; i < length; i++) {
			Integer v1 = i < levels1.length ? Integer.parseInt(levels1[i]) : 0;
			Integer v2 = i < levels2.length ? Integer.parseInt(levels2[i]) : 0;
			int comparison = v1.compareTo(v2);
			if (comparison != 0) {
				return comparison;
			}
		}

		return 0; // Versions are equal

	}

	public static void updateJsonFile(MultipartHttpServletRequest httpServletRequest, String newVersion, String newReleaseDate, String newChanges, String newDownloadUrl){
		String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
		File folder = new File(rootpath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String jsonFilePath = rootpath + "/clients_history.json";

		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File(jsonFilePath);

		try {
			// JSON 파일 읽기
			JsonNode root = objectMapper.readTree(file);
			ObjectNode currentVersion = (ObjectNode) root.get("currentVersion");
			ArrayNode updateHistory = (ArrayNode) root.get("updateHistory");

			// currentVersion 수정
			currentVersion.put("version", newVersion);
			currentVersion.put("releaseDate", newReleaseDate);
			currentVersion.put("changes", newChanges);
			currentVersion.put("downloadUrl", newDownloadUrl);

			// updateHistory에 신규 항목 추가
			ObjectNode newUpdate = objectMapper.createObjectNode();
			newUpdate.put("version", newVersion);
			newUpdate.put("releaseDate", newReleaseDate);
			newUpdate.put("changes", newChanges);
			newUpdate.put("downloadUrl", newDownloadUrl);

			// 새로운 배열을 생성하여 맨 위에 새로운 항목을 추가하고 나머지 항목들을 추가
			ArrayNode newUpdateHistory = objectMapper.createArrayNode();
			newUpdateHistory.add(newUpdate);
			newUpdateHistory.addAll(updateHistory);

			// 기존 updateHistory를 새로운 배열로 교체
			((ObjectNode) root).set("updateHistory", newUpdateHistory);

			// 수정된 내용을 JSON 파일에 다시 쓰기
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ObjectNode createEmptyCurrentVersion(ObjectMapper objectMapper) {
		ObjectNode currentVersion = objectMapper.createObjectNode();
		currentVersion.put("version", "");
		currentVersion.put("releaseDate", "");
		currentVersion.put("changes", "");
		currentVersion.put("downloadUrl", "");
		return currentVersion;
	}

	public static void saveJsonToFile(File file, JsonNode currentVersion, JsonNode updateHistory, ObjectMapper objectMapper) {
		ObjectNode root = objectMapper.createObjectNode();
		root.set("currentVersion", currentVersion);
		root.set("updateHistory", updateHistory);
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 폴더를 압축하는 메서드
	public static void zipFolder(File folder, String parentFolderName, ZipOutputStream zos) throws IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				zipFolder(file, parentFolderName + "/" + file.getName(), zos);
				continue;
			}
			zos.putNextEntry(new ZipEntry(parentFolderName + "/" + file.getName()));
			try (FileInputStream fis = new FileInputStream(file)) {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
			}
			zos.closeEntry();
		}
	}

	public static void logUpdate(MultipartHttpServletRequest httpServletRequest, String version, String releaseDate, String changes, String downloadUrl, String ip) {
		String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
		File folder = new File(rootpath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String jsonFilePath = rootpath + "/clients_update.log";

		try (FileWriter fw = new FileWriter(jsonFilePath, true)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = sdf.format(new Date());

			fw.write(String.format("[%s] Version: %s, ReleaseDate: %s, Changes: %s, DownloadUrl: %s, IP: %s\n",
					timestamp, version, releaseDate, changes, downloadUrl, ip));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logRequest(HttpServletRequest httpServletRequest, String identifier, String requestVersion, String version, String ip, boolean needUpdate) {
		String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
		File folder = new File(rootpath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String jsonFilePath = rootpath + "/clients_request.log";

		try (FileWriter fw = new FileWriter(jsonFilePath, true)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = sdf.format(new Date());
			fw.write(String.format("[%s] Identifier: %s, Request Version: %s, Current Version: %s, Need Update: %s, IP: %s\n",
					timestamp, identifier, requestVersion, version, needUpdate, ip));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logRequestFail(HttpServletRequest httpServletRequest, String identifier, String requestVersion, String ip) {
		String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
		File folder = new File(rootpath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String jsonFilePath = rootpath + "/clients_request_fail.log";

		try (FileWriter fw = new FileWriter(jsonFilePath, true)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = sdf.format(new Date());
			fw.write(String.format("[%s] Identifier: %s, Request Version: %s, IP: %s\n",
					timestamp, (identifier!=null&&identifier.length()>0?identifier:"null"), (requestVersion!=null&&requestVersion.length()>0?requestVersion:"null"), ip));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logDownload(HttpServletRequest httpServletRequest, String fileName, String ip) {
		String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
		File folder = new File(rootpath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String jsonFilePath = rootpath + "/clients_download.log";

		try (FileWriter fw = new FileWriter(jsonFilePath, true)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = sdf.format(new Date());
			fw.write(String.format("[%s] Download. File Name: %s, IP: %s\n",
					timestamp, fileName, ip));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logDownloadFail(HttpServletRequest httpServletRequest, String fileName, String ip) {
		String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
		File folder = new File(rootpath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String jsonFilePath = rootpath + "/clients_download_fail.log";

		try (FileWriter fw = new FileWriter(jsonFilePath, true)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = sdf.format(new Date());
			fw.write(String.format("[%s] Download Fail. File Name: %s, IP: %s\n",
					timestamp, fileName, ip));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printMessage(HttpServletResponse resp, String msg) throws Exception {
		resp.setCharacterEncoding("utf-8");
		resp.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		//target이 지정되지 않은 경우 history.back() 으로 처리
		out.println("<script type='text/javascript'>");
		out.println("alert('" + msg + "');");
		out.println("history.back();");
		out.println("</script>");
	}
}
