package com.company.map;
import java.io.File;
import java.io.File;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CollectorDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	//----------//
	
	public void savestatic(File opendrive) {
		try {        
			// set
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(opendrive);
            doc.getDocumentElement().normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // header
            String h_id_temp = opendrive.getName();
            String h_id = h_id_temp.substring(0, h_id_temp.lastIndexOf('.'));
            Node geoReference = (Node) xPath.evaluate("/OpenDRIVE/header/geoReference", doc, XPathConstants.NODE);
            String center_lat = geoReference.getAttributes().getNamedItem("lat_0").getNodeValue();
            String center_lon = geoReference.getAttributes().getNamedItem("lon_0").getNodeValue();
            Node header = (Node) xPath.evaluate("/OpenDRIVE/header", doc, XPathConstants.NODE);
            String east = header.getAttributes().getNamedItem("east").getNodeValue();
            String west = header.getAttributes().getNamedItem("west").getNodeValue();
            String north = header.getAttributes().getNamedItem("north").getNodeValue();
            String south = header.getAttributes().getNamedItem("south").getNodeValue();      
            String h_sql = "INSERT INTO header (id, center_lat, center_lon, east, west, north, south) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(h_sql, h_id, center_lat, center_lon, east, west, north, south);
            
            // n_road_lane
		
            // n_junction_connection 
		} 
		catch (Exception e) {
	        e.printStackTrace();
		}
	}
	
	//----------//
	
	public void savedynamic() {
		System.out.println("동적정보저장");
	}
	
}
