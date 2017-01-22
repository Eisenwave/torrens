package net.grian.torrens.io;

import net.grian.spatium.geo.AxisAlignedBB;
import net.grian.torrens.object.MCElement;
import net.grian.torrens.object.Texture;
import net.grian.torrens.object.MCModel;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SerializerModelJSONTest {

    @Test
    public void serialize() throws Exception {
        MCModel model = new MCModel();

        model.addTexture("texture", new Texture(16, 16));
        model.addElement(new MCElement(AxisAlignedBB.fromPoints(0, 0, 0, 16, 16, 16)));

        File out = new File("D:\\Users\\Jan\\Desktop\\SERVER\\SERVERS\\TEST\\plugins\\VoxelVert\\files\\SerializerModelJSONTest.json");
        if (!out.exists() && !out.createNewFile()) throw new IOException("failed to create "+out);

        new SerializerModelJSON().toFile(model, out);
    }

}