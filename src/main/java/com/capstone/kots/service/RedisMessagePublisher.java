package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements MessagePublisher {

    @Autowired
    private RedisTemplate<String, Case> redisTemplate;
    @Autowired
    private ChannelTopic topic;

    public RedisMessagePublisher() {
    }

    @Override
    public void publish(Case caseOne) {
        redisTemplate.convertAndSend(topic.getTopic(), caseOne);
    }
}
