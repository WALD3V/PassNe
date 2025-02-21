/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pasantias;

/**
 *
 * @author PC
 */
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String empCodigo = "EMP001"; // Código del empleado que quieres consultar
        String fileName = "RolGeneral_" + empCodigo + ".pdf"; // Nombre del archivo PDF

        try {
            // Conexión a la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/prueba", "root", "040902");

            // Extraer detalles del rol
            RolGeneral rol = ExtractDetails.getRolByEmpleado(connection, empCodigo);

            // Generar PDF
            if (rol != null) {
                GeneratePDF.generate(rol, fileName);
                System.out.println("PDF generado exitosamente: " + fileName);
            } else {
                System.out.println("Empleado no encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
