<!doctype html>
<html>

<head>
    <title>Info Windows</title>
    <script>
        async function initMap() {
            const contentString = `
          <div>
            <h1>Uluru</h1>
            <div>
              <p>
                <b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large
                sandstone rock formation in the southern part of the
                Northern Territory, central Australia. It lies 335&#160;km (208&#160;mi)
                south west of the nearest large town, Alice Springs; 450&#160;km
                (280&#160;mi) by road. Kata Tjuta and Uluru are the two major
                features of the Uluru - Kata Tjuta National Park. Uluru is
                sacred to the Pitjantjatjara and Yankunytjatjara, the
                Aboriginal people of the area. It has many springs, waterholes,
                rock caves and ancient paintings. Uluru is listed as a World
                Heritage Site.
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

        #map {
            height: 50%;
        }

        #hello {
            height: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 48px;
            font-weight: bold;
        }
    </style>
</head>

<body>
    <div id="map">
        <gmp-map center="-25.363, 131.044" zoom="4" map-id="DEMO_MAP_ID">
            <gmp-advanced-marker position="-25.363, 131.044" title="Uluru" gmp-clickable></gmp-advanced-marker>
        </gmp-map>
    </div>
    <div id="hello">
        HELLO
    </div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCIFlQg-guItGQsdwDFu-l3M-ZeCujCM0Y&callback=initMap&libraries=marker&v=beta&solution_channel=GMP_CCS_infowindows_v2" defer></script>
</body>

</html>
