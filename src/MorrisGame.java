import java.util.List;
import java.util.Map;

public abstract class MorrisGame {
    
    BoardAI board_ai;
    char player;

    public abstract void play();

    public void printNext(BoardAI board_ai, int estimate) {
    	Map<Integer, Position> map = board_ai.board_pos;
    	for(Map.Entry<Integer, Position> entry: map.entrySet()) {
    		
    		if(entry.getValue().staticEstimate == estimate) {
    			List<BoardAI> list = entry.getValue().next_board_ai;
    			
    			if(list == null || list.isEmpty()) {
    				continue;
    			}
    			for(BoardAI b: list) {
    				Map<Integer, Position> nextmap = b.board_pos;
    		    	for(Map.Entry<Integer, Position> nextentry: nextmap.entrySet()) {
    		    		if(nextentry.getValue().staticEstimate == estimate) {
    		    			System.out.println("Board Position: "+new String(b.board.position));
    		    			Main.writeFile(new String(board_ai.board.position));
    		    			return;
    		    		}
    		    	}
    			}
    		}
    	}
    }
    
    public boolean traverse(BoardAI board_ai, int estimate) {
    	Map<Integer, Position> map = board_ai.board_pos;
    	boolean found = false;
    	for(Map.Entry<Integer, Position> entry: map.entrySet()) {
    		if(found) {
    			break;
    		}
    		List<BoardAI> list = entry.getValue().next_board_ai;
    		if(entry.getValue().staticEstimate == estimate) {
    			if(list == null || list.isEmpty()) {
    				System.out.println("Board Position: "+new String(board_ai.board.position));
    				Main.writeFile(new String(board_ai.board.position));
    				return true;
    			}
    			for(BoardAI b: list) {
        			found = traverse(b, estimate);
        			if(found) {
        				break;
        			}
    			}
    		}
    	}
    	return found;
    }
}
