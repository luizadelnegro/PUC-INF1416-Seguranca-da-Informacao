import java.security.*;
import javax.crypto.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.math.BigInteger;

final class Arq  {
    private String name;
    private EnumStatus status;
    private String calculatedHash;

    private static HashMap<String, Arq> RegisteredArqs = new HashMap<String, Arq>();

    public String get_name(){
        return this.name;
    }

    public void set_status(EnumStatus es) {
        this.status = es;
    }
    
    public EnumStatus get_status() {
        return this.status;
    }

    public String get_string() {
        return this.name + " " + this.calculatedHash + " " + this.status.toString();
    }

    public void set_calculatedHash(String hash){
        this.calculatedHash = hash;
    }

    public static Set<String> get_arqNames(){
        return Arq.RegisteredArqs.keySet();
    }

    public String get_calculatedHash() {
        return this.calculatedHash;
    }

    public Arq(String name) {
        this.name = name;
        this.status = EnumStatus.NOT_FOUND;
    }

    public static Arq get_arq(String name) {
        return Arq.RegisteredArqs.get(name);
    }

    public static Arq get_set_arq(String name) {
        Arq obj = Arq.get_arq(name);
        if(obj == null) {
            obj = new Arq(name);
            Arq.RegisteredArqs.put(name, obj);
        }
        return obj;
    }

    public static boolean colides(Arq a){
        for(String key : Arq.RegisteredArqs.keySet()) {
            if(!key.equals(a.name)) {
                // Nomes diferentes
                if(Arq.RegisteredArqs.get(key).get_calculatedHash().equals(a.get_calculatedHash())) return true; // Mesmo hash
            }
        }
        return false;
    }



}


public class DigestCalculator {
    static final String HEADER = "" +
    "**************************************" +
    "\tINF1416 - Segurança da informação\t" +
    "**************************************\n" +
    "\tTrabalho 3 - MySignature 2021-04-24\n" +
    "\tGrupo: Luiza e Lucas\n" + 
    "**************************************\n\n";

    // private HashMap<String, String> fileHash = new HashMap<String, String>(); // HashMap as FileName => Calculated Hash
    private HashMap<String, String> arqOnList = new HashMap<String,String>();
    private MessageDigest mg;
    private String hashName;
    private String arqListPath;

    private static final Map<String,String> mapAlgorithmName = Map.of("SHA1", "SHA-1",
        "SHA256", "SHA-256",
        "SHA512", "SHA-512",
        "MD5", "MD5"
    );

    public static String toHex(byte[] bytes){
        return String.format("%032x", new BigInteger(1, bytes));
    }

    private void calculateFileHash(Path filePath) {
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(filePath);
            
        }
        catch (IOException e) {
            System.out.println("Exception reading file " + filePath.toString());
            return;
        }
        this.mg.update(fileContent);
        Arq a = Arq.get_set_arq(filePath.getFileName().toString());
        a.set_calculatedHash(DigestCalculator.toHex(this.mg.digest()));
        // this.fileHash.put(filePath.getFileName().toString(), DigestCalculator.toHex(this.mg.digest()));
    }

    private void calculateAllFilesHash(String dirPath) throws IOException {
        Files.list(new File(dirPath).toPath()).forEach(path -> {calculateFileHash(path);});
    }

    private void checkFile(String fileName) {
        Arq arq = Arq.get_arq(fileName);
        if (arq.get_status() == EnumStatus.COLISION) return;
        String calculated = arq.get_calculatedHash();
        String expected = this.arqOnList.get(fileName);
        if(calculated.equals(expected)){
            arq.set_status(EnumStatus.OK);
        }
        else {
            if(expected != null)
                arq.set_status(EnumStatus.NOT_OK);
        }
        for(String key : this.arqOnList.keySet()) {
            if(!key.equals(arq.get_name())) {
                // Nomes diferentes
                if(this.arqOnList.get(key).equals(arq.get_calculatedHash())){
                    // Mesmo digest
                    arq.set_status(EnumStatus.COLISION);
                    Arq.get_arq(key).set_status(EnumStatus.COLISION);
                }
            }
        }

        if(Arq.colides(arq)) {  // Verifica outro arquivo com mesmo hash calculado
            arq.set_status(EnumStatus.COLISION);
        }
        
    }

    private void printOrSave(String fileName) {
        Arq a = Arq.get_arq(fileName);
        if (a.get_status() != EnumStatus.NOT_FOUND) {
            System.out.println(fileName + " " + a.get_calculatedHash() + " " + a.get_status().toString());
        }
        else {
            boolean found = false;
            List<String> newLines = new ArrayList<>();
            List<String> oldLines = new ArrayList<String>();
            try {
                oldLines = Files.readAllLines(Paths.get(this.arqListPath), StandardCharsets.UTF_8);
            } catch(IOException e) {
                System.out.println(e);
            }
            for (String line : oldLines) {
                if (line.contains(fileName)) {
                    System.out.println("APPENDED! " + fileName + " " + this.hashName + " " + a.get_calculatedHash());
                    found = true;
                    newLines.add(line.concat(" " + this.hashName + " " + a.get_calculatedHash()));
                } else {
                    newLines.add(line);
                }
            }
            if(! found) {
                System.out.println("APPENDED NEW LINE! " + fileName + " " + this.hashName + " " + a.get_calculatedHash());
                newLines.add(fileName + " " + this.hashName + " " + a.get_calculatedHash());
            }
            try {   
                Files.write(Paths.get(this.arqListPath), newLines, StandardCharsets.UTF_8);
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }

    }

    private void checkAllFromFile(String filePath) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        Scanner input = new Scanner(file);
    
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] splitted = line.split(" ");
            String arqName = splitted[0];
            int index = Arrays.asList(splitted).indexOf(this.hashName);

            if(index != -1) {
                this.arqOnList.put(arqName, splitted[index + 1]);
            }
        }
        Set<String> allArqs = Arq.get_arqNames();
        allArqs.forEach(fileName -> {this.checkFile(fileName);});
        allArqs.forEach(fileName -> {this.printOrSave(fileName);});
        input.close();           
    }

    public DigestCalculator(String hashType) throws NoSuchAlgorithmException {
        this.mg = MessageDigest.getInstance(mapAlgorithmName.get(hashType));
        this.hashName = hashType;
    }
    public static void main(String[] args) throws Exception {
        String hashType = args[0];
        String arqDigest = args[1];
        String arqDir = args[2];
            
        DigestCalculator dc = new DigestCalculator(hashType);
        dc.arqListPath = arqDigest;
        dc.calculateAllFilesHash(arqDir);
        dc.checkAllFromFile(arqDigest);
    }

}