package com.leonds;

import com.leonds.blog.console.service.SequenceService;
import com.leonds.blog.domain.enums.Sequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LeondsApplicationTests {

    @Autowired
    private SequenceService sequenceService;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 10; i++) {
            String sequence = sequenceService.getSequence(Sequence.SEQ_CATE.name());
            System.err.println(sequence);
        }
    }

}
