package org.example;

import java.util.HashMap;

public class Cluster {
    public int w = 0, n = 0;
    public double s = 0, h = 0;
    HashMap<String, Integer> d = new HashMap<>();

    public void addTran(String t){
        for (char c : t.toCharArray()){
            if (!d.containsKey(String.valueOf(c))){
                d.put(String.valueOf(c), 1);
                w++;
            }
            else {
                d.replace(String.valueOf(c), d.get(String.valueOf(c)) + 1);
            }
            s++;
        }
        h = s / w;
        n++;
    }

    public void deleteTran(String tran){
        for (char c : tran.toCharArray()){
            d.replace(String.valueOf(c), d.get(String.valueOf(c)) - 1);
            if (d.get(String.valueOf(c)) == 0) {
                d.remove(String.valueOf(c));
                w--;
            }
            s--;
        }
        h = s / w;
        n--;
    }
}
