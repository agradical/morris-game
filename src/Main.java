import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class Main {
	static String infile;
	static String outfile;
	
    public static void main(String args[]) throws Exception{
        if(args.length < 3) {
            System.out.println("inputfile, outputfile, depth, player, gametype(MinMax[1]/Alpha[2]), opening[o]/play[p], improved[1]");
            System.exit(1);
        }
        
        Main.infile = args[0];
        Main.outfile = args[1];
        
        File inf = new File(args[0]);
        File of  = new File(args[1]);
        
        if(!inf.exists()) {
            throw new Exception("File not found");
        }
        if(!of.exists()) {
            of.createNewFile();
        }
        
        
        int depth = Integer.parseInt(args[2]);
        
        char player = 'w';
        boolean opening = true; //opening sequence
        int gametype = 1;  // 1 : MiniMax, 2 : AlphaBeta
        boolean improved = false; //improved version, true:false
        
        if(args.length >= 4) {
            if(args[3].equals("B") || args[3].equals("b")) {
                player = 'b';
            }
            if(args.length >= 5) {
                gametype = Integer.parseInt(args[4]);
            	if(args.length >= 6) {
            		if(!args[5].equals("o")) {
            			opening = false;
            		}
            		if(args.length >= 7) {
            			if(Integer.parseInt(args[6]) == 1) {
            				improved = true;
            			}
            		}
            	}
            }
        }
        
        Scanner s = new Scanner(inf);
        String inputBoardConf = s.nextLine();
        
        Board boardConfiguration = new Board(inputBoardConf, improved, opening);
        BoardAI boardAI = new BoardAI(boardConfiguration, player);
        boardAI.getAI(depth, null);
        
        MorrisGame game; 
        
        if(gametype == 2) {
        	game = new AlphaBeta(boardAI, player);
        } else {
        	game = new MiniMax(boardAI, player);
        }
        
        game.play();
        
        s.close();
    }
    
    public static void writeFile(String data) {
    	try {
    		FileWriter fw = new FileWriter(new File(Main.outfile));
    		fw.write(data);
    		fw.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
}
