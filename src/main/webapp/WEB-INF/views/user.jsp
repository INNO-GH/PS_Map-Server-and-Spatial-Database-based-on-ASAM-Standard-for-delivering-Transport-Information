<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <meta charset="UTF-8">
    <title>title</title>
    <script>
    	let map;
		let marker;    
		function initMap() {
  			map = new google.maps.Map(document.getElementById("map"), {
    			zoom: 8,
    			center: { lat: -34.397, lng: 150.644 },
    			mapTypeControl: false,
  			});
  			marker = new google.maps.Marker({
    			map,
  			});
  			map.addListener("click", (e) => {
    			marker.setMap(null);
    	    	marker = new google.maps.Marker({
    	        	position: e.latLng,
    	        	map: map,
    	    	});
    	    	document.getElementsByName("coordinate")[0].value = e.latLng.lat() + ", " + e.latLng.lng();
  			});
		}
		window.initMap = initMap;
    </script>
    <style>
        html,
        body {
            height: 100%;
            margin: 0;
            display: flex;
            flex-direction: column;
        }
        #user {
        	height: 7.5%;
        	display: flex;
            justify-content: center;
            align-items: center;
        }
        #collector {
        	height: 7.5%;
        	display: flex;
            justify-content: center;
            align-items: center;	
        }
        #map {
            height: 70%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        #receive {
        	height: 15%;
        	display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>

<body>
	<div id="user">
		<h3>User</h3>
	</div>
	<div id="collector">
		<form action="/map/collector">  
			<input type="submit" value="Collector" >
		</form>
	</div>
    <div id="map">
        <gmp-map center="-25.363, 131.044" zoom="4" map-id="DEMO_MAP_ID">
            <gmp-advanced-marker position="-25.363, 131.044" title="Uluru" gmp-clickable></gmp-advanced-marker>
        </gmp-map>
    </div>
    <div id="receive">
		<form action="/map/receive">
			<input type="text" name="coordinate" >  
			<input type="text" name="situation" >  
			<input type="submit" value="Opendrive" >
		</form>
	</div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCIFlQg-guItGQsdwDFu-l3M-ZeCujCM0Y&callback=initMap&v=weekly&solution_channel=GMP_CCS_geocodingservice_v2" defer></script>
</body>

</html>
