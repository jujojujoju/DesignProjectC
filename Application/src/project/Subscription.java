package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jaehyun on 2017-06-20.
 */
public class Subscription {
    private Map<Author, List<Paper>> subscriptMap;
    private Map<Author, Paper> subscriptStack;

    public Subscription() {
        subscriptMap = new HashMap<Author, List<Paper>>();
        subscriptMap.put(new Author("AAA"), new ArrayList<Paper>());
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
}
