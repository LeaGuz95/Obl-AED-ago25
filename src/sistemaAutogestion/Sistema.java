package sistemaAutogestion;
import dominio.Bicicleta;
import dominio.Estacion;
import dominio.EstadoBicicleta;
import dominio.TipoBicicleta;
import dominio.Usuario;
import tads.ListaBicicletas;
import tads.ListaEstaciones;
import tads.ListaSE;
import tads.ListaUsuarios;
import tads.MatrizEstaciones;

//version original comentario para saber si publique en git
//Agregar aquí nombres y números de estudiante de los integrantes del equipo
//Leandro Guzman 321788

public class Sistema implements IObligatorio {
    
    private ListaUsuarios usuarios;
    private ListaEstaciones estaciones;
    private ListaBicicletas deposito;

    
  
    public ListaBicicletas getDeposito() {
    return deposito;
    }
    public ListaEstaciones getEstaciones() {
    return estaciones;
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
        nueva.setEstado(EstadoBicicleta.Disponible); 
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

//2.7. Eliminar bicicleta----------------------------------------------------
   @Override
    public Retorno eliminarEstacion(String nombre) {

        // 1) Validación de nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            return Retorno.error1(); // Nombre inválido
        }

        Estacion est = estaciones.buscar(nombre);
        if (est == null) {
            return Retorno.error2(); // Estación inexistente
        }

        // 2) No debe tener bicicletas ancladas
        if (est.getAnclajes().contar() > 0) {
            return Retorno.error3(); // Tiene bicicletas ancladas
        }

        // 3) No debe tener usuarios en cola de espera
        if (est.getEsperaAlquiler() != null && !est.getEsperaAlquiler().estaVacia()) {
            return Retorno.error3(); // También ERROR_3 según letra
        }

        // 4) Eliminar estación de la lista
        ListaSE<Estacion> lista = estaciones.getLista();

        for (int i = 0; i < lista.longitud(); i++) {
            try {
                if (lista.obtener(i).getNombre().equalsIgnoreCase(nombre)) {
                    lista.eliminar(i);
                    return Retorno.ok();
                }
            } catch (Exception e) {
                // ignorar, tu ListaSE lanza excepciones por índice
            }
        }

        // fallback improbable
        return Retorno.error2();
    }


//2.8. Asignar bicicleta a estación ----------------------------------------------------
    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {

        // ERROR 1: parámetros inválidos
        if (codigo == null || codigo.isEmpty() ||
            nombreEstacion == null || nombreEstacion.isEmpty()) {
            return Retorno.error1();
        }

        // Buscar bicicleta
        Bicicleta bici = deposito.buscar(codigo);
        Estacion estacionActual = null;

        // Si no está en el depósito, buscar en estaciones
        if (bici == null) {
            ListaSE<Estacion> lista = estaciones.getLista();
            for (int i = 0; i < lista.longitud(); i++) {
                try {
                    Estacion e = lista.obtener(i);
                    if (e.buscarBicicleta(codigo) != null) {
                        estacionActual = e;
                        bici = e.buscarBicicleta(codigo);
                        break;
                    }
                } catch (Exception ex) { }
            }
        }

        // ERROR 2: bici no existe o no está disponible
        if (bici == null || bici.getEstado() != EstadoBicicleta.Disponible) {
            return Retorno.error2();
        }

        // Buscar estación destino
        Estacion destino = estaciones.buscar(nombreEstacion);

        // ERROR 3: estación no existe
        if (destino == null) {
            return Retorno.error3();
        }

        // ERROR 4: sin espacio
        if (!destino.hayLugar()) {
            return Retorno.error4();
        }

        // Si la bici está en otra estación → removerla de allí
        if (estacionActual != null) {
            estacionActual.sacarBicicleta(codigo);
        } else {
            // Si estaba en depósito → sacarla del depósito
            deposito.sacar(codigo);
        }

        // Finalmente anclar en estación destino
        destino.anclarBicicleta(bici);

        return Retorno.ok();
    }

//2.9. Alquilar bicicleta ------
    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {

        // ERROR 1: parámetros inválidos
        if (cedula == null || cedula.isEmpty() ||
            nombreEstacion == null || nombreEstacion.isEmpty()) {
            return Retorno.error1();
        }

        // Buscar usuario
        Usuario u = usuarios.buscar(cedula);
        if (u == null) {
            return Retorno.error2();
        }

        // Buscar estación
        Estacion est = estaciones.buscar(nombreEstacion);
        if (est == null) {
            return Retorno.error3();
        }

        // 1) Buscar bicicleta disponible en la estación
        Bicicleta disponible = est.getAnclajes().buscarDisponible();

        if (disponible != null) {
            // Hay una → asignar directo
            est.getAnclajes().sacar(disponible.getCodigo());
            disponible.setEstado(EstadoBicicleta.Alquilada);
            u.setBicicletaActual(disponible);
            return Retorno.ok();
        }

        // 2) Si no hay bicicletas → usuario entra a la cola
        est.getEsperaAlquiler().encolar(u);
        return Retorno.ok();
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
