/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PassJasperReports;

/**
 *
 * @author ventas 1
 */


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import pasantias.ExtractDetails;
import pasantias.RolGeneral;
public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3308/prueba";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "040902";

    public static void main(String[] args) {
        String empCodigo = "EMP002"; // Código del empleado a consultar
        String fileName = "RolGeneral_" + empCodigo + ".pdf"; // Nombre del archivo PDF
        String reportPath = "Blank_A4.jrxml"; // Archivo de JasperReports

        // Conexión con try-with-resources para garantizar cierre automático
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("✅ Conexión a la base de datos exitosa.");

            // Extraer detalles del rol
            RolGeneral rol = ExtractDetails.getRolByEmpleado(connection, empCodigo);

            // Generar PDF si el empleado existe
            if (rol != null) {
                generateJasperReport(rol, fileName, reportPath);
                System.out.println("📄 PDF generado exitosamente: " + fileName);
            } else {
                System.out.println("⚠️ Empleado con código " + empCodigo + " no encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void generateJasperReport(RolGeneral rol, String outputFileName, String reportPath) {
        try {
            // Compilar el reporte JRXML a un archivo Jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Convertir el objeto a una lista para JasperReports
            List<RolGeneral> dataList = Collections.singletonList(rol);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

            // Mapa de parámetros (puedes agregar más si es necesario)
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", "Reporte de Rol General");

            // Llenar el reporte con los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // Exportar el reporte a PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFileName);

            System.out.println("✅ Reporte generado correctamente: " + outputFileName);
        } catch (JRException e) {
            System.err.println("❌ Error al generar el reporte Jasper: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
