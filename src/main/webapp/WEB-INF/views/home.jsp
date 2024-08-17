<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Info Windows</title>
    <script>
        async function initMap() {
            const contentString = `
          <div>
            <h1>Uluru</h1>
            <div>
              <p>
                <b>Uluru</b>, also
              </p>
            </div>
          </div>`;
            const infoWindow = new google.maps.InfoWindow({
                content: contentString,
                ariaLabel: "Uluru",
            });
            const marker = document.querySelector('gmp-advanced-marker');
            marker.addEventListener('gmp-click', () => {
                infoWindow.open({ anchor: marker });
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
        }
        #user {
        	height: 7.5%;	
        }
        #map {
            height: 70%;
        }
        #send {
        	height: 15%;
        }
    </style>
</head>

<body>
	<div id="collector">
		<form action="/collector">  
			<input type="submit" value="Collector" >
		</form>
	</div>
	<div id="user">
		<form action="/user">  
			<input type="submit" value="User" >
		</form>
	</div>
    <div id="map">
        <gmp-map center="-25.363, 131.044" zoom="4" map-id="DEMO_MAP_ID">
            <gmp-advanced-marker position="-25.363, 131.044" title="Uluru" gmp-clickable></gmp-advanced-marker>
        </gmp-map>
    </div>
    <div id="send">
		<form action="/send">
			<input type="file" name="xodr" accept=".xodr" >
			<input type="text" name="coordinate" >  
			<input type="text" name="situation" ><br />  
			<input type="submit" value="Send" >  
		</form>
	</div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCIFlQg-guItGQsdwDFu-l3M-ZeCujCM0Y&callback=initMap&libraries=marker&v=beta&solution_channel=GMP_CCS_infowindows_v2" defer></script>
</body>

</html>
