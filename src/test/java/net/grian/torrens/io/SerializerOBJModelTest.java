package net.grian.torrens.io;

import net.grian.torrens.object.OBJFace;
import net.grian.torrens.object.OBJModel;
import net.grian.torrens.object.OBJTriplet;
import net.grian.torrens.object.Vertex3f;
import org.junit.Test;

public class SerializerOBJModelTest {

    @Test
    public void toWriter() throws Exception {
        OBJModel model = new OBJModel();

        model.addNormal(new Vertex3f(1, 2, 3));
        model.addNormal(new Vertex3f(4, 5, 6));
        model.addNormal(new Vertex3f(7, 8, 9));

        model.addVertex(new Vertex3f(0, 0, 0));
        model.addVertex(new Vertex3f(0, 1, 0));
        model.addVertex(new Vertex3f(0, 0, 1));

        model.addFace(new OBJFace(new OBJTriplet(0, 2, -1), new OBJTriplet(1, 1, -1), new OBJTriplet(2, 0, -1)));

        String str = new SerializerOBJModel().toString(model);
        System.out.println(str);
    }

}