package sistemaAutogestion;
import dominio.Barrio;
import dominio.Bicicleta;
import dominio.Estacion;
import dominio.EstadoBicicleta;
import dominio.Retiro;
import dominio.TipoBicicleta;
import dominio.TipoUso;
import dominio.Usuario;
import tads.ColaSE;
import tads.ListaBarrio;
import tads.ListaBicicletas;
import tads.ListaEstaciones;
import tads.ListaSE;
import tads.ListaUsuarios;
import tads.MatrizEstaciones;
import tads.NodoSE;
import tads.PilaRetiros;

//version original comentario para saber si publique en git
//Agregar aquí nombres y números de estudiante de los integrantes del equipo
//Leandro Guzman 321788

public class Sistema implements IObligatorio {
    
    private ListaUsuarios usuarios;
    private ListaEstaciones estaciones;
    private ListaBicicletas deposito;
    private PilaRetiros historialRetiros;
    private ListaBarrio barrios;
  
    public ListaBicicletas getDeposito() {
    return deposito;
    }
    public ListaEstaciones getEstaciones() {
    return estaciones;
    }
    public ListaUsuarios getUsuarios() {
    return usuarios;
    }

    public PilaRetiros getHistorialRetiros() {
        return historialRetiros;
    }

    public ListaBarrio getBarrios() {
        return barrios;
    }
    
    //2.1. Crear Sistema de Gestión--------------------------------------
  @Override
    public Retorno crearSistemaDeGestion() {
        usuarios = new ListaUsuarios();
        estaciones = new ListaEstaciones();
        deposito = new ListaBicicletas();
        barrios = new ListaBarrio();

        historialRetiros = new PilaRetiros();  // inicializar la pila
    
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

      
        return Retorno.error2();
    }


//2.8. Asignar bicicleta a estación ----------------------------------------------------
  @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {

        if (codigo == null || codigo.isEmpty() || nombreEstacion == null || nombreEstacion.isEmpty()) {
            return Retorno.error1();
        }

        Bicicleta bici = deposito.buscar(codigo); // buscar en depósito
        Estacion estacionActual = null;

        if (bici == null) { 
            // buscar en estaciones
            for (int i = 0; i < estaciones.longitud(); i++) {
                try {
                    Estacion e = estaciones.obtener(i);
                    Bicicleta b = e.buscarBicicleta(codigo);
                    if (b != null) {
                        bici = b;
                        estacionActual = e;
                        break;
                    }
                } catch (Exception ex) { }
            }
        }

        if (bici == null || bici.getEstado() != EstadoBicicleta.Disponible ) {
            return Retorno.error2(); // no disponible para asignar a estación
        }


        Estacion destino = estaciones.buscar(nombreEstacion);
        if (destino == null) return Retorno.error3();
        if (!destino.hayLugar()) return Retorno.error4();

        // Sacar de la estación actual o del depósito
         if (estacionActual != null) {
             estacionActual.sacarBicicleta(codigo); // sacar de la estación donde está actualmente
         } else {
             deposito.sacar(codigo); // si estaba en depósito
         }

        // Anclar en destino y actualizar estado/estación
        bici.setEstado(EstadoBicicleta.Disponible);
        bici.setEstacionActual(destino);
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

            // Incrementar contador de veces alquilada
            disponible.setVecesAlquilada(disponible.getVecesAlquilada() + 1);

            return Retorno.ok();
        }

        // 2) Si no hay bicicletas → usuario entra a la cola
        est.getEsperaAlquiler().encolar(u);
        return Retorno.ok();
    }

//2.10. Devolver bicicleta -----------------------------------
    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        // ERROR 1: parámetros inválidos
        if (cedula == null || cedula.isEmpty() ||
            nombreEstacionDestino == null || nombreEstacionDestino.isEmpty()) {
            return Retorno.error1();
        }

        // Buscar usuario
        Usuario u = usuarios.buscar(cedula);
        if (u == null || u.getBicicletaActual() == null) {
            return Retorno.error2();
        }

        // Buscar estación destino
        Estacion destino = estaciones.buscar(nombreEstacionDestino);
        if (destino == null) {
            return Retorno.error3();
        }

        Bicicleta bici = u.getBicicletaActual();

        // Si hay lugar en la estación → anclar la bici
        if (destino.hayLugar()) {
            destino.anclarBicicleta(bici);
            bici.setEstado(EstadoBicicleta.Disponible);
            u.setBicicletaActual(null);

            // Si hay usuarios esperando en la estación, entregar bici automáticamente
            if (!destino.getEsperaAlquiler().estaVacia()) {
                Usuario primerUsuario = destino.getEsperaAlquiler().desencolar();
                primerUsuario.setBicicletaActual(destino.getAnclajes().buscarDisponible());
                // Sacar bici del anclaje y marcar como alquilada
                destino.getAnclajes().sacar(primerUsuario.getBicicletaActual().getCodigo());
                primerUsuario.getBicicletaActual().setEstado(EstadoBicicleta.Alquilada);
            }

            return Retorno.ok();
        } else {
            // Si no hay lugar → poner usuario en espera de anclaje
            destino.getEsperaAnclaje().encolar(u);
            return Retorno.ok();
        }
    }

//2.11. deshacerUltimosRetiros -----------------------------------
   @Override
   
    public Retorno deshacerUltimosRetiros(int n) {
        if (n <= 0)
            return Retorno.error1();

        if (historialRetiros.estaVacia())
            return Retorno.error2();  // si tenés un error para "no hay retiros"

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {

            if (historialRetiros.estaVacia())
                break;

            Retiro r = historialRetiros.desapilar();

            Bicicleta bici = r.getBicicleta();
            Usuario usuario = r.getUsuario();
            Estacion origen = r.getEstacionOrigen();

            // devolver bici a estación de origen
            if (origen.hayLugar()) {
                origen.getAnclajes().insertarOrdenado(bici);// <<--- CORRECTO
            } else {
                origen.getEsperaAnclajeBici().encolar(bici);   // <<--- CORRECCIÓN CLAVE
            }

            // revertir alquiler
            usuario.setBicicletaActual(null);

            // armar string
            if (sb.length() > 0)
                sb.append("|");

            sb.append(bici.getCodigo())
              .append("#")
              .append(usuario.getCedula())
              .append("#")
              .append(origen.getNombre());
        }

        return Retorno.ok(sb.toString());
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




//3.5. Listar bicis de estación------------------------
    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        // ERROR 1: parámetro inválido
        if (nombreEstacion == null || nombreEstacion.isEmpty()) {
            return Retorno.error1();
        }

        // Buscar estación
        Estacion est = estaciones.buscar(nombreEstacion);
        if (est == null) {
            return Retorno.error3(); // si querés usar error3 para "no existe"
        }

        // Lista de bicicletas de la estación
        ListaBicicletas anclajes = est.getAnclajes();

        // Recorrer la lista y armar el string
        StringBuilder sb = new StringBuilder();
        NodoSE<Bicicleta> aux = anclajes.getLista().getInicio();
        while (aux != null) {
            if (sb.length() > 0) sb.append("|");
            sb.append(aux.getDato().getCodigo());
            aux = aux.getSiguiente();
        }

        return Retorno.ok(sb.toString());
    }

//3.6. Estaciones con disponibilidad mayor ------------------------
   @Override
    public Retorno estacionesConDisponibilidad(int n) {
        if (n <= 1)
            return Retorno.error1();

        int contador = 0;
        NodoSE<Estacion> act = estaciones.getPrimero();

        while (act != null) {
            Estacion e = act.getDato();
            if (e.cantidadDisponibles() > n)
                contador++;

            act = act.getSiguiente();
        }

        return Retorno.ok(contador);
    }

//3.7. Ocupación promedio por barrio  ------------------------
  @Override
    public Retorno ocupacionPromedioXBarrio() {
        if (estaciones.vacia()) {
            return Retorno.error1();
        }

        ListaBarrio listaBarrios = new ListaBarrio();

        // Recorrer estaciones y acumular datos por barrio
        for (int i = 0; i < estaciones.longitud(); i++) {
            try {
                Estacion e = estaciones.obtener(i);
                listaBarrios.agregarOBuscarYActualizar(
                    e.getBarrio(),
                    e.getAnclajes().contar(),
                    e.getCapacidad()
                );
            } catch (Exception ex) {
                // ignorar errores individuales
            }
        }

        listaBarrios.ordenar(); // ordenar alfabéticamente

        // Armar string de salida
        StringBuilder sb = new StringBuilder();
        ListaSE<Barrio> barrios = listaBarrios.getLista();

        for (int i = 0; i < barrios.longitud(); i++) {
            try {
                Barrio b = barrios.obtener(i);
                int porcentaje = 0;
                if (b.getCapacidadTotal() > 0) {
                    porcentaje = (int) Math.round((double) b.getBicisAncladas() * 100 / b.getCapacidadTotal());
                }

                if (sb.length() > 0) sb.append("|");
                sb.append(b.getNombre()).append("#").append(porcentaje);
            } catch (Exception ex) {}
        }

        return Retorno.ok(sb.toString());
    }



//3.8. Ranking por tipo de uso-----------
   @Override
    public Retorno rankingTiposPorUso() {
        ListaSE<TipoUso> ranking = new ListaSE<>();

        // Recorrer bicicletas del depósito
        NodoSE<Bicicleta> act = deposito.getLista().getInicio();
        while (act != null) {
            Bicicleta b = act.getDato();
            agregarOTipoUso(ranking, b);
            act = act.getSiguiente();
        }

        // Recorrer bicicletas en estaciones
        NodoSE<Estacion> estAct = estaciones.getLista().getInicio();
        while (estAct != null) {
            Estacion e = estAct.getDato();
            NodoSE<Bicicleta> biciAct = e.getAnclajes().getLista().getInicio();
            while (biciAct != null) {
                agregarOTipoUso(ranking, biciAct.getDato());
                biciAct = biciAct.getSiguiente();
            }
            estAct = estAct.getSiguiente();
        }

        // Ordenar ranking
        ordenarListaSE(ranking);

        // Construir string final
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ranking.longitud(); i++) {
            try {
                TipoUso t = ranking.obtener(i);
                if (sb.length() > 0) sb.append("|");
                sb.append(t.getTipo().name()).append("#").append(t.getCantidadAlquileres());
            } catch (Exception ex) {}
        }

        return Retorno.ok(sb.toString());
    }

    // Método auxiliar: agrega o incrementa tipo en ranking
    private void agregarOTipoUso(ListaSE<TipoUso> ranking, Bicicleta bici) {
        if (bici == null) return;
        TipoBicicleta tipo = bici.getTipo();
        for (int i = 0; i < ranking.longitud(); i++) {
            try {
                TipoUso t = ranking.obtener(i);
                if (t.getTipo() == tipo) {
                    t.incrementar(bici.getVecesAlquilada());
                    return;
                }
            } catch (Exception e) {}
        }
        // Si no existía, agregar nuevo
        TipoUso nuevo = new TipoUso(tipo);
        nuevo.incrementar(bici.getVecesAlquilada());
        ranking.insertar(nuevo, ranking.longitud());
    }

    // Método auxiliar para ordenar ListaSE según compareTo
    private <T extends Comparable<T>> void ordenarListaSE(ListaSE<T> lista) {
        if (lista.vacia() || lista.longitud() == 1) return;
        for (int i = 0; i < lista.longitud() - 1; i++) {
            for (int j = i + 1; j < lista.longitud(); j++) {
                try {
                    T a = lista.obtener(i);
                    T b = lista.obtener(j);
                    if (a.compareTo(b) > 0) {
                        lista.insertar(b, i);
                        lista.eliminar(j + 1); // ajustar índice
                    }
                } catch (Exception ex) {}
            }
        }
    }

//3.9. Usuarios en espera por alquiler----------------------------- 
   @Override
    public Retorno usuariosEnEspera(String nombreEstacion) {

        // Buscar estación
        Estacion est = estaciones.buscar(nombreEstacion);
        if (est == null) {
            return Retorno.error1();    // “ERROR” genérico según lo que permite Retorno
        }

        // Obtener cola de espera
        ColaSE<Usuario> espera = est.getEsperaAlquiler();

        // Si está vacía -> OK pero string vacío
        if (espera.estaVacia()) {
            return Retorno.ok("");
        }

        // Construir string en orden de llegada
        StringBuilder sb = new StringBuilder();
        espera.recorrer(u -> {
            if (sb.length() > 0) sb.append("|");
            sb.append(u.getCedula());
        });

        return Retorno.ok(sb.toString());
    }


//3.10. Usuario con mayor cantidad de alquileres

  @Override
    public Retorno usuarioMayor() {
        if (usuarios.estaVacia()) {
            return Retorno.error1(); // o Retorno.ok("") si no hay usuarios, según convenga
        }

        Usuario mayor = null;

        for (int i = 0; i < usuarios.longitud(); i++) {
            try {
                Usuario u = usuarios.obtener(i);
                if (mayor == null || 
                    u.getCantidadAlquileres() > mayor.getCantidadAlquileres() || 
                    (u.getCantidadAlquileres() == mayor.getCantidadAlquileres() &&
                     u.getCedula().compareTo(mayor.getCedula()) < 0)) {
                    mayor = u;
                }
            } catch (Exception ex) {
                // ignorar errores individuales
            }
        }

        if (mayor == null) return Retorno.error1();
        return Retorno.ok(mayor.getCedula());
    }


}
