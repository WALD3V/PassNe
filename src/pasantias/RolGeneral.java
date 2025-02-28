package pasantias;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Clase que representa los detalles del rol de un empleado.
 */
public class RolGeneral {

    private String periodoAnio;
    private int periodoMes;
    private String fechaCorte;
    private String empCodigo;
    private String empNombres;
    private String empApellidos;
    private String empFuncion;

    private List<DetalleRol> ingresos;
    private List<DetalleRol> egresos;
    private List<DetalleRol> descuentos; // Nueva lista para descuentos

    // Constructor con todos los parámetros
    public RolGeneral(String periodoAnio, int periodoMes, String fechaCorte, String empCodigo, String empNombres, String empApellidos, String empFuncion, List<DetalleRol> ingresos, List<DetalleRol> egresos, List<DetalleRol> descuentos) {
        this.periodoAnio = periodoAnio;
        this.periodoMes = periodoMes;
        this.fechaCorte = fechaCorte;
        this.empCodigo = empCodigo;
        this.empNombres = empNombres;
        this.empApellidos = empApellidos;
        this.empFuncion = empFuncion;
        this.ingresos = ingresos;
        this.egresos = egresos;
        this.descuentos = descuentos; // Inicializar la nueva lista
    }

    // Métodos de acceso a los campos
    public String getPeriodoAnio() {
        return periodoAnio;
    }

    public String getPeriodoMes() {
        String formattedMonth = String.format("%02d", periodoMes);
        return formattedMonth;
    }

    public String getFechaCorte() {
        String fechaFormateada = "";
        String fechaOriginal = fechaCorte;
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date fecha = formatoEntrada.parse(fechaOriginal);
            fechaFormateada = formatoSalida.format(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaFormateada;
    }

    public String getEmpCodigo() {
        return empCodigo;
    }

    public String getEmpNombres() {
        return empNombres;
    }

    public String getEmpApellidos() {
        return empApellidos;
    }

    public String getEmpFuncion() {
        return empFuncion;
    }

    public List<DetalleRol> getIngresos() {
        return ingresos;
    }

    public List<DetalleRol> getEgresos() {
        return egresos;
    }

    public List<DetalleRol> getDescuentos() {
        return descuentos;
    }

    // Formateo de argumentos adicionales al objeto RolGeneral
    public String getNombreMes() {
        String nombreMes = Month.of(periodoMes).getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase();
        return nombreMes;
    }
}
