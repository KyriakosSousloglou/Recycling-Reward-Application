<?php
// Στοιχεία σύνδεσης στη βάση δεδομένων
$servername = "localhost";
$username = "root";
$password = "";
$database = "recycling";

// Δημιουργία σύνδεσης
$conn = new mysqli($servername, $username, $password, $database);

// Έλεγχος σύνδεσης
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Έλεγχος αν το αίτημα είναι POST για να ενημερώσουμε τα δεδομένα
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Έλεγχος αν έχουμε λάβει το username
    if (isset($_POST['username'])) {
        $username = $_POST['username'];
    } else {
        die("Username is required");
    }

    // Λήψη των στοιχείων από τη φόρμα
    if (isset($_POST['plastic'], $_POST['glass'], $_POST['aluminium'], $_POST['paper'], $_POST['general_waste'])) {
        $plastic = $_POST['plastic'];
        $glass = $_POST['glass'];
        $aluminium = $_POST['aluminium'];
        $paper = $_POST['paper'];
        $general_waste = $_POST['general_waste'];

        // Επιλογή των υφιστάμενων τιμών για το συγκεκριμένο όνομα χρήστη
        $select_sql = "SELECT plastic, glass, aluminium, paper, general_waste, total_points, points_left, achievements FROM users WHERE username='$username'";
        $result = $conn->query($select_sql);


        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            //υπολογισμός πόντων για κάθε υλικό
            if($plastic >'0' && $plastic<='30')
                $plastic_points = '3';
            elseif($plastic >'30' && $plastic<='70')
                $plastic_points = '6';
            elseif($plastic>'70')
                $plastic_points = '10';
            else{
                $plastic_points = '0';
                $plastic = '0';
            }
                
            
            if($glass>'0' && $glass<='30')
                $glass_points = '3';
            elseif($glass >'30' && $glass<='70')
                $glass_points = '6';
            elseif($glass>'70')
                $glass_points = '10';
            else{
                    $glass_points = '0';
                    $glass = '0';
            }
        
            if($aluminium >'0' && $aluminium<='30')
                $aluminium_points = '3';
            elseif($aluminium >'30' && $aluminium<='70')
                $aluminium_points = '6';
            elseif($aluminium>'70')
                $aluminium_points = '10';
            else{
                    $aluminium_points = '0';
                    $aluminium = '0';
            }
                
            if($paper >'0' && $paper<='30')
                $paper_points = '3';
            elseif($paper >'30' && $paper<='70')
                $paper_points = '6';
            elseif($paper>'70')
                $paper_points = '10';
            else{
                    $paper_points = '0';
                    $paper = '0';
            }
            
            if($general_waste >'0' && $general_waste<='30')
                $general_waste_points = '3';
            elseif($general_waste >'30' && $general_waste<='70')
                $general_waste_points = '6';
            elseif($general_waste>'70')
                $general_waste_points = '10';
            else{
                    $general_waste_points = '0';
                    $general_waste = '0';
            }

            // Προσθήκη των νέων τιμών στις υπάρχουσες
            $plastic += $row['plastic'];
            $glass += $row['glass'];
            $aluminium += $row['aluminium'];
            $paper += $row['paper'];
            $general_waste += $row['general_waste'];
            $achievements = $row['achievements'];
            $total_points = $plastic_points + $glass_points + $aluminium_points + $paper_points + $general_waste_points;
            $total_points_by_time = $plastic_points + $glass_points + $aluminium_points + $paper_points + $general_waste_points;
            $total_points += $row['total_points'];
            if($total_points <= '500')
                $points_left = '500' - $total_points;
            else{ 
                $points_left = '500' - ($total_points % '500');
                $achievemets += $row['achievements'];
            }
                            
            // Ενημέρωση των τιμών στη βάση δεδομένων
            $update_sql = "UPDATE users SET plastic=$plastic, glass=$glass, aluminium=$aluminium, paper=$paper, general_waste=$general_waste, total_points=$total_points, points_left = $points_left, achievements = $achievements WHERE username='$username'";

            if ($conn->query($update_sql) === TRUE) {
                echo "Materials were updated successfully.   You collected " . $total_points_by_time . " points!";
            } else {
                echo "Error updating record: " . $conn->error;
            }
        } else {
            echo "No user found with the provided username";
        }

    } else {
        echo "All fields are required";
    }
}

// Κλείσιμο σύνδεσης
$conn->close();
?>
