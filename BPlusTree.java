import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 * TODO: Rename to BPlusTree
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;

	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {
		LeafNode<K,T> leaf = (LeafNode<K,T>) treeSearch(root, key);
		if(leaf == null) return null;
		T result;
		for (int i=0; i<leaf.keys.size(); i++){
			if(key == leaf.keys.get(i)){
				result = leaf.values.get(i);
			}
		}
		return result;
	}
	
	/*
	 * helper function 
	 */
	public Node<K,T> treeSearch(Node<K,T> nodepointer, K key){
		if (nodepointer.isLeafNode) {
			return nodepointer;
		}
		else {
			IndexNode<K,T> target = ((IndexNode<K, T>) nodepointer);
			// K < K_0
			if (key.compareTo(nodepointer.keys.get(0)) < 0) {
				Node<K,T> child = target.children.get(0);
				return treeSearch(child, key); 
			} else {
				// K >= K_m
				if(key.compareTo(nodepointer.keys.get(nodepointer.keys.size()-1)) >= 0){
					Node<K,T> child = target.children.get(nodepointer.keys.size());
					return treeSearch(child, key);
				}
				else{
					// K_i <= K < K_{i+1}
					for(int i=0; i < nodepointer.keys.size(); i++){
						if(key.compareTo(nodepointer.keys.get(i)) >= 0 
								&& key.compareTo(nodepointer.keys.get(i+1)) < 0){
							Node<K,T> child = target.children.get(i);
							return treeSearch(child, key);
						}
					}
				}
			}
		}
		
		return null;
	}

	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	//
	// need a MAP for tracking parent nodes? e.g. map(key= node, value= parent node)
	public void insert(K key, T value) {
		//Entry<K,T> entry = new AbstractMap.SimpleEntry<K,T>(key,value);
		insertToTree(root, key, value, null);
	}
	
	public void insertToTree(Node<K,T> nodepointer, K key, T value, Entry<K,Node<K,T>>newChildren){
		if(!nodepointer.isLeafNode){
			for(int i=0; i < nodepointer.keys.size(); i++){
				if(key.compareTo(nodepointer.keys.get(i)) >= 0 
						&& key.compareTo(nodepointer.keys.get(i+1)) < 0){
					IndexNode<K,T> target = (IndexNode<K,T>)nodepointer;
					Node<K,T> child = target.children.get(i);
					insertToTree(child, key, value, newChildren);
				}
				if(newChildren == null) return;
				else{
					if(nodepointer.isUnderflowed()){
						IndexNode<K,T> target = (IndexNode<K,T>)nodepointer;
						int newIndex=0;
						K newKey = newChildren.getKey();
						while(newKey.compareTo(nodepointer.keys.get(newIndex))<0 
								&& newIndex <= nodepointer.keys.size()){
							newIndex++;
						}
						target.insertSorted(newChildren, newIndex);
						newChildren = null;
					}
					else{
						K newKey = newChildren.getKey();
						IndexNode<K,T> target = (IndexNode<K,T>)nodepointer;
						Entry<K, Node<K,T>> newEntry = splitIndexNode(target, newKey);
						//remove last d keys from left node
						
						//if nodepointer is root
						if(nodepointer.equals(root)){
							IndexNode<K,T> newRoot = new IndexNode<K,T>()
						}
						
							
						}
					}
				}
				
			}
		
	}
		//when root is empty
		/*
		if (root == null){
			Node<K,T> newLeaf = new LeafNode<K,T>(key,value);
			root = newLeaf;
			root.keys.add(key);
			
		}
		//base case
		if(target.isUnderflowed())
		//AbstractMap<Node<K,T>,Node<K,T>> tracer = new AbstractMap<Node<K,T>,Node<K,T>>();
		
		//if target is leaf
		LeafNode<K,T> target = (LeafNode<K,T>)treeSearch(root, key);
		if (target.isUnderflowed()){
			//when node has available space for one more key
			target.insertSorted(key, value);
		}
		else if(target.isOverflowed()){
			//when node is full, but its parent has space for one more key
			Entry<K, Node<K,T>>spliter = splitLeafNode(target);
			target.keys
		}
		else if(){
			//when the target node and its parent are both full
		}
		*/
		
	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param leaf, any other relevant data
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf, ...) {
		

		return null;
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index, any other relevant data
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> indexNode, K newKey) {
		int newIndex=0;
		ArrayList<K> tempArray = indexNode.keys;
		while(newKey.compareTo(indexNode.keys.get(newIndex))<0 
				&& newIndex <= indexNode.keys.size()){
			newIndex++;
		}
		tempArray.add(newIndex,newKey);
		int splitIndex = tempArray.size()/2+1;
		
		K splitKey = tempArray.get(splitIndex);
		IndexNode<K,T> rightNode = 
				new IndexNode<K,T>(splitKey, indexNode.children.get(splitIndex), 
						indexNode.children.get(splitIndex+1));
		int i = splitIndex+1;
		while(i <= tempArray.size()){
			rightNode.keys.add(tempArray.get(i));
			rightNode.children.add(indexNode.children.get(i+1));
		}
		Entry<K,Node<K,T>> result = new AbstractMap.SimpleEntry<K,Node<K,T>>(splitKey, rightNode);
		return result;
	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {

	}

	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {
		return -1;

	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		return -1;
	}

}
