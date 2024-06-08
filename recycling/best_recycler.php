    <?php
    // Στοιχεία σύνδεσης με τη βάση δεδομένων
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "recycling";

    // Δημιουργία σύνδεσης
    $conn = new mysqli($servername, $username, $password, $dbname);

    // Έλεγχος σύνδεσης
    if ($conn->connect_error) {
        die("Σφάλμα σύνδεσης: " . $conn->connect_error);
    }

    $sql = "SELECT username, plastic FROM users WHERE CAST(plastic AS SIGNED) = (SELECT MAX(CAST(plastic AS SIGNED)) FROM users) LIMIT 1";
    $result = $conn->query($sql);

    $sql1 = "SELECT username, glass FROM users WHERE CAST(glass AS SIGNED) = (SELECT MAX(CAST(glass AS SIGNED)) FROM users) LIMIT 1";
    $result1 = $conn->query($sql1);

    $sql2 = "SELECT username, aluminium FROM users WHERE CAST(aluminium AS SIGNED) = (SELECT MAX(CAST(aluminium AS SIGNED)) FROM users) LIMIT 1";
    $result2 = $conn->query($sql2);

    $sql3 = "SELECT username, paper FROM users WHERE CAST(paper AS SIGNED) = (SELECT MAX(CAST(paper AS SIGNED)) FROM users) LIMIT 1";
    $result3 = $conn->query($sql3);

    $sql4 = "SELECT username, achievements FROM users WHERE CAST(achievements AS SIGNED) = (SELECT MAX(CAST(achievements AS SIGNED)) FROM users WHERE username <> 'admin') AND username <> 'admin' LIMIT 1";
    $result4 = $conn->query($sql4);

    $sql5 = "SELECT username, total_points FROM users WHERE CAST(total_points AS SIGNED) = (SELECT MAX(CAST(total_points AS SIGNED)) FROM users) LIMIT 1";
    $result5 = $conn->query($sql5);

    if ($result->num_rows > 0 && $result1->num_rows > 0 && $result2->num_rows > 0 && $result3->num_rows > 0 && $result4->num_rows > 0 && $result5->num_rows > 0) {
        // Εξαγωγή του ονόματος
        $row = $result->fetch_assoc();
        $row1 = $result1->fetch_assoc();
        $row2 = $result2->fetch_assoc();
        $row3 = $result3->fetch_assoc();
        $row4 = $result4->fetch_assoc();
        $row5 = $result5->fetch_assoc();
        echo json_encode(array("status" => "success","plastic" => $row["username"], "plastic_num" => $row["plastic"], "glass" => $row1["username"], "glass_num" => $row1["glass"], "aluminium" => $row2['username'], "aluminium_num" => $row2["aluminium"], "paper" => $row3['username'], "paper_num" => $row3["paper"], "achievements" => $row4['username'], "achievements_num" => $row4["achievements"], "total points" => $row5['username'], "total_points_num" => $row5["total_points"]));
    }else {
        echo json_encode(array("status" => "error", "message" => "There are no results"));
    }

    // Κλείσιμο σύνδεσης
    $conn->close();
    ?>

