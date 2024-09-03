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
    			zoom: 18,
    			center: { lat: 49.0, lng: 8.0 },
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
        #receive {
        	height: 50%;
        	display: flex;
            justify-content: center;
            align-items: center;
            background-color: skyblue;
        }
        #receive form {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
            width: 100%;
        }
        #receive textarea {
            width: 30%;
            height: 400px;
            padding: 10px;
            box-sizing: border-box;
        }
        #map {
            height: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>

<body>
	<div id="receive">
		<form action="/map/receive" method="post" enctype="multipart/form-data">
			<select name="opendrive">
				<option value="no">No</option>
				<option value="yes">Yes</option>
		    </select>
		    <textarea name="coordinate" placeholder="Coordinate"></textarea>
		    <textarea name="situation" placeholder="Situation">${situation}</textarea>
			<input type="submit" value="Receive" >
		</form>
	</div>
    <div id="map">
    </div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCIFlQg-guItGQsdwDFu-l3M-ZeCujCM0Y&callback=initMap&v=weekly&solution_channel=GMP_CCS_geocodingservice_v2" defer></script>
</body>

</html>
