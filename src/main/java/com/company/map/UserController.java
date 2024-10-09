package com.company.map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.company.map.vo.UserVo;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user() {
		return "user";
	}
	
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public String receive(UserVo userVo, HttpServletResponse response, Model model) {
		if(userVo.getOpendrive().equals("yes") && !userVo.getCoordinate().isEmpty()) {
			// 데이터베이스 헤더테이블에서 위치맞는 오픈드라이브 찾기
			String file_name = "C:/Users/INNO/Git/PS_Map-Server-and-Spatial-Database-based-on-ASAM-Standard-for-delivering-Transport-Information/src/main/webapp/resources/"+userService.receivestatic(userVo.getCoordinate())+".xodr";
			// 파일에서 오픈드라이브 보내기
			File file = new File(file_name);					
			if (file.exists()) {
				String fileName = file.getName();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
				response.setContentLength((int) file.length());				
				try (FileInputStream fis = new FileInputStream(file); OutputStream os = response.getOutputStream()) {					
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = fis.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}						
					os.flush();						
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(!userVo.getCoordinate().isEmpty()) {
			// 데이터베이스에서 위치맞는 오픈드라이브 정보를 정리보내기
			String situation = userService.receivedynamic(userVo.getCoordinate());
			model.addAttribute("situation", situation);
		}
		return "user";
	}
	
}
