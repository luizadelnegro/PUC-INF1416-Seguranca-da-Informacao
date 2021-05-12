package controllers;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PhoneticKeyBoard {
    public static final String[] PHONEMES = {"BA", "BE", "BO", "CA", "CE", "CO", "DA", "DE", "DO", "FA", "FE", "FO", "GA", "GE", "GO", "HA", "HE", "HO"};

    private static Random ran = new Random();
    
    private static final Logger LOGGER = Logger.getLogger(PhoneticKeyBoard.class.getName());

    private ArrayList<ArrayList<String>> randomKeys = new ArrayList();
    private ArrayList<ArrayList<String>> selectedKeys = new ArrayList();
    
    
    public PhoneticKeyBoard(){
        randomizeKeys();
    }

    public void randomizeKeys() {
        randomKeys = new ArrayList();
        ArrayList<String> allGroups = new ArrayList<String>(Arrays.asList(PHONEMES));
        Collections.shuffle(allGroups);
        while(!allGroups.isEmpty()) {
            ArrayList group = new ArrayList();
            int i;
            for(i=0; i<3; i++){
                group.add(allGroups.remove(0));
            }
            randomKeys.add(group);
        }
    }

    public String getAsSingleString(){
        String all = "";
        int i = 1;
        for(List<String> sg : randomKeys){
            String choices = "";
            for(String s : sg) {
                choices += s + "-";
            }
            all += Integer.toString(i) + "= " + choices + "\t";
            i += 1;
        }
        return all;
    }

    public void pressGroup(int i) {
        System.out.println(String.join(",", randomKeys.get(i-1)));
        selectedKeys.add(randomKeys.get(i-1));
    }
}
