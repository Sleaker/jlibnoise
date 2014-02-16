package net.jlibnoise.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.jlibnoise.module.generator.Perlin;

public class TutorialTest {

    @Test
    public void tutorial2() {
        Perlin myModule = new Perlin();

        double value = myModule.getValue(1.25, 0.75, 0.50);
        assertEquals(0.686347, value, 0.0000009);

        value = myModule.getValue(1.2501, 0.7501, 0.5001);
        assertEquals(0.685644, value, 0.0000009);

        value = myModule.getValue(14.50, 20.25, 75.75);
        assertEquals(-0.972999, value, 0.0000009);
    }
}
