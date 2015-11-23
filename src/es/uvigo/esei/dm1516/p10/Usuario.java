package es.uvigo.esei.dm1516.p10;

public class Usuario {
    private String email;
    private String nombre;
    private String contrasenha;

    public Usuario(String correo, String nombre, String contrasenha) {
        this.email = correo;
        this.nombre = nombre;
        this.contrasenha = contrasenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }
}
