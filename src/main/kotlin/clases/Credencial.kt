package clases

class Credencial(val user: String = "", val pass: String = "")
{
    private var usuario = ""
        set(value){
            field = value
        }
        get() = field
    private var password = ""
        set(value){
            field = value
        }
        get() = field

    init
    {
        usuario = if (user == "") "usuario" else user
        password = if (pass == "") "kotlin123456" else pass
    }
}