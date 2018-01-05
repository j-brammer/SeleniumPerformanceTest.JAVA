import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;

public class SeleniumPerformanceTest {

    private static PrintWriter outFile;

    public static void main(String[] args){
        try{
            outFile = new PrintWriter("test.txt");
        } catch (FileNotFoundException fnf) {
            System.out.println("file not found!");
        }

        //Test(new ChromeDriver());
        //Test(new FirefoxDriver());
        Test(new EdgeDriver());

        outFile.close();
    }

    private static void Test(WebDriver driver){
        final long startTime = System.currentTimeMillis();

        JavascriptExecutor js = (JavascriptExecutor)driver;
        driver.manage().window().maximize();
        driver.get("http://automationpractice.com");
        WebElement signIn = driver.findElement(By.className("login"));

        signIn.click();

        //Edge errors out if you don't wait. Bummer
        WaitAndClickById("email", driver);

        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("passwd"));
        WebElement signInButton = driver.findElement(By.id("SubmitLogin"));

        emailInput.sendKeys("jbrammer@callibrity.com");
        passwordInput.sendKeys("demopass");
        signInButton.click();

        //In Edge, if we navigate too quickly the login doesn't seem to stick? I mean it is a demo site, lets not split hairs
        if(driver.getClass().toString().contains("EdgeDriver")){
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ie){
                System.out.println("Couldn't sleep");
            }
        }

        WaitAndClickByCssSelector("#block_top_menu > ul > li:nth-child(3) > a", driver);

        WaitForElementByCssSelector(".right-block > h5:nth-child(1) > a:nth-child(1)", driver);
        WebElement clickShirt = driver.findElement(By.cssSelector(".right-block > h5:nth-child(1) > a:nth-child(1)"));
        js.executeScript("arguments[0].scrollIntoView(true);", clickShirt);
        clickShirt.click();

        WaitAndClickById("add_to_cart", driver);

        WaitAndClickByCssSelector("#layer_cart > div.clearfix > div.layer_cart_cart.col-xs-12.col-md-6 > div.button-container > a", driver);

        WaitAndClickByCssSelector("#center_column > p.cart_navigation.clearfix > a.button.btn.btn-default.standard-checkout.button-medium", driver);

        WaitAndClickByCssSelector("#center_column > form > p > button", driver);

        WaitAndClickByCssSelector("#cgv", driver);

        WaitAndClickByCssSelector("#form > p > button", driver);

        WaitAndClickByCssSelector("#HOOK_PAYMENT > div:nth-child(2) > div > p > a", driver);

        WaitAndClickByCssSelector("#cart_navigation > button", driver);

        WaitAndClickByCssSelector("#header > div.nav > div > div > nav > div:nth-child(2) > a", driver);

        final long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        CloseDriver(driver, runTime);
    }

    private static void CloseDriver(WebDriver driver, long runTime){
        String driverType = driver.getClass().toString();
        outFile.println(driverType);
        outFile.println(runTime);
        System.out.println(runTime + " " + driverType);
        driver.close();
    }

    private static void WaitAndClickById(String id, WebDriver driver){
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        driver.findElement(By.id(id)).click();
    }

    private static void WaitAndClickByCssSelector(String cssSelector, WebDriver driver){
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
        driver.findElement(By.cssSelector(cssSelector)).click();
    }

    private static void WaitForElementByCssSelector(String cssSelector, WebDriver driver){
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
    }
}