import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;

public class TestHandler {
    private static Object obj;

    public static void start(Class aClass) {
        // если @BeforeSuite или @AfterSuite присутствуют более чем в одном экземпляре то бросаем исключение
        if (!beforeAndAfterAnnotationsCorrect(aClass)) {
            throw new RuntimeException();
        }

        Method before = null;
        Method after = null;

        try {
            obj = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        ArrayList<Method> testMethods = new ArrayList<>();

        // получаем массив всех методов класса
        Method[] objMethods = aClass.getDeclaredMethods();
        // пробегаемся
        for (Method method : objMethods) {
            // если у метода есть аннотация @BeforeSuite, то добавляем этот метот в переменную before, и тоже самое для @AfterSuite. А если у метода есть аннотация @Test, то закидываем этот метод в лист
            if (method.getAnnotation(BeforeSuite.class) != null) {
                before = method;
            } else if (method.getAnnotation(AfterSuite.class) != null) {
                after = method;
            } else if (method.getAnnotation(Test.class) != null) {
                testMethods.add(method);
            }
        }

        // сортируем лист с помощью компаратора. Сортировка по приоритету в аннотации @Test
        testMethods.sort(Comparator.comparingInt(o -> o.getAnnotation(Test.class).priority()));

        // соответственно если у нас была аннотация @BeforeSuite, то добавляем этот метод в самое начало листа
        if (before != null) {
            testMethods.add(0, before);
        }
        // тоже самое для аннотации @AfterSuite, только закидываем в конец листа
        if (after != null) {
            testMethods.add(after);
        }

        // пробегаемся по методам, если они приватные, делаем их accessible
        for (Method testMethod : testMethods) {
            if (Modifier.isPrivate(testMethod.getModifiers())) {
                testMethod.setAccessible(true);
            }
            try {
                // вызываем методы класса
                testMethod.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }


    private static boolean beforeAndAfterAnnotationsCorrect(Class aClass) {
        int beforeAnnotationCount = 0;// счетчик аннотации @BeforeSuite
        int afterAnnotationCount = 0;// счетчик аннотации @AfterSuite

        // если в методах класса есть какая либо из аннотация, то увеличиваем счетчик
        for (Method method : aClass.getDeclaredMethods()) {
            if (method.getAnnotation(BeforeSuite.class) != null) {
                beforeAnnotationCount++;
            }
            if (method.getAnnotation(AfterSuite.class) != null) {
                afterAnnotationCount++;
            }
        }

        // соответственно если значение для одного и для другого счетчика меньше двух, то возвращаем true.
        return (beforeAnnotationCount < 2) && (afterAnnotationCount < 2);
    }
}
