import java.util.List;
import java.util.Map;

public class MiniMax extends MorrisGame {

    public MiniMax(BoardAI board_ai, char player) {
       this.board_ai = board_ai;
       this.player = player;
    }

    @Override
    public void play() {
    	Tracker t = new Tracker();
    	MaxMin(this.board_ai, t);
    	int estimate = Integer.MIN_VALUE;
    	for(Map.Entry<Integer, Position> entry: this.board_ai.board_pos.entrySet()) {	
    		List<BoardAI> list = entry.getValue().next_board_ai;
    		if(list==null || list.isEmpty()) {
    			continue;
    		} else {
    			estimate = Math.max(estimate, entry.getValue().staticEstimate);
    		}
    	}
    	System.out.println("Mimimax Estimate: "+ estimate);
    	System.out.println("Position Evaluated: "+t.node_evaluated);
    	printNext(this.board_ai, estimate);
    }
    
    public int MaxMin(BoardAI board_ai, Tracker t) {
    	Map<Integer, Position> map = board_ai.board_pos;
    	int result = Integer.MIN_VALUE;

    	for(Map.Entry<Integer, Position> entry: map.entrySet()) {
    		List<BoardAI> list = entry.getValue().next_board_ai;
    		t.node_evaluated++;
    		if(list==null || list.isEmpty()) {
    			int temp = board_ai.getStaticEstimation(this.player);
    			entry.getValue().staticEstimate = temp;
    		} else {
    			int temp = Integer.MIN_VALUE;
    			for(BoardAI b: list) {
    				temp = Math.max(MinMax(b, t), temp);
    				entry.getValue().staticEstimate = temp;
    			}
    		}
    	}
    	
    	for(Map.Entry<Integer, Position> entry: map.entrySet()) {
    		result = Math.max(entry.getValue().staticEstimate, result);
    	}
    	
    	return result;
    }
    
    public int MinMax(BoardAI board_ai, Tracker t) {
    	Map<Integer, Position> map = board_ai.board_pos;
    	int result = Integer.MAX_VALUE;
    	
    	for(Map.Entry<Integer, Position> entry: map.entrySet()) {
    		List<BoardAI> list = entry.getValue().next_board_ai;
    		t.node_evaluated++;
    		if(list==null || list.isEmpty()) {
    			int temp = board_ai.getStaticEstimation(this.player);
    			entry.getValue().staticEstimate = temp;
    		} else {
    			int temp = Integer.MAX_VALUE;
    			for(BoardAI b: list) {
    				temp = Math.min(MaxMin(b, t), temp);
    				entry.getValue().staticEstimate = temp;
    			}
    		}
    	}
    	
    	for(Map.Entry<Integer, Position> entry: map.entrySet()) {
    		result = Math.min(entry.getValue().staticEstimate, result);
    	}
    	
    	return result;
    }
}
