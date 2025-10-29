package model;

public class ClientePremium extends Cliente {
    public static final double DESCUENTO_ENVIO_PREMIUM = 20.0;
    public static final int CUOTA_ANUAL_PREMIUM = 30;

    private double descuentoEnvio;
    private int cuotaAnual;

    public ClientePremium(String email, String nombre, String domicilio, String NIF, double descuentoEnvio, int cuotaAnual) {
        super(email, nombre, domicilio, NIF);
        this.descuentoEnvio = descuentoEnvio;
        this.cuotaAnual = cuotaAnual;
    }


    public double getDescuentoEnvio() { return descuentoEnvio; }
    public void setDescuentoEnvio(double descuentoEnvio) { this.descuentoEnvio = descuentoEnvio; }

    public int getCuotaAnual() { return cuotaAnual; }
    public void setCuotaAnual(int cuotaAnual) { this.cuotaAnual = cuotaAnual; }

    @Override
    public String toString() {
        return super.toString()+
                "ClientePremium{" +
                "descuentoEnvio=" + descuentoEnvio +
                ", cuotaAnual=" + cuotaAnual +
                '}';
    }
}

