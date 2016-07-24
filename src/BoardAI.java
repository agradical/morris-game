import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BoardAI {
	
	Map<Integer, Position> board_pos;
	Board board;
	char player;
	int depth;
	
	public BoardAI(Board board, char player) {
		this.board = board;
		this.player = player; 
		Map<Integer, Position> map = new HashMap<Integer, Position>();
		char[] position_arr = board.position;
		
		for(int i=0; i<position_arr.length; i++) {
			Position pos = new Position(i, position_arr[i]);
			map.put(i, pos);
		}
		this.board_pos = map;
	}
	
	public void getAI(int depth, Position parent) {
		if(depth<0) return;
		this.depth = depth;
		//boolean improved = board.improved;
		if(parent!=null) {
			parent.addNextLevel(this);
		}
		
		if(!this.board.opening) {
			System.out.println("play section:"+new String(this.board.position)+":"+depth);
			for(Map.Entry<Integer, Position> entry: this.board_pos.entrySet()) {
				Position pos = entry.getValue();
				if(pos.content == this.player) {
					int[] possibleMove = Board.possibleMoves[entry.getKey()];
					for(int i=0; i<possibleMove.length; i++) {
						if(this.board.position[possibleMove[i]] == 'x') {
							char[] new_position_arr = Arrays.copyOf(this.board.position, this.board.position.length);
							
							new_position_arr[entry.getKey()] = 'x';
							new_position_arr[possibleMove[i]] = this.player;
							Board _board = new Board(new String(new_position_arr), this.board.improved, this.board.opening);
							
							char nextPlayer = this.player;
							if(this.player == 'w') {
								nextPlayer = 'b';
							} else {
								nextPlayer = 'w';
							}
							
							boolean isClosingMill = _board.isClosingMill(player, i);
							if(isClosingMill) {
								_board = generateRemove(_board, nextPlayer, this.board.improved);
							}
							
							BoardAI nxtboardAi = new BoardAI(_board, nextPlayer);
							nxtboardAi.getAI(depth-1, pos);
							//pos.addNextLevel(nxtboardAi);
						}
					}
				}
			}
			
		} else {
			System.out.println("Opening section:"+new String(this.board.position)+":"+depth);
			for(Map.Entry<Integer, Position> entry: this.board_pos.entrySet()) {
				Position pos = entry.getValue();
				if(pos.content == 'x') {
					char[] new_position_arr = Arrays.copyOf(this.board.position, this.board.position.length);
					new_position_arr[pos.index] = this.player;
					Board _board = new Board(new String(new_position_arr), this.board.improved, true);
					
					char nextPlayer = this.player;
					if(this.player == 'w') {
						nextPlayer = 'b';
					} else {
						nextPlayer = 'w';
					}
					
					boolean isClosingMill = _board.isClosingMill(this.player, pos.index);
					if(isClosingMill) {
						_board = generateRemove(_board, nextPlayer, this.board.improved);
					}
					
					BoardAI nxtboardAi = new BoardAI(_board, nextPlayer);
					nxtboardAi.getAI(depth-1, pos);
				}
			}
		}
	}
    
    public Board generateRemove(Board board, char nextPlayer, boolean improved) {
    	char[] positions = board.position;

    	for(int i=0; i<positions.length; i++) {
    		if(positions[i]==nextPlayer) {
    			boolean mill = board.isClosingMill(nextPlayer, i);
    			if(!mill) {
    				positions[i] = 'x';
    				break;
    			}
    		}
    	}
    	
    	Board _board = new Board(positions.toString(), improved, this.board.opening);
		return _board;
    }
    
    public int getPossibleNextMoves(char player) {
    	int count = 0;
    	char nextplayer = 'w';
    	if(player=='w'){
    		nextplayer = 'b';
    	} 
    	for(Map.Entry<Integer, Position> entry: this.board_pos.entrySet()) {
			Position pos = entry.getValue();
			if(pos.content == nextplayer) {
				int[] possibleMove = Board.possibleMoves[entry.getKey()];
				for(int i=0; i<possibleMove.length; i++) {
					if(board.position[possibleMove[i]] != 'x') {
						count++;
					}
				}
			}
		}
    	return count;
    }
    
    public int getStaticEstimation(char player) {
    	int white=0;
		int black=0;
		
		for(int i=0; i<this.board.position.length; i++) {
			if(this.board.position[i]==player) {
				white++;
			} else if(this.board.position[i]!='x') {
				black++;
			}
		}
		
		if(!this.board.improved) {
    		if(this.board.opening) {
    			return white-black;
    		} else {
    			int numBlackMoves = getPossibleNextMoves(player);
    			if (black <= 2) return(10000);
    			else if (white <= 2) return(-10000);
    			else if (numBlackMoves==0) return(10000);
    			else return (1000*(white - black) - numBlackMoves);
    		}
    	}
		else {
			
    		if(this.board.opening) {	
    			int proximity_to_mill = 0;
    			for(int i=0; i<this.board.position.length; i++) {
    				if(this.board.position[i]==player) {
    					int[] possibleMove = Board.possibleMoves[i];
    					for(int j=0; j<possibleMove.length; j++) {
    						if(this.board.position[j]=='x') {
    							//high preference to proximity to closing my mill
    							proximity_to_mill++;
    						}
    					}
    				}
    			}
    			return (1000*(white-black)+proximity_to_mill);
    			
    		} else {
    			int numBlackMoves = getPossibleNextMoves(player);
    			if (black <= 2) return(10000);
    			else if (white <= 2) return(-10000);
    			else if (numBlackMoves==0) return(10000);
    			else {
    				int proximity_to_mill = 0;
    				for(int i=0; i<this.board.position.length; i++) {
    					if(this.board.position[i]==player) {
    						int[] possibleMove = Board.possibleMoves[i];
    						for(int j=0; j<possibleMove.length; j++) {
    							if(this.board.position[j]=='x') {
    								//high preference to proximity to closing other mills
    								int[] vicinity = getVicinityPositions(j);
    								for(int k=0; k<vicinity.length; k++) {
    									if(vicinity[k] != i) {
    										proximity_to_mill += 5;
    									}
    								}
    							}
    						}
    					}
    				}
    				return (1000*(white-black)+proximity_to_mill-numBlackMoves);
    			}
    		}
    	} 
    }
    
    public int[] getVicinityPositions(int index) {
    	char player = this.board.position[index];
    	Map<Integer, Integer> map= new HashMap<Integer, Integer>();
    	int [][] arrs = Board.mills[index];
    	for(int i=0; i<arrs.length; i++) {
    		int[] arr = arrs[i];
    		for(int j=0; j<arr.length; j++) {
    			if(this.board.position[arr[j]] == player) {
    				map.put(j, 1);
    			}
    		}
    	}
    	int[] list = new int[map.size()];
    	int i=0;
    	for(Map.Entry<Integer, Integer> entry: map.entrySet()) {
    		list[i] = entry.getKey();
    		i++;
    	}
    	return list;
    }
}


