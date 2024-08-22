package com.company.map;
import java.io.File;
import java.io.StringWriter;
import java.io.File;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
        	StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            
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
            String h_sql = "DELETE FROM header WHERE id=" + h_id;
            jdbcTemplate.execute(h_sql);
            h_sql = "INSERT INTO header (id, center_lat, center_lon, east, west, north, south) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(h_sql, h_id, center_lat, center_lon, east, west, north, south);
            
            // n_road_lane
            String r_sql = "DROP TABLE IF EXISTS " + h_id + "_road_lane";
            jdbcTemplate.execute(r_sql);
            r_sql = "CREATE TABLE " + h_id + "_road_lane " + "(" + "id INT PRIMARY KEY," + "link LONGTEXT," + "planview LONGTEXT," + "elevation LONGTEXT," + "lanes LONGTEXT," + "situation LONGTEXT" + ");";
            jdbcTemplate.execute(r_sql);
            NodeList roads = (NodeList) xPath.evaluate("/OpenDRIVE/road", doc, XPathConstants.NODESET);
            for (int i = 0; i < roads.getLength(); i++) {
            	Node road = roads.item(i);
            	String r_id = road.getAttributes().getNamedItem("id").getNodeValue();
            	Node link_node = (Node) xPath.evaluate("link", road, XPathConstants.NODE);
                transformer.transform(new DOMSource(link_node), new StreamResult(writer));
                String link = writer.toString();
                writer.getBuffer().setLength(0);
                Node planview_node = (Node) xPath.evaluate("planView", road, XPathConstants.NODE);
                transformer.transform(new DOMSource(planview_node), new StreamResult(writer));
                String planview = writer.toString();
                writer.getBuffer().setLength(0);
                Node elevation_node = (Node) xPath.evaluate("elevationProfile", road, XPathConstants.NODE);
                transformer.transform(new DOMSource(elevation_node), new StreamResult(writer));
                String elevation = writer.toString();
                writer.getBuffer().setLength(0);
                Node lanes_node = (Node) xPath.evaluate("lanes", road, XPathConstants.NODE);
                transformer.transform(new DOMSource(lanes_node), new StreamResult(writer));
                String lanes = writer.toString();
                writer.getBuffer().setLength(0);
                r_sql = "INSERT INTO " + h_id + "_road_lane " + "(id, link, planview, elevation, lanes) " + "VALUES (?, ?, ?, ?, ?)";
                jdbcTemplate.update(r_sql, r_id, link, planview, elevation, lanes);
            }
            
            // n_junction_connection
            String j_sql = "DROP TABLE IF EXISTS " + h_id + "_junction_connection";
            jdbcTemplate.execute(j_sql);
            j_sql = "CREATE TABLE " + h_id + "_junction_connection " + "(" + "id INT PRIMARY KEY," + "connections LONGTEXT" + ");";
            jdbcTemplate.execute(j_sql);
            NodeList junctions = (NodeList) xPath.evaluate("/OpenDRIVE/junction", doc, XPathConstants.NODESET);
            for (int i = 0; i < junctions.getLength(); i++) {
            	Node junction = junctions.item(i);
            	String j_id = junction.getAttributes().getNamedItem("id").getNodeValue();
                transformer.transform(new DOMSource(junction), new StreamResult(writer));
                String connections = writer.toString();
                writer.getBuffer().setLength(0);
                j_sql = "INSERT INTO " + h_id + "_junction_connection " + "(id, connections) " + "VALUES (?, ?)";
                jdbcTemplate.update(j_sql, j_id, connections);
            }
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
