import java.security.*;
import javax.crypto.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.math.BigInteger;

final class Arq  {
    private String name;
    private EnumStatus status;
    private String calculatedHash;

    private static HashMap<String, Arq> RegisteredArqs;

    public void set_status(EnumStatus es) {
        this.status = es;
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

    public static boolean colides(String fileName, String hash){
        Arq.RegisteredArqs.forEach((k,v) -> {
            if(k)
        });
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
        if(arq.get_status() == EnumStatus.COLISION) return;
        String calculated = arq.get_calculatedHash();
        String expected = this.arqOnList.get(fileName);
        if(calculated.equals(expected)){
            arq.set_status(EnumStatus.OK);
        }
        else {
            arq.set_status(EnumStatus.NOT_OK);
        }
        this.arqOnList.forEach((k,v) -> {
            if (k != fileName) {
                if (v == calculated) {
                    arq.set_status(EnumStatus.COLISION);
                }
            }
        });
        
    }

    private void checkAllFromFile(String filePath) throws FileNotFoundException {
        EnumStatus status = EnumStatus.NOT_FOUND;
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
        // while (input.hasNextLine()) {
        //     String line = input.nextLine();
        //     String[] splitted = line.split(" ");
        //     String arqName = splitted[0];
        //     arqOnArqList.add(arqName);
        //     String expectedHash = this.fileHash.get(arqName);
        //     System.out.println(arqName + " expected: " + this.fileHash.get(arqName));

        //     int index = Arrays.asList(splitted).indexOf(this.hashName);
        //     if(index != -1){
        //         String hash = splitted[index+1];
        //         if(hash.equals(this.fileHash.get(arqName))) {
        //             status = EnumStatus.OK;
        //         }
        //         else {
        //             status = EnumStatus.NOT_OK;
        //         }
        //         HashMap<String, String> copyHash = (HashMap<String, String>) this.fileHash.clone();
        //         if (status == EnumStatus.OK) {
        //             copyHash.remove(arqName);
        //             Collection<String> hashValues = copyHash.values();
        //             if(hashValues.contains(hash)) {
        //                 status = EnumStatus.COLISION;
        //             }
        //         }
        //     }
            System.out.println(arqName + " " + this.hashName + " " + expectedHash + " " + status.toString());
            
        }
        input.close();

    }

    public DigestCalculator(String hashType) throws NoSuchAlgorithmException {
        this.mg = MessageDigest.getInstance(hashType);
        this.hashName = hashType;
    }
    public static void main(String[] args) throws Exception {
        String hashType = args[0];
        String arqDigest = args[1];
        String arqDir = args[2];
            
        DigestCalculator dc = new DigestCalculator(hashType);
        dc.calculateAllFilesHash(arqDir);
        dc.checkAllFromFile(arqDigest);
    }

}