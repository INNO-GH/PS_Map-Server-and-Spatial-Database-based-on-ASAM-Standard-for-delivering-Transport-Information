package com.company.map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.company.map.vo.CollectorVo;
import java.io.File;
import java.io.IOException;

@Controller
public class CollectorController {
	
	@RequestMapping(value = "/collector", method = RequestMethod.GET)
	public String collector() {
		return "collector";
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(CollectorVo collectorVo) {
		if(!collectorVo.getOpendrive().isEmpty()) {
			String folderPath = "C:/Users/INNO/STS/project/PS_Map-Server-and-Spatial-Database-based-on-ASAM-Standard-for-delivering-Transport-Information/src/main/webapp/resources/";
			String fileName = collectorVo.getOpendrive().getOriginalFilename();
			File destFile = new File(folderPath + fileName);
			try {
				collectorVo.getOpendrive().transferTo(destFile);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			// 데이터베이스를 만든다음에 정적정보를 업데이트하는 인터페이스코드를 짜야함
		}
		if(!collectorVo.getCoordinate().isEmpty() && !collectorVo.getSituation().isEmpty()) {
			System.out.println(collectorVo.getCoordinate()+" / "+collectorVo.getSituation());
			// 데이터베이스를 만든다음에 동적정보를 업데이트하는 인터페이스코드를 짜야함
		}
		return "collector";
	}
	
}
