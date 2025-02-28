package pasantias;

/**
 * Clase secundaria para la generación de PDFs con detalles del rol de todos los empleados.
 * Se conecta a una base de datos, extrae los detalles del rol de cada empleado
 * y genera un archivo PDF con esa información.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        try {
            // Conexión a la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/prueba", "root", "040902");

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

    /**
     * Método para obtener todos los códigos de empleados desde la base de datos.
     * 
     * @param connection Conexión a la base de datos.
     * @return Lista de códigos de empleados.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
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