import clases.Credencial
import clases.Curso
import clases.Services
import datos.Utileria
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

var usuarioLogeado = Credencial()

fun main() {
    println("¡Bienvenido a Elearning!")
    // Mostramos el menú de opciones
    println("1. Iniciar sesión")
    println("2. Registrarse")
    println("3. Modificar contraseña")
    println("0. Salir")
    try
    {
        runBlocking {
            val opcion = readLine()?.toInt()
            val resultado = async {
                when (opcion) {
                    1 -> iniciarProcesoLogin()
                    2 -> registrarUsuario()
                    3 -> modificarContrasena()
                    0 -> println("*** Hasta Pronto! ***")
                    else -> {
                        println("*** Opción inválida! ***")
                        main()
                    }
                }
            }
            resultado.await()
        }
    }
    catch (e: NumberFormatException)
    {
        println("*** La opción elegida es inválida! ***")
        main()
    }
}

suspend fun iniciarProcesoLogin() {
    try
    {
        println(Utileria.TEXTO_USUARIO)
        val usuario = readLine()!!.toString()
        println(Utileria.TEXTO_PASSWORD)
        val contrasena = readLine()!!.toString()
        if (usuario.isNotEmpty() && contrasena.isNotEmpty())
        {
            val servicio = Services()
            println(Utileria.TEXTO_CARGANDO)
            delay(Utileria.TIEMPO_ESPERA)
            var respuesta = servicio.iniciarSesion(usuario, contrasena)
            if (respuesta.exito)
            {
                usuarioLogeado = Credencial(usuario, contrasena)
                inicioCursos()
            }
            else
            {
                println(respuesta.mensaje)
                println("*** Desea intentalo nuevamente.? ***")
                val opcion = readLine()!!.toString()
                if (opcion != "" && opcion == "si")
                {
                    iniciarProcesoLogin()
                }
                return
            }
        }
        else
        {
            println("*** Los datos de entradas son requeridos!!!")
        }
    }
    catch (e: Exception)
    {
        println("Hubo un error en: ${e.message}")
    }
}

suspend fun registrarUsuario() {
    // Pedimos el nombre de usuario y la contraseña
    print("Ingresa tu nombre de usuario: ")
    val usuario = readLine()!!.toString()

    print("Ingresa tu contraseña: ")
    val contrasena = readLine()!!.toString()

    val servicio = Services()
    println(Utileria.TEXTO_CARGANDO)
    delay(Utileria.TIEMPO_ESPERA)
    var respuesta = servicio.registrarUsuario(usuario, contrasena)
    if (respuesta.exito)
    {
        println(respuesta.mensaje)
        main()
    }
    else
    {
        println(respuesta.mensaje)
        registrarUsuario()
    }
}

suspend fun modificarContrasena() {
    // Pedimos el nombre de usuario y la contraseña actual
    print("Ingresa tu nombre de usuario: ")
    val usuario = readLine()!!.toString()

    print("Ingresa tu contraseña actual: ")
    val nuevoPassword = readLine()!!.toString()

    val servicio = Services()
    println(Utileria.TEXTO_CARGANDO)
    delay(Utileria.TIEMPO_ESPERA)
    var respuesta = servicio.recuperarPassword(usuario, nuevoPassword)
    if (respuesta.exito)
    {
        println(respuesta.mensaje)
    }
    else{
        println(respuesta.mensaje)
    }
    main()
}

suspend fun inicioCursos(){
    //var cursoComprado: Int
    var opcion: Int

    println("""
            *** Bienvenido - Elearning *** 
            
            1.- Mis cursos
            2.- Comprar cursos
            0.- Salir
        """.trimIndent())

    println("Elige una opción: ")
    opcion = try {
        readlnOrNull()?.toInt() as Int
    } catch (e: NumberFormatException) {
        0
    }

    runBlocking {
        val resultado = async {
            when (opcion) {
                0 -> main()
                1 -> cursosUsuario()
                2 -> comprarCursos(Services())
                else -> {
                    println("Opción no válida")
                }
            }
        }
        resultado.await()
    }

}

suspend fun cursosUsuario(){
    val servicio = Services()
    println(Utileria.TEXTO_CARGANDO)
    delay(Utileria.TIEMPO_ESPERA)
    val misCursos = servicio.cursosUsuario(usuarioLogeado.user)
    if (misCursos?.size != 0){
        misCursos?.forEach {
            println(
                """Curso:
                ID: ${it.getId()}
                Categoría : ${it.getCategoria()}
                Nombre: ${it.getNombre()}
                Duración: ${it.getDuracion()} horas
                Precio: ${it.getPrecio()} pesos
                """
            )
        }
    }
    else{
        println("No tiene cursos comprados....")
    }
    inicioCursos()
}

suspend fun comprarCursos(servicio: Services){
    //val servicio = Services()
    var opcion: Int
    println("""
            *** Categorías Disponibles - Elearning ***
            
            1.- Todos
            2.- Desarrollo Web
            3.- Programación
            4.- Diseño
            0.- Regresar
        """.trimIndent()
    )
    try
    {
        println("Elige una opción: ")
        opcion = try{ readlnOrNull()?.toInt() as Int } catch(e: NumberFormatException) { 0 }
        if(opcion != 0)
        {
            println(Utileria.TEXTO_CARGANDO)
            delay(Utileria.TIEMPO_ESPERA)
            val cursosDisponibles = when (opcion) {
                1 -> servicio.mostrarCursosDisponibles("Todos")
                2 -> servicio.mostrarCursosDisponibles("Desarrollo Web")
                3 -> servicio.mostrarCursosDisponibles("Programación")
                4 -> servicio.mostrarCursosDisponibles("Diseño")
                else -> null
            }
            cursosDisponibles?.forEach {
                println(
                    """Curso:
                    ID: ${it.getId()}
                    Categoría : ${it.getCategoria()}
                    Nombre: ${it.getNombre()}
                    Duración: ${it.getDuracion()} horas
                    Precio: ${it.getPrecio()} pesos
                    """)
            }

            """Ingrese el ID del curso a comprar
                (cero para salir) ->
            """.trimMargin()
            //val pago = Pago()
            val idCurso = try { readlnOrNull()?.toInt() as Int } catch (e: NumberFormatException) { 0 }
            val cursoSeleccionado = cursosDisponibles?.first() { it.getId() == idCurso }
            println("""
                    Selecciones su forma de Pago
                    
                    1.- Efectivo
                    2.- Tarjeta (Crédito disponible:$${servicio.saldo} MXN)                    
                """.trimIndent()
            )
            val op = try { readlnOrNull()?.toInt() as Int } catch (e: NumberFormatException) { 0 }
            val formaPago = when(op){
                1 -> Utileria.FormaPago.EFECTIVO
                2 -> Utileria.FormaPago.TARJETA
                else -> null
            }
            println(Utileria.TEXTO_CARGANDO)
            delay(Utileria.TIEMPO_ESPERA)
            val respuesta = servicio.comprarCursos(usuarioLogeado.user, cursoSeleccionado as Curso, formaPago)
            if (respuesta.exito)
            {
                println(respuesta.mensaje)
                println("!Has adquirido el curso ${cursoSeleccionado.getNombre()}!")
            }
            else{
                println(respuesta.mensaje)
            }
            comprarCursos(servicio)
        }
        else
        {
            inicioCursos()
        }
    }
    catch (e: NumberFormatException){
        println(e.message)
    }
    catch (e: NoSuchElementException)
    {
        println("El ID seleccionado no existe, por favor seleccione otro!")
    }
    finally {
        comprarCursos(servicio)
    }
}

private fun generarTransaccionCompra(servicio: Services, cursoSeleccionado: Curso, formaPago: Utileria.FormaPago){

}