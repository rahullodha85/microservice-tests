package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
//        tags={"@debug"},
        format = {"pretty", "html:target/html/", "json:target/json/output.json"},
        features = {"src/test/resources/features"}
)
public class CucumberRunnerIntegrationTest {

}
