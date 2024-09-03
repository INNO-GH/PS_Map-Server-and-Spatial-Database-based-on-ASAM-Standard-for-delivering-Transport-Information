package com.company.map;
import java.io.File;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.company.map.vo.CollectorVo;

@Service
public class CollectorService {

	@Autowired
	CollectorDao collectorDao;
	
	//----------//
	
	public void sendstatic(File opendrive) {
		collectorDao.savestatic(opendrive);
	}
	
	//----------//
	
	public void senddynamic(String coordinate, String situation) {
		collectorDao.savedynamic(coordinate, situation);
	}
	
}
