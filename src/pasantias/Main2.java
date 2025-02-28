
package pasantias;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        try {
            // Conexión a la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/prueba", "root", "04090202");

            // Obtener todos los códigos de empleados
            List<String> empCodigos = getAllEmpCodigos(connection);

            // Generar un PDF para cada empleado
            for (String empCodigo : empCodigos) {
                String fileName = "RolGeneral_" + empCodigo + ".pdf"; // Nombre del archivo PDF

                // Extraer detalles del rol
                RolGeneral rol = ExtractDetails.getRolByEmpleado(connection, empCodigo);

                // Generar PDF
                if (rol != null) {
                    GeneratePDF.generate(rol, fileName);
                    System.out.println("PDF generado exitosamente: " + fileName);
                } else {
                    System.out.println("Empleado no encontrado: " + empCodigo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getAllEmpCodigos(Connection connection) throws SQLException {
        List<String> empCodigos = new ArrayList<>();
        String query = "SELECT EMP_CODIGO FROM rolgeneral_cab";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                empCodigos.add(rs.getString("EMP_CODIGO"));
            }
        }

        return empCodigos;
    }
}