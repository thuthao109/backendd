package com.capstone.kots.service;

import com.capstone.kots.entity.Case;

public interface MessagePublisher {
    void publish(Case caseOne);
}
