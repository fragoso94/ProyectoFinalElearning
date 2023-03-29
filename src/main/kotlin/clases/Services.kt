package clases

import datos.MensajeRespuesta
import datos.User
import datos.Utileria
import interfaces.IAutenticacion
import interfaces.ICurso

//Variables Globales
var contadorId = 0  //asigna automaticamente id´s a los cursos
val users = mutableListOf<User>()
lateinit var cursos: MutableList<Curso>

class Services: Pago(), IAutenticacion, ICurso
{
    override fun iniciarSesion(user: String, pass: String): MensajeRespuesta
    {
        val response = MensajeRespuesta()
        // Verificamos si el usuario ya existe
        val usuarioExistente = users.find { it.username == user && it.password == pass }
        if (usuarioExistente != null)
        {
            cargarCursos()
            response.exito = true
            response.mensaje = "Inicio de sesión correctamente"
            response.cantidad = cursos.count()

        }
        else
        {
            response.exito = false
            response.mensaje = "Credenciales Incorrectas"
        }
        return response
    }

    //Método que se encaragará de registrar un usuario nuevo
    override fun registrarUsuario(user: String, pass: String): MensajeRespuesta
    {
        val response = MensajeRespuesta()
        val usuarioExistente = users.find { it.username == user } // Verificamos si el usuario ya existe
        if (usuarioExistente != null)
        {
            response.exito = false
            response.mensaje = "El usuario ya existe"
        }
        else
        {
            // Creamos un nuevo usuario y lo agregamos a la lista de usuarios
            users.add(User(user, pass))
            response.exito = true
            response.mensaje = "Usuario registrado con éxito"
        }
        return response
    }

    // Método que se encaragará de realizar la recuperación de contraseña, de un usuario registrado
    override fun recuperarPassword(user: String, pass: String): MensajeRespuesta
    {
        val response = MensajeRespuesta()
        val usuarioEncontrado = users.find { it.username == user }
        if (usuarioEncontrado != null) {
            usuarioEncontrado.password = pass!!
            response.exito = true
            response.mensaje = "Contraseña actualizada con éxito"
        } else {
            response.exito = false
            response.mensaje = "El nombre de usuario no se encuentra registrado."
        }
        return response
    }

    // Método que obtiene todos los cursos comprados de un usuario
    override fun cursosUsuario(user: String): List<Curso>?
    {
        val usuario = users.find { it.username == user }
        return usuario?.cursos
    }

    //Método que procesará la compra de un curso
    override fun comprarCursos(user: String, curso: Curso, metodoPago: Utileria.FormaPago?): MensajeRespuesta
    {
        val respuesta = MensajeRespuesta()
        var comprado = false
        try
        {
            val usuario = users.find { it.username == user }
            usuario?.cursos?.forEach {
                comprado = it.getId() == curso.getId()
            }
            if(!comprado)
            {
                if(metodoPago == Utileria.FormaPago.EFECTIVO)
                {
                    usuario?.cursos?.add(curso)
                    respuesta.exito = true
                    respuesta.mensaje = "El curso se agrego correctamente"
                }
                else if(metodoPago == Utileria.FormaPago.TARJETA)
                {
                    if(pagar(curso.getPrecio()))
                    {
                        usuario?.cursos?.add(curso)
                        respuesta.exito = true
                        respuesta.mensaje = "El curso se agrego correctamente"
                    }
                    else
                    {
                        respuesta.exito = false
                        respuesta.mensaje = "Saldo insuficiente."
                    }
                }
                else
                {
                    respuesta.exito = false
                    respuesta.mensaje = "La forma de pago es inválida"
                }
            }
            else
            {
                println("!Ya has adquirido este curso!")
            }
        }
        catch (e: Exception)
        {
            respuesta.exito = false
            respuesta.mensaje = "Hubo un error al procesar la compra: ${e.message}"
        }
        return respuesta
    }

    //Método que mostrará los cursos disponibles en la app
    override fun mostrarCursosDisponibles(opcion: String): List<Curso>
    {
        if(opcion.uppercase() == "TODOS")
        {
            return cursos
        }
        else
        {
            return cursos.filter { c -> c.getCategoria() == opcion }
        }
    }
}

private fun cargarCursos()
{
    //Declación de cursos
    val photoshop = Curso(++contadorId, "Diseño","Photoshop", 40F, 269.90F )
    val kotlin = Curso(++contadorId, "Programación","Kotlin", 60F, 289.90F )
    val java = Curso(++contadorId, "Programación","Java", 100F, 389.90F )
    val canva = Curso(++contadorId, "Diseño","Canva", 20F, 169.90F )
    val css = Curso(++contadorId, "Desarrollo Web","CSS 3", 35.5F, 99.90F )
    val csharp = Curso(++contadorId, "Programación","C#", 80.5F, 349.90F )
    val javascript = Curso(++contadorId, "Dasarrollo Web","JavaScript", 75.5F, 319.90F )
    val ruby = Curso(++contadorId, "Programación","Ruby", 32.5F, 189.90F )
    val corelDraw = Curso(++contadorId, "Diseño","Corel Draw", 30F, 279.90F )
    val perl = Curso(++contadorId, "Programación","Perl", 34F, 239.90F )
    val wordPress = Curso(++contadorId, "Desarrollo Web","WordPress", 60F, 359.90F )
    val html = Curso(++contadorId, "Desarrollo Web","HTML 5", 27.5F, 189.90F )
    val python = Curso(++contadorId, "Programación","Python", 50.5F, 359.90F )
    val go = Curso(++contadorId, "Programación","Go", 28.5F, 199.90F )
    val bootstrap = Curso(++contadorId, "Desarrollo Web","Bootstrap", 20F, 139.90F )
    val cPlusPlus = Curso(++contadorId, "Programación","C++", 105F, 399.90F )
    val visualBasic = Curso(++contadorId, "Programación","Visual Basic", 45.5F, 299.90F )
    val figma = Curso(++contadorId, "Diseño","Figma", 24F, 219.90F )
    val sass = Curso(++contadorId, "Desarrollo Web","SASS", 26.5F, 219.90F )
    val illustrator = Curso(++contadorId, "Diseño","Illustrator", 40F, 249.90F )
    val affinity = Curso(++contadorId, "Diseño","Affinity", 20F, 149.90F )
    val php = Curso(++contadorId, "Desarrollo Web","PHP", 60F, 279.90F )
    val sketch = Curso(++contadorId, "Diseño","Sketch", 46.5F, 329.90F )

    cursos = mutableListOf(photoshop, kotlin, java, canva, css, csharp, javascript,
        ruby, corelDraw, perl, wordPress, html, python, go, bootstrap,
        cPlusPlus, visualBasic, figma, sass, illustrator,  affinity,
        php, sketch)
}