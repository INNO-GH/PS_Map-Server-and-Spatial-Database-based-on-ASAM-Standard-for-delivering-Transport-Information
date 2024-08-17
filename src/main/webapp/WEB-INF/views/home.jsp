<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
              <p>
                Attribution: Uluru,
                <a href="https://en.wikipedia.org/w/index.php?title=Uluru&oldid=297882194">
                  https://en.wikipedia.org/w/index.php?title=Uluru
                </a>
                (last visited June 22, 2009).
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

        #top {
            height: 15%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0;
        }

        .left-half,
        .right-half {
            width: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        #abc-button,
        #def-button {
            font-size: 24px;
            font-weight: bold;
            padding: 10px 20px;
            cursor: pointer;
        }

        #map {
            height: 70%;
        }

        #bottom {
            height: 15%;
            display: flex;
            padding: 0;
        }

        .bottom-section {
            width: 25%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        #xml-input {
            font-size: 16px;
        }

        #coordinate-input,
        #status-input {
            font-size: 24px;
            padding: 5px;
            width: 180px;
        }

        #kkk-button {
            font-size: 24px;
            font-weight: bold;
            padding: 10px 20px;
            cursor: pointer;
        }
    </style>
</head>

<body>
    <div id="top">
        <div class="left-half">
            <button id="abc-button">ABC</button>
        </div>
        <div class="right-half">
            <button id="def-button">DEF</button>
        </div>
    </div>
    <div id="map">
        <gmp-map center="-25.363, 131.044" zoom="4" map-id="DEMO_MAP_ID">
            <gmp-advanced-marker position="-25.363, 131.044" title="Uluru" gmp-clickable></gmp-advanced-marker>
        </gmp-map>
    </div>
    <div id="bottom">
        <div class="bottom-section">
            <input id="xml-input" type="file" accept=".xml">
        </div>
        <div class="bottom-section">
            <input id="coordinate-input" type="text" placeholder="좌표 입력">
        </div>
        <div class="bottom-section">
            <input id="status-input" type="text" placeholder="상황 입력">
        </div>
        <div class="bottom-section">
            <button id="kkk-button">KKK</button>
        </div>
    </div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCIFlQg-guItGQsdwDFu-l3M-ZeCujCM0Y&callback=initMap&libraries=marker&v=beta&solution_channel=GMP_CCS_infowindows_v2" defer></script>
</body>

</html>
