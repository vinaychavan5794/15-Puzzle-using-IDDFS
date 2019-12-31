import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Iddfs {

	static int nodeSearched=0;
	
	private static class Node{
		public Node down=null;
		public Node right=null;
		public int tree=0;
		public Node left=null;
		public Node parent=null;
		public Node up=null;
		public int matrix[][]=new int [4][4];
		public String move="";
		public int xeroX,xeroY;
	}
	/*(Method Description)
	 * This function asks user to load the puzzle(2D array) with initial state.*/
	private static int [][] fillPuzzleMatrix(Node object) {
		Scanner input= new Scanner(System.in);
		System.out.println("Please Enter the Numbers in the Puzzle");
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				object.matrix[i][j]=input.nextInt();
				if(object.matrix[i][j] == 0){
					object.xeroX = i;
					object.xeroY = j;
	               }
			}
		}
		input.close();
		return object.matrix;
	}
	
	/*(Method Description)
	 * This function loads the 2D array with goal state.*/ 
	private static int [][] fillCorrectMatrix() {
		int [][] data=new int[4][4];
		int flag=1;
		for(int i=0;i<4;++i) {
			for(int j=0;j<4;++j) {
				data[i][j]=flag;
				flag++;
			}
		}
		data[3][3]=0;
		return data;
	}
	
	/*(Method Description)
	 * This function creates new node depending on the action chosen:right,left,up,down*/
	public static int[][] createNewNode(int currentMatrix [][],String nextMove,int xeroX,int xeroY){
		int newNode [][] = new int[4][4];
		for(int j=0;j<4;j++) {
			int[] dimension=currentMatrix[j];
			int length =dimension.length;
			newNode[j]=new int[length];
			System.arraycopy(dimension, 0, newNode[j], 0, length);
		}
		switch(nextMove) {
		case "LEFT":
			newNode [xeroX][xeroY] = newNode[xeroX][xeroY-1];
			newNode [xeroX][xeroY-1]=0;
			return newNode;
		case "RIGHT":
			newNode [xeroX][xeroY] = newNode[xeroX][xeroY+1];
			newNode [xeroX][xeroY+1]=0;
			return newNode;
		case "DOWN":
			newNode [xeroX][xeroY] = newNode[xeroX+1][xeroY];
			newNode [xeroX+1][xeroY]=0;
			return newNode;
		case "UP":
			newNode [xeroX][xeroY] = newNode[xeroX-1][xeroY];
			newNode [xeroX-1][xeroY]=0;
			return newNode;
		}
		return newNode;
	}
	
	/*(Method Description)
	 * This function prints the state of the puzzle in 2D matrix.*/
	private static void printMatrix(int [][] matrixToPrint,int row,int col) {
		for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                System.out.print(matrixToPrint[i][j]+"\t");
            }             
            System.out.println();
        }	
	}
	
	/*(Method Description)
	 * This function calls createNewNode function to create new node for respective moves
	 *  if right,left,up,down moves are possible.*/
	public static void findNodes(Node currentNode) {
		if(currentNode.xeroX>0) {
			currentNode.up=new Node();
			currentNode.up.tree=currentNode.tree+1;
			currentNode.up.xeroY=currentNode.xeroY;
			currentNode.up.parent=currentNode;
			currentNode.up.xeroX=currentNode.xeroX-1;
			currentNode.up.move="U";
			currentNode.up.matrix=createNewNode(currentNode.matrix, "UP", currentNode.xeroX, currentNode.xeroY);	
		}
		if(currentNode.xeroX<3) {
			currentNode.down=new Node();
			currentNode.down.tree=currentNode.tree+1;
			currentNode.down.xeroY=currentNode.xeroY;
			currentNode.down.parent=currentNode;
			currentNode.down.xeroX=currentNode.xeroX+1;
			currentNode.down.move="D";
			currentNode.down.matrix=createNewNode(currentNode.matrix, "DOWN", currentNode.xeroX, currentNode.xeroY);	
		}
		if(currentNode.xeroY>0) {
			currentNode.left=new Node();
			currentNode.left.tree=currentNode.tree+1;
			currentNode.left.xeroY=currentNode.xeroY-1;
			currentNode.left.parent=currentNode;
			currentNode.left.xeroX=currentNode.xeroX;
			currentNode.left.move="L";
			currentNode.left.matrix=createNewNode(currentNode.matrix, "LEFT", currentNode.xeroX, currentNode.xeroY);
		}
		if(currentNode.xeroY<3) {
			currentNode.right=new Node();
			currentNode.right.tree=currentNode.tree+1;
			currentNode.right.xeroY=currentNode.xeroY+1;
			currentNode.right.parent=currentNode;
			currentNode.right.xeroX=currentNode.xeroX;
			currentNode.right.move="R";
			currentNode.right.matrix=createNewNode(currentNode.matrix, "RIGHT", currentNode.xeroX, currentNode.xeroY);	
		}
	}
	
	/*(Method Description)
	 * This function is used to check if the goal state is reached.*/
	public static boolean goalReached(Node solvedNode){
        int puzzleSolved [][]  = fillCorrectMatrix();
        for(int j=0;j<solvedNode.matrix.length;++j) {
			for(int i=0;i<solvedNode.matrix[j].length;++i) {
				if(solvedNode.matrix[j][i]!=puzzleSolved[j][i]) {
					return false;
				}
			}
		}
		return true;
    }
	/*(Method Description)
	 * This function returns the solved puzzle state (2D array) 
	 * and the number of nodes expanded according to IDDFS logic*/
	public static Node iddfsSolver(Node firstNode) {
		int depth=0;
		int counter=0;
		while(depth<Integer.MAX_VALUE) {
			ArrayList<Node> list =new ArrayList<Node>();
			LinkedList<String> markedNode=new LinkedList<String>();
			list.add(firstNode);
			while(!list.isEmpty()) {
				Node recentnode = list.remove(list.size()-1);
				counter++;
				String lastVisited = visited(recentnode);
				if(goalReached(recentnode)) {
					setnodeSearched(counter);
					return recentnode;
				}
				else if (!markedNode.contains(lastVisited) && recentnode.tree<depth) {
					markedNode.add(lastVisited);
					findNodes(recentnode);
					
					if(recentnode.right!=null) {
						list.add(recentnode.right);
					}
					if(recentnode.left!=null) {
						list.add(recentnode.left);
					}
					if(recentnode.down!=null) {
						list.add(recentnode.down);
					}
					if(recentnode.up!=null) {
						list.add(recentnode.up);
					}
				}
			}
			depth++;
		}
		return null;
	}
	
	/*(Method Description)
	 * This function converts 2D array to String*/
	public static String visited (Node visitedNode) {
		String markdown="";
		for(int x=0;x<4;x++) {
			for(int y=0;y<4;y++) {
				markdown=markdown+visitedNode.matrix[x][y];
			}
		}
		return markdown;
	}
	
	/*(Method Description)
	 * This function is used to print moves taken to reach the goal state*/
	public static void printMoves(Node outputMoves) {
		ArrayList<Node> outputSequence=new ArrayList<Node>();
		String moves="";
		Node currentSequence =outputMoves;
		while(currentSequence!=null) {
			outputSequence.add(0,currentSequence);
			currentSequence=currentSequence.parent;
		}
		for (Node temp : outputSequence) {
        	moves+=temp.move;
        }
        System.out.println("Moves:"+moves);
	}
	public  static void setnodeSearched(int counter) {
		nodeSearched=counter;
	}
	
	/*(Method Description)
	 * This function returns the number of nodes expanded during IDDFS in search of the goal state*/
	public static int getnodeSearched() {
		return nodeSearched;
	}
	
	public static void main(String[] args) { 
		Node startNode = new Node();
		Iddfs.fillPuzzleMatrix(startNode);
		System.out.println("Initial Puzzle State\n");
		Iddfs.printMatrix(startNode.matrix, 4, 4);
		System.out.println("\nAfter IDDFS:\n");
       	Long memUsedBefore= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   		Long starttime=System.currentTimeMillis();
       	try{ 
       		Node solutionNode = iddfsSolver(startNode);
       		Long endtime=System.currentTimeMillis();
       		Long memUsedAfter= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
       		Long memoryUsedActual=memUsedAfter-memUsedBefore;
    	   	if(solutionNode != null){
    	   		printMoves(solutionNode);
    	   	}
    		System.out.println("Nodes Expanded:"+Iddfs.getnodeSearched());
    		System.out.println("Time taken:"+(endtime-starttime+"ms"));
    		System.out.println("Memory Used:"+memoryUsedActual/1024+"KB");
       	}
       	catch(OutOfMemoryError e){
       		System.out.println("\nError: No Solution found as IDDFS ran out of Memory!\n");
       }
	}
}

