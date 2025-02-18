package com.example.banco_api.service;

import com.example.banco_api.model.Usuario;
import com.example.banco_api.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.AuthErrorCode;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(String nombre, String correo, String contraseña) throws FirebaseAuthException {
        // 1. Crear usuario en Firebase Authentication
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(correo)
                .setPassword(contraseña);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Usuario de Firebase creado con UID: " + userRecord.getUid());

        // 2. Guardar usuario en la base de datos local
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContraseña(contraseña); // ¡OJO! En producción, hashear la contraseña antes de guardar.
        return usuarioRepository.save(usuario);
    }

    public String autenticarUsuario(String correo, String contraseña) throws FirebaseAuthException {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(correo);
            // En un sistema real, aquí generarías un token JWT tras verificar las credenciales en Firebase.
            // Para simplificar, solo retornamos un mensaje de éxito.
            return "Autenticación exitosa para el usuario: " + userRecord.getEmail();
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                throw new IllegalArgumentException("Usuario no encontrado");
            } else if (e.getAuthErrorCode() == AuthErrorCode.EMAIL_NOT_FOUND) {
                throw new IllegalArgumentException("Correo electrónico inválido");
            } else {
                throw e; // Re-lanza otras excepciones de Firebase
            }
        }
    }


    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}