package Maps;

import java.util.Map;

public class Mapping {
        protected long row;
        protected long column;
        protected static Region [][] Mapp;
        public Mapping(long row,long column){
            this.row = row;
            this.column = column;
            Mapp = new Region[(int) this.row][(int)this.column];

        }
        public static void computeConnect(Region r){
            Map<String,Region> connect = r.getConnectReg();

            if (r.getI()-1 != -1 && Mapp[(int) r.getI()-1][(int)r.getJ()] != null ){
                connect.put("N", Mapp[(int) r.getI()-1][(int)r.getJ()]);
            }
            if(r.getI()-1 != Mapp.length && Mapp[(int) r.getI()+1][(int)r.getJ()] != null ){
                connect.put("S", Mapp[(int) r.getI()+1][(int)r.getJ()]);
            }

        }




}
