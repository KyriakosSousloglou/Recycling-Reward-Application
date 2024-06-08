<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "recycling";
$name = "name";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user = $_POST['username'];
    $pass = $_POST['password'];

    $sql = "SELECT name,surname FROM users WHERE username='$user' AND password='$pass'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0 && !($user == "admin")) {
        $row = $result->fetch_assoc();
        echo json_encode(array("status" => "success", "message" => "Login successful", "surname" => $row["surname"],"name" => $row["name"]));
    } else {
        echo json_encode(array("status" => "error", "message" => "Invalid username or password"));
    }
}

$conn->close();
?>



