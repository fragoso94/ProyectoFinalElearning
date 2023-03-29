package datos
class Utileria
{
    companion object
    {
        const val TEXTO_USUARIO = "Ingrese el nombre de usuario:"
        const val TEXTO_PASSWORD = "Ingrese su contraseña:"
        const val TIEMPO_ESPERA = 1500L
        const val TEXTO_CARGANDO = "Cargando, espere un momento por favor..."
    }

    enum class FormaPago
    {
        TARJETA, EFECTIVO
    }
}