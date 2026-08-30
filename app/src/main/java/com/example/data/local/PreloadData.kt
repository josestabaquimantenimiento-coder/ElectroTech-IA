package com.example.data.local

object PreloadData {
    val sampleNotebooks = listOf(
        TechnicalNotebook(
            id = 1L,
            name = "Bomba Hidráulica Grundfos CR-32",
            machineCode = "BOM-01",
            area = "Sala de Calderas / Central Hidráulica",
            description = "Grupo de presión principal, presostato Danfoss KP35, maniobra estrella-triángulo y bomba auxiliar.",
            colorHex = "#0284C7",
            iconName = "pump",
            createdAt = System.currentTimeMillis() - 86400000L * 30
        ),
        TechnicalNotebook(
            id = 2L,
            name = "Variadores Sinamics G120 & V20",
            machineCode = "VFD-L1",
            area = "Línea 1 Embotellado / Cintas",
            description = "Accionamientos de frecuencia variable Siemens, resistencias de frenado choppers y parámetros de Bus DC.",
            colorHex = "#10B981",
            iconName = "bolt",
            createdAt = System.currentTimeMillis() - 86400000L * 28
        ),
        TechnicalNotebook(
            id = 3L,
            name = "Cuadros de Maniobra & Contactores TeSys",
            machineCode = "CCM-01",
            area = "Taller de Mantenimiento & Nave Central",
            description = "Contactores Schneider TeSys D (LC1D), relés térmicos LRD, sondas termistor PTC en devanados y bornas de mando 230VAC.",
            colorHex = "#F59E0B",
            iconName = "settings",
            createdAt = System.currentTimeMillis() - 86400000L * 25
        ),
        TechnicalNotebook(
            id = 4L,
            name = "Autómata PLC Siemens S7-1200 & Red",
            machineCode = "PLC-01",
            area = "Armario de Control General",
            description = "CPU 1214C DC/DC/DC, módulos de E/S digitales 24VDC, señales analógicas 4-20mA y comunicación PROFINET.",
            colorHex = "#8B5CF6",
            iconName = "memory",
            createdAt = System.currentTimeMillis() - 86400000L * 20
        ),
        TechnicalNotebook(
            id = 5L,
            name = "Compresores de Tornillo Atlas Copco",
            machineCode = "CMP-01",
            area = "Sala de Compresores y Neumática",
            description = "Generación de aire comprimido a 8.5 bar, secador frigorífico, variador de velocidad y purgas automáticas.",
            colorHex = "#06B6D4",
            iconName = "compressor",
            createdAt = System.currentTimeMillis() - 86400000L * 15
        ),
        TechnicalNotebook(
            id = 6L,
            name = "Seguridad de Planta & Protocolo LOTO",
            machineCode = "SEG-01",
            area = "Planta Completa",
            description = "Normas UNE-EN 50110, 5 Reglas de Oro, consignación eléctrica con candados y verificación de ausencia de tensión.",
            colorHex = "#EC4899",
            iconName = "shield",
            createdAt = System.currentTimeMillis() - 86400000L * 10
        )
    )

    val sampleNotes = listOf(
        NotebookNote(
            id = 1L,
            notebookId = 1L,
            title = "Ajuste de Presión Diferencial en Bomba",
            content = "El presostato SP1 corta a 180 bar. Si la bomba cicla con demasiada frecuencia, purgar el calderín hidroneumático de la bancada.",
            author = "José Técnico",
            timestamp = System.currentTimeMillis() - 86400000L * 2
        ),
        NotebookNote(
            id = 2L,
            notebookId = 2L,
            title = "Limpieza de Filtros en Armario VFD",
            content = "Los variadores de la cinta 1 acumulan polvo de cartón. Soplar los disipadores cada 15 días para evitar alarma F0004.",
            author = "Equipo Turno Mañana",
            timestamp = System.currentTimeMillis() - 86400000L * 4
        ),
        NotebookNote(
            id = 3L,
            notebookId = 5L,
            title = "Revisión de Nivel de Aceite Sintético",
            content = "Utilizar únicamente aceite Roto-Inject Fluid. La mirilla debe estar a 3/4 con el compresor en marcha a plena carga.",
            author = "Mantenimiento Preventivo",
            timestamp = System.currentTimeMillis() - 86400000L * 6
        )
    )

    val sampleDocuments = listOf(
        TechnicalDocument(
            id = 1L,
            title = "Protección Térmica de Motores con Sonda PTC",
            manufacturer = "ABB / Siemens Motors",
            model = "Série M3BP / 1LE1",
            category = "Protección Motor",
            pageReference = "Pág. 45, Secc. 4.2 - Circuitos de Termistores",
            contentSnippet = "Si el motor dispara por alarma de 'Sobretemperatura' con ventilador forzado girando libremente y sin obstrucción mecánica, inspeccionar la sonda termistor PTC insertada en los devanados del estator.",
            expectedValues = "Resistencia en frío (20°C-25°C): 10.000 Ω (10 kΩ) a 12 kΩ. Umbral de disparo térmico: R > 4.000 Ω (en caliente límite) / Corte relé a 1650 Ω según curva DIN 44081/44082. Tensión máxima en bornes de sonda: 2.5 V.",
            diagnosticProcedure = "1. Desconectar bornes T1-T2 de la sonda en la caja de bornes del motor.\n2. Medir con multímetro en escala de 20 kΩ.\n3. Si marca circuito abierto (∞) o valor desfasado (>20kΩ a temp ambiente), sustituir sonda o verificar cableado de apantallamiento.\n4. Si el valor es ~10kΩ pero el relé dispara, calibrar o sustituir el relé termistor de disparo.",
            notebookId = 3L
        ),
        TechnicalDocument(
            id = 2L,
            title = "Esquema y Control de Contactor K1 y Relé Térmico F2",
            manufacturer = "Schneider Electric",
            model = "TeSys D (LC1D09..D38) & LRD",
            category = "Protección Motor",
            pageReference = "Pág. 18, Secc. 2.1 - Circuito de Maniobra",
            contentSnippet = "La bobina del contactor K1 (bornes A1-A2) se energiza a través de la línea de mando (L2 o 24VDC) tras pasar en serie por el contacto auxiliar normalmente cerrado (NC 95-96) del relé térmico F2 y el pulsador de paro de emergencia.",
            expectedValues = "Tensión en bornes A1-A2: 230 VAC ±10% (207 V - 253 V) o 24 VDC según modelo. Resistencia bobina LC1D09 230V: aprox. 540 Ω. Contactos auxiliares 13-14 (NA) y 21-22 (NC): continuidad < 0.2 Ω en reposo.",
            diagnosticProcedure = "1. Verificar presencia de tensión entre fase L2 y neutro.\n2. Comprobar si el contacto 95-96 del relé F2 está cerrado (0 Ω). Si está abierto, el relé ha disparado por sobrecorriente o falta de fase; rearmar tras enfriar.\n3. Medir tensión con voltímetro AC directamente en bornes A1 y A2 durante la orden de marcha.\n4. Si hay 230V pero el contactor no enclava, medir resistencia de bobina (si marca ∞ está abierta).",
            notebookId = 3L
        ),
        TechnicalDocument(
            id = 3L,
            title = "Grupo Motobomba Hidráulica de Alta Presión",
            manufacturer = "Grundfos / Rexroth",
            model = "Hydro CR-32 & A10VSO",
            category = "Bomba Hidráulica",
            pageReference = "Pág. 32, Secc. 5.3 - Ajustes de Presión y Caudal",
            contentSnippet = "Control de arranque estrella-triángulo (KM1, KM2, KM3) enclavado con el presostato de seguridad SP1 y el detector de nivel de aceite HL1.",
            expectedValues = "Presión de trabajo nominal: 160 bar ±5 bar. Tarado presostato SP1: 180 bar (corte máx). Presión mínima de aspiración: 0.8 bar. Tiempo de transición estrella-triángulo: 4.5 segundos.",
            diagnosticProcedure = "1. Abrir esquema eléctrico sección bomba hidráulica.\n2. Verificar que el contacto del presostato SP1 en bornes 11-12 esté cerrado.\n3. Inspeccionar el manómetro de línea y purga de aire en la válvula de alivio proporcional.\n4. Si el motor arranca en estrella pero no commuta a triángulo, comprobar relé temporizador KT1 y contactos auxiliares de KM2/KM3.",
            notebookId = 1L
        ),
        TechnicalDocument(
            id = 4L,
            title = "Variador de Frecuencia Sinamics V20 / G120",
            manufacturer = "Siemens",
            model = "Sinamics V20 7.5kW",
            category = "Variador VFD",
            pageReference = "Pág. 88, Tabla de Fallos F0001 - F0050",
            contentSnippet = "F0001: Sobrecorriente en etapa de salida IGBT. F0002: Sobretensión en bus DC (Vdc > 820V). F0004: Sobretemperatura del disipador (>95°C). F0011: Sobretemperatura I2t del motor.",
            expectedValues = "Tensión Bus DC en reposo (red 400V): ~560 VDC. Tensión de choque de frenado: 750 VDC. Resistencia de aislamiento motor (fase a tierra con megóhmetro a 500V): > 100 MΩ.",
            diagnosticProcedure = "1. Si aparece F0002 tras frenado rápido, verificar resistencia de chopper de frenado externa (R_brake = 40 Ω).\n2. Si aparece F0001, desconectar cables U-V-W del motor y comprobar si el variador arranca en vacío.\n3. Para F0011, revisar parámetro P0625 (temperatura ambiente) y estado del ventilador del motor.",
            notebookId = 2L
        ),
        TechnicalDocument(
            id = 5L,
            title = "Autómata Programable PLC CJ2M / S7-1200",
            manufacturer = "Omron / Siemens",
            model = "S7-1200 CPU 1214C DC/DC/DC",
            category = "Autómata PLC",
            pageReference = "Pág. 112, Secc. 3.4 - Entradas y Salidas Digitales",
            contentSnippet = "Módulos de entradas 24VDC tipo Sink/Source. Salidas a relé y transistor para pilotaje de bobinas de electroválvulas y contactores de maniobra.",
            expectedValues = "Tensión de alimentación CPU: 20.4 VDC a 28.8 VDC (nominal 24VDC). Umbral de entrada activa (High): 15V - 30VDC (> 2.5 mA). Consumo por canal de salida: máx 0.5A.",
            diagnosticProcedure = "1. Comprobar LED de estado 'RUN' (verde continuo) y 'ERROR' (rojo si hay fallo de hardware o watchdog).\n2. Medir 24VDC entre bornes L+ y M de la fuente auxiliar.\n3. Forzar entrada con interruptor y verificar encendido del LED correspondiente en la regleta.",
            notebookId = 4L
        ),
        TechnicalDocument(
            id = 6L,
            title = "Protocolo de 5 Reglas de Oro en Trabajos Eléctricos",
            manufacturer = "Normativa Seguridad Eléctrica",
            model = "UNE-EN 50110 / RD 614/2001",
            category = "Normativa",
            pageReference = "Protocolo LOTO Secc. 1",
            contentSnippet = "Procedimiento obligatorio antes de intervenir en armarios eléctricos o maquinaria industrial de baja y media tensión.",
            expectedValues = "Ausencia de tensión comprobada con detector homologado: 0.0 VAC / 0.0 VDC entre todas las fases y tierra.",
            diagnosticProcedure = "1. Desconectar todas las fuentes de alimentación (seccionador general Q1).\n2. Prevenir cualquier posible realimentación (bloqueo LOTO con candado y tarjeta).\n3. Verificar la ausencia de tensión con multímetro / voltímetro.\n4. Poner a tierra y en cortocircuito (en instalaciones donde aplique).\n5. Proteger frente a elementos próximos en tensión y delimitar la zona de trabajo.",
            notebookId = 6L
        )
    )

    val sampleInterventions = listOf(
        InterventionEntity(
            equipmentName = "Bomba Hidráulica Principal P-01",
            cabinetCode = "ARM-04-HIDRAULICA",
            failureDescription = "El motor de la bomba no arranca. El piloto de fallo térmico está iluminado en la puerta del armario.",
            rootCause = "Disparo del relé térmico F2 por desequilibrio en la fase L2 debido a borne flojo en contactor K1.",
            actionsTaken = "Medición de tensión en A1-A2 (230V OK), reapriete del borne de potencia L2 en K1, rearme manual de F2 tras verificación de consumo de 14.2A nominal.",
            partsReplaced = "Reapriete general de regleta X1",
            status = "Resuelto",
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            technicalNotes = "Se recomienda termografía infrarroja en la próxima revisión mensual preventiva.",
            notebookId = 1L
        ),
        InterventionEntity(
            equipmentName = "Línea de Envasado - Motor Cinta K3",
            cabinetCode = "CCM-02-ENVASADO",
            failureDescription = "Alarma recurrente de sobretemperatura en motor pero el ventilador gira a velocidad normal.",
            rootCause = "Sonda PTC del devanado del estator dañada por vibración mecánica excesiva (resistencia en abierto > 200 kΩ a temperatura ambiente).",
            actionsTaken = "Comprobación de valor con multímetro en bornes T1-T2. Sustitución de la sonda PTC y recalibrado del relé de termistores.",
            partsReplaced = "Sonda PTC 10kΩ ABB (Ref: 1SDA050211R1)",
            status = "Resuelto",
            timestamp = System.currentTimeMillis() - 86400000L * 5,
            technicalNotes = "Alineación de poleas corregida para eliminar vibración en el eje.",
            notebookId = 3L
        ),
        InterventionEntity(
            equipmentName = "Compresor de Tornillo Atlas Copco",
            cabinetCode = "ARM-01-COMPRESORES",
            failureDescription = "Fallo F0002 en el variador de frecuencia al desacelerar la carga.",
            rootCause = "Sobretensión en el bus DC por rotura de la resistencia de frenado dinámica externa.",
            actionsTaken = "Medición óhmica de la resistencia de frenado (marcó infinito, circuito abierto). Se solicita repuesto al proveedor.",
            partsReplaced = "Pendiente resistencia de frenado 40Ω 1500W",
            status = "Pendiente Repuesto",
            timestamp = System.currentTimeMillis() - 86400000L * 1,
            technicalNotes = "Compresor configurado temporalmente con rampa de parada suave prolongada a 25s.",
            notebookId = 5L
        )
    )

    val sampleMemories = listOf(
        AiMemoryEntity(
            title = "Alimentación de Maniobra Cuadros ARM-01 a ARM-05",
            category = "Máquina / Cuadro",
            content = "En los cuadros principales de nave (ARM-01 a ARM-05) la tensión de control es de 230 VAC aislada por transformador T1 de 500VA. El neutro secundario está puesto a tierra en el borne N-PE.",
            source = "Regla de Taller",
            isEnabled = true,
            timestamp = System.currentTimeMillis() - 86400000L * 7,
            notebookId = 3L
        ),
        AiMemoryEntity(
            title = "Comportamiento Térmico Variador Sinamics G120",
            category = "Fallo Frecuente",
            content = "El variador Siemens G120 de la cinta 2 dispara con fallo F0004 si la temperatura del cuadro supera 38°C. En verano, encender el extractor de techo del cuadro.",
            source = "Aprendido en Planta",
            isEnabled = true,
            timestamp = System.currentTimeMillis() - 86400000L * 4,
            notebookId = 2L
        ),
        AiMemoryEntity(
            title = "Preferencia: Secuencia de Diagnóstico con Multímetro",
            category = "Regla de Diagnóstico",
            content = "Siempre sugerir primero mediciones de tensión directa con multímetro en bornes A1-A2 de contactores y continuidad 95-96 de relés térmicos antes de desmontar elementos mecánicos.",
            source = "Preferencia de Técnico",
            isEnabled = true,
            timestamp = System.currentTimeMillis() - 86400000L * 3,
            notebookId = 3L
        ),
        AiMemoryEntity(
            title = "Sensor de Presión Hidráulica SP1 Grundfos",
            category = "Parámetro Especial",
            content = "El presostato Danfoss KP35 de la bomba P-01 está calibrado para abrir contacto de corte por encima de 180 bar. La señal de rearme diferencial está en 15 bar.",
            source = "Manual Técnico",
            isEnabled = true,
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            notebookId = 1L
        ),
        AiMemoryEntity(
            title = "Protocolo de Bloqueo LOTO Obligatorio",
            category = "Regla de Diagnóstico",
            content = "Antes de comprobar bornes de potencia en guardamotores Q1 o motores trifásicos, recordar al técnico aplicar candado de consignación y verificar 0.0V en las tres fases.",
            source = "Seguridad Laboral",
            isEnabled = true,
            timestamp = System.currentTimeMillis() - 86400000L * 1,
            notebookId = 6L
        )
    )
}
