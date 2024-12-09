<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JSP - Hello World</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f4f4f4;
            margin: 0;
        }

        .container {
            text-align: center;
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        h1 {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
        }

        .form-group {
            margin-bottom: 15px;
        }

        input[type="text"], input[type="hidden"] {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            transition: border-color 0.3s ease;
        }

        input[type="text"]:hover, input[type="text"]:focus {
            border-color: #333;
        }

        input[type="submit"] {
            background-color: #000;
            color: #fff;
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #333;
        }

        a {
            color: #000;
            text-decoration: none;
            margin: 10px 0;
            display: inline-block;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<%--<div class="container">--%>
<%--    <h1><%= "Hello World!" %></h1>--%>
<%--    <a href="hello-servlet">Hello Servlet</a>--%>
<%--    <form action="hello-servlet" method="get">--%>
<%--        <div class="form-group">--%>
<%--            <input type="text" name="number" placeholder="Enter number" value=""/>--%>
<%--        </div>--%>
<%--        <div class="form-group">--%>
<%--            <input type="hidden" name="time" value=""/>--%>
<%--        </div>--%>
<%--        <input type="submit" name="submit" value="Send"/>--%>
<%--    </form>--%>
<%--</div>--%>
<jsp:forward page="jsp/login.jsp"/>
</body>
</html>
