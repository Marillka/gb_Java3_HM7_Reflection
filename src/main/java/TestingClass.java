public class TestingClass {

    @Test(priority = 10)
    public void testMethod1() {
        System.out.println("Priority = 10");
    }

    @Test(priority = 1)
    public void testMethod2() {
        System.out.println("Priority = 1");
    }

    @Test(priority = 3)
    public void testMethod3() {
        System.out.println("Priority = 3");
    }

    @Test(priority = 7)
    public void testMethod4() {
        System.out.println("Priority = 7");
    }

    @AfterSuite
    public void afterMethods() {
        System.out.println("AfterSuite");
    }

    @BeforeSuite
    public void beforeMethods() {
        System.out.println("BeforeSuite");
    }
}
