package services;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/** Respaldo lógico local. No ejecuta restauraciones ni altera la BD. */
public final class BackupService {
    private BackupService() {}
    public static final class Resultado {
        public final Path archivo;
        public final Path checksum;
        public final long bytes;
        Resultado(Path archivo, Path checksum, long bytes) {this.archivo=archivo;this.checksum=checksum;this.bytes=bytes;}
    }
    public static Resultado crear(Path carpeta, String ejecutable) throws Exception {
        Properties p=new Properties();
        try(InputStream in=Files.newInputStream(Paths.get("config.properties"))){p.load(in);}
        String jdbc=p.getProperty("db.url", "");
        URI uri=URI.create(jdbc.replaceFirst("^jdbc:", ""));
        if(!"mysql".equalsIgnoreCase(uri.getScheme()) && !"mariadb".equalsIgnoreCase(uri.getScheme())) throw new IOException("Motor JDBC no admitido");
        String db=uri.getPath().replaceFirst("^/", "");
        if(!db.matches("[A-Za-z0-9_]+")) throw new IOException("Nombre de base inválido");
        String host=uri.getHost(); int port=uri.getPort()>0?uri.getPort():3306;
        if(host==null) throw new IOException("Host inválido en config.properties");
        Files.createDirectories(carpeta);
        if(!Files.isDirectory(carpeta)||!Files.isWritable(carpeta)) throw new IOException("Carpeta sin permisos de escritura");
        String stamp=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        Path destino=carpeta.resolve("serenasoft_"+db+"_"+stamp+".sql");
        Path temporal=Files.createTempFile("serenasoft_dump_", ".cnf");
        Path parcial=carpeta.resolve(destino.getFileName()+".part");
        try {
            // Archivo temporal de credenciales: evita exponer la contraseña en argumentos del proceso.
            String user=p.getProperty("db.user", ""); String pass=p.getProperty("db.password", "");
            if(user.isBlank()) throw new IOException("Usuario de base de datos no configurado");
            String cfg="[client]\nuser="+quote(user)+"\npassword="+quote(pass)+"\nhost="+quote(host)+"\nport="+port+"\n";
            Files.writeString(temporal,cfg,StandardCharsets.UTF_8);
            try {Files.setPosixFilePermissions(temporal,java.nio.file.attribute.PosixFilePermissions.fromString("rw-------"));} catch(UnsupportedOperationException ignored) {}
            String exe=(ejecutable==null||ejecutable.isBlank())?"mysqldump":ejecutable.trim();
            List<String> cmd=Arrays.asList(exe,"--defaults-extra-file="+temporal.toAbsolutePath(),"--single-transaction","--quick","--routines","--triggers","--events","--default-character-set=utf8mb4","--result-file="+parcial.toAbsolutePath(),db);
            Process proc=new ProcessBuilder(cmd).redirectErrorStream(true).start();
            ByteArrayOutputStream logs=new ByteArrayOutputStream();
            Thread reader=new Thread(()->{try(InputStream in=proc.getInputStream()){byte[] b=new byte[2048];int n;while((n=in.read(b))!=-1 && logs.size()<65536)logs.write(b,0,n);}catch(IOException ignored){}});
            reader.setDaemon(true);reader.start();
            boolean finished=proc.waitFor(10,TimeUnit.MINUTES);
            if(!finished){proc.destroyForcibly();throw new IOException("Tiempo de respaldo excedido");}
            reader.join(3000);
            if(proc.exitValue()!=0)throw new IOException("mysqldump falló (código "+proc.exitValue()+"). Revisá instalación y permisos. "+safeLog(logs.toString(StandardCharsets.UTF_8),pass));
            if(!Files.exists(parcial)||Files.size(parcial)<100)throw new IOException("El respaldo está vacío o incompleto");
            Files.move(parcial,destino,StandardCopyOption.ATOMIC_MOVE);
            String sha=sha256(destino);
            Path sum=destino.resolveSibling(destino.getFileName()+".sha256");
            Files.writeString(sum,sha+"  "+destino.getFileName()+"\n",StandardCharsets.UTF_8);
            return new Resultado(destino,sum,Files.size(destino));
        } finally {Files.deleteIfExists(temporal);Files.deleteIfExists(parcial);}
    }
    private static String quote(String s){return "\""+s.replace("\\","\\\\").replace("\"","\\\"").replace("\n", "").replace("\r", "")+"\"";}
    private static String safeLog(String log,String secret){return secret.isEmpty()?log:log.replace(secret,"[OCULTO]");}
    public static String sha256(Path file)throws Exception{MessageDigest d=MessageDigest.getInstance("SHA-256");try(InputStream in=Files.newInputStream(file)){byte[] b=new byte[65536];int n;while((n=in.read(b))!=-1)d.update(b,0,n);}StringBuilder out=new StringBuilder();for(byte b:d.digest())out.append(String.format("%02x",b&255));return out.toString();}
    public static boolean verificar(Path file)throws Exception{Path sum=file.resolveSibling(file.getFileName()+".sha256");if(!Files.isRegularFile(file)||!Files.isRegularFile(sum))return false;String expected=Files.readString(sum).trim().split("\\s+")[0];return expected.equalsIgnoreCase(sha256(file));}
}
