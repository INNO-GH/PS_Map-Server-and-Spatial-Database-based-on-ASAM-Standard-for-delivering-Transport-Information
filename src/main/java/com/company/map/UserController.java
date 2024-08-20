package com.company.map;
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
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user() {
		return "user";
	}
	
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public String receive(UserVo userVo, HttpServletResponse response) {
		if(userVo.getOpendrive().equals("yes") && !userVo.getCoordinate().isEmpty()) {
			// 데이터베이스를 만든다음에 정적파일 위치로뭔지찾는 인터페이스코드를 짜야함
			File file = new File("C:/Users/INNO/STS/project/PS_Map-Server-and-Spatial-Database-based-on-ASAM-Standard-for-delivering-Transport-Information/src/main/webapp/resources/Test.xodr");					
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
			System.out.println(userVo.getCoordinate());
			// 데이터베이스를 만든다음에 동적정보를 보내는 인터페이스코드를 짜야함
		}
		return "user";
	}
	
}
