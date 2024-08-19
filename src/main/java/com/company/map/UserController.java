package com.company.map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.company.map.vo.UserVo;

@Controller
public class UserController {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user() {
		return "user";
	}
	
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public String receive(UserVo userVo) {
		if(!userVo.getCoordinate().isEmpty()) {
			System.out.println(userVo.getCoordinate());
			// 유저뷰에서도 파일을 불러올까 말까를 선택할수 있는 인풋을 만들고 - 여기에다가 콜컨처럼 이프문나누고 파일가져오는거
			// 데이터베이스를 만든다음에 동적정보를 보내는 인터페이스코드를 짜야함
		}
		return "user";
	}
	
}
