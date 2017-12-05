package eisenwave.torrens.wavefront;

import eisenwave.torrens.object.Vertex3f;
import org.junit.Test;

import static org.junit.Assert.*;

public class SerializerOBJTest {

    @Test
    public void toWriter() throws Exception {
        OBJModel model = new OBJModel();

        model.addNormal(new Vertex3f(1, 2, 3).normalized());
        model.addNormal(new Vertex3f(4, 5, 6).normalized());
        model.addNormal(new Vertex3f(7, 8, 9).normalized());

        model.addVertex(new Vertex3f(0, 0, 0));
        model.addVertex(new Vertex3f(0, 1, 0));
        model.addVertex(new Vertex3f(0, 0, 1));

        model.getDefaultGroup().addFace(new OBJFace(new OBJTriplet(1, 0, 2), new OBJTriplet(2, 0, 1), new OBJTriplet(3, 0, 0)));

        assertNotNull(new SerializerOBJ().toString(model));
    }

}
