<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная страница</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
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

        form {
            margin: 20px 0;
        }

        label {
            display: block;
            margin: 10px 0 5px;
            font-weight: bold;
        }

        input[type="text"], input[type="number"], input[type="file"], select {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 15px;
        }

        button {
            background-color: #000;
            color: #fff;
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #333;
        }

        .logout-link {
            margin-top: 20px;
            display: inline-block;
            text-decoration: none;
            color: #000;
            font-size: 16px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Здравствуйте, <%= request.getSession().getAttribute("user") != null ? request.getSession().getAttribute("user") : "Гость" %></h1>
    <h2>Online Аптека</h2>
    <form id="orderForm" action="process_order.php" method="POST">
        <label for="medicine">Выберите препарат:</label>
        <select id="medicine" name="medicine" required>
            <option value="1">Парацетамол</option>
            <option value="2">Амоксициллин</option>
            <!-- Динамически добавляемые опции с серверной стороны -->
        </select>

        <label for="dosage">Дозировка:</label>
        <input type="text" id="dosage" name="dosage" placeholder="Например, 500 мг" required>

        <label for="quantity">Количество:</label>
        <input type="number" id="quantity" name="quantity" min="1" required>

        <label for="prescription">Электронный рецепт:</label>
        <input type="file" id="prescription" name="prescription">

        <button type="submit">Оформить заказ</button>
    </form>
    <a href="controller?command=logout" class="logout-link">Выход</a>
</div>
</body>
</html>