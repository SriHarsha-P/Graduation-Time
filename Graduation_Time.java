import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graduation_Time {
	
	// List of the variable that are used in this class
	private HashMap<String, Set<String>> adjList;
	List<String> sortedNodes = new ArrayList<>();
	HashMap<String, String> destSourceMap = new HashMap<String, String>();
	int[] distance;
	List<String> longestPath = new ArrayList<>();
	Stack stack = new Stack();
	int numNodes;
	int[] arr = new int[numNodes];
	int length = 0;
	HashMap<String, Boolean> visited = new HashMap<>(numNodes);

	public Graduation_Time() {
		this.adjList = new HashMap<>();
	}
	
	// Creating the vertex or nodes 
	public void insertVertex(String node) {
		if (this.adjList.containsKey(node)) {
			throw new IllegalArgumentException("Given vertex is already there.");
		}
		this.adjList.put(node, new HashSet<String>());
		this.visited.put(node, false);
	}
	
	// Creating the route for the edges
	public void insertEdge(String node, String map) {
		if (!this.adjList.containsKey(node) || !this.adjList.containsKey(map)) {
			throw new IllegalArgumentException();
		}
		this.adjList.get(node).add(map);
	}
	
	// Implementation of the topological sort
	public void topologicalsorting() {
		Iterator itr = adjList.entrySet().iterator();
		while (itr.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) itr.next();
			String str = (String) entry.getKey();
			if (visited.get(str) == false)
				sortUtil(str);
		}
		while (stack.empty() == false) {
			sortedNodes.add((String) stack.pop());
		}
		System.out.println("Topological Sorting: ");
		System.out.println(sortedNodes);
		System.out.println(" ");
	}

	public void sortUtil(String str) {
		visited.put(str, true);
		Iterator<String> itr = adjList.get(str).iterator();
		while (itr.hasNext()) {
			String var = itr.next();
			if (!visited.get(var))
				sortUtil(var);
		}
		stack.push(str);
	}
	
	// File reader from the input files
	public void ReadFile() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:\\graph.txt")); // Path for the input files
			String line;
			int Value = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("#")) {
					Value++;
					continue;
				}
				if (Value == 0) {
					numNodes = Integer.valueOf(line);
				} else if (Value == 1) {
					insertVertex(line);
				} else if (Value == 2) {
					String[] arrOfStr = line.split(" ");
					insertEdge(arrOfStr[0], arrOfStr[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	//Labeling the nodes
	public String[] labelNodes(List<String> sortedNodes) {
		String[] labeledNodes = new String[sortedNodes.size()];
		int index = 0;
		for (String node : sortedNodes) {
			labeledNodes[index] = node;
			index += 1;
		}
		return labeledNodes;
	}

	// Calculating the distance of the paths
	public void calculateDistance(int n, String[] labeledNodes) {
		distance = new int[sortedNodes.size()];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (adjList.containsKey(labeledNodes[i])) {
					if (adjList.get(labeledNodes[i]).contains(labeledNodes[j])) {
						if (distance[j] < distance[i] + 1) {
							distance[j] = distance[i] + 1;
							if (destSourceMap.containsKey(labeledNodes[j]))
								destSourceMap.replace(labeledNodes[j], labeledNodes[i]);
							else
								destSourceMap.put(labeledNodes[j], labeledNodes[i]);
						}
					}
				}
			}
		}
	}

	public String getEndNode(int n, String[] labeledNodes) {
		String endNode = "";
		int max = Integer.MIN_VALUE;
		for (int index = 0; index < n; index++) {
			if (distance[index] > max) {
				max = distance[index];
				endNode = labeledNodes[index];
			}
		}
		length = max;
		return endNode;
	}

	// Finding the longest path
	public void longestPath(String node) {
		if (!destSourceMap.containsKey(node)) {
			longestPath.add(node);
			return;
		}
		longestPath(destSourceMap.get(node));
		longestPath.add(node);
	}

	// Main methods for the class
	public static void main(String[] args) {
		Graduation_Time graph = new Graduation_Time();
		graph.ReadFile();
		graph.topologicalsorting();
		String[] labeledNodes = graph.labelNodes(graph.sortedNodes);
		graph.calculateDistance(graph.numNodes, labeledNodes);
		String endNode = graph.getEndNode(graph.numNodes, labeledNodes);
		System.out.println("Length of the Longest Path: ");
		System.out.println(graph.length);
		graph.longestPath(endNode);
		System.out.println(" ");
		System.out.println("A Longest Path : ");
		System.out.println(graph.longestPath);
	}
}
