import static org.junit.Assert.*;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.*;
import java.util.Map;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MarkdownParseTest {

    @Test
    public void testFile1() throws IOException {
        String contents= Files.readString(Path.of("./test-file.md"));
        List<String> expect = List.of("https://something.com", "some-thing.html");
        assertEquals(MarkdownParse.getLinks(contents), expect);
    }

    @Test
    public void testFile2() throws IOException {
        String contents= Files.readString(Path.of("./test-file2.md"));
        List<String> expect = List.of("https://something.com", "some-page.html");
        assertEquals(MarkdownParse.getLinks(contents), expect);
    }

    @Test
    public void testLinkAtBeginning() {
        String contents= "[link title](a.com)";
        List<String> expect = List.of("a.com");
        assertEquals(MarkdownParse.getLinks(contents), expect);
    }

    @Test
    public void testSpaceInURL() {
        String contents = "[title](space in-url.com)";
        List<String> expect = List.of();
        assertEquals(MarkdownParse.getLinks(contents), expect);
    }

    @Test
    public void testSpaceAfterParen() {
        String contents = "[title]( space-in-url.com)";
        List<String> expect = List.of("space-in-url.com");
        assertEquals(expect, MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSpaceBeforeParen() {
        String contents = "[title]   (should-not-count.com)";
        List<String> expect = List.of();
        assertEquals(MarkdownParse.getLinks(contents), expect);
    }

    @Test
    public void testNestedParens() throws IOException {
        String contents = Files.readString(Path.of("test-parens-inside-link.md"));
        List<String> expect = List.of("something.com()", "something.com((()))", "something.com", "boring.com");
        assertEquals(expect, MarkdownParse.getLinks(contents));
    }

    @Test
    public void testMissingCloseParen() throws IOException {
        String contents = Files.readString(Path.of("test-missing-paren-plus-test-file2.md"));
        List<String> expect = List.of("https://something.com", "some-page.html");
        assertEquals(MarkdownParse.getLinks(contents), expect);
    }

    @Test
    public void Snippet1Contains2Links() throws IOException {
        String snippet = Files.readString(Path.of("./test-file9.md"));
        //`[a link`] and [another link] -- 2 expected
        assertEquals(MarkdownParse.getLinks(snippet).size(), 2);
    }

    @Test
    public void Snippet2HasParentheses() throws IOException{
        boolean expected = true;
        boolean result;
        String snippet = Files.readString(Path.of("./test-file10.md"));
        for(String cutSnippet: MarkdownParse.getLinks(snippet)){
            if(cutSnippet.contains("(())")){
                result = true;
            }
            else{
                result = false;
            }
            assertEquals(expected, result);
            return;
        } 
        
        
    }

    @Test
    public void Snippet3LineBreakisLink() throws IOException{
        String snippet = Files.readString(Path.of("./test-file11.md"));
        boolean expected = true;
        boolean result;
        String givenLinkTitle = MarkdownParse.getLinks(snippet).get(0);
        String expectedLinkTitle = "this title text is really long and takes up more than \none line\n\nand has some line breaks";

        if(givenLinkTitle.equals(expectedLinkTitle)){
            result = true;
        }
        else{
            result = false; 
        }
        assertEquals(expected, result);
    }
    
    
}
