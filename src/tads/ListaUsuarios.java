/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

import dominio.Usuario;
/**
 *
 * @author ljgp2
 */
public class ListaUsuarios {
  
    private ListaSE<Usuario> usuarios;

    public ListaUsuarios() {
        usuarios = new ListaSE<>();
    }
    
    public int longitud() {
    return usuarios.longitud();
}

    public boolean estaVacia() {
        return usuarios.vacia();
    }
    
    public Usuario obtener(int i) {
    try {
        return usuarios.obtener(i);
    } catch (Exception e) {
        return null;
    }
    }
    
   
    public Usuario buscar(String cedula) {
        for (int i = 0; i < usuarios.longitud(); i++) {
           try {
                Usuario u = usuarios.obtener(i);
                if (u.getCedula().equals(cedula)) return u;
           } catch (Exception e) { }
        }
        return null;
    }
    
    
    public NodoSE<Usuario> getPrimero() {
        try {
            return usuarios.obtener(0) != null 
                ? new NodoSE<>(usuarios.obtener(0)) 
                : null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existeUsuario(String cedula) {
        for (int i = 0; i < usuarios.longitud(); i++) {
            try {
                if (usuarios.obtener(i).getCedula().equals(cedula)) return true;
            } catch (Exception e) { }
        }
        return false;
    }
//para orden alfabetico 
    public void insertarOrdenado(Usuario nuevo) {
        if (nuevo == null) return;

        if (usuarios.vacia() || nuevo.getNombre().compareTo(usuarios.obtener(0).getNombre()) < 0) {
            try {
                usuarios.insertar(nuevo, 0);
            } catch (Exception e) { }
        } else {
            for (int i = 0; i < usuarios.longitud(); i++) {
                try {
                    Usuario actual = usuarios.obtener(i);
                    if (i + 1 == usuarios.longitud() || nuevo.getNombre().compareTo(usuarios.obtener(i + 1).getNombre()) < 0) {
                        usuarios.insertar(nuevo, i + 1);
                        break;
                    }
                } catch (Exception e) { }
            }
        }
    }


  public String listar() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < usuarios.longitud(); i++) {
        try {
            Usuario u = usuarios.obtener(i);  // usamos obtener() de ListaSE
            if (sb.length() > 0) sb.append("|");
            sb.append(u.getNombre()).append("#").append(u.getCedula());
        } catch (Exception e) {}
    }
    return sb.toString();
}




    public ListaSE<Usuario> getLista() {
        return usuarios;
    }
}

