package services;
import Dao.ClienteDao;
import model.Cliente;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;
public class ClienteService {
 private final ClienteDao dao=new ClienteDao();
 private static String clean(String s){return s==null?"":s.trim();}
 public void validar(Cliente c){
  if(c==null)throw new IllegalArgumentException("Cliente obligatorio");
  if(clean(c.getNombre()).isEmpty()&&clean(c.getRazonSocial()).isEmpty())throw new IllegalArgumentException("Ingresá nombre o razón social");
  if(!clean(c.getNroDocumento()).isEmpty()&&clean(c.getTipoDocumento()).isEmpty())throw new IllegalArgumentException("Elegí el tipo de documento");
  if(c.getLimiteCredito()==null||c.getLimiteCredito().compareTo(BigDecimal.ZERO)<0)throw new IllegalArgumentException("Límite de crédito inválido");
  if(!c.isPermiteCuentaCorriente()&&c.getLimiteCredito().signum()>0)throw new IllegalArgumentException("Habilitá cuenta corriente para asignar un límite");
  if(!clean(c.getEmail()).isEmpty()&&!clean(c.getEmail()).matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))throw new IllegalArgumentException("Correo electrónico inválido");
 }
 public int crear(Cliente c)throws SQLException{validar(c);return dao.guardar(c);}
 public void modificar(Cliente c)throws SQLException{validar(c);if(c.getIdCliente()<=0)throw new IllegalArgumentException("ID inválido");dao.actualizar(c);}
 public void cambiarActivo(int id,boolean activo)throws SQLException{dao.cambiarActivo(id,activo);}
 public Cliente buscarPorId(int id)throws SQLException{return dao.buscarPorId(id);}
 public List<Cliente> listar(String filtro,boolean inactivos)throws SQLException{return dao.listar(filtro,inactivos);}
}
