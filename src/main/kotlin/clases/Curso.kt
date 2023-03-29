package clases

class Curso(private val id: Int,
            private val categoria : String,
            private val nombre:String,
            private val duracion: Float,
            private var precio: Float)
{
    fun getId(): Int
    {
        return this.id
    }

    fun getCategoria(): String
    {
        return this.categoria
    }

    fun getNombre(): String
    {
        return this.nombre
    }

    fun getDuracion(): Float
    {
        return this.duracion
    }

    fun getPrecio(): Float
    {
        return this.precio
    }
}