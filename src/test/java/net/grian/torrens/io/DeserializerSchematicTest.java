package net.grian.torrens.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeserializerSchematicTest {

    @Test
    public void deserialize() throws Exception {
        new DeserializerSchematic().deserialize(getClass(), "bunny.schematic");
    }

}