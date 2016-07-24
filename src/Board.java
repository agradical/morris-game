public class Board {
    
    char[] position = new char[23];
    boolean improved = false;
    boolean opening = false;
    
    static int[][] possibleMoves = new int[][] {
        {8,1,3}, //a0
        {0,2,4}, //d0
        {1,5,13}, //g0
        {0,9,4,6}, //b1
        {1,3,5}, //d1
        {2,4,7,12}, //f1
        {10,7,3}, //c2
        {11,6,5}, //e2
        {20,0,9}, //a3
        {8,10,17,3}, //b3
        {9,6,14}, //c3
        {7,16,12}, //e3
        {11,13,5,19}, //f3
        {12,2,22}, //g3
        {17,10,15}, //c4
        {14,18,16}, //d4
        {15,19,11}, //e4
        {14,9,18,20}, //b5
        {17,15,19,21}, //d5
        {16,18,12,22}, //f5
        {8,17,21},
        {20,22,18},
        {21,13,19},
    };
    
    static int[][][] mills = new int[][][] {
        {{8,20},{0,1},{3,6}}, //a0
        {{0,2}}, //d0
        {{0,1},{5,6},{13,22}}, //g0
        {{0,6},{9,17},{4,5}}, //b1
        {{3,5}}, //d1
        {{2,6},{3,4},{12,19}}, //f1
        {{0,3},{10,14}}, //c2
        {{2,5},{11,16}}, //e2
        {{0,22},{9,10}}, //a3
        {{8,10},{3,17}}, //b3
        {{8,9},{6,14}}, //c3
        {{7,16},{12,13}}, //e3
        {{5,19},{11,13}}, //f3
        {{11,12},{2,22}}, //g3
        {{6,10},{15,16},{17,20}}, //c4
        {{14,16},{18,21}}, //d4
        {{14,15},{7,11},{19,22}}, //e4
        {{3,9},{18,19},{14,20}}, //b5
        {{17,19},{15,21}}, //d5
        {{16,22},{17,18},{5,12}}, //f5
        {{0,8},{14,17},{21,22}}, //a6
        {{15,18},{20,22}}, //d6
        {{20,21},{2,13},{16,19}} //g6      
    };

    public Board(String state, boolean improved, boolean opening) {
    	this.position = state.toLowerCase().toCharArray();
        this.improved = improved;
        this.opening = opening;
    }
   
    
    public boolean isClosingMill(char player, int positionJustMoved) {
    	int[][] millpositions = mills[positionJustMoved];
    	for(int i=0; i<millpositions.length; i++) {
    		int[] millposition = millpositions[i];
    		for(int j=0; j<millposition.length; i++) {
    			if(millposition[j]!=player) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
}
