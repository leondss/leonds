package com.leonds;

import com.leonds.blog.console.service.PostsService;
import com.leonds.blog.console.service.SequenceService;
import com.leonds.blog.domain.enums.Sequence;
import org.eclipse.jgit.api.Git;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LeondsApplicationTests {

    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private PostsService postsService;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 10; i++) {
            String sequence = sequenceService.getSequence(Sequence.SEQ_CATE.name());
            System.err.println(sequence);
        }
    }

    @Test
    public void testGig() {
        String repository = "https://github.com/leondss/articles.git";

        //检查目标文件夹是否存在
        try {
            Git.cloneRepository().setURI(repository).setDirectory(new File("D:\\data\\articles")).call();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testaa() {
        String md = "This is *Sparta* # 标题一";
//        Parser parser = Parser.builder().build();
//        Node document = parser.parse(md);
//        HtmlRenderer renderer = HtmlRenderer.builder().build();
//        String render = renderer.render(document);
//        System.err.println(render);

        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(md);
        System.err.println(htmlContent);
    }

}
