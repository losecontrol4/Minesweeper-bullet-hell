/*----------------------------------------------------------------
 *  Author:   L.King
 *  Email:    lking@holycross.edu
 *  Written:  7/27/2015
 *
 *----------------------------------------------------------------*/

public class Node {
    public boolean recent; 
    public int val;
    public Node next;
    public String username;

    public Node() {
        val = 0;
        username = "arhall21";
        recent = false;
        next = null;
    }

    public Node(int v, String name) {
        val = v;
        username = name;
        recent = false;
        next = null;
    }

    public Node(int v, String name, Node n) {
        val = v;
        username = name;
        recent = false;
        next = n;
    }
    public Node(int v, String name, Node n, boolean Recent) {
        val = v;
        username = name;
        recent = Recent;
        next = n;
    }
    public Node(int v, String name, boolean Recent){
        val = v;
    username = name;
    recent = Recent;
    next = null;
    }
    public void NodeRecentChange(int v, String name, int i){
        if (i == 0)
            this.recent = false;
        if (i == 1)
            this.recent = true;
        return;
            }
    public boolean Recent(){
        return this.recent;
    }

    public String toString() {
        return String.format("Node @ %p { val = %d, name equal = %s, next = %p }", this, val, username, next);
    }

} // Node Class
