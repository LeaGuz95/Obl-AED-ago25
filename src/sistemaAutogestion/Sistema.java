package sistemaAutogestion;
import dominio.Bicicleta;
import dominio.Estacion;
import dominio.EstadoBicicleta;
import dominio.TipoBicicleta;
import dominio.Usuario;
import tads.ListaBicicletas;
import tads.ListaEstaciones;
import tads.ListaUsuarios;
import tads.MatrizEstaciones;


//Agregar aquí nombres y números de estudiante de los integrantes del equipo
//Leandro Guzman 321788

public class Sistema implements IObligatorio {
    
    private ListaUsuarios usuarios;
    private ListaEstaciones estaciones;
    private ListaBicicletas deposito;

    
  
    public ListaBicicletas getDeposito() {
    return deposito;
    }
    
    //2.1. Crear Sistema de Gestión--------------------------------------
  @Override
    public Retorno crearSistemaDeGestion() {
        usuarios = new ListaUsuarios();
        estaciones = new ListaEstaciones();
        deposito = new ListaBicicletas();
     
       return Retorno.ok();
    }
    
    //2.2. Registrar Estación--------------------------------------------
    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {

        if (nombre == null || barrio == null || nombre.trim().isEmpty() || barrio.trim().isEmpty()) {
            return Retorno.error1();
        }

        if (capacidad <= 0) {
            return Retorno.error2();
        }

        if (estaciones.existeEstacion(nombre)) {
            return Retorno.error3();
        }

        Estacion nueva = new Estacion(nombre, barrio, capacidad);
        estaciones.insertarOrdenado(nueva); 

        return Retorno.ok();
    }

    //2.3. Registrar Usuario--------------------------------------------
   @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        if (cedula == null || nombre == null || cedula.isEmpty() || nombre.isEmpty()) {
            return Retorno.error1();
        }

        if (!cedula.matches("\\d{8}")) {
            return Retorno.error2();
        }

        if (usuarios.existeUsuario(cedula)) {
            return Retorno.error3();
        }

        usuarios.insertarOrdenado(new Usuario(cedula, nombre));
        return Retorno.ok();
    }

    //2.4. Registrar Bicicleta--------------------------------------------
   @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        if (codigo == null || tipo == null || codigo.isEmpty() || tipo.isEmpty()) {
            return Retorno.error1();
        }

        if (codigo.length() != 6) {
            return Retorno.error2();
        }

        TipoBicicleta tipoEnum;
        try {
            tipoEnum = TipoBicicleta.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Retorno.error3();
        }

        if (deposito.buscar(codigo) != null) { 
            return Retorno.error4();
        }

        Bicicleta nueva = new Bicicleta(codigo, tipoEnum);
        deposito.agregar(nueva);

        return Retorno.ok();
    }
    
    //2.5. Poner bicicleta en mantenimiento---------------------------------------------
   @Override
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        if (codigo == null || codigo.isEmpty() || motivo == null || motivo.isEmpty()) {
            return Retorno.error1();
        }

        Bicicleta bici = deposito.buscar(codigo);
        if (bici == null) {
            return Retorno.error2();
        }

        if (bici.getEstado() == EstadoBicicleta.Alquilada) {
            return Retorno.error3();
        }

        if (bici.getEstado() == EstadoBicicleta.Mantenimiento) {
            return Retorno.error4();
        }


        bici.setEstado(EstadoBicicleta.Mantenimiento);
        bici.setMotivoMantenimiento(motivo);

        // depósito
       if (deposito.buscar(bici.getCodigo()) == null) {
            deposito.agregar(bici);
        }


        return Retorno.ok();
    }

    //2.6. Reparar bicicleta----------------------------------------------------
    @Override
    public Retorno repararBicicleta(String codigo) {
        
        if (codigo == null || codigo.isEmpty()) {
            return Retorno.error1();
        }

        // bicicleta en el depósito
        Bicicleta bici = deposito.buscar(codigo);
        if (bici == null) {
            return Retorno.error2();
        }

        // esta en mantenimiento??
        if (bici.getEstado() != EstadoBicicleta.Mantenimiento) {
            return Retorno.error3();
        }

        // Cambiar estado a disponible y asegurar que quede en depósito
        bici.setEstado(EstadoBicicleta.Disponible);
        bici.setMotivoMantenimiento(null); // limpiar motivo
        if (deposito.buscar(codigo) == null) {
            deposito.agregar(bici);
        }

        return Retorno.ok();
    }


    @Override
    public Retorno eliminarEstacion(String nombre) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno deshacerUltimosRetiros(int n) {
        return Retorno.noImplementada();
    }
    
    //3.1.Obtener Usuario------------------------------------------------------
   @Override
    public Retorno obtenerUsuario(String cedula) {
        // parámetros
        if (cedula == null || cedula.isEmpty()) {
            return Retorno.error1();
        }

        // formato de cédula
        if (!cedula.matches("\\d{8}")) {
            return Retorno.error2();
        }

        //  Buscar el usuario
        Usuario u = usuarios.buscar(cedula);
        if (u == null) {
            return Retorno.error3();
        }

      
          return Retorno.ok(u.getNombre() + "#" + u.getCedula());
    }

    //3.2. Listar usuarios----------------------------------------------------
    @Override
    public Retorno listarUsuarios() {
    if (usuarios == null || usuarios.estaVacia()) return Retorno.ok("");

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < usuarios.longitud(); i++) {
        Usuario u = usuarios.obtener(i); 
        if (sb.length() > 0) sb.append("|");
        sb.append(u.getNombre()).append("#").append(u.getCedula());
    }

    return Retorno.ok(sb.toString());
}



    
    //3.3. Listar bicis en depósito--------------------------------------------
    @Override
    public Retorno listarBicisEnDeposito() {
        if (deposito == null || deposito.estaVacia()) {
            return Retorno.error1(); // 
        }

        String listado = deposito.listarConEstado();
        return Retorno.ok(listado);
    }

    //3.4 informaciónMapa------------------------------------------
  
   @Override
    public Retorno informaciónMapa(String[][] mapa) {
           MatrizEstaciones m = new MatrizEstaciones(mapa);

           if (!m.esValido())
               return Retorno.ok("0#ambas|no existe");

           int maxFila = m.maximoFila();
           int maxColumna = m.maximoColumna();

           String tipoMax;
           if (maxFila > maxColumna)
               tipoMax = "fila";
           else if (maxColumna > maxFila)
               tipoMax = "columna";
           else
               tipoMax = "ambas";

           boolean ascendente = m.hayTresColumnasAscendentes();

          int maximo;
            switch (tipoMax) {
                case "fila":
                    maximo = maxFila;
                    break;
                case "columna":
                    maximo = maxColumna;
                    break;
                default:
                    maximo = maxFila;
                    break;
            }

           String valorString = maximo + "#" + tipoMax + "|" + (ascendente ? "si existe" : "no existe");


           return Retorno.ok(valorString);
       }





    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno ocupacionPromedioXBarrio() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno rankingTiposPorUso() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno usuariosEnEspera(String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno usuarioMayor() {
        return Retorno.noImplementada();
    }

}
