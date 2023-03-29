package interfaces

import datos.MensajeRespuesta
import datos.User

interface IAutenticacion
{
    fun iniciarSesion(user: String, pass: String): MensajeRespuesta
    fun registrarUsuario(user: String, pass: String): MensajeRespuesta
    fun recuperarPassword(user: String, pass: String): MensajeRespuesta
}