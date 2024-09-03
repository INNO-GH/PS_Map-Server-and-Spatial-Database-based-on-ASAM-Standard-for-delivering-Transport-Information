<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <meta charset="UTF-8">
    <title>title</title>
    <style>
        html,
        body {
            height: 100%;
            margin: 0;
            display: flex;
            flex-direction: column;
        }
        #collector {
        	height: 50%;
        	display: flex;
            justify-content: center;
            align-items: center;
            background-color: skyblue;
        }
        #user {
        	height: 50%;
        	display: flex;
            justify-content: center;
            align-items: center;
            background-color: lightgreen;	
        }
    </style>
</head>

<body>
	<div id="collector">
		<form action="/map/collector">  
			<input type="submit" value="Collector" >
		</form>
	</div>
	<div id="user">
		<form action="/map/user">  
			<input type="submit" value="User" >
		</form>
	</div>
</body>

</html>
