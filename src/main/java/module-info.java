module com.example.demo1 {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.sql;

        opens com.intissar.examen.Controles to javafx.fxml;
        exports com.intissar.examen;
        exports com.intissar.examen.Modelo;
        exports com.intissar.examen.DAO;
    exports com.intissar.examen.Conexion;
    exports com.intissar.examen.Controles;




        }
