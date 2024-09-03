package com.company.map;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	//----------//
	
	public String receivestatic(String coordinate) {
		return userDao.loadstatic(coordinate);
	}
	
	//----------//
	
	public String receivedynamic(String coordinate) {
		return userDao.loaddynamic(coordinate);
	}
	
}
