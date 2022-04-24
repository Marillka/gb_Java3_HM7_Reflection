public class Main {
    public static void main(String[] args) {

        TestingClass testingClass = new TestingClass();
        TestHandler.start(testingClass.getClass());
    }
}

/*
BeforeSuite
Priority = 1
Priority = 3
Priority = 7
Priority = 10
AfterSuite
 */
