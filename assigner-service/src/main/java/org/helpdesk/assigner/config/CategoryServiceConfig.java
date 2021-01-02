package org.helpdesk.assigner.config;

import org.helpdesk.assigner.model.PostStreamDto;
import org.helpdesk.assigner.service.AssignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@EnableBinding(Sink.class)
public class CategoryServiceConfig {

    @Autowired
    private SubscribableChannel input;

    @Autowired
    private AssignerService assignerService;

    @StreamListener("input")
    public void consumeMessage(PostStreamDto request) {
        assignerService.assignPostToUser(request);
//        input.subscribe()
    }

}
