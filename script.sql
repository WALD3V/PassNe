 --Insertar datos en la tabla RolGeneral_cab (Cabecera de Rol)
INSERT INTO RolGeneral_cab (cia_codigo, Periodo_anio, Periodo_mes, Fecha_corte, EMP_CODIGO, EMP_NOMBRES, EMP_APELLIDOS, EMP_FUNCION)
VALUES
    ('01', 2025, 1, '20250131', 'EMP001', 'Juan', 'Perez', 'Gerente'),
    ('01', 2025, 1, '20250131', 'EMP002', 'Ana', 'Lopez', 'Contadora'),
    ('01', 2025, 1, '20250131', 'EMP003', 'Carlos', 'Martinez', 'Asistente'),
    ('01', 2025, 1, '20250131', 'EMP004', 'Maria', 'Gonzalez', 'Jefe de ventas');

 --Insertar detalles en la tabla RolGeneral_Det (Detalles del Rol)
 --Para el EMP001
INSERT INTO RolGeneral_Det (detalle_codigo, nombre, tipo, referencia, valor, saldo, cia_codigo, Periodo_anio, Periodo_mes, EMP_CODIGO)
VALUES
    ('D001', 'Sueldo Base', 'I', 'REF001', 3000.00, 3000.00, '01', 2025, 1, 'EMP001'),
    ('D002', 'Bonificación por ventas', 'I', 'REF002', 500.00, 500.00, '01', 2025, 1, 'EMP001'),
    ('D003', 'Descuento por seguro', 'E', 'REF003', 100.00, 100.00, '01', 2025, 1, 'EMP001'),
    ('D004', 'Desglose de horas extras', 'D', 'REF004', 150.00, 150.00, '01', 2025, 1, 'EMP001'),
    ('D005', 'Ajuste de planilla', 'E', 'REF005', 50.00, 50.00, '01', 2025, 1, 'EMP001');

 --Para el EMP002
INSERT INTO RolGeneral_Det (detalle_codigo, nombre, tipo, referencia, valor, saldo, cia_codigo, Periodo_anio, Periodo_mes, EMP_CODIGO)
VALUES
    ('D001', 'Sueldo Base', 'I', 'REF001', 2800.00, 2800.00, '01', 2025, 1, 'EMP002'),
    ('D002', 'Bonificación por desempeño', 'I', 'REF002', 400.00, 400.00, '01', 2025, 1, 'EMP002'),
    ('D003', 'Descuento por préstamos', 'E', 'REF003', 200.00, 200.00, '01', 2025, 1, 'EMP002'),
    ('D004', 'Desglose de vacaciones', 'D', 'REF004', 100.00, 100.00, '01', 2025, 1, 'EMP002'),
    ('D005', 'Ajuste de planilla', 'E', 'REF005', 30.00, 30.00, '01', 2025, 1, 'EMP002');

-- Para el EMP003
INSERT INTO RolGeneral_Det (detalle_codigo, nombre, tipo, referencia, valor, saldo, cia_codigo, Periodo_anio, Periodo_mes, EMP_CODIGO)
VALUES
    ('D001', 'Sueldo Base', 'I', 'REF001', 2200.00, 2200.00, '01', 2025, 1, 'EMP003'),
    ('D002', 'Bonificación por puntualidad', 'I', 'REF002', 200.00, 200.00, '01', 2025, 1, 'EMP003'),
    ('D003', 'Descuento por tardanza', 'E', 'REF003', 50.00, 50.00, '01', 2025, 1, 'EMP003'),
    ('D004', 'Desglose de horas extras', 'D', 'REF004', 80.00, 80.00, '01', 2025, 1, 'EMP003'),
    ('D005', 'Ajuste de planilla', 'E', 'REF005', 20.00, 20.00, '01', 2025, 1, 'EMP003');

 --Para el EMP004
INSERT INTO RolGeneral_Det (detalle_codigo, nombre, tipo, referencia, valor, saldo, cia_codigo, Periodo_anio, Periodo_mes, EMP_CODIGO)
VALUES
    ('D001', 'Sueldo Base', 'I', 'REF001', 2500.00, 2500.00, '01', 2025, 1, 'EMP004'),
    ('D002', 'Bonificación por metas', 'I', 'REF002', 600.00, 600.00, '01', 2025, 1, 'EMP004'),
    ('D003', 'Descuento por seguro', 'E', 'REF003', 150.00, 150.00, '01', 2025, 1, 'EMP004'),
    ('D004', 'Desglose de horas extras', 'D', 'REF004', 120.00, 120.00, '01', 2025, 1, 'EMP004'),
    ('D005', 'Ajuste de planilla', 'E', 'REF005', 40.00, 40.00, '01', 2025, 1, 'EMP004');
