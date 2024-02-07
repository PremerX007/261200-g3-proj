public class Mapping {
        protected long row;
        protected long column;
        protected Region [][] Maps;
        public Mapping(long row,long column){
            this.row = row;
            this.column = column;
            this.Maps = new Region[(int) this.row][(int)this.column];

        }
        public static void computeConnect(Region r){

        }


}
