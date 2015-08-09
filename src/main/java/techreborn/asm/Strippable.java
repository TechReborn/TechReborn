package techreborn.asm;

import java.lang.annotation.*;

/**
 * When used on a class, methods from referenced interfaces will not be removed <br>
 * When using this annotation on methods, ensure you do not switch on an enum inside that method. JavaC implementation details means this will cause crashes.
 * <p/>
 * Can also strip on modid using "mod:<MODID>" as a value <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.TYPE})
public @interface Strippable {

    public String[] value();

}