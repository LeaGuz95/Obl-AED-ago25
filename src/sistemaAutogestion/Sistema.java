package sistemaAutogestion;

import dominio.Bicicleta;
import dominio.Estacion;
import dominio.EstadoBicicleta;
import dominio.Retiro;
import dominio.TipoBicicleta;
import dominio.TipoUso;
import dominio.Usuario;
import tads.ColaSE;

import tads.ListaBicicletas;
import tads.ListaEstaciones;
import tads.ListaSE;
import tads.ListaUsuarios;
import tads.MatrizEstaciones;
import tads.NodoSE;
import tads.PilaRetiros;


//Agregar aquí nombres y números de estudiante de los integrantes del equipo
//Leandro Guzman 321788 
//PARTE 2

public class Sistema implements IObligatorio {
    
    private ListaUsuarios usuarios;
    private ListaEstaciones estaciones;
    private ListaBicicletas deposito;
    private PilaRetiros historialRetiros;
   
  
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

   
    
    //2.1. Crear Sistema de Gestión--------------------------------------
  @Override
    public Retorno crearSistemaDeGestion() {
        usuarios = new ListaUsuarios();
        estaciones = new ListaEstaciones();
        deposito = new ListaBicicletas();
     

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
        if (nombre == null || nombre.trim().isEmpty())
            return Retorno.error1();

        // 2) Buscar estación
        Estacion est = estaciones.buscar(nombre);
        if (est == null) return Retorno.error2();

        // 3) Validar que no tenga bicicletas ni usuarios en espera
        if (est.getAnclajes().contar() > 0 || 
            (est.getEsperaAlquiler() != null && !est.getEsperaAlquiler().estaVacia())) {
            return Retorno.error3();
        }

        // 4) Eliminar estación de la lista
        for (int i = 0; i < estaciones.longitud(); i++) {
            try {
                if (estaciones.obtener(i).getNombre().equalsIgnoreCase(nombre)) {
                    estaciones.getLista().eliminar(i);
                    return Retorno.ok();
                }
            } catch (Exception e) {
              
            }
        }

        // si no se pudo eliminar, devolver ERROR_2
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
        destino.anclarBicicleta(bici);
        bici.setEstado(EstadoBicicleta.Disponible);
        bici.setEstacionActual(destino);

       entregarBicisAEspereUsuarios(destino);



            
        return Retorno.ok();
    }

        // Entregar bicicletas disponibles a usuarios en espera de alquiler
    private void entregarBicisAEspereUsuarios(Estacion destino) {
        while (!destino.getEsperaAlquiler().estaVacia()) {
            // Buscar la primera bici disponible
            Bicicleta biciDisponible = destino.getAnclajes().buscarDisponible();
            if (biciDisponible == null) break; // No hay más bicis disponibles

            // Tomar el primer usuario de la cola
            Usuario usuarioEnEspera = destino.getEsperaAlquiler().desencolar();

            // Asignar la bicicleta
            destino.getAnclajes().sacar(biciDisponible.getCodigo());
            biciDisponible.setEstado(EstadoBicicleta.Alquilada);
            usuarioEnEspera.setBicicletaActual(biciDisponible);

            // Incrementar contador de veces alquilada
            biciDisponible.setVecesAlquilada(biciDisponible.getVecesAlquilada() + 1);
        }
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
            u.sumarAlquiler();
            // Incrementar contador de veces alquilada
            disponible.setVecesAlquilada(disponible.getVecesAlquilada() + 1);

            
                // Registrar retiro en el historial
            Retiro r = new Retiro(disponible, u, est);
            historialRetiros.apilar(r);
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

        // Buscar estación destino primero
        Estacion destino = estaciones.buscar(nombreEstacionDestino);
        if (destino == null) {
            return Retorno.error3();
        }

        // Buscar usuario
        Usuario u = usuarios.buscar(cedula);
        if (u == null || u.getBicicletaActual() == null) {
            return Retorno.error2();
        }

        Bicicleta bici = u.getBicicletaActual();

        if (destino.hayLugar()) {
            // Si hay lugar, anclar la bici
            destino.anclarBicicleta(bici);
            bici.setEstado(EstadoBicicleta.Disponible);
            u.setBicicletaActual(null);

            // Entregar automáticamente a usuarios en espera de alquiler
            while (!destino.getEsperaAlquiler().estaVacia() && destino.cantidadDisponibles() > 0) {
                Usuario primerUsuario = destino.getEsperaAlquiler().desencolar();
                Bicicleta biciParaAlquilar = destino.getAnclajes().buscarDisponible();
                if (biciParaAlquilar != null) {
                    destino.getAnclajes().sacar(biciParaAlquilar.getCodigo());
                    biciParaAlquilar.setEstado(EstadoBicicleta.Alquilada);
                    primerUsuario.setBicicletaActual(biciParaAlquilar);
                    biciParaAlquilar.setVecesAlquilada(biciParaAlquilar.getVecesAlquilada() + 1);
                }
            }
        } else {
            // Si no hay lugar → usuario queda en espera de anclaje
            destino.getEsperaAnclaje().encolar(u);
             u.setBicicletaActual(null); 
            // Mantener la bici “temporalmente” con el usuario hasta que pueda anclarla
            // u.getBicicletaActual() sigue siendo bici
            return Retorno.ok();
        }

        return Retorno.ok();
    }



//2.11. deshacerUltimosRetiros -----------------------------------
   @Override
   
    public Retorno deshacerUltimosRetiros(int n) {
        if (n <= 0)
            return Retorno.error1();

        if (historialRetiros.estaVacia())
            return Retorno.error2();  

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
                origen.getAnclajes().insertarOrdenado(bici);
            } else {
                origen.getEsperaAnclajeBici().encolar(bici);  
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
        if (nombreEstacion == null || nombreEstacion.isEmpty()) 
            return Retorno.error1();

        Estacion est = estaciones.buscar(nombreEstacion);
        if (est == null) 
            return Retorno.error2();

        ListaBicicletas anclajes = est.getAnclajes();
        if (anclajes.estaVacia()) 
            return Retorno.ok(""); // Devuelve string vacío si no hay bicis

        // Recorremos una sola vez y vamos insertando códigos en orden creciente
        StringBuilder sb = new StringBuilder();
        NodoSE<Bicicleta> aux = anclajes.getLista().getInicio();

        while (aux != null) {
            String codigo = aux.getDato().getCodigo();

            if (sb.length() == 0) {
                sb.append(codigo);
            } else {
                // Inserción ordenada en el string
                String[] partes = sb.toString().split("\\|");
                int pos = 0;
                while (pos < partes.length && partes[pos].compareTo(codigo) < 0) {
                    pos++;
                }

                StringBuilder nueva = new StringBuilder();
                for (int i = 0; i < pos; i++) nueva.append(partes[i]).append("|");
                nueva.append(codigo);
                for (int i = pos; i < partes.length; i++) nueva.append("|").append(partes[i]);
                sb = nueva;
            }
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
        // contar solo si la estación tiene más de n bicis disponibles
        if (e.cantidadDisponibles() > n)
            contador++;
        act = act.getSiguiente();
    }

    return Retorno.ok(contador); // valorInt = cantidad de estaciones
}



//3.7. Ocupación promedio por barrio  ------------------------
  @Override
    public Retorno ocupacionPromedioXBarrio() {
        if (estaciones == null || estaciones.vacia()) return Retorno.error1();

        ListaSE<String> barrios = new ListaSE<>();

        // 1. Obtener barrios únicos
        NodoSE<Estacion> act = estaciones.getPrimero();
        while (act != null) {
            String b = act.getDato().getBarrio();
            boolean existe = false;
            NodoSE<String> iter = barrios.getInicio();
            while (iter != null) {
                if (iter.getDato().equalsIgnoreCase(b)) {
                    existe = true;
                    break;
                }
                iter = iter.getSiguiente();
            }
            if (!existe) barrios.adicionar(b);
            act = act.getSiguiente();
        }

        // 2. Ordenar barrios alfabéticamente
        barrios.bubbleSort((s1, s2) -> s1.compareToIgnoreCase(s2));

        // 3. Calcular porcentaje por barrio
        StringBuilder sb = new StringBuilder();
        NodoSE<String> nodoBarrio = barrios.getInicio();
        while (nodoBarrio != null) {
            String barrio = nodoBarrio.getDato();
            int totalBicis = 0;
            int totalCapacidad = 0;

            NodoSE<Estacion> aux = estaciones.getPrimero();
            while (aux != null) {
                Estacion e = aux.getDato();
                if (e.getBarrio().equalsIgnoreCase(barrio)) {
                    totalBicis += e.getAnclajes().contar();
                    totalCapacidad += e.getCapacidad();
                }
                aux = aux.getSiguiente();
            }

            int porcentaje = (totalCapacidad > 0) ? (int) Math.round((totalBicis * 100.0) / totalCapacidad) : 0;

            if (sb.length() > 0) sb.append("|");
            sb.append(barrio).append("#").append(porcentaje);

            nodoBarrio = nodoBarrio.getSiguiente();
        }

        return Retorno.ok(sb.toString());
    }



//3.8. Ranking por tipo de uso-----------
   @Override
    public Retorno rankingTiposPorUso() {
        if (estaciones == null || deposito == null)
           return Retorno.error1();

       
        ListaSE<TipoUso> usos = new ListaSE<>();

        // TODAS LAS BICIS PAKA
        for (TipoBicicleta tipo : TipoBicicleta.values()) {
            usos.adicionar(new TipoUso(tipo));
        }

        // TODAS LAS ESTACIONES Y SUMATE TODO
        NodoSE<Estacion> nodoEst = estaciones.getPrimero();
        while (nodoEst != null) {
            Estacion e = nodoEst.getDato();
            NodoSE<Bicicleta> nodoBici = e.getAnclajes().getLista().getInicio();
            while (nodoBici != null) {
                Bicicleta b = nodoBici.getDato();
                // BUSCA EL TIPO
                for (int i = 0; i < usos.longitud(); i++) {
                    try {
                        TipoUso tu = usos.obtener(i);
                        if (tu.getTipo() == b.getTipo()) {
                            tu.incrementar(b.getVecesAlquilada());
                            break;
                        }
                    } catch (Exception ex) { }
                }
                nodoBici = nodoBici.getSiguiente();
            }
            nodoEst = nodoEst.getSiguiente();
        }

        // RECORRETE EL DEPOSITO
        NodoSE<Bicicleta> nodoDep = deposito.getLista().getInicio();
        while (nodoDep != null) {
            Bicicleta b = nodoDep.getDato();
            for (int i = 0; i < usos.longitud(); i++) {
                try {
                    TipoUso tu = usos.obtener(i);
                    if (tu.getTipo() == b.getTipo()) {
                        tu.incrementar(b.getVecesAlquilada());
                        break;
                    }
                } catch (Exception ex) { }
            }
            nodoDep = nodoDep.getSiguiente();
        }

        // Ordenar lista por cantidad descendente y luego alfabético
        usos.selectionSort((a, b) -> a.compareTo(b));

        // Construir string 
        StringBuilder sb = new StringBuilder();
        NodoSE<TipoUso> nodoUso = usos.getInicio();
        while (nodoUso != null) {
            TipoUso tu = nodoUso.getDato();
            if (sb.length() > 0) sb.append("|");
            sb.append(tu.getTipo().name()).append("#").append(tu.getCantidadAlquileres());
            nodoUso = nodoUso.getSiguiente();
        }

        return Retorno.ok(sb.toString());
    }


//3.9. Usuarios en espera por alquiler----------------------------- 
     @Override
    public Retorno usuariosEnEspera(String nombreEstacion) {
     if (estaciones == null)
         return Retorno.noImplementada();

     Estacion est = estaciones.buscar(nombreEstacion);
     if (est == null)
         return Retorno.error1(); // si la estación no existe

     if (est.getEsperaAlquiler().estaVacia())
         return Retorno.ok(""); // sin usuarios en espera

     // Obtener todos los usuarios en orden de llegada
     ListaSE<Usuario> listaUsuarios = est.getEsperaAlquiler().obtenerElementos();
     StringBuilder sb = new StringBuilder();
     NodoSE<Usuario> nodo = listaUsuarios.getInicio();
     while (nodo != null) {
         if (sb.length() > 0) sb.append("|");
         sb.append(nodo.getDato().getCedula());
         nodo = nodo.getSiguiente();
     }

     return Retorno.ok(sb.toString());
 }




//3.10. Usuario con mayor cantidad de alquileres

@Override
public Retorno usuarioMayor() {
    if (usuarios == null || usuarios.getPrimero() == null) {
        System.out.println("DEBUG: lista vacía");
        return Retorno.error1();
    }

    NodoSE<Usuario> nodo = usuarios.getPrimero();
    Usuario mayor = nodo.getDato();
    System.out.println("DEBUG: inicial mayor = " + mayor.getCedula() + 
                       " (" + mayor.getCantidadAlquileres() + ")");
    nodo = nodo.getSiguiente();

    while (nodo != null) {
        Usuario actual = nodo.getDato();
        System.out.println("DEBUG: evaluando = " + actual.getCedula() + 
                           " (" + actual.getCantidadAlquileres() + ")");

        if (actual.getCantidadAlquileres() > mayor.getCantidadAlquileres()) {
            System.out.println("DEBUG: nuevo mayor por alquileres → " + actual.getCedula());
            mayor = actual;

        } else if (actual.getCantidadAlquileres() == mayor.getCantidadAlquileres()) {
            System.out.println("DEBUG: empate, comparando cédulas → " 
                               + actual.getCedula() + " vs " + mayor.getCedula());
            if (actual.getCedula().compareTo(mayor.getCedula()) < 0) {
                System.out.println("DEBUG: nuevo mayor por cédula → " + actual.getCedula());
                mayor = actual;
            }
        }

        nodo = nodo.getSiguiente();
    }

    System.out.println("DEBUG: resultado final = " + mayor.getCedula());
    return Retorno.ok(mayor.getCedula());
}


}
