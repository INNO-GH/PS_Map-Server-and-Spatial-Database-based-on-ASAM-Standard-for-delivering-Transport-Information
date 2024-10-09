package com.company.map;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	//----------//
	
	public String loadstatic(String coordinate) {
		
		// set
		String[] latlon = coordinate.split(",");
		double lat = Double.parseDouble(latlon[0].trim());
		double lon = Double.parseDouble(latlon[1].trim());
		double y = 0;
		double x = 0;
		String name = "";
			    
	    // header
		String sql = "SELECT * FROM header";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for(Map<String, Object> row : rows) {
			double center_lat = ((BigDecimal)row.get("center_lat")).doubleValue();
			double center_lon = ((BigDecimal)row.get("center_lon")).doubleValue();
		    double east = ((BigDecimal)row.get("east")).doubleValue();
		    double west = ((BigDecimal)row.get("west")).doubleValue();
		    double north = ((BigDecimal)row.get("north")).doubleValue();
		    double south = ((BigDecimal)row.get("south")).doubleValue();
		    y = (lat-center_lat)*(111319.49); 
		    x = (lon-center_lon)*(111319.49)*(Math.cos(Math.toRadians(center_lat)));
		    if( (north>y) && (y>south) && (west<x) && (x<east) ) {
		    	name = (String)row.get("name");
		        break;
		    }
		}
		
		// return
		return name;
				
	}
	
	//----------//

	public String loaddynamic(String coordinate) {
	    
		// set
		String situation = "";
		String[] latlon = coordinate.split(",");
	    double lat = Double.parseDouble(latlon[0].trim());
	    double lon = Double.parseDouble(latlon[1].trim());
	    double y = 0;
	    double x = 0;
	    String name = "";
	    
	    // header
        String sql = "SELECT * FROM header";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for(Map<String, Object> row : rows) {
        	double center_lat = ((BigDecimal)row.get("center_lat")).doubleValue();
            double center_lon = ((BigDecimal)row.get("center_lon")).doubleValue();
            double east = ((BigDecimal)row.get("east")).doubleValue();
            double west = ((BigDecimal)row.get("west")).doubleValue();
            double north = ((BigDecimal)row.get("north")).doubleValue();
            double south = ((BigDecimal)row.get("south")).doubleValue();
        	y = (lat-center_lat)*(111319.49); 
        	x = (lon-center_lon)*(111319.49)*(Math.cos(Math.toRadians(center_lat)));
        	if( (north>y) && (y>south) && (west<x) && (x<east) ) {
        		name = (String)row.get("name");
        		break;
        	}
        }
        
        // n_road_lane
        sql = "SELECT * FROM " + name + "_road_lane";
        rows = jdbcTemplate.queryForList(sql);
        for(Map<String, Object> row : rows) {
        	
        	String road_id = String.valueOf(row.get("id"));
        	String planview_raw = (String)row.get("planview");
        	String lanes_raw = (String)row.get("lanes"); 
        	
        	try {
        		
        		// 1. set
        		DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder Builder = Factory.newDocumentBuilder();
                Document lanes = Builder.parse(new java.io.ByteArrayInputStream(lanes_raw.getBytes("UTF-8")));
                lanes.getDocumentElement().normalize();
                Document planview = Builder.parse(new java.io.ByteArrayInputStream(planview_raw.getBytes("UTF-8")));
                planview.getDocumentElement().normalize();
                XPath xPath = XPathFactory.newInstance().newXPath();
                
                // 2. get lanes information
                NodeList lane_list = (NodeList) xPath.evaluate("//lane", lanes, XPathConstants.NODESET); // 레인들의 아이디와 폭을 배열로 가져옴
                ArrayList<double[]> ldwt = new ArrayList<>();
                double ld_pre = 10000.0;
                for (int i = 0; i < lane_list.getLength(); i++) { 
                    Node lane = lane_list.item(i);
                    double ld = Double.parseDouble(lane.getAttributes().getNamedItem("id").getNodeValue());
                    if(ld_pre <= ld) {
                    	break;
                    }
                    ld_pre = ld;
                    double wt = 0.0;
                    Node width = (Node) xPath.evaluate("width", lane, XPathConstants.NODE);
                	if(width != null) {
                		wt = Double.parseDouble(width.getAttributes().getNamedItem("a").getNodeValue());
                	}
                    double[] ldwt_element = {ld, wt};
                    ldwt.add(0, ldwt_element);
                }
                int index = -1; // 레인배열에서 중앙선 기준 떨어진 폭으로 변경
                for (int i = 0; i < ldwt.size(); i++) {
                    if (ldwt.get(i)[0] == 0.0) {
                        index = i;
                        break;
                    }
                }
                for (int i = index+1; i < ldwt.size(); i++) {
                	ldwt.get(i)[1] = ldwt.get(i-1)[1] + ldwt.get(i)[1];
                }
                for (int i = index-1; i > -1; i--) {
                	ldwt.get(i)[1] = ldwt.get(i+1)[1] - ldwt.get(i)[1];
                }
                
                // 3. check planview with lanes
                NodeList geometry_list = (NodeList) xPath.evaluate("/planView/geometry", planview, XPathConstants.NODESET);
                for (int i = 0; i < geometry_list.getLength(); i++) {
                	
                	// 지오메트리섹션의 값들 획득
                	Node geometry = geometry_list.item(i);
                	double s = Double.parseDouble(geometry.getAttributes().getNamedItem("s").getNodeValue());
                	double sx = Double.parseDouble(geometry.getAttributes().getNamedItem("x").getNodeValue());
                	double sy = Double.parseDouble(geometry.getAttributes().getNamedItem("y").getNodeValue());
                	double sh = Double.parseDouble(geometry.getAttributes().getNamedItem("hdg").getNodeValue());
                	double len = Double.parseDouble(geometry.getAttributes().getNamedItem("length").getNodeValue());
                	double cur = 0.0;
                	if( sh > 2*Math.PI ) sh = sh - 2*Math.PI;
            		if( sh < 0 ) sh = sh + 2*Math.PI;
                	Node arc = (Node) xPath.evaluate("arc", geometry, XPathConstants.NODE);
                	if(arc != null) {
                		cur = Double.parseDouble(arc.getAttributes().getNamedItem("curvature").getNodeValue());
                	}
                	
                	// 지오메트리 직선경우 포함확인
                	if(cur == 0.0) {
                		double x_dif = x - sx;
                		double y_dif = y - sy;
                		double point_dif = Math.sqrt(Math.pow(x_dif, 2) + Math.pow(y_dif, 2));
                		double line_dif = 0.0;
                		double angle_dif = 0.0;
                		if( x_dif>0 && y_dif>0 ) angle_dif = (Math.atan(y_dif/x_dif)) - sh;
                		if( x_dif<0 && y_dif>0 ) angle_dif = (Math.PI + Math.atan(y_dif/x_dif)) - sh;
                		if( x_dif<0 && y_dif<0 ) angle_dif = (Math.PI + Math.atan(y_dif/x_dif)) - sh;
                		if( x_dif>0 && y_dif<0 ) angle_dif = (2*Math.PI + Math.atan(y_dif/x_dif)) - sh;
                		if( angle_dif > Math.PI ) angle_dif = angle_dif - 2*Math.PI;
                		if( angle_dif < -Math.PI ) angle_dif = angle_dif + 2*Math.PI;
                		if( -Math.PI/2<angle_dif && angle_dif<Math.PI/2 && point_dif*Math.cos(angle_dif)<len ) {
                    		line_dif = point_dif * Math.sin(angle_dif);
                    		for (int j = 0; j < ldwt.size(); j++) {
                    			if(ldwt.get(j)[0] > 0) {
                    				if(ldwt.get(j)[1]>line_dif && line_dif>ldwt.get(j-1)[1]) {
                    					situation += "RoadID : " + road_id+"\n";
                    					situation += "LaneID : " + (int)ldwt.get(j)[0]+"\n";
                    					situation += "Situation : " + (String)row.get("situation")+"\n";
                    					situation += "XML\n" + (String)row.get("link")+"\n" + (String)row.get("planview")+"\n" + (String)row.get("elevation")+"\n" + (String)row.get("lanes");
                    				}
                    			}
                    			if(ldwt.get(j)[0] < 0) {
                    				if(ldwt.get(j)[1]<line_dif && line_dif<ldwt.get(j+1)[1]) {
                    					situation += "RoadID : " + road_id+"\n";
                    					situation += "LaneID : " + (int)ldwt.get(j)[0]+"\n";
                    					situation += "Situation : " + (String)row.get("situation")+"\n";
                    					situation += "XML\n" + (String)row.get("link")+"\n" + (String)row.get("planview")+"\n" + (String)row.get("elevation")+"\n" + (String)row.get("lanes");
                    				}
                    			}
                    		}
                		}
                	}
                	
                	// 지오메트리 아크경우 포함확인
                	if(cur != 0.0) {
                		double center_a = 0.0;
                		double center_x = 0.0;
                		double center_y = 0.0;
                		double arc_r = Math.abs(1/cur);
                		double arc_sa = 0.0;
                		double arc_ea = 0.0;
                		if( cur > 0 ) center_a = sh + Math.PI/2;
                		if( cur < 0 ) center_a = sh - Math.PI/2;
                		if( center_a > 2*Math.PI ) center_a = center_a - 2*Math.PI;
                		if( center_a < 0 ) center_a = center_a + 2*Math.PI;
                		center_x = sx + arc_r*Math.cos(center_a); 
                		center_y = sy + arc_r*Math.sin(center_a);
                		if( cur > 0 ) {
                			if( (sx-center_x)>0 && (sy-center_y)>0 ) arc_sa = Math.atan((sy-center_y)/(sx-center_x));
                    		if( (sx-center_x)<0 && (sy-center_y)>0 ) arc_sa = Math.PI + Math.atan((sy-center_y)/(sx-center_x));
                    		if( (sx-center_x)<0 && (sy-center_y)<0 ) arc_sa = Math.PI + Math.atan((sy-center_y)/(sx-center_x));
                    		if( (sx-center_x)>0 && (sy-center_y)<0 ) arc_sa = 2*Math.PI + Math.atan((sy-center_y)/(sx-center_x));
                    		arc_ea = arc_sa + len/arc_r;
                    		if( arc_ea > 2*Math.PI ) arc_ea = arc_ea - 2*Math.PI;
                		}
                		if( cur < 0 ) {
                			if( (sx-center_x)>0 && (sy-center_y)>0 ) arc_ea = Math.atan((sy-center_y)/(sx-center_x));
                    		if( (sx-center_x)<0 && (sy-center_y)>0 ) arc_ea = Math.PI + Math.atan((sy-center_y)/(sx-center_x));
                    		if( (sx-center_x)<0 && (sy-center_y)<0 ) arc_ea = Math.PI + Math.atan((sy-center_y)/(sx-center_x));
                    		if( (sx-center_x)>0 && (sy-center_y)<0 ) arc_ea = 2*Math.PI + Math.atan((sy-center_y)/(sx-center_x));
                    		arc_sa = arc_ea - len/arc_r;
                    		if( arc_sa < 0 ) arc_sa = arc_sa + 2*Math.PI;
                		}
                		double x_dif = x - center_x;
                		double y_dif = y - center_y;
                		double point_dif = Math.sqrt(Math.pow(x_dif, 2) + Math.pow(y_dif, 2));
                		double angle_dif = 0.0;
                		if( x_dif>0 && y_dif>0 ) angle_dif = (Math.atan(y_dif/x_dif));
                		if( x_dif<0 && y_dif>0 ) angle_dif = (Math.PI + Math.atan(y_dif/x_dif));
                		if( x_dif<0 && y_dif<0 ) angle_dif = (Math.PI + Math.atan(y_dif/x_dif));
                		if( x_dif>0 && y_dif<0 ) angle_dif = (2*Math.PI + Math.atan(y_dif/x_dif));
                		if( (arc_sa<arc_ea && arc_sa<angle_dif && angle_dif<arc_ea) || (arc_sa>arc_ea && (arc_sa<angle_dif || angle_dif<arc_ea)) ) {
                			for (int j = 0; j < ldwt.size(); j++) {
                				if(ldwt.get(j)[0]>0 && cur>0) {
                					if(arc_r-ldwt.get(j)[1]<point_dif && point_dif<arc_r-ldwt.get(j-1)[1]) {
                    					situation += "RoadID : " + road_id+"\n";
                    					situation += "LaneID : " + (int)ldwt.get(j)[0]+"\n";
                    					situation += "Situation : " + (String)row.get("situation")+"\n";
                    					situation += "XML\n" + (String)row.get("link")+"\n" + (String)row.get("planview")+"\n" + (String)row.get("elevation")+"\n" + (String)row.get("lanes");
                    				}
                				}
                				if(ldwt.get(j)[0]<0 && cur>0) {
                    				if(arc_r-ldwt.get(j)[1]>point_dif && point_dif>arc_r-ldwt.get(j+1)[1]) {
                    					situation += "RoadID : " + road_id+"\n";
                    					situation += "LaneID : " + (int)ldwt.get(j)[0]+"\n";
                    					situation += "Situation : " + (String)row.get("situation")+"\n";
                    					situation += "XML\n" + (String)row.get("link")+"\n" + (String)row.get("planview")+"\n" + (String)row.get("elevation")+"\n" + (String)row.get("lanes");
                    				}
                    			}
                    			if(ldwt.get(j)[0]>0 && cur<0) {
                    				if(arc_r+ldwt.get(j)[1]>point_dif && point_dif>arc_r+ldwt.get(j-1)[1]) {
                    					situation += "RoadID : " + road_id+"\n";
                    					situation += "LaneID : " + (int)ldwt.get(j)[0]+"\n";
                    					situation += "Situation : " + (String)row.get("situation")+"\n";
                    					situation += "XML\n" + (String)row.get("link")+"\n" + (String)row.get("planview")+"\n" + (String)row.get("elevation")+"\n" + (String)row.get("lanes");
                    				}
                    			}
                    			if(ldwt.get(j)[0]<0 && cur<0) {
                    				if(arc_r+ldwt.get(j)[1]<point_dif && point_dif<arc_r+ldwt.get(j+1)[1]) {
                    					situation += "RoadID : " + road_id+"\n";
                    					situation += "LaneID : " + (int)ldwt.get(j)[0]+"\n";
                    					situation += "Situation : " + (String)row.get("situation")+"\n";
                    					situation += "XML\n" + (String)row.get("link")+"\n" + (String)row.get("planview")+"\n" + (String)row.get("elevation")+"\n" + (String)row.get("lanes");
                    				}
                    			}
                    		}
                		}
                	}
                	
                }
                
        	}
        	catch (Exception e) {
    	        e.printStackTrace();
    		}
        	
        }
        
        // return
        return situation;
        
	}

}
