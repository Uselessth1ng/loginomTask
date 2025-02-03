package org.example;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class Handler {

    Vector<Cluster> clusters = new Vector<>();
    double r;

    public void handle() throws SQLException {
        ArrayList<String> data;
        DB db = new DB();
        r = db.connect();
        int numRows = db.getNumRows();

        //фаза 1

        for (int i = 1; i <= numRows; i++) {
            data = db.readNote(i);
            data.set(2, defineCluster(data).toString());
            db.setCluster(data);
        }

        //фаза 2

        boolean moved;
        do {
            moved = false;
            for (int i = 1; i <= numRows; i++) {
                data = db.readNote(i);
                Integer newCluster = reDefineCluster(data);
                if (newCluster != Integer.parseInt(data.get(2))) {
                    clusters.get(Integer.parseInt(data.get(2))).deleteTran(data.get(1));
                    data.set(2, newCluster.toString());
                    moved = true;

                }
                db.setCluster(data);
            }
        } while (moved);
        clusters.removeIf(c -> c.w == 0);
        db.disconnect();
    }

    public Integer defineCluster(ArrayList<String> data){
        String tran = data.get(1);
        double max = -1, prom = -1;
        Integer maxCluster = -1;

        for (int i = 0; i < clusters.size(); i++) {
            prom = profit(clusters.get(i), tran);
            if (prom > max) {
                max = prom;
                maxCluster = i;
            }
        }
        Cluster newCluster = new Cluster();

        double res = profit(newCluster, tran);

        if (res > max) {
            newCluster.addTran(tran);
            clusters.add(newCluster);
            maxCluster = clusters.size() - 1;
        } else {
            clusters.get(maxCluster).addTran(tran);
        }
        return maxCluster;
    }

    public Integer reDefineCluster(ArrayList<String> data){
        int prevCluster = Integer.parseInt(data.get(2));
        String tran = data.get(1);
        double max = -999, prom = -999;
        Integer maxCluster = -1;

        for (int i = 0; i < clusters.size(); i++) {
            prom = profit(clusters.get(i), tran);
            if (prom > max && i != prevCluster) {
                max = prom;
                maxCluster = i;
            }
            else if (i == prevCluster){
                clusters.get(i).deleteTran(tran);
                prom = profit(clusters.get(i), tran);
                if (prom >= max) {
                    max = prom;
                    maxCluster = i;
                }
                clusters.get(i).addTran(tran);
            }
        }
        Cluster newCluster = new Cluster();

        double res = profit(newCluster, tran);

        if (res > max) {
            newCluster.addTran(tran);
            clusters.add(newCluster);
            maxCluster = clusters.size() - 1;
        } else if (maxCluster != prevCluster){
            clusters.get(maxCluster).addTran(tran);
        }

        return maxCluster;
    }

    public double profit(Cluster c, String t){
        double s = c.s + t.length();
        int w = c.w;
        for (int i = 0; i < t.length(); i++){
            if (!c.d.containsKey(t.charAt(i))){
                w++;
            }
        }
        double r1 = 0, r2 = 0, r3 = 0, r4 = 0;
        for (Cluster cluster : clusters){
            if (!c.equals(cluster)) {
                r1 += cluster.n * cluster.s / Math.pow(cluster.w, r);
                r2 += cluster.n;

            }
            else {
                r1 += (cluster.n + 1) * s / Math.pow(w, r);
                r2 += cluster.n + 1;
            }
            r3 += cluster.n * cluster.s / Math.pow(cluster.w, r);
            r4 += cluster.n;
        }
        r1 = Double.isNaN(r1 / r2) ? 0 : r1 / r2;
        r3 = Double.isNaN(r3 / r4) ? 0 : r3 / r4;
        return r1 - r3;
    }

}
