<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Main</title>
</head>
<body>
<h1>Hello, <%= request.getSession().getAttribute("user") != null ? request.getSession().getAttribute("user") : "Гость" %></h1>
<h1>Online Аптека</h1>
<form id="orderForm" action="process_order.php" method="POST">
    <label for="medicine">Выберите препарат:</label>
    <select id="medicine" name="medicine" required>
        <option value="1">Парацетамол</option>
        <option value="2">Амоксициллин</option>
        <!-- Динамически добавляемые опции с серверной стороны -->
    </select>
    <br><br>

    <label for="dosage">Дозировка:</label>
    <input type="text" id="dosage" name="dosage" placeholder="Например, 500 мг" required>
    <br><br>

    <label for="quantity">Количество:</label>
    <input type="number" id="quantity" name="quantity" min="1" required>
    <br><br>

    <label for="prescription">Электронный рецепт:</label>
    <input type="file" id="prescription" name="prescription">
    <br><br>

    <button type="submit">Оформить заказ</button>
</form>
<a href="controller?command=logout">Logout</a>
</body>
</html>