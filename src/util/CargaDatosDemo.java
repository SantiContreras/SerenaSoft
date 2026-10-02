package util;

import java.math.BigDecimal;

import model.Categoria;
import model.Marca;
import model.Producto;
import model.Proveedor;
import model.Rubro;
import model.TipoIva;
import model.UnidadMedida;

import services.CategoriaService;
import services.MarcaService;
import services.ProductoService;
import services.ProveedorService;
import services.ResultadoOperacion;
import services.RubroService;
import services.TipoIvaService;
import services.UnidadMedidaService;

public class CargaDatosDemo {

    // =========================================================
    // SERVICES
    // =========================================================

    private static final RubroService rubroService =
            new RubroService();

    private static final CategoriaService categoriaService =
            new CategoriaService();

    private static final MarcaService marcaService =
            new MarcaService();

    private static final UnidadMedidaService unidadService =
            new UnidadMedidaService();

    private static final TipoIvaService ivaService =
            new TipoIvaService();

    private static final ProveedorService proveedorService =
            new ProveedorService();

    private static final ProductoService productoService =
            new ProductoService();


    // =========================================================
    // CONTADORES
    // =========================================================

    private static int creados = 0;
    private static int existentes = 0;
    private static int errores = 0;


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println(
                "===================================================="
        );
        System.out.println(
                "CARGA DE DATOS DEMO - SERENA SOFT"
        );
        System.out.println(
                "===================================================="
        );

        try {

            // =================================================
            // 1. UNIDADES
            // =================================================

            System.out.println();
            System.out.println("1) UNIDADES DE MEDIDA");
            System.out.println("---------------------");

            asegurarUnidad(
                    "UN",
                    "Unidad",
                    false
            );

            asegurarUnidad(
                    "KG",
                    "Kilogramo",
                    true
            );

            asegurarUnidad(
                    "CAJA",
                    "Caja",
                    false
            );

            asegurarUnidad(
                    "PACK",
                    "Pack",
                    false
            );

            asegurarUnidad(
                    "BULTO",
                    "Bulto",
                    false
            );


            // =================================================
            // 2. TIPOS IVA
            // =================================================

            System.out.println();
            System.out.println("2) TIPOS DE IVA");
            System.out.println("---------------------");

            asegurarIva(
                    "IVA21",
                    "IVA 21%",
                    "21.000"
            );

            asegurarIva(
                    "IVA105",
                    "IVA 10,5%",
                    "10.500"
            );


            // =================================================
            // 3. RUBROS
            // =================================================

            System.out.println();
            System.out.println("3) RUBROS");
            System.out.println("---------------------");

            asegurarRubro(
                    "Bebidas",
                    "Bebidas con y sin alcohol - datos demo"
            );

            asegurarRubro(
                    "Almacén",
                    "Productos generales de almacén"
            );

            asegurarRubro(
                    "Lácteos",
                    "Leches, yogures y derivados"
            );

            asegurarRubro(
                    "Fiambres",
                    "Fiambres y productos vendidos por peso"
            );

            asegurarRubro(
                    "Limpieza",
                    "Productos de limpieza del hogar"
            );

            asegurarRubro(
                    "Higiene Personal",
                    "Productos de higiene y cuidado personal"
            );


            // =================================================
            // 4. CATEGORÍAS
            // =================================================

            System.out.println();
            System.out.println("4) CATEGORÍAS");
            System.out.println("---------------------");

            asegurarCategoria(
                    "Bebidas",
                    "Gaseosas",
                    "Gaseosas y bebidas carbonatadas"
            );

            asegurarCategoria(
                    "Bebidas",
                    "Aguas",
                    "Agua mineral y saborizada"
            );

            asegurarCategoria(
                    "Bebidas",
                    "Jugos",
                    "Jugos y bebidas frutales"
            );

            asegurarCategoria(
                    "Bebidas",
                    "Energizantes",
                    "Bebidas energizantes"
            );

            asegurarCategoria(
                    "Almacén",
                    "Arroz",
                    "Arroz envasado"
            );

            asegurarCategoria(
                    "Almacén",
                    "Pastas",
                    "Pastas secas"
            );

            asegurarCategoria(
                    "Almacén",
                    "Yerbas",
                    "Yerba mate"
            );

            asegurarCategoria(
                    "Almacén",
                    "Conservas",
                    "Conservas alimenticias"
            );

            asegurarCategoria(
                    "Almacén",
                    "Galletitas",
                    "Galletitas dulces y saladas"
            );

            asegurarCategoria(
                    "Lácteos",
                    "Leches",
                    "Leches envasadas"
            );

            asegurarCategoria(
                    "Lácteos",
                    "Yogures",
                    "Yogures"
            );

            asegurarCategoria(
                    "Lácteos",
                    "Quesos",
                    "Quesos y derivados"
            );

            asegurarCategoria(
                    "Fiambres",
                    "Fiambres por Peso",
                    "Fiambres vendidos por kilogramo"
            );

            asegurarCategoria(
                    "Limpieza",
                    "Lavandinas",
                    "Lavandinas y desinfectantes"
            );

            asegurarCategoria(
                    "Limpieza",
                    "Detergentes",
                    "Detergentes y lavavajillas"
            );

            asegurarCategoria(
                    "Limpieza",
                    "Lavado de Ropa",
                    "Jabones y productos para ropa"
            );

            asegurarCategoria(
                    "Higiene Personal",
                    "Desodorantes",
                    "Desodorantes personales"
            );

            asegurarCategoria(
                    "Higiene Personal",
                    "Shampoo",
                    "Productos para cabello"
            );

            asegurarCategoria(
                    "Higiene Personal",
                    "Higiene Bucal",
                    "Pastas y productos bucales"
            );


            // =================================================
            // 5. MARCAS
            // =================================================

            System.out.println();
            System.out.println("5) MARCAS");
            System.out.println("---------------------");

            asegurarMarca("Coca-Cola", "Bebidas");
            asegurarMarca("Manaos", "Bebidas");
            asegurarMarca("Villavicencio", "Aguas");
            asegurarMarca("Cepita", "Jugos");
            asegurarMarca("Speed", "Energizantes");

            asegurarMarca("Gallo", "Almacén");
            asegurarMarca("Lucchetti", "Pastas");
            asegurarMarca("Taragüi", "Yerba mate");
            asegurarMarca("La Campagnola", "Conservas");
            asegurarMarca("Bagley", "Galletitas");

            asegurarMarca("La Serenísima", "Lácteos");
            asegurarMarca("Ilolay", "Lácteos");
            asegurarMarca("Paladini", "Fiambres");

            asegurarMarca("Ayudín", "Limpieza");
            asegurarMarca("Ala", "Lavado de ropa");
            asegurarMarca("Magistral", "Detergentes");

            asegurarMarca("Rexona", "Higiene personal");
            asegurarMarca("Sedal", "Cuidado del cabello");
            asegurarMarca("Colgate", "Higiene bucal");


            // =================================================
            // 6. PROVEEDORES
            // =================================================

            System.out.println();
            System.out.println("6) PROVEEDORES");
            System.out.println("---------------------");

            asegurarProveedor(
                    "30-90000001-1",
                    "Distribuidora Litoral Demo S.R.L.",
                    "Distribuidora Litoral",
                    "Responsable Inscripto",
                    "Av. Demo 1250",
                    "Resistencia",
                    "Chaco",
                    "3624000001",
                    "ventas1@demo.local",
                    "Carlos Demo",
                    "3624100001",
                    "Proveedor demo de bebidas"
            );

            asegurarProveedor(
                    "30-90000002-2",
                    "Bebidas del Nordeste Demo S.A.",
                    "Bebidas Nordeste",
                    "Responsable Inscripto",
                    "Ruta Demo 16 Km 5",
                    "Resistencia",
                    "Chaco",
                    "3624000002",
                    "ventas2@demo.local",
                    "Mariana Demo",
                    "3624100002",
                    "Proveedor demo de bebidas"
            );

            asegurarProveedor(
                    "30-90000003-3",
                    "Alimentos Chaco Demo S.R.L.",
                    "Alimentos Chaco",
                    "Responsable Inscripto",
                    "Av. Almacén 850",
                    "Resistencia",
                    "Chaco",
                    "3624000003",
                    "ventas3@demo.local",
                    "José Demo",
                    "3624100003",
                    "Proveedor demo de almacén"
            );

            asegurarProveedor(
                    "30-90000004-4",
                    "Mayorista Norte Demo S.A.",
                    "Mayorista Norte",
                    "Responsable Inscripto",
                    "Parque Industrial Demo",
                    "Puerto Tirol",
                    "Chaco",
                    "3624000004",
                    "ventas4@demo.local",
                    "Lucía Demo",
                    "3624100004",
                    "Proveedor mayorista demo"
            );

            asegurarProveedor(
                    "30-90000005-5",
                    "Lácteos Regionales Demo S.R.L.",
                    "Lácteos Regionales",
                    "Responsable Inscripto",
                    "Av. Lácteos 455",
                    "Resistencia",
                    "Chaco",
                    "3624000005",
                    "ventas5@demo.local",
                    "Martín Demo",
                    "3624100005",
                    "Proveedor demo de lácteos"
            );

            asegurarProveedor(
                    "30-90000006-6",
                    "Frigorífico Comercial Demo S.A.",
                    "Frigorífico Demo",
                    "Responsable Inscripto",
                    "Calle Frigorífico 920",
                    "Resistencia",
                    "Chaco",
                    "3624000006",
                    "ventas6@demo.local",
                    "Ana Demo",
                    "3624100006",
                    "Proveedor demo de fiambres"
            );

            asegurarProveedor(
                    "30-90000007-7",
                    "Limpieza Integral Demo S.R.L.",
                    "Limpieza Integral",
                    "Responsable Inscripto",
                    "Av. Limpieza 1100",
                    "Resistencia",
                    "Chaco",
                    "3624000007",
                    "ventas7@demo.local",
                    "Diego Demo",
                    "3624100007",
                    "Proveedor demo de limpieza"
            );

            asegurarProveedor(
                    "30-90000008-8",
                    "Distribuciones Hogar Demo S.A.",
                    "Distribuciones Hogar",
                    "Responsable Inscripto",
                    "Calle Hogar 330",
                    "Barranqueras",
                    "Chaco",
                    "3624000008",
                    "ventas8@demo.local",
                    "Laura Demo",
                    "3624100008",
                    "Proveedor demo de limpieza"
            );

            asegurarProveedor(
                    "30-90000009-9",
                    "Higiene Nordeste Demo S.R.L.",
                    "Higiene Nordeste",
                    "Responsable Inscripto",
                    "Av. Higiene 725",
                    "Resistencia",
                    "Chaco",
                    "3624000009",
                    "ventas9@demo.local",
                    "Pablo Demo",
                    "3624100009",
                    "Proveedor demo de higiene personal"
            );

            asegurarProveedor(
                    "30-90000010-0",
                    "Comercial del NEA Demo S.R.L.",
                    "Comercial NEA",
                    "Responsable Inscripto",
                    "Av. Comercio 2100",
                    "Resistencia",
                    "Chaco",
                    "3624000010",
                    "ventas10@demo.local",
                    "Sofía Demo",
                    "3624100010",
                    "Proveedor general demo"
            );


            // =================================================
            // 7. PRODUCTOS
            // =================================================

            System.out.println();
            System.out.println("7) PRODUCTOS");
            System.out.println("---------------------");

            /*
             * Ya tenemos productos de pruebas anteriores:
             *
             * BEB-0001       Coca-Cola 500 ml
             * JAM-CRUDO-001  Jamón Crudo
             * AGU-0001       Agua Mineral 500 ml
             *
             * Los siguientes completan un catálogo de
             * aproximadamente 30 productos.
             */


            // =================================================
            // BEBIDAS
            // =================================================

            asegurarProducto(
                    "BEB-0002",
                    "INT-BEB-0002",
                    "7791000000002",
                    "Coca-Cola 2,25 L",
                    "Gaseosa cola botella 2,25 litros",
                    "A-01-02",
                    "Bebidas",
                    "Gaseosas",
                    "Coca-Cola",
                    "PACK",
                    "UN",
                    "6",
                    "9000",
                    "2200",
                    "46.67",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "BEB-0003",
                    "INT-BEB-0003",
                    "7791000000003",
                    "Manaos Cola 2,25 L",
                    "Gaseosa cola botella 2,25 litros",
                    "A-01-03",
                    "Bebidas",
                    "Gaseosas",
                    "Manaos",
                    "PACK",
                    "UN",
                    "6",
                    "6000",
                    "1500",
                    "50",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "AGU-0002",
                    "INT-AGU-0002",
                    "7791000000004",
                    "Agua Mineral 1,5 L",
                    "Agua mineral botella 1,5 litros",
                    "A-02-01",
                    "Bebidas",
                    "Aguas",
                    "Villavicencio",
                    "CAJA",
                    "UN",
                    "6",
                    "4800",
                    "1200",
                    "50",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "JUG-0001",
                    "INT-JUG-0001",
                    "7791000000005",
                    "Jugo Naranja 1 L",
                    "Jugo sabor naranja",
                    "A-03-01",
                    "Bebidas",
                    "Jugos",
                    "Cepita",
                    "CAJA",
                    "UN",
                    "12",
                    "12000",
                    "1500",
                    "50",
                    "IVA21",
                    "12",
                    "180",
                    false
            );

            asegurarProducto(
                    "ENE-0001",
                    "INT-ENE-0001",
                    "7791000000006",
                    "Energizante 473 ml",
                    "Bebida energizante en lata",
                    "A-04-01",
                    "Bebidas",
                    "Energizantes",
                    "Speed",
                    "CAJA",
                    "UN",
                    "12",
                    "18000",
                    "2300",
                    "53.33",
                    "IVA21",
                    "12",
                    "120",
                    false
            );


            // =================================================
            // ALMACÉN
            // =================================================

            asegurarProducto(
                    "ARR-0001",
                    "INT-ARR-0001",
                    "7791000000007",
                    "Arroz Largo Fino 1 kg",
                    "Arroz largo fino paquete 1 kg",
                    "B-01-01",
                    "Almacén",
                    "Arroz",
                    "Gallo",
                    "BULTO",
                    "UN",
                    "10",
                    "11000",
                    "1700",
                    "54.55",
                    "IVA105",
                    "10",
                    "100",
                    false
            );

            asegurarProducto(
                    "ARR-0002",
                    "INT-ARR-0002",
                    "7791000000008",
                    "Arroz Parboil 1 kg",
                    "Arroz parboil paquete 1 kg",
                    "B-01-02",
                    "Almacén",
                    "Arroz",
                    "Gallo",
                    "BULTO",
                    "UN",
                    "10",
                    "12500",
                    "1900",
                    "52",
                    "IVA105",
                    "10",
                    "100",
                    false
            );

            asegurarProducto(
                    "FID-0001",
                    "INT-FID-0001",
                    "7791000000009",
                    "Fideos Spaghetti 500 g",
                    "Pasta seca spaghetti",
                    "B-02-01",
                    "Almacén",
                    "Pastas",
                    "Lucchetti",
                    "CAJA",
                    "UN",
                    "20",
                    "16000",
                    "1250",
                    "56.25",
                    "IVA105",
                    "20",
                    "200",
                    false
            );

            asegurarProducto(
                    "FID-0002",
                    "INT-FID-0002",
                    "7791000000010",
                    "Fideos Tirabuzón 500 g",
                    "Pasta seca tirabuzón",
                    "B-02-02",
                    "Almacén",
                    "Pastas",
                    "Lucchetti",
                    "CAJA",
                    "UN",
                    "20",
                    "16500",
                    "1300",
                    "57.58",
                    "IVA105",
                    "20",
                    "200",
                    false
            );

            asegurarProducto(
                    "YER-0001",
                    "INT-YER-0001",
                    "7791000000011",
                    "Yerba Mate 1 kg",
                    "Yerba mate paquete 1 kg",
                    "B-03-01",
                    "Almacén",
                    "Yerbas",
                    "Taragüi",
                    "BULTO",
                    "UN",
                    "10",
                    "28000",
                    "4200",
                    "50",
                    "IVA105",
                    "10",
                    "100",
                    false
            );

            asegurarProducto(
                    "YER-0002",
                    "INT-YER-0002",
                    "7791000000012",
                    "Yerba Mate 500 g",
                    "Yerba mate paquete 500 gramos",
                    "B-03-02",
                    "Almacén",
                    "Yerbas",
                    "Taragüi",
                    "BULTO",
                    "UN",
                    "20",
                    "30000",
                    "2400",
                    "60",
                    "IVA105",
                    "20",
                    "200",
                    false
            );

            asegurarProducto(
                    "CON-0001",
                    "INT-CON-0001",
                    "7791000000013",
                    "Atún al Natural 170 g",
                    "Conserva de atún",
                    "B-04-01",
                    "Almacén",
                    "Conservas",
                    "La Campagnola",
                    "CAJA",
                    "UN",
                    "24",
                    "48000",
                    "3100",
                    "55",
                    "IVA21",
                    "24",
                    "240",
                    false
            );

            asegurarProducto(
                    "GAL-0001",
                    "INT-GAL-0001",
                    "7791000000014",
                    "Galletitas Surtidas 400 g",
                    "Galletitas surtidas",
                    "B-05-01",
                    "Almacén",
                    "Galletitas",
                    "Bagley",
                    "CAJA",
                    "UN",
                    "12",
                    "18000",
                    "2300",
                    "53.33",
                    "IVA21",
                    "12",
                    "120",
                    false
            );


            // =================================================
            // LÁCTEOS
            // =================================================

            asegurarProducto(
                    "LEC-0001",
                    "INT-LEC-0001",
                    "7791000000015",
                    "Leche Entera 1 L",
                    "Leche entera larga vida",
                    "C-01-01",
                    "Lácteos",
                    "Leches",
                    "La Serenísima",
                    "CAJA",
                    "UN",
                    "12",
                    "15000",
                    "1900",
                    "52",
                    "IVA105",
                    "12",
                    "144",
                    false
            );

            asegurarProducto(
                    "LEC-0002",
                    "INT-LEC-0002",
                    "7791000000016",
                    "Leche Descremada 1 L",
                    "Leche descremada larga vida",
                    "C-01-02",
                    "Lácteos",
                    "Leches",
                    "La Serenísima",
                    "CAJA",
                    "UN",
                    "12",
                    "15500",
                    "1950",
                    "50.97",
                    "IVA105",
                    "12",
                    "144",
                    false
            );

            asegurarProducto(
                    "YOG-0001",
                    "INT-YOG-0001",
                    "7791000000017",
                    "Yogur Bebible 1 L",
                    "Yogur bebible",
                    "C-02-01",
                    "Lácteos",
                    "Yogures",
                    "Ilolay",
                    "CAJA",
                    "UN",
                    "6",
                    "7800",
                    "2000",
                    "53.85",
                    "IVA105",
                    "6",
                    "60",
                    false
            );

            asegurarProducto(
                    "QUE-0001",
                    "INT-QUE-0001",
                    "7791000000018",
                    "Queso Cremoso",
                    "Queso cremoso vendido por peso",
                    "C-03-01",
                    "Lácteos",
                    "Quesos",
                    "La Serenísima",
                    "KG",
                    "KG",
                    "1",
                    "8500",
                    "13000",
                    "52.94",
                    "IVA105",
                    "3",
                    "30",
                    true
            );

            asegurarProducto(
                    "QUE-0002",
                    "INT-QUE-0002",
                    "7791000000019",
                    "Queso Pategrás",
                    "Queso pategrás vendido por peso",
                    "C-03-02",
                    "Lácteos",
                    "Quesos",
                    "Ilolay",
                    "KG",
                    "KG",
                    "1",
                    "11000",
                    "16500",
                    "50",
                    "IVA105",
                    "2",
                    "20",
                    true
            );


            // =================================================
            // FIAMBRES
            // =================================================

            asegurarProducto(
                    "FIA-0001",
                    "INT-FIA-0001",
                    "7791000000020",
                    "Jamón Cocido",
                    "Jamón cocido vendido por peso",
                    "D-01-01",
                    "Fiambres",
                    "Fiambres por Peso",
                    "Paladini",
                    "KG",
                    "KG",
                    "1",
                    "9000",
                    "14500",
                    "61.11",
                    "IVA21",
                    "2",
                    "25",
                    true
            );

            asegurarProducto(
                    "FIA-0002",
                    "INT-FIA-0002",
                    "7791000000021",
                    "Salame Milan",
                    "Salame vendido por peso",
                    "D-01-02",
                    "Fiambres",
                    "Fiambres por Peso",
                    "Paladini",
                    "KG",
                    "KG",
                    "1",
                    "10500",
                    "16500",
                    "57.14",
                    "IVA21",
                    "2",
                    "20",
                    true
            );


            // =================================================
            // LIMPIEZA
            // =================================================

            asegurarProducto(
                    "LIM-0001",
                    "INT-LIM-0001",
                    "7791000000022",
                    "Lavandina 1 L",
                    "Lavandina concentrada",
                    "E-01-01",
                    "Limpieza",
                    "Lavandinas",
                    "Ayudín",
                    "CAJA",
                    "UN",
                    "12",
                    "9000",
                    "1200",
                    "60",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "LIM-0002",
                    "INT-LIM-0002",
                    "7791000000023",
                    "Lavandina 2 L",
                    "Lavandina botella 2 litros",
                    "E-01-02",
                    "Limpieza",
                    "Lavandinas",
                    "Ayudín",
                    "CAJA",
                    "UN",
                    "6",
                    "7500",
                    "1900",
                    "52",
                    "IVA21",
                    "6",
                    "60",
                    false
            );

            asegurarProducto(
                    "DET-0001",
                    "INT-DET-0001",
                    "7791000000024",
                    "Detergente 500 ml",
                    "Detergente lavavajillas",
                    "E-02-01",
                    "Limpieza",
                    "Detergentes",
                    "Magistral",
                    "CAJA",
                    "UN",
                    "12",
                    "14500",
                    "1850",
                    "53.10",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "JAB-0001",
                    "INT-JAB-0001",
                    "7791000000025",
                    "Jabón en Polvo 800 g",
                    "Jabón para lavado de ropa",
                    "E-03-01",
                    "Limpieza",
                    "Lavado de Ropa",
                    "Ala",
                    "BULTO",
                    "UN",
                    "10",
                    "22000",
                    "3300",
                    "50",
                    "IVA21",
                    "10",
                    "100",
                    false
            );


            // =================================================
            // HIGIENE PERSONAL
            // =================================================

            asegurarProducto(
                    "HIG-0001",
                    "INT-HIG-0001",
                    "7791000000026",
                    "Desodorante Aerosol 150 ml",
                    "Desodorante personal aerosol",
                    "F-01-01",
                    "Higiene Personal",
                    "Desodorantes",
                    "Rexona",
                    "CAJA",
                    "UN",
                    "12",
                    "24000",
                    "3100",
                    "55",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "HIG-0002",
                    "INT-HIG-0002",
                    "7791000000027",
                    "Shampoo 340 ml",
                    "Shampoo para cabello",
                    "F-02-01",
                    "Higiene Personal",
                    "Shampoo",
                    "Sedal",
                    "CAJA",
                    "UN",
                    "12",
                    "30000",
                    "3900",
                    "56",
                    "IVA21",
                    "12",
                    "120",
                    false
            );

            asegurarProducto(
                    "HIG-0003",
                    "INT-HIG-0003",
                    "7791000000028",
                    "Pasta Dental 90 g",
                    "Pasta dental",
                    "F-03-01",
                    "Higiene Personal",
                    "Higiene Bucal",
                    "Colgate",
                    "CAJA",
                    "UN",
                    "12",
                    "21000",
                    "2800",
                    "60",
                    "IVA21",
                    "12",
                    "120",
                    false
            );


            // =================================================
            // RESUMEN
            // =================================================

            System.out.println();
            System.out.println(
                    "===================================================="
            );
            System.out.println(
                    "CARGA DEMO FINALIZADA"
            );
            System.out.println(
                    "===================================================="
            );

            System.out.println(
                    "Registros creados:    "
                    + creados
            );

            System.out.println(
                    "Registros existentes: "
                    + existentes
            );

            System.out.println(
                    "Errores:               "
                    + errores
            );

            System.out.println();

            System.out.println(
                    "Productos actuales: "
                    + productoService.listarTodos().size()
            );

            System.out.println(
                    "Proveedores actuales: "
                    + proveedorService.listarTodos().size()
            );

            System.out.println();

            System.out.println(
                    "IMPORTANTE:"
            );

            System.out.println(
                    "Esta carga NO modificó stock."
            );

            System.out.println(
                    "El stock será generado mediante "
                    + "compras confirmadas."
            );

            System.out.println(
                    "===================================================="
            );


        } catch (Exception ex) {

            System.out.println();
            System.out.println(
                    "ERROR GENERAL EN CARGA DEMO:"
            );

            System.out.println(
                    ex.getMessage()
            );

            ex.printStackTrace();
        }
    }


    // =========================================================
    // ASEGURAR RUBRO
    // =========================================================

    private static Rubro asegurarRubro(
            String nombre,
            String descripcion) {

        Rubro existente =
                rubroService.buscarPorNombre(
                        nombre
                );

        if (existente != null) {

            existente(nombre);
            return existente;
        }

        ResultadoOperacion resultado =
                rubroService.crearRubro(
                        nombre,
                        descripcion
                );

        if (!resultado.isExitoso()) {

            error(
                    "Rubro "
                    + nombre
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado(nombre);

        return rubroService.buscarPorNombre(
                nombre
        );
    }


    // =========================================================
    // ASEGURAR CATEGORÍA
    // =========================================================

    private static Categoria asegurarCategoria(
            String nombreRubro,
            String nombreCategoria,
            String descripcion) {

        Categoria existente =
                categoriaService.buscarPorNombre(
                        nombreCategoria
                );

        if (existente != null) {

            existente(nombreCategoria);
            return existente;
        }

        Rubro rubro =
                rubroService.buscarPorNombre(
                        nombreRubro
                );

        if (rubro == null) {

            error(
                    "No existe rubro "
                    + nombreRubro
                    + " para categoría "
                    + nombreCategoria
            );

            return null;
        }

        ResultadoOperacion resultado =
                categoriaService.crearCategoria(
                        rubro.getIdRubro(),
                        nombreCategoria,
                        descripcion
                );

        if (!resultado.isExitoso()) {

            error(
                    "Categoría "
                    + nombreCategoria
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado(nombreCategoria);

        return categoriaService.buscarPorNombre(
                nombreCategoria
        );
    }


    // =========================================================
    // ASEGURAR MARCA
    // =========================================================

    private static Marca asegurarMarca(
            String nombre,
            String descripcion) {

        Marca existente =
                marcaService.buscarPorNombre(
                        nombre
                );

        if (existente != null) {

            existente(nombre);
            return existente;
        }

        ResultadoOperacion resultado =
                marcaService.crearMarca(
                        nombre,
                        descripcion
                );

        if (!resultado.isExitoso()) {

            error(
                    "Marca "
                    + nombre
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado(nombre);

        return marcaService.buscarPorNombre(
                nombre
        );
    }


    // =========================================================
    // ASEGURAR UNIDAD
    // =========================================================

    private static UnidadMedida asegurarUnidad(
            String codigo,
            String nombre,
            boolean permiteDecimales) {

        UnidadMedida existente =
                unidadService.buscarPorCodigo(
                        codigo
                );

        if (existente != null) {

            existente("Unidad " + codigo);
            return existente;
        }

        ResultadoOperacion resultado =
                unidadService.crearUnidad(
                        codigo,
                        nombre,
                        permiteDecimales
                );

        if (!resultado.isExitoso()) {

            error(
                    "Unidad "
                    + codigo
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado("Unidad " + codigo);

        return unidadService.buscarPorCodigo(
                codigo
        );
    }


    // =========================================================
    // ASEGURAR IVA
    // =========================================================

    private static TipoIva asegurarIva(
            String codigo,
            String nombre,
            String porcentaje) {

        TipoIva existente =
                ivaService.buscarPorCodigo(
                        codigo
                );

        if (existente != null) {

            existente(codigo);
            return existente;
        }

        ResultadoOperacion resultado =
                ivaService.crearTipoIva(
                        codigo,
                        nombre,
                        new BigDecimal(
                                porcentaje
                        )
                );

        if (!resultado.isExitoso()) {

            error(
                    codigo
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado(codigo);

        return ivaService.buscarPorCodigo(
                codigo
        );
    }


    // =========================================================
    // ASEGURAR PROVEEDOR
    // =========================================================

    private static Proveedor asegurarProveedor(
            String cuit,
            String razonSocial,
            String nombreComercial,
            String condicionIva,
            String direccion,
            String localidad,
            String provincia,
            String telefono,
            String email,
            String personaContacto,
            String telefonoContacto,
            String observaciones) {

        Proveedor existente =
                proveedorService.buscarPorCuit(
                        cuit
                );

        if (existente != null) {

            existente(
                    "Proveedor "
                    + nombreComercial
            );

            return existente;
        }

        Proveedor proveedor =
                new Proveedor();

        proveedor.setRazonSocial(
                razonSocial
        );

        proveedor.setNombreComercial(
                nombreComercial
        );

        proveedor.setCuit(
                cuit
        );

        proveedor.setCondicionIva(
                condicionIva
        );

        proveedor.setDireccion(
                direccion
        );

        proveedor.setLocalidad(
                localidad
        );

        proveedor.setProvincia(
                provincia
        );

        proveedor.setTelefono(
                telefono
        );

        proveedor.setEmail(
                email
        );

        proveedor.setPersonaContacto(
                personaContacto
        );

        proveedor.setTelefonoContacto(
                telefonoContacto
        );

        proveedor.setObservaciones(
                observaciones
        );

        ResultadoOperacion resultado =
                proveedorService.crear(
                        proveedor
                );

        if (!resultado.isExitoso()) {

            error(
                    "Proveedor "
                    + nombreComercial
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado(
                "Proveedor "
                + nombreComercial
        );

        return proveedorService.buscarPorCuit(
                cuit
        );
    }


    // =========================================================
    // ASEGURAR PRODUCTO
    // =========================================================

    private static Producto asegurarProducto(
            String codigo,
            String codigoInterno,
            String codigoBarra,
            String nombre,
            String descripcion,
            String ubicacion,
            String nombreRubro,
            String nombreCategoria,
            String nombreMarca,
            String codigoUnidadCompra,
            String codigoUnidadVenta,
            String factorConversion,
            String precioCompra,
            String precioVenta,
            String margen,
            String codigoIva,
            String stockMinimo,
            String stockMaximo,
            boolean pesable) {

        Producto existente =
                productoService.buscarPorCodigo(
                        codigo
                );

        if (existente != null) {

            existente(
                    "Producto "
                    + codigo
                    + " - "
                    + nombre
            );

            return existente;
        }


        // -----------------------------------------------------
        // RESOLVER RELACIONES
        // -----------------------------------------------------

        Rubro rubro =
                rubroService.buscarPorNombre(
                        nombreRubro
                );

        Categoria categoria =
                categoriaService.buscarPorNombre(
                        nombreCategoria
                );

        Marca marca =
                marcaService.buscarPorNombre(
                        nombreMarca
                );

        UnidadMedida unidadCompra =
                unidadService.buscarPorCodigo(
                        codigoUnidadCompra
                );

        UnidadMedida unidadVenta =
                unidadService.buscarPorCodigo(
                        codigoUnidadVenta
                );

        TipoIva iva =
                ivaService.buscarPorCodigo(
                        codigoIva
                );


        // -----------------------------------------------------
        // VALIDAR RELACIONES
        // -----------------------------------------------------

        if (rubro == null
                || categoria == null
                || marca == null
                || unidadCompra == null
                || unidadVenta == null
                || iva == null) {

            error(
                    "No se pudo crear producto "
                    + codigo
                    + " porque falta alguna relación."
            );

            return null;
        }


        // -----------------------------------------------------
        // CREAR MEDIANTE PRODUCTOSERVICE
        // -----------------------------------------------------

        ResultadoOperacion resultado =
                productoService.crearProducto(

                        codigo,

                        codigoInterno,

                        codigoBarra,

                        nombre,

                        descripcion,

                        ubicacion,

                        rubro.getIdRubro(),

                        categoria.getIdCategoria(),

                        marca.getIdMarca(),

                        unidadCompra.getIdUnidad(),

                        unidadVenta.getIdUnidad(),

                        new BigDecimal(
                                factorConversion
                        ),

                        new BigDecimal(
                                precioCompra
                        ),

                        new BigDecimal(
                                precioVenta
                        ),

                        new BigDecimal(
                                margen
                        ),

                        iva.getIdIva(),

                        new BigDecimal(
                                stockMinimo
                        ),

                        new BigDecimal(
                                stockMaximo
                        ),

                        true,       // controlaStock

                        false,      // permiteVentaSinStock

                        pesable,

                        true,       // permiteDescuento

                        "Producto generado por CargaDatosDemo"
                );


        if (!resultado.isExitoso()) {

            error(
                    "Producto "
                    + codigo
                    + " - "
                    + nombre
                    + ": "
                    + resultado.getMensaje()
            );

            return null;
        }

        creado(
                "Producto "
                + codigo
                + " - "
                + nombre
        );

        return productoService.buscarPorCodigo(
                codigo
        );
    }


    // =========================================================
    // MENSAJES
    // =========================================================

    private static void creado(
            String texto) {

        creados++;

        System.out.println(
                "[CREADO] "
                + texto
        );
    }


    private static void existente(
            String texto) {

        existentes++;

        System.out.println(
                "[YA EXISTE] "
                + texto
        );
    }


    private static void error(
            String texto) {

        errores++;

        System.out.println(
                "[ERROR] "
                + texto
        );
    }
}