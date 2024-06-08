<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "recycling";

// Δημιουργία σύνδεσης
$conn = new mysqli($servername, $username, $password, $dbname);

// Έλεγχος σύνδεσης
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Έλεγχος αν το αίτημα είναι POST για να λάβουμε τα δεδομένα
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['username'])) {
        $user = $_POST['username'];

        // SQL για να επιλέξουμε το total_points του χρήστη
        $sql = "SELECT total_points, points_left, achievements FROM users WHERE username='$user'";
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo json_encode(array("status" => "success", "total_points" => $row["total_points"], "points_left" => $row["points_left"], "achievements" => $row["achievements"]));
        } else {
            echo json_encode(array("status" => "error", "message" => "No user found with the provided username"));
        }
    } else {
        echo json_encode(array("status" => "error", "message" => "Username is required"));
    }
}

$conn->close();
?>
