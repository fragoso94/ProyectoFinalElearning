package interfaces

import clases.Curso
import datos.MensajeRespuesta
import datos.Utileria

interface ICurso
{
    fun cursosUsuario(user: String) : List<Curso>?
    fun comprarCursos(user: String, curso: Curso, metodoPago: Utileria.FormaPago?) : MensajeRespuesta
    fun mostrarCursosDisponibles(opcion: String) : List<Curso>
}