package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Cliente {
 private int idCliente; private String tipoDocumento,nroDocumento,razonSocial,nombre,apellido,condicionIva,tipoCliente="MINORISTA",telefono,email,direccion,localidad,provincia;
 private boolean permiteCuentaCorriente,activo=true; private BigDecimal limiteCredito=BigDecimal.ZERO; private LocalDateTime fechaRegistro,fechaModificacion;
 public int getIdCliente(){return idCliente;} public void setIdCliente(int v){idCliente=v;}
 public String getTipoDocumento(){return tipoDocumento;} public void setTipoDocumento(String v){tipoDocumento=v;}
 public String getNroDocumento(){return nroDocumento;} public void setNroDocumento(String v){nroDocumento=v;}
 public String getRazonSocial(){return razonSocial;} public void setRazonSocial(String v){razonSocial=v;}
 public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
 public String getApellido(){return apellido;} public void setApellido(String v){apellido=v;}
 public String getCondicionIva(){return condicionIva;} public void setCondicionIva(String v){condicionIva=v;}
 public String getTipoCliente(){return tipoCliente;} public void setTipoCliente(String v){tipoCliente=v;}
 public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v;}
 public String getLocalidad(){return localidad;} public void setLocalidad(String v){localidad=v;}
 public String getProvincia(){return provincia;} public void setProvincia(String v){provincia=v;}
 public boolean isPermiteCuentaCorriente(){return permiteCuentaCorriente;} public void setPermiteCuentaCorriente(boolean v){permiteCuentaCorriente=v;}
 public boolean isActivo(){return activo;} public void setActivo(boolean v){activo=v;}
 public BigDecimal getLimiteCredito(){return limiteCredito;} public void setLimiteCredito(BigDecimal v){limiteCredito=v;}
 public LocalDateTime getFechaRegistro(){return fechaRegistro;} public void setFechaRegistro(LocalDateTime v){fechaRegistro=v;}
 public LocalDateTime getFechaModificacion(){return fechaModificacion;} public void setFechaModificacion(LocalDateTime v){fechaModificacion=v;}
 public String getNombreMostrar(){String n=((nombre==null?"":nombre)+" "+(apellido==null?"":apellido)).trim();return n.isEmpty()?(razonSocial==null?"":razonSocial):n;}
}
