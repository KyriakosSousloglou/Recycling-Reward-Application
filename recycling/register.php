<?php
// Στοιχεία σύνδεσης στη βάση δεδομένων
$servername = "localhost"; // Αν η βάση δεδομένων είναι στον ίδιο server
$username = "root"; // Όνομα χρήστη της βάσης
$password = ""; // Κωδικός χρήστη της βάσης
$database = "recycling"; // Όνομα της βάσης δεδομένων

// Δημιουργία σύνδεσης
$conn = new mysqli($servername, $username, $password, $database);

// Έλεγχος σύνδεσης
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Αν το αίτημα είναι POST (δηλαδή υποβλήθηκε ένα form)
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Λήψη των στοιχείων από τη φόρμα
    $name = $_POST['name'];
    $surname = $_POST['surname'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];

    // Εισαγωγή των δεδομένων στη βάση
    $sql = "INSERT INTO users (name,surname,username, password, email, phone, plastic, glass, aluminium, paper, general_waste, total_points, points_left, achievements) VALUES ('$name','$surname','$username', '$password', '$email', '$phone', '0', '0', '0','0','0', '0','500', '0')";

    if ($conn->query($sql) === TRUE) {
        echo "Your account has been created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}

// Κλείσιμο σύνδεσης
$conn->close();
?>
