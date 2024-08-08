package com.mfafa.defectimage.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mfafa.defectimage.Common.Config;
import com.mfafa.defectimage.Common.ContextUtil;
import com.mfafa.defectimage.Common.Logger;
import com.mfafa.defectimage.Common.Util;
import com.mfafa.defectimage.Model.Login;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@CrossOrigin(origins = "*")
@RequestMapping("/")
@Controller
public class PageController {
    private static final String TAG="PAGE_CONTROLLER";

    @GetMapping("/")
    public String Index() {
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");
        return login!=null ? "redirect:/Download" : "redirect:/Login";
    }

    @GetMapping("/Login")
    public String Login() {
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");
        return login!=null ? "redirect:/Download" : "login";
    }

    @PostMapping("/doLogin")
    public String DoLogin(HttpServletRequest httpServletRequest, Model model) {
        Login login = null;
        String p_password = (String) httpServletRequest.getParameter("p_password");
        String password = Config.loginInfo.get("loginPW").toString();

        if(password.equals(p_password)){
            //IP
            /*String ip = httpServletRequest.getHeader("X-Forwarded-For");
			if (ip == null) ip = httpServletRequest.getRemoteAddr();*/
            String ip = httpServletRequest.getRemoteAddr();

            login = new Login();
            login.setLOGIN_IP(ip);
            login.setLOGIN_DT(Util.getDateNowNum());
            ContextUtil.setAttrToSession("__LOGIN", login);
        }else {
            model.addAttribute("__ALERT", "Incorrect password.");
        }
        return login!=null ? "redirect:/Download" : "login";
    }

    @GetMapping("/Download")
    public String Download(HttpServletRequest httpServletRequest, Model model) {
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");

        String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "files";
        File folder = new File(rootpath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        List <String> dateList = new ArrayList<String>();
        for(final File fileEntry : folder.listFiles()){
            if(fileEntry.isDirectory()){
                dateList.add(fileEntry.getName());
            }
        }

        if(dateList.size()>0){
            // 파일명 오름차순
            Collections.sort(dateList);
            model.addAttribute("__DATELIST", dateList);
        }

        return login!=null ? "download" : "redirect:/Login";
    }

    @PostMapping("/doDownload")
    public void DoDownload(HttpServletRequest httpServletRequest, HttpServletResponse response, Model model) throws ServletException, IOException{
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");
        String p_date = (String) httpServletRequest.getParameter("p_date");
        // 압축할 폴더 경로
        String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "files/" + p_date;

        // 클라이언트에게 전송할 파일 이름
        String zipFileName = p_date+".zip";

        // response에 설정
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");

        // ZipOutputStream을 사용하여 압축 파일 생성
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            File folder = new File(rootpath);
            Util.zipFolder(folder, folder.getName(), zos);
        }
    }

    @GetMapping("/UpdateClientLogin")
    public String UpdateClientLogin() {
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");
        return login!=null ? "redirect:/UpdateClient" : "update-client-login";
    }

    @PostMapping("/doUpdateClientLogin")
    public String DoUpdateClientLogin(HttpServletRequest httpServletRequest, Model model) {
        Login login = null;
        String p_password = (String) httpServletRequest.getParameter("p_password");
        String password = Config.loginInfo.get("loginPW").toString();

        if(password.equals(p_password)){
            //IP
            /*String ip = httpServletRequest.getHeader("X-Forwarded-For");
			if (ip == null) ip = httpServletRequest.getRemoteAddr();*/
            String ip = httpServletRequest.getRemoteAddr();

            login = new Login();
            login.setLOGIN_IP(ip);
            login.setLOGIN_DT(Util.getDateNowNum());
            ContextUtil.setAttrToSession("__LOGIN", login);
        }else {
            model.addAttribute("__ALERT", "Incorrect password.");
        }
        return login!=null ? "redirect:/UpdateClient" : "update-client-login";
    }

    @GetMapping("/UpdateClient")
    public String UpdateClient(HttpServletRequest httpServletRequest, Model model) {
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");

        String rootpath = httpServletRequest.getServletContext().getRealPath("/") + "clients";
        File folder = new File(rootpath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String jsonFilePath = rootpath + "/clients_history.json";
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode currentVersion;
        JsonNode updateHistory;

        File jsonFile = new File(jsonFilePath);

        if (jsonFile.exists()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(jsonFile);
                currentVersion = jsonNode.get("currentVersion");
                updateHistory = jsonNode.get("updateHistory");
            } catch (IOException e) {
                e.printStackTrace();
                // 기본 값 설정
                currentVersion = Util.createEmptyCurrentVersion(objectMapper);
                updateHistory = objectMapper.createArrayNode();
            }
        } else {
            // 기본 값 설정 및 새 파일 생성
            currentVersion = Util.createEmptyCurrentVersion(objectMapper);
            updateHistory = objectMapper.createArrayNode();
            Util.saveJsonToFile(jsonFile, currentVersion, updateHistory, objectMapper);
        }

        model.addAttribute("currentVersion", currentVersion);
        model.addAttribute("updateHistory", updateHistory);

        return login!=null ? "update-client" : "redirect:/UpdateClientLogin";
    }

    @PostMapping("/doUpdateClient")
    @Consumes(MediaType.MULTIPART_FORM_DATA + ";charset=UTF-8")
    public String DoUpdateClient(MultipartHttpServletRequest Request, @RequestParam HashMap<String, Object> param, Model model) throws Exception {
        Login login = (Login) ContextUtil.getAttrFromSession("__LOGIN");
        if(login==null)return "redirect:/UpdateClientLogin";

        try {
            String rootpath = Request.getServletContext().getRealPath("/") + "clients";
            File folder = new File(rootpath);
            if (!folder.exists()) {
                folder.mkdir();
            }

//			String version = Request.getParameter("version");
            String version = (String) param.get("version");
//			String changes = Request.getParameter("changes");
            String changes = (String) param.get("changes");
            MultipartFile formFile = Request.getFile("formFile");

            if (formFile != null && formFile.getSize() > 0) {
                String OriName = formFile.getOriginalFilename();
                String dateNowNum = Util.getDateNowNum();
                String releaseDate = Util.formatDateString(dateNowNum);
                String ext = OriName.substring(OriName.lastIndexOf(".") + 1).toLowerCase();
                String downloadUrl = "";

                if(ext!=null && ext.length() > 0) {
                    String realFileName =  "client_" + version + "_" + dateNowNum + "." + ext;

                    File setFile = new File(rootpath + "/" + realFileName);
                    formFile.transferTo(setFile);
                    if (setFile.exists()) {
                        Logger.Info(TAG, "FilePath : " + setFile.getAbsolutePath());
                        downloadUrl = "http://hogarm.com:8080/clients/" + realFileName;
                        // clients_history.json 파일 수정
                        Util.updateJsonFile(Request, version, releaseDate, changes, downloadUrl);
                        // 로그 추가
                        Util.logUpdate(Request, version, releaseDate, changes, downloadUrl, login.getLOGIN_IP());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/UpdateClient";
    }

    @GetMapping("/Logout")
    public String Logout(HttpServletRequest httpServletRequest) {
        String referer = (String)httpServletRequest.getHeader("REFERER");

        ContextUtil.setAttrToSession("__LOGIN", null);
        return  "redirect:"+referer;
    }

    @GetMapping("/clients/{fileName}")
    public void downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest Request, HttpServletResponse response) throws IOException{
        String rootpath = Request.getServletContext().getRealPath("/") + "clients";
        File rootdir = new File(rootpath);
        if (!rootdir.exists()) {
            rootdir.mkdir();
        }

        String filePath = rootpath + "/" + fileName;
        try {
            String IP = Request.getRemoteAddr();
            File fp = new File(filePath);
            if(fp.exists()) {
                Util.logDownload(Request, fileName, IP);
                byte[] fileByte = FileUtils.readFileToByteArray(fp);
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName, "UTF-8")+"\";");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.getOutputStream().write(fileByte);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }else {
                Util.logDownloadFail(Request, fileName, IP);
                Util.printMessage(response, "File doesn't exists.");
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

}
