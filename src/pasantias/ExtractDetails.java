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
import java.util.*;

public class ExtractDetails {

    public static RolGeneral getRolByEmpleado(Connection connection, String empCodigo) throws SQLException {
        List<DetalleRol> ingresos = new ArrayList<>();
        List<DetalleRol> egresos = new ArrayList<>();
        List<DetalleRol> descuentos = new ArrayList<>(); // Nueva lista para descuentos
        RolGeneral rol = null;

        String queryEmpleado = "SELECT * FROM rolgeneral_cab WHERE EMP_CODIGO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(queryEmpleado)) {
            stmt.setString(1, empCodigo);
            ResultSet rsEmpleado = stmt.executeQuery();

            if (rsEmpleado.next()) {
                String periodoAnio = rsEmpleado.getString("Periodo_anio");
                int periodoMes = rsEmpleado.getInt("Periodo_Mes");
                java.sql.Date fechaCorte = rsEmpleado.getDate("Fecha_corte");
                String codigo = rsEmpleado.getString("EMP_CODIGO");
                String nombres = rsEmpleado.getString("EMP_NOMBRES");
                String apellidos = rsEmpleado.getString("EMP_APELLIDOS");
                String funcion = rsEmpleado.getString("EMP_FUNCION");

                // Consulta para obtener detalles de ingresos, egresos y descuentos
                String queryDetalles = "SELECT * FROM rolgeneral_det WHERE EMP_CODIGO = ?";
                try (PreparedStatement stmtDetalles = connection.prepareStatement(queryDetalles)) {
                    stmtDetalles.setString(1, codigo);
                    ResultSet rsDetalles = stmtDetalles.executeQuery();

                    while (rsDetalles.next()) {
                        String tipo = rsDetalles.getString("TIPO");
                        String nombre = rsDetalles.getString("NOMBRE");
                        double valor = rsDetalles.getDouble("VALOR");
                        double saldo = rsDetalles.getDouble("SALDO");

                        DetalleRol detalle = new DetalleRol(codigo, nombre, tipo, "Referencia", valor, saldo);

                        if ("I".equals(tipo)) {
                            ingresos.add(detalle);
                        } else if ("E".equals(tipo)) {
                            egresos.add(detalle);
                        } else if ("D".equals(tipo)) {
                            descuentos.add(detalle);
                        }
                    }
                }

                String fechaCorteStr = fechaCorte.toString();

                // Crear el objeto RolGeneral con todos los detalles
                rol = new RolGeneral(periodoAnio, periodoMes, fechaCorteStr, codigo, nombres, apellidos, funcion, ingresos, egresos, descuentos);
            }
        }
        return rol; // Se devuelve el rol procesado o null si no se encontró el empleado
    }

}
