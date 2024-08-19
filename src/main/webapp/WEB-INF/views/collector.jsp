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
        #collector {
        	height: 7.5%;
        	display: flex;
            justify-content: center;
            align-items: center;
        }
        #user {
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
        #send {
        	height: 15%;
        	display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>

<body>
	<div id="collector">
		<h3>Collector</h3>
	</div>
	<div id="user">
		<form action="/map/user">  
			<input type="submit" value="User" >
		</form>
	</div>
    <div id="map">
    </div>
    <div id="send">
		<form action="/map/send">
			<input type="file" name="opendrive" accept=".xodr" >
			<input type="text" name="coordinate" >  
			<input type="text" name="situation" >
			<input type="submit" value="Send" >
		</form>
	</div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCIFlQg-guItGQsdwDFu-l3M-ZeCujCM0Y&callback=initMap&v=weekly&solution_channel=GMP_CCS_geocodingservice_v2" defer></script>
</body>

</html>
