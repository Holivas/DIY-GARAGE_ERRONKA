<html>

<body style='margin:auto;width:600px;margin-top:100px;'>
    <div>
        <?php
        include("test_connect_db.php");
        $link = connectDataBase();
        $emaitza = mysqli_query($link, "select * from customer");
        ?>
        <TABLE BORDER=1 CELLSPACING=1 CELLPADDING=1>
            <Tr>
                <Td>Username</Td>
                <Td>Name</Td>
            </Tr>
            <?php
            while ($erregistroa = mysqli_fetch_array($emaitza)) {
                printf("<tr><td>%s</td><td>%s</td></tr>", $erregistroa[0], $erregistroa[1]);
            }
            mysqli_free_result($emaitza);
            mysqli_close($link);
            ?>
        </table>
    </div>
    <br />
    <a href="index.php">Back to menu</a>
</body>

</html>