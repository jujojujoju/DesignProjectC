package project;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jaehyun on 2017-06-20.
 */
public class Subscription implements java.io.Serializable {
    private Map<Author, List<Paper>> subscriptMap;
    private Map<Author, Paper> subscriptStack;

    public Subscription() {
        subscriptMap = new HashMap<Author, List<Paper>>();
    }

    public Map<Author, List<Paper>> getSubscriptMap() {
        return subscriptMap;
    }

    public boolean checkSubscriptionList(Paper paper, Author author) {
        if(subscriptMap.containsKey(author)) {
            if(subscriptMap.get(author).contains(paper)) {
                //해당 구독이 이미 체크가 된 경우
                return false;
            }
            else {
                subscriptMap.get(author).add(paper);
                return true;
            }
        }
        return false;
    }

    public void addSubscription(Author author) {
        subscriptMap.put(author, new ArrayList<>());
    }

    public void deleteSubscription(Author author) {
        subscriptMap.remove(author);
    }

    public void writeObject() {
        try{
            FileOutputStream f = new FileOutputStream("tmp");
            ObjectOutput s = new ObjectOutputStream(f);
            s.writeObject(subscriptMap);
            s.flush();
            s.close();
            f.close();
        }
        catch(IOException e) { }
        System.out.println(subscriptMap.toString()+"저장");
    }

    public void readObject() {
        try {
            FileInputStream in = new FileInputStream("tmp");
            ObjectInput s = new ObjectInputStream(in);
            subscriptMap = (Map<Author, List<Paper>>)s.readObject();

            System.out.println(subscriptMap.toString()+"복구");
            s.close();
            in.close();
        }
        catch(IOException e) {  }
        catch(ClassNotFoundException e) {  }
    }
}
