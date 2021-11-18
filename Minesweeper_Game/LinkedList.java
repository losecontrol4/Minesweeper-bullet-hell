/*----------------------------------------------------------------
 * Adam Hall
 *----------------------------------------------------------------*/

public class LinkedList {

    public Node head;

    public LinkedList() {
        head = null;
    }

    public LinkedList(String filename) {
        head = null;

        In inputStream = new In(filename);

        Node last = null;
        Node newItem = null;

        while (!inputStream.isEmpty()) {
            String name = inputStream.readString();
            int val = inputStream.readInt();

            // Create a node to hold this value
            newItem = new Node(val, name);

            if (head == null) {  
                head = newItem;
            } //insert in empty list       
            else { 
                last.next = newItem;
            } //insert at the end

            // Advance last pointer to point at the new item
            last = newItem;
        } // while more items to insert

        inputStream.close();
    }

    public String toString() {
        return String.format("LinkedList { head = %p }", head);
    }

    public void print() {
        Node curr = head;
        while (curr != null) {
            System.out.println("User " + curr.username + " won in  " + curr.val + " seconds");
            curr = curr.next;
        } // still items left to print
    } // end of print

    public void insertFront(int val, String name) {
        if (head == null) {  
            head = new Node(val, name);
        } // list is empty, so make the head be that value
        else { 
            Node temp = head;
            head = new Node(val, name);
            head.next = temp;
        } // insert new value at the front
    } // end of insertFront
    public void insertFront(int val, String name, boolean Recent) {
        if (head == null) {  
            head = new Node(val, name, Recent);
        } // list is empty, so make the head be that value
        else { 
            Node temp = head;
            head = new Node(val, name, Recent);
            head.next = temp;
        } // insert new value at the front
    } // end of insertFront

    public void insertRear(int val, String name) {
        if (head == null) {  
            head = new Node(val, name);
        } // list is empty, so make the head be that value
        else { 
            Node curr = head;
            while (curr.next != null) { 
                curr = curr.next;
            } // find the end of the list
            // insert the new node
            curr.next = new Node(val, name);
        } // insert at the end
    } // end of insertRear

    public void insertOrdered(int newVal, String name) {
    
         Node curr = head;
        if (head == null){//adds to the front of the list if empty
            insertFront(newVal, name);
            return;
        }
       
        if (newVal < curr.val){//adds to the Front if lowest value
            insertFront(newVal, name);
            return;
        }
        while(curr.next != null){
            if (newVal >= curr.val && newVal < curr.next.val){//adds between
                Node insert = new Node(newVal, name, curr.next);
                curr.next = insert;
                return;
            }
            curr = curr.next;
            
        }
        curr.next = new Node(newVal, name);//adds to the end
        //this next bit is to chop off anything after ten items for leaderboard.
        curr = head;
        for(int i = 0; i < 10; i++){
            if (curr != null)
                return;
            curr = curr.next;
        }
        curr.next = null;
        
        return;
        
        
    } //insertOrdered
    public int NumItems() {
        int i = 0;
        Node curr = head;
        while(curr != null){
            i++;
            curr = curr.next;
        }
        return i;
    }

    public int getScore(int num){
        Node curr = head;
        for(int i = 0; i < num - 1; i++)
            curr = curr.next;
        return curr.val;
    }
     public String getName(int num){
        Node curr = head;
        for(int i = 0; i < num - 1; i++)
            curr = curr.next;
        return curr.username;
    }
    
        public boolean contains(int targetVal) {
        boolean contains = false;
        Node curr = head;
        while (curr != null) {
            if (curr.val == targetVal){
                contains = true;
                break;
            }
                curr = curr.next;
            }
                    return contains;
    }  // end of contains()





    public void insertOrdered(int newVal, String name, boolean Recent) {
    
         Node curr = head;
        if (head == null){//adds to the front of the list if empty
            insertFront(newVal, name, Recent);
            return;
        }
       
        if (newVal < curr.val){//adds to the Front if lowest value
            insertFront(newVal, name, Recent);
            return;
        }
        while(curr.next != null){
            if (newVal >= curr.val && newVal < curr.next.val){//adds between
                Node insert = new Node(newVal, name, curr.next, Recent);
                curr.next = insert;
                
                return;
            }
            curr = curr.next;
            
        }
        curr.next = new Node(newVal, name, Recent);//adds to the end
        //this next bit is to chop off anything after ten items for leaderboard.
        curr = head;
        for(int i = 0; i < 10; i++){
            if (curr != null)
                return;
            curr = curr.next;
        }
        curr.next = null;
        
        return;
        
        
    } //insertOrdered

    public int SearchRecent(){
        int i = 0;
        Node curr = head;
        while(curr != null){
            i++;
            if (curr.Recent())
                return i;           
            curr = curr.next;
        }
        return 0;
    }
}// end of LinkedList Class

 
