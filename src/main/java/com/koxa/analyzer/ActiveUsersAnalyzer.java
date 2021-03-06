package com.koxa.analyzer;

import akka.actor.AbstractActor;
import com.koxa.model.Events;
import com.koxa.model.MostActiveUsers;
import com.koxa.model.ReviewItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denys on 2017-05-03.
 */
public class ActiveUsersAnalyzer extends AbstractActor {

    private Map<String, Integer> users = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReviewItem.class, this::processItem)
                .matchEquals(Events.NO_MORE_RECORDS, it -> processResult())
                .build();
    }

    private void processResult() {
        getSender().tell(new MostActiveUsers(users), self());
    }

    private void processItem(ReviewItem reviewItem) {
        users.compute(reviewItem.getProfileName(), (key, value) -> value == null ? 1 : value + 1);
    }
}
