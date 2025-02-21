package pasantias;

import java.util.List;

public class RolGeneral {

    private String periodoAnio;
    private String periodoMes;
    private String fechaCorte;
    private String empCodigo;
    private String empNombres;
    private String empApellidos;
    private String empFuncion;

    private List<DetalleRol> ingresos;
    private List<DetalleRol> egresos;

    // Constructor con todos los parámetros
    public RolGeneral(String periodoAnio, String periodoMes, String fechaCorte, String empCodigo, String empNombres, String empApellidos, String empFuncion, List<DetalleRol> ingresos, List<DetalleRol> egresos) {
        this.periodoAnio = periodoAnio;
        this.periodoMes = periodoMes;
        this.fechaCorte = fechaCorte;
        this.empCodigo = empCodigo;
        this.empNombres = empNombres;
        this.empApellidos = empApellidos;
        this.empFuncion = empFuncion;
        this.ingresos = ingresos;
        this.egresos = egresos;
    }

    // Métodos de acceso a los campos
    public String getPeriodoAnio() {
        return periodoAnio;
    }

    public String getPeriodoMes() {
        return periodoMes;
    }

    public String getFechaCorte() {
        return fechaCorte;
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
}
