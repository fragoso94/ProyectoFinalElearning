package clases

open class Pago
{
    var saldo = 500f

    open fun pagar(monto: Float): Boolean
    {
        if (saldo > monto)
        {
            this.saldo -= monto
            return true
        }
        else
        {
            return false
        }
    }
}