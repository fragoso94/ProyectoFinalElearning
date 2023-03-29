package datos

import clases.Curso

data class MensajeRespuesta(var exito: Boolean = false, var mensaje: String = "", var cantidad: Int = 0)
data class User(val username: String, var password: String, var cursos: MutableList<Curso> = mutableListOf())

