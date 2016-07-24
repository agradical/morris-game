import java.util.ArrayList;
import java.util.List;

public class Position {
    int index;
    char content;
    int staticEstimate;
    List<BoardAI> next_board_ai;
    
    public Position(int index, char content) {
        this.index = index;
        this.content = content;
    }
    
    public void addNextLevel(BoardAI board_ai) {
    	if(next_board_ai==null) {
    		next_board_ai = new ArrayList<BoardAI>();
    	}
		next_board_ai.add(board_ai);
    }
}
